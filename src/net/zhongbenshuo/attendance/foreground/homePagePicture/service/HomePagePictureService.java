package net.zhongbenshuo.attendance.foreground.homePagePicture.service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.homePagePicture.bean.HomePagePicture;

public interface HomePagePictureService {
	/**
	 * 安卓轮播图的查询
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param name
	 * @return
	 */
	JSONObject findhomePagePicture(int bNum, int rows, String bt, String et, String remarks,int company_id);
	/**
	 * 提交新增轮播图
	 * @param remarks
	 * @param addressUrl
	 * @param state
	 * @param fileDirPath
	 * @param effective
	 * @return
	 */
	int uploadImg(String remarks, String addressUrl, String state, String fileDirPath, String effective,String company_id);
	/**
	 * 查询需要修改的轮播图详情信息
	 * @param id
	 * @return
	 */
	HomePagePicture findHomePagePictureByID(String id);
	/**
	 * 修改没有改变图片的轮播图信息
	 * @param remarks
	 * @param state
	 * @param addressUrl
	 * @param effective
	 * @param id
	 * @return
	 */
	int updateuploadNotImg(String remarks, String state, String addressUrl, String effective, String id);
	/**
	 * 修改有改过图片的轮播图信息
	 * @param remarks
	 * @param state
	 * @param addressUrl
	 * @param effective
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int updateuploadImg(String remarks, String state, String addressUrl, String effective, String id,
			String fileDirPath);
	
	
}
