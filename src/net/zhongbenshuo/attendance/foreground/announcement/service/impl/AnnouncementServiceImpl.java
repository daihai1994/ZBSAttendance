package net.zhongbenshuo.attendance.foreground.announcement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.Announcement;
import net.zhongbenshuo.attendance.foreground.announcement.bean.AnnouncementType;
import net.zhongbenshuo.attendance.foreground.announcement.bean.Priority;
import net.zhongbenshuo.attendance.foreground.announcement.mapper.AnnouncementMapper;
import net.zhongbenshuo.attendance.foreground.announcement.service.AnnouncementService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
	public static Logger logger = LogManager.getLogger(AnnouncementServiceImpl.class);
	
	@Autowired
	LoggerMapper loggerMapper;
	
	@Autowired
	AnnouncementMapper announcementMapper;
	/**
	 * 查询公告制度的类型
	 */
	@Override
	public List<AnnouncementType> findAnnouncementType() {
		List<AnnouncementType> announcementTypes = new ArrayList<AnnouncementType>();
		announcementTypes = announcementMapper.findAnnouncementType();
		return announcementTypes;
	}
	/**
	 * 查询优先级
	 */
	@Override
	public List<Priority> findPriority() {
		List<Priority> prioritys = new ArrayList<Priority>();
		prioritys = announcementMapper.findPriority();
		return prioritys;
	}
	/**
	 * 新增没有点击跳转的公告
	 */
	@Override
	public int uploadImg(String title, String addressUrl, String external_link, String fileDirPath, String effectiveTime,
			String company_id, String priority, String type, int user_id, String user_name) {
		int i = 0;
		try {
			i = announcementMapper.uploadImg(title,addressUrl,external_link,fileDirPath,effectiveTime,company_id,priority,type,user_id,user_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 查询公告制度
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param title
	 * @param company_id
	 * @return
	 */
	@Override
	public JSONObject findannouncement(int bNum, int rows, String bt, String et, String title, int company_id) {

		JSONObject jsonObject = new JSONObject();
		 bt = bt+" 00:00:00";
		 et = et+" 23:59:59";
		List<Announcement> announcementList= new ArrayList<Announcement>();
		int size = 0;
		try {
			announcementList = announcementMapper.findannouncement(bNum,rows,bt,et,title,company_id);
			if(announcementList!=null&&announcementList.size()>0){
				size = announcementList.get(0).getSize();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询公告制度报错!");
		}
		jsonObject.put("total",size);
		jsonObject.put("rows",announcementList);
		return jsonObject;
	
	
	
	}
	@Override
	public int uploadImgEdit(String title, String addressUrl, String external_link, String fileDirPath,
			String effectiveTime, String id, String priority, String type, int user_id, String user_name) {
		int i = 0;
		try {
			i = announcementMapper.uploadImgEdit(title,addressUrl,external_link,fileDirPath,effectiveTime,id,priority,type,user_id,user_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	@Override
	public int uploadImgEditNotPicture(String title, String addressUrl, String effectiveTime, String id, String priority,
			String type, int user_id, String user_name) {
		int i = 0;
		try {
			i = announcementMapper.uploadImgEditNotPicture(title,addressUrl,effectiveTime,id,priority,type,user_id,user_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	

}
