package net.zhongbenshuo.attendance.foreground.announcement.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.announcement.bean.AnnouncementType;
import net.zhongbenshuo.attendance.foreground.announcement.bean.Priority;

public interface AnnouncementService {
	/**
	 * 查询公告制度的类型
	 * @return
	 */
	List<AnnouncementType> findAnnouncementType();
	/**
	 * 查询优先级
	 * @return
	 */
	List<Priority> findPriority();
	
	int uploadImg(String title, String addressUrl, String external_link, String fileDirPath, String effectiveTime,
			String company_id, String priority, String type, int user_id, String user_name);
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
	JSONObject findannouncement(int bNum, int rows, String bt, String et, String title, int company_id);
	/**
	 * 修改公告信息（带图片）
	 * @param title
	 * @param addressUrl
	 * @param external_link
	 * @param fileDirPath
	 * @param effective
	 * @param id
	 * @param priority
	 * @param type
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	int uploadImgEdit(String title, String addressUrl, String external_link, String fileDirPath, String effectiveTime,
			String id, String priority, String type, int user_id, String user_name);
	/**
	 * 修改公告信息（无图片）
	 * @param title
	 * @param addressUrl
	 * @param effective
	 * @param id
	 * @param priority
	 * @param type
	 * @param user_id
	 * @param user_name
	 * @return
	 */
	int uploadImgEditNotPicture(String title, String addressUrl, String effectiveTime, String id, String priority,
			String type, int user_id, String user_name);
	
	
}
