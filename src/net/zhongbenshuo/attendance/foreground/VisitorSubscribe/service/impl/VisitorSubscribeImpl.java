package net.zhongbenshuo.attendance.foreground.VisitorSubscribe.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.bean.Face;
import net.zhongbenshuo.attendance.bean.FaceWebsocket;
import net.zhongbenshuo.attendance.bean.VisitorSubscribe;
import net.zhongbenshuo.attendance.foreground.VisitorSubscribe.mapper.VisitorSubscribeMapper;
import net.zhongbenshuo.attendance.foreground.VisitorSubscribe.service.VisitorSubscribeService;
import net.zhongbenshuo.attendance.netty.WebSocketFrameHandler;
import net.zhongbenshuo.attendance.utils.Futil;
import net.zhongbenshuo.attendance.utils.GsonUtils;
import net.zhongbenshuo.attendance.utils.JiguangPush;
import net.zhongbenshuo.attendance.utils.PushData;
import net.zhongbenshuo.attendance.utils.PushType;
import net.zhongbenshuo.attendance.utils.TimeUtils;

@Service
public class VisitorSubscribeImpl implements VisitorSubscribeService {
	public static Logger logger = LogManager.getLogger(VisitorSubscribeImpl.class);
	@Autowired
	VisitorSubscribeMapper visitorSubscribeMapper;
	/**
	 * 查询访客预约审批
	 */
	@Override
	public JSONObject findvisitorSubscribe(String bt, String et, String user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<VisitorSubscribe> visitorSubscribeList = new ArrayList<VisitorSubscribe>();
		try {
			visitorSubscribeList = visitorSubscribeMapper.findvisitorSubscribe(bt,et,user_id,bNum,rows);
			for(VisitorSubscribe subscribe : visitorSubscribeList){
				subscribe.setCreateTime(subscribe.getCreateTime().substring(0, 19));
				subscribe.setStartDate(subscribe.getStartDate().substring(0,10));
				subscribe.setEndDate(subscribe.getEndDate().substring(0,10));
			}
			if(visitorSubscribeList!=null&&visitorSubscribeList.size()>0){
				size = visitorSubscribeList.get(0).getSize();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询访客预约审批");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", visitorSubscribeList);
		return jsonObject;
	}
	/**
	 * 提交访客预约审批信息
	 */
	@Override
	public JSONObject submitAuditvisitorSubscribe(String id, String audit_status, String audit_resmarks) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = visitorSubscribeMapper.submitAuditvisitorSubscribe(id,audit_status,audit_resmarks);
			if(i==1){
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
				if(audit_status.equals("1")){
					VisitorSubscribe visitorSubscribe = new VisitorSubscribe();
					visitorSubscribe = visitorSubscribeMapper.findVistorSubscribeById(id);
					Face facenew = new Face();
					facenew.setUser_id(visitorSubscribe.getUser_id());
					facenew.setUrl(visitorSubscribe.getFaceUrl());
					facenew.setStartDate(visitorSubscribe.getStartDate());
					facenew.setStartTime(visitorSubscribe.getStartTime());
					facenew.setEndTime(visitorSubscribe.getEndTime());
					facenew.setIdentity(1);
					facenew.setName(visitorSubscribe.getUser_name());
					facenew.setEndDate(visitorSubscribe.getEndDate());
					facenew.setEffectiveDay(visitorSubscribe.getEffectiveDay());
					facenew.setEffective(1);
					facenew.setWorkingDay(visitorSubscribe.getWorkingDay());
					facenew.setServerStatus(0);
					visitorSubscribeMapper.insertFace(facenew);
					int ids = facenew.getId();
					String pictureUrl = facenew.getUrl().split("webapps")[1];
					facenew.setUrl(pictureUrl);
					FaceWebsocket faceWebsocket = new FaceWebsocket();
					faceWebsocket.setType("addNoFaceFeature");
					faceWebsocket.setId(ids);
					faceWebsocket.setFace(facenew);
					JSONObject websocket = new JSONObject();
					websocket.put("key",PushType.SERVER_CLIENT);
					websocket.put("message", faceWebsocket);
					WebSocketFrameHandler.sendData(websocket);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("提交访客预约审批信息"+e);
		}
		return jsonObject;
	
	}
	
}
