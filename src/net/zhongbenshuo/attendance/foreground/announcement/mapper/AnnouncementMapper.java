package net.zhongbenshuo.attendance.foreground.announcement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.Announcement;
import net.zhongbenshuo.attendance.foreground.announcement.bean.AnnouncementType;
import net.zhongbenshuo.attendance.foreground.announcement.bean.Priority;

public interface AnnouncementMapper {
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
	/**
	 * 新增无跳转链接的公告·
	 * @param title
	 * @param addressUrl
	 * @param external_link
	 * @param fileDirPath
	 * @param effective
	 * @param company_id
	 * @param priority
	 * @param type
	 * @return
	 */
	int uploadImg(@Param("title")String title, @Param("addressUrl")String addressUrl, @Param("external_link")String external_link, @Param("fileDirPath")String fileDirPath, 
			@Param("effectiveTime")String effectiveTime,@Param("company_id")String company_id, @Param("priority")String priority, 
			@Param("type")String type, @Param("user_id")int user_id, @Param("user_name")String user_name);
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
	List<Announcement> findannouncement(@Param("bNum")int bNum, @Param("rows")int rows, @Param("bt")String bt, @Param("et")String et, 
			@Param("title")String title, @Param("company_id")int company_id);
	/**
	 * 修改公告（带图片）
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
	int uploadImgEdit(@Param("title")String title, @Param("addressUrl")String addressUrl, 
			@Param("external_link")String external_link, @Param("fileDirPath")String fileDirPath, 
			@Param("effectiveTime")String effectiveTime,@Param("id")String id, @Param("priority")String priority, 
			@Param("type")String type, @Param("user_id")int user_id, @Param("user_name")String user_name);
	/**
	 *  修改公告（无图片）
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
	int uploadImgEditNotPicture(@Param("title")String title, @Param("addressUrl")String addressUrl,
			@Param("effectiveTime")String effectiveTime, @Param("id")String id, @Param("priority")String priority,
			@Param("type")String type, @Param("user_id")int user_id, @Param("user_name")String user_name);
	
	
}
