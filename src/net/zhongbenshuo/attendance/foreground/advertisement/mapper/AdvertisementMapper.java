package net.zhongbenshuo.attendance.foreground.advertisement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.foreground.advertisement.bean.Advertisement;

public interface AdvertisementMapper {
	/***
	 * 查询广告
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param name
	 * @return
	 */
	List<Advertisement> findAdvertisement(@Param("bNum")int bNum, @Param("rows")int rows, @Param("bt")String bt, @Param("et")String et, @Param("name")String name);
	/***
	 * 存储广告数据
	 * @param name
	 * @param showTime
	 * @param adUrl
	 * @param start_time
	 * @param end_time
	 * @param fileDirPath
	 * @param force 
	 * @param effective 
	 * @return
	 */
	int uploadImg(@Param("name")String name, @Param("showTime")String showTime, @Param("adUrl")String adUrl, @Param("start_time")String start_time, 
			@Param("end_time")String end_time,@Param("fileDirPath")String fileDirPath,  @Param("effective")String effective,  @Param("force")String force);
	/***
	 * 如果是强制播放，就取消别的强制
	 */
	void updateAdvertisementForce();
	/***
	 * 根据广告ID查询广告数据
	 * @param id
	 * @return
	 */
	Advertisement findAdvertisementByID(@Param("id")String id);
	/***
	 * 修改带广告的广告信息
	 * @param name
	 * @param showTime
	 * @param adUrl
	 * @param start_time
	 * @param end_time
	 * @param fileDirPath
	 * @param effective
	 * @param force
	 * @param id
	 * @return
	 */
	int updateuploadImg(@Param("name")String name, @Param("showTime")String showTime, @Param("adUrl")String adUrl, @Param("start_time")String start_time, 
			@Param("end_time")String end_time,@Param("fileDirPath")String fileDirPath,  @Param("effective")String effective,  @Param("force")String force, 
			@Param("id")String id);
	/***
	 * 修改不带广告的广告信息
	 * @param name
	 * @param showTime
	 * @param adUrl
	 * @param start_time
	 * @param end_time
	 * @param effective
	 * @param force
	 * @param id
	 * @return
	 */
	int updateuploadNotImg(@Param("name")String name, @Param("showTime")String showTime, @Param("adUrl")String adUrl, @Param("start_time")String start_time, 
			@Param("end_time")String end_time,  @Param("effective")String effective,  @Param("force")String force, 
			@Param("id")String id);

}
