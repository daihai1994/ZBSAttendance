package net.zhongbenshuo.attendance.foreground.outAttendance.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfoAuditRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.mapper.OutAttendanceMapper;
import net.zhongbenshuo.attendance.foreground.outAttendance.service.OutAttendanceService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.netty.WebSocketFrameHandler;
import net.zhongbenshuo.attendance.service.IUserService;
import net.zhongbenshuo.attendance.utils.Futil;
import net.zhongbenshuo.attendance.utils.GsonUtils;
import net.zhongbenshuo.attendance.utils.JPushData;
import net.zhongbenshuo.attendance.utils.JiguangPush;
import net.zhongbenshuo.attendance.utils.PushData;
import net.zhongbenshuo.attendance.utils.PushType;
import net.zhongbenshuo.attendance.utils.TimeUtils;

@Service
public class OutAttendanceServiceImpl implements OutAttendanceService {
	
	private static int QIANDAO = 1;//签到
	private static int QIANTUI = 2;//签退
	private static int QIANDAOCHENGGONG = 1;//签到成功
	private static int QIANTUICHENGGONG = 2;//签退成功
	private static int QIANDAOCHIDAO = 3;//迟到打卡
	private static int QIANTUICHIDAO = 4;//早退打卡
	private static int SHENPICHENGGONG = 7;//审批成功

	public static Logger logger = LogManager.getLogger(OutAttendanceServiceImpl.class);
	@Autowired
	OutAttendanceMapper outAttendanceMapper;
	
	@Autowired
	LoggerMapper loggerMapper;
	
	@Autowired
	private  IUserService iuserService;
	
	@Autowired
	ConfigInforMapper configInforMapper;
	/**
	 * 查询外勤打卡需要审批信息
	 */
	@Override
	public JSONObject findOutAttendance(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outAttendanceInfoList = outAttendanceMapper.findOutAttendance(bt,et,user_id,applicant_user_id,bNum,rows);
			if(outAttendanceInfoList!=null&&outAttendanceInfoList.size()>0){
				size = outAttendanceInfoList.get(0).getSize();
				for(OutAttendanceInfo attendanceInfo : outAttendanceInfoList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findOutAttendanceInfoAuditRecord(attendanceInfo.getOut_attendance_id());
					attendanceInfo.setOutAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤打卡需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", outAttendanceInfoList);
		return jsonObject;
	}
	/***
	 * 外勤打卡审批处理
	 */
	@Override
	public JSONObject submitAuditOutAttendance(String outAttendanceRecordAudit_id, String out_attendance_id,
			String result_id, String audit_status, String audit_resmarks,String id, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			AttendanceRecord attendanceRecord = new AttendanceRecord();
			attendanceRecord = outAttendanceMapper.findAttendanceRecordByout_attendance_id(out_attendance_id);
			i = outAttendanceMapper.submitAuditOutAttendance(outAttendanceRecordAudit_id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeople(outAttendanceRecordAudit_id);//修改下一个需要审批的人
					if(x>0){
						outAttendanceMapper.updateOutAttendanceRecordStatus(out_attendance_id,1);//修改外勤详情信息的状态  状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateAttendanceRecordresult(out_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeople(outAttendanceRecordAudit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.OUT_ATTENDANCE_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OUT_ATTENDANCE_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.OUT_ATTENDANCE_APPLY);
						 pushData.setData(id);
						 String message = "您收到了一条外勤打卡申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserId(out_attendance_id);
						 websocket = new JSONObject();
						websocket.put("key",PushType.OUT_ATTENDANCE_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.OUT_ATTENDANCE_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.OUT_ATTENDANCE_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条外勤打卡申请已有人处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						outAttendanceMapper.updateOutAttendanceRecordStatus(out_attendance_id,2);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateAttendanceRecordresult(out_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserId(out_attendance_id);
						JSONObject websocket = new JSONObject();
						websocket = new JSONObject();
						websocket.put("key",PushType.OUT_ATTENDANCE_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						PushData pushData = new PushData();
						 pushData.setType(PushType.OUT_ATTENDANCE_APPROVAL);
						 pushData.setData(id);
						String  message = "您的一条外勤打卡申请已有人处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPROVAL),message);
						
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OUT_ATTENDANCE_APPROVAL);
						 alert.put("data", id);
						insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						//外勤打卡审批通过的时候
						calculatingManHours(attendanceRecord);
					}
				}else if(audit_status.equals("3")){//审核不通过
					outAttendanceMapper.updateOutAttendanceRecordStatus(out_attendance_id,3);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateAttendanceRecordresult(out_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserId(out_attendance_id);
					JSONObject websocket = new JSONObject();
					websocket = new JSONObject();
					websocket.put("key",PushType.OUT_ATTENDANCE_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					PushData pushData = new PushData();
					 pushData.setType(PushType.OUT_ATTENDANCE_APPROVAL);
					 pushData.setData(id);
					String  message = "您的一条外勤打卡申请已有人处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPROVAL),message);
					
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.OUT_ATTENDANCE_APPROVAL);
					 alert.put("data", id);
					insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(attendanceRecord).toString()+"申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("外勤打卡审批处理报错"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 外勤审批记录
	 */
	@Override
	public JSONObject findauditOutAttendance_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outAttendanceInfoList = outAttendanceMapper.findauditOutAttendance_historical_records(bt,et,user_id,applicant_user_id,bNum,rows);
			if(outAttendanceInfoList!=null&&outAttendanceInfoList.size()>0){
				size = outAttendanceInfoList.get(0).getSize();
				for(OutAttendanceInfo attendanceInfo : outAttendanceInfoList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findOutAttendanceInfoAuditRecord(attendanceInfo.getOut_attendance_id());//查询外勤打卡审批的相关记录
					attendanceInfo.setOutAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤打卡需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", outAttendanceInfoList);
		return jsonObject;
	}
	/**
	 * 补卡审批查询历史记录
	 */
	@Override
	public JSONObject findauditappealAttendance_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			appealAttendanceInfoList = outAttendanceMapper.findauditappealAttendance_historical_records(bt,et,user_id,applicant_user_id,bNum,rows);
			if(appealAttendanceInfoList!=null&&appealAttendanceInfoList.size()>0){
				size = appealAttendanceInfoList.get(0).getSize();
				for(AppealAttendanceInfo attendanceInfo : appealAttendanceInfoList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findAppealRecordInfoAuditRecord(attendanceInfo.getAppeal_attendance_id());
					attendanceInfo.setAppealAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询补卡需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", appealAttendanceInfoList);
		return jsonObject;
	}
	/**
	 * 查询公司ID
	 * @param user_id
	 * @return
	 */
	public  int getCompangId(int user_id) {
		int comPanyId = 0;
		comPanyId = iuserService.getCompangIdByUserId(user_id);
		return comPanyId;
	}
	
	/***
	 * 新增推送消息
	 * @param alert 内容
	 * @param outAttendanceApply 推送类型
	 * @param companyId 公司ID
	 * @param currentFormatDateTime 时间
	 * @param pushType  别名推送，全部推送，标签推送
	 * @param resDate 激光返回结果
	 * @param setResultCode 激光返回码
	 * @param userIdList 推送给谁的list
	 */
	public   void insertJPushData(String alert, int outAttendanceApply, int companyId, String currentFormatDateTime,
			String pushType, String resDate, int setResultCode, String userIdList) {
		JPushData jpushData = new JPushData();
		jpushData.setContent(alert);
		jpushData.setDataType(outAttendanceApply);
		jpushData.setPusher(companyId);
		jpushData.setPushTime(currentFormatDateTime);
		jpushData.setPushType(pushType);
		jpushData.setResultCode(setResultCode);
		jpushData.setResultContent(resDate);
		jpushData.setReceiver(userIdList);
		iuserService.insertJPushData(jpushData);
	}
	/**
	 * 补卡审批查询
	 */
	@Override
	public JSONObject findappealAttendance(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			appealAttendanceInfoList = outAttendanceMapper.findAppealAttendance(bt,et,user_id,applicant_user_id,bNum,rows);
			if(appealAttendanceInfoList!=null&&appealAttendanceInfoList.size()>0){
				size = appealAttendanceInfoList.get(0).getSize();
				for(AppealAttendanceInfo attendanceInfo : appealAttendanceInfoList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findAppealRecordInfoAuditRecord(attendanceInfo.getAppeal_attendance_id());
					attendanceInfo.setAppealAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询补卡需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", appealAttendanceInfoList);
		return jsonObject;
	}
	/**
	 * 加班审批查询
	 */
	@Override
	public JSONObject findoverTime(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			overTimeRecordList = outAttendanceMapper.findoverTime(bt,et,user_id,applicant_user_id,bNum,rows);
			if(overTimeRecordList!=null&&overTimeRecordList.size()>0){
				size = overTimeRecordList.get(0).getSize();
				for(OverTimeRecord overTimeRecord : overTimeRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoverTimeauditRecord(overTimeRecord.getId());
					overTimeRecord.setOverTimeAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询加班需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", overTimeRecordList);
		return jsonObject;
	}
	/**
	 * 补卡审批提交
	 */
	@Override
	public JSONObject submitAuditappealAttendance(String appealAttendanceRecordAudit_id, String appeal_attendance_id,
			String result_id, String audit_status, String audit_resmarks,String id, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			AttendanceRecord attendanceRecord = new AttendanceRecord();
			attendanceRecord = outAttendanceMapper.findAttendanceRecordByAppeal_attendance_id(appeal_attendance_id);
			i = outAttendanceMapper.submitAuditappealAttendance(appealAttendanceRecordAudit_id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleAppeal(appealAttendanceRecordAudit_id);//修改下一个需要审批的人
					if(x>0){
						outAttendanceMapper.updateAppealAttendanceRecordStatus(appeal_attendance_id,1);//修改外勤详情信息的状态  状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateAppealAttendanceRecordresult(appeal_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleAppeal(appealAttendanceRecordAudit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.ATTENDANCE_APPEAL_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.ATTENDANCE_APPEAL_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.ATTENDANCE_APPEAL_APPLY);
						 pushData.setData(id);
						 String message = "您收到了一条补卡申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdAppeal(appeal_attendance_id);
						 websocket = new JSONObject();
						websocket.put("key",PushType.ATTENDANCE_APPEAL_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.ATTENDANCE_APPEAL_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.ATTENDANCE_APPEAL_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条补卡申请已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						outAttendanceMapper.updateAppealAttendanceRecordStatus(appeal_attendance_id,2);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateAppealAttendanceRecordresult(appeal_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdAppeal(appeal_attendance_id);
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.ATTENDANCE_APPEAL_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.ATTENDANCE_APPEAL_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条补卡申请已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.ATTENDANCE_APPEAL_APPROVAL);
						 alert.put("data", id);
						insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						calculatingManHours(attendanceRecord);
					}
				}else if(audit_status.equals("3")){//审核不通过
					outAttendanceMapper.updateOutAttendanceRecordStatus(appeal_attendance_id,3);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateAppealAttendanceRecordresult(appeal_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdAppeal(appeal_attendance_id);
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.ATTENDANCE_APPEAL_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.ATTENDANCE_APPEAL_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条补卡申请已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.ATTENDANCE_APPEAL_APPROVAL);
					 alert.put("data", id);
					insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(attendanceRecord).toString()+"补卡申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("补卡审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 当补卡审批过，外勤打卡审批过的时候，时间和当前系统时间做对比，当补卡时间小于系统时间超过一天的时候，就重新计算下补卡那天的工时
	 * @param attendanceRecord
	 * @throws Exception
	 */
	private void calculatingManHours(AttendanceRecord attendanceRecord) throws Exception {
		String attendance_time = attendanceRecord.getAttendance_time();//获取当前审批的补卡时间
		int size = TimeUtils.getDaysBetween(attendance_time, TimeUtils.getCurrentDateTime());//获取补卡时间和系统时间的相差几天
		if(size>=1){//如果当系统时间和补卡的时间大于一天的时候，就从新计算下补卡那天的工作时间
			JSONObject object  = new JSONObject();
			object = TimeUtils.getUpdateDates(attendance_time,1);//当前计算工时的开始时间，结束时间
			String bt = object.optString("start").substring(0, 10)+" 04:00:00";//开始时间
			String et = object.optString("et").substring(0, 10)+" 04:00:00";//结束时间
			int user_id = attendanceRecord.getUser_id();
			boolean qiandaostatus = false;//默认签到状态是false
			boolean qiantuistatus = false;//默认签退状态是false
			int qiandaoMinute = 0;//签到迟到多少分钟
			int qiantuiMinute = 0;//签退早多少分钟
			float hours = 8;//一天工作了几个小时
			
			List<AttendanceRecord> attendanceRecords = new ArrayList<AttendanceRecord>();//每人每天的考勤详情（包括正常打卡，补卡，外勤等）
			attendanceRecords = configInforMapper.findAttendanceRecordByUserId(user_id,bt,et);
			List<AttendanceRecord> attendanceRecordQIANDAO = new ArrayList<AttendanceRecord>();//每人每天的签到考勤详情
			List<AttendanceRecord> attendanceRecordQIANTUI = new ArrayList<AttendanceRecord>();//每人每天的签退考勤详情
			
			for(AttendanceRecord attendanceRecord1 : attendanceRecords){//遍历全部的，分为签到和签退两个list
				if(attendanceRecord1.getAttendance_type()==QIANDAO){
					attendanceRecordQIANDAO.add(attendanceRecord1);
				}else if(attendanceRecord1.getAttendance_type()==QIANTUI){
					attendanceRecordQIANTUI.add(attendanceRecord1);
				}
			}
			
			for(AttendanceRecord attendanceRecord1 : attendanceRecordQIANDAO){//遍历签到，判断是否是正常签到，如是迟到签到，迟到多少时间
				if(attendanceRecord1.getOut_attendance()==1&&attendanceRecord1.getResult_id()==SHENPICHENGGONG){//外勤签到是否审批成功
					qiandaostatus = true;
					qiandaoMinute = 0;
					break;
				}
				if(attendanceRecord1.getAppeal_attendance()==1&&attendanceRecord1.getResult_id()==SHENPICHENGGONG){//补卡签到是否审批成功
					if (TimeUtils.getTimeCompareSize(attendanceRecord1.getAttendance_time(), attendanceRecord1.getRule_time_work()) > 1) {//正常补卡签到
						qiandaostatus = true;
						qiantuiMinute = 0;
						break;
					}else{
						qiandaostatus = false;
						int minute = TimeUtils.getMinute( attendanceRecord1.getRule_time_work(),attendanceRecord1.getAttendance_time().substring(11, 16));
						qiandaoMinute = minute;
					}
					
				}
				if(attendanceRecord1.getResult_id()==QIANDAOCHENGGONG){//签到成功
					qiandaostatus = true;
					qiantuiMinute = 0;
					break;
				}
				if(attendanceRecord1.getResult_id()==QIANDAOCHIDAO){//签到迟到
					qiandaostatus = false;
					int minute = TimeUtils.getMinute( attendanceRecord1.getRule_time_work(),attendanceRecord1.getAttendance_time().substring(11, 16));
					qiandaoMinute = minute;
				}
			}
			
			for(AttendanceRecord attendanceRecord1 : attendanceRecordQIANTUI){//遍历签退，判断是否是正常签退，如是早退签退，早退多少时间
				if(attendanceRecord1.getOut_attendance()==1&&attendanceRecord1.getResult_id()==SHENPICHENGGONG){//外勤签退是否审批成功
					qiantuistatus = true;
					qiantuiMinute = 0;
					break;
				}
				if(attendanceRecord1.getAppeal_attendance()==1&&attendanceRecord1.getResult_id()==SHENPICHENGGONG){//补卡签退是否审批成功
					
					if (TimeUtils.getTimeCompareSize(attendanceRecord1.getAttendance_time(), attendanceRecord1.getRule_time_off_work()) < 3) {
						// 正常签退
						qiantuistatus = true;
						qiantuiMinute = 0;
						break;
					} else {
						// 早退打卡
						qiantuistatus = false;
						String rest_start = attendanceRecord1.getRule_rest_start();//休息开始时间
						String rest_end  = attendanceRecord1.getRule_rest_end();//休息结束时间
						String attendanceRecord_time = attendanceRecord1.getAttendance_time().substring(11, 16);//打卡时间（时分）
						if(attendanceRecord_time.compareTo(rest_end)>=1){//如果打卡时间大于结束休息时间，用打卡时间计算缺勤分钟
							qiantuiMinute = TimeUtils.getMinute(attendanceRecord_time,attendanceRecord1.getRule_time_off_work());
						}else{//其它的都用休息结束时间计算缺勤分钟
							qiantuiMinute = TimeUtils.getMinute(rest_end,attendanceRecord1.getRule_time_off_work());
						}
					}
				}
				if(attendanceRecord1.getResult_id()==QIANTUICHENGGONG){//签退成功
					qiantuistatus = true;
					qiantuiMinute = 0;
					break;
				}
				if(attendanceRecord1.getResult_id()==QIANTUICHIDAO){//早退
					qiantuistatus = false;
					String rest_start = attendanceRecord1.getRule_rest_start();//休息开始时间
					String rest_end  = attendanceRecord1.getRule_rest_end();//休息结束时间
					String attendanceRecord_time = attendanceRecord1.getAttendance_time().substring(11, 16);//打卡时间（时分）
					if(attendanceRecord_time.compareTo(rest_end)>=1){//如果打卡时间大于结束休息时间，用打卡时间计算缺勤分钟
						qiantuiMinute = TimeUtils.getMinute(attendanceRecord_time,attendanceRecord1.getRule_time_off_work());
					}else{//其它的都用休息结束时间计算缺勤分钟
						qiantuiMinute = TimeUtils.getMinute(rest_end,attendanceRecord1.getRule_time_off_work());
					}
				}
			}
			if(qiandaostatus&&qiantuistatus){//当签到签退都是正常的时候，默认是正常的
				hours = 8;
			}else if(!qiandaostatus&&qiandaoMinute==0){//当签到不正常且缺勤时间是0
				hours = 0;
			}else if(!qiantuistatus&&qiantuiMinute==0){//当签退不正常且缺勤时间是0
				hours = 0;
			}else if (!qiandaostatus||!qiantuistatus){//当签到签退不是正常的时候
					float a = (qiandaoMinute+qiantuiMinute)/60;
					hours = hours-a;
			}
			//configInforMapper.updateWorkingUser(user_id,hours,bt.substring(0, 10));//修改人员的每天工作时间
		}
	}
	/**
	 * 加班审批提交
	 */
	@Override
	public JSONObject submitAuditoverTime(String audit_id,String id, String result_id, String audit_status, String audit_resmarks,
			HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditoverTimeAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleOverTime(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateOverTimeAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleOverTime(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.OVERTIME_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OVERTIME_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.OVERTIME_APPLY);
						 pushData.setData(id);
						 String message = "您收到了一条加班申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.OVERTIME_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdOverTime(id);//查询加班申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.OVERTIME_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.OVERTIME_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.OVERTIME_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条加班申请已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.OVERTIME_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateOverTimeAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdOverTime(id);//查询加班申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.OVERTIME_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OVERTIME_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.OVERTIME_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条加班申请已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.OVERTIME_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateOverTimeAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdOverTime(id);//查询加班申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.OVERTIME_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.OVERTIME_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.OVERTIME_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条加班申请已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.OVERTIME_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					OverTimeRecord overTimeRecord = new OverTimeRecord();
					overTimeRecord = outAttendanceMapper.findAttendanceRecordByOverTime_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(overTimeRecord).toString()+"加班补卡申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("加班审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 加班审批查询历史审批记录
	 */
	@Override
	public JSONObject findauditoverTime_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			overTimeRecordList = outAttendanceMapper.findauditoverTime_historical_records(bt,et,user_id,applicant_user_id,bNum,rows);
			if(overTimeRecordList!=null&&overTimeRecordList.size()>0){
				size = overTimeRecordList.get(0).getSize();
				for(OverTimeRecord overTimeRecord : overTimeRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoverTimeauditRecord(overTimeRecord.getId());
					overTimeRecord.setOverTimeAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询加班需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", overTimeRecordList);
		return jsonObject;
	}
	/**
	 * 假期查询审批
	 */
	@Override
	public JSONObject findvacation(String bt, String et, String user_id, String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			vacationRecordList = outAttendanceMapper.findvacation(bt,et,user_id,applicant_user_id,bNum,rows);
			if(vacationRecordList!=null&&vacationRecordList.size()>0){
				size = vacationRecordList.get(0).getSize();
				for(VacationRecord vacationRecord : vacationRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findvacationauditRecord(vacationRecord.getId());
					vacationRecord.setVacationAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询假期需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", vacationRecordList);
		return jsonObject;
	}
	/**
	 * 假期审批的提交
	 */
	@Override
	public JSONObject submitAuditvacation(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditvacationAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeoplevacation(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updatevacationAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeoplevacation(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.VACATION_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.VACATION_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.VACATION_APPLY);
						 pushData.setData(id);
						 String message = "您收到一条假期审批!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.VACATION_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdvacation(id);//查询假期申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.VACATION_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.VACATION_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.VACATION_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条假期审批已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.VACATION_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updatevacationAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdvacation(id);//查询假期申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.VACATION_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.VACATION_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.VACATION_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条假期审批已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.VACATION_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updatevacationAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdvacation(id);//查询假期申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.VACATION_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.VACATION_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.VACATION_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条假期审批已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.VACATION_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					VacationRecord vacationRecord = new VacationRecord();
					vacationRecord = outAttendanceMapper.findAttendanceRecordByvacation_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(vacationRecord).toString()+"假期申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("假期审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 假期审批记录查询
	 */
	@Override
	public JSONObject findauditvacation_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			vacationRecordList = outAttendanceMapper.findauditvacation_historical_records(bt,et,user_id,applicant_user_id,bNum,rows);
			if(vacationRecordList!=null&&vacationRecordList.size()>0){
				size = vacationRecordList.get(0).getSize();
				for(VacationRecord vacationRecord : vacationRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findvacationauditRecord(vacationRecord.getId());
					vacationRecord.setVacationAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询加班需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", vacationRecordList);
		return jsonObject;
	}
	/**
	 *  外出审批查询
	 */
	@Override
	public JSONObject findoutGoing(String bt, String et, String user_id, String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outGoingRecordList = outAttendanceMapper.findoutGoing(bt,et,user_id,applicant_user_id,bNum,rows);
			if(outGoingRecordList!=null&&outGoingRecordList.size()>0){
				size = outGoingRecordList.get(0).getSize();
				for(OutGoingRecord outGoingRecord : outGoingRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoutGoingauditRecord(outGoingRecord.getId());
					outGoingRecord.setOutGoingAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外出需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", outGoingRecordList);
		return jsonObject;
	}
	/**
	 * 提交外出审批的信息
	 * @param audit_id
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitAuditoutGoing(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditoutGoingAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleOutGoing(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateOutGoingAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleOutGoing(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.GO_OUT_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.GO_OUT_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.GO_OUT_APPLY);
						 pushData.setData(id);
						 String message = "您收到一条外出审批!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.GO_OUT_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdOutGoing(id);//查询加班申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.GO_OUT_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.GO_OUT_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.GO_OUT_APPLY);
						 pushData.setData(id);
						  message = "您的一条外出审批已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPLY),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.GO_OUT_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateOutGoingAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdOutGoing(id);//查询加班申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.GO_OUT_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.GO_OUT_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.GO_OUT_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条外出审批已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.GO_OUT_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateOutGoingAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdOutGoing(id);//查询加班申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.GO_OUT_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.GO_OUT_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.GO_OUT_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条外出审批已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.GO_OUT_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					OutGoingRecord outGoingRecord = new OutGoingRecord();
					outGoingRecord = outAttendanceMapper.findAttendanceRecordByOutGoing_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(outGoingRecord).toString()+"外出补卡申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("外出审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 外出审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject findauditoutGoing_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outGoingRecordList = outAttendanceMapper.findauditoutGoing_historical_records(bt,et,user_id,applicant_user_id,bNum,rows);
			if(outGoingRecordList!=null&&outGoingRecordList.size()>0){
				size = outGoingRecordList.get(0).getSize();
				for(OutGoingRecord outGoingRecord : outGoingRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoutGoingauditRecord(outGoingRecord.getId());
					outGoingRecord.setOutGoingAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外出需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", outGoingRecordList);
		return jsonObject;
	}
	/**
	 * 出差审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject findbusinessTraveI(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			businessTraveIRecordList = outAttendanceMapper.findbusinessTraveI(bt,et,user_id,applicant_user_id,bNum,rows);
			if(businessTraveIRecordList!=null&&businessTraveIRecordList.size()>0){
				size = businessTraveIRecordList.get(0).getSize();
				for(BusinessTraveIRecord businessTraveIRecord : businessTraveIRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findbusinessTraveIauditRecord(businessTraveIRecord.getId());
					businessTraveIRecord.setBusinessTraveIAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询出差需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", businessTraveIRecordList);
		return jsonObject;
	}
	/**
	 * 提交出差审批的信息
	 * @param audit_id
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitAuditbusinessTraveI(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditbusinessTraveIAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleBusinessTraveI(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateBusinessTraveIAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleBusinessTraveI(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.BUSINESS_TRIP_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.BUSINESS_TRIP_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.BUSINESS_TRIP_APPLY);
						 pushData.setData(id);
						 String message = "您收到一条出差申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdBusinessTraveI(id);//查询加班申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.BUSINESS_TRIP_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.BUSINESS_TRIP_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.BUSINESS_TRIP_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条出差申请已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateBusinessTraveIAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdBusinessTraveI(id);//查询加班申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.BUSINESS_TRIP_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.BUSINESS_TRIP_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.BUSINESS_TRIP_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条出差申请已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateBusinessTraveIAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdBusinessTraveI(id);//查询加班申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.BUSINESS_TRIP_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.BUSINESS_TRIP_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.BUSINESS_TRIP_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条出差申请已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					BusinessTraveIRecord businessTraveIRecord = new BusinessTraveIRecord();
					businessTraveIRecord = outAttendanceMapper.findAttendanceRecordByBusinessTraveI_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(businessTraveIRecord).toString()+"出差申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("出差审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 出差审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject findauditbusinessTraveI_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			businessTraveIRecordList = outAttendanceMapper.findauditbusinessTraveI_historical_records(bt,et,user_id,applicant_user_id,bNum,rows);
			if(businessTraveIRecordList!=null&&businessTraveIRecordList.size()>0){
				size = businessTraveIRecordList.get(0).getSize();
				for(BusinessTraveIRecord businessTraveIRecord : businessTraveIRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findbusinessTraveIauditRecord(businessTraveIRecord.getId());
					businessTraveIRecord.setBusinessTraveIAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询出差需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", businessTraveIRecordList);
		return jsonObject;
	}
}
