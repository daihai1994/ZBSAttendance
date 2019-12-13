package net.zhongbenshuo.attendance.foreground.advertisement.service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.advertisement.bean.Advertisement;

public interface AdvertisementService {

	/***
	 * 查询广告
	 * @param rows 
	 * @param bNum 
	 * @param bt
	 * @param et
	 * @param name
	 * @return
	 */
	JSONObject findAdvertisement(int bNum, int rows, String bt, String et, String name);
	/***
	 * 存储数据
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
	int uploadImg(String name, String showTime, String adUrl, String start_time, String end_time, String fileDirPath, String effective, String force);
	/***
	 * 根据ID查询广告信息
	 * @param id
	 * @return
	 */
	Advertisement findAdvertisementByID(String id);
	/***
	 * 修改带图片的广告信息
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
	int updateuploadImg(String name, String showTime, String adUrl, String start_time, String end_time,
			String fileDirPath, String effective, String force, String id);
	/***
	 * 修改不带图片的广告信息
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
	int updateuploadNotImg(String name, String showTime, String adUrl, String start_time, String end_time,
			String effective, String force, String id);

}
