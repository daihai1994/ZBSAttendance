package net.zhongbenshuo.attendance.foreground.advertisement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.advertisement.bean.Advertisement;
import net.zhongbenshuo.attendance.foreground.advertisement.mapper.AdvertisementMapper;
import net.zhongbenshuo.attendance.foreground.advertisement.service.AdvertisementService;
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
	public static Logger logger = LogManager.getLogger(AdvertisementServiceImpl.class);
	@Autowired
	AdvertisementMapper advertisementMapper;
	/***
	 * 查询广告
	 */
	@Override
	public JSONObject findAdvertisement(int bNum,int rows,String bt, String et, String name) {
		JSONObject jsonObject = new JSONObject();
		 bt = bt+" 00:00:00";
		 et = et+" 23:59:59";
		List<Advertisement> advertisementList= new ArrayList<Advertisement>();
		int size = 0;
		try {
			advertisementList = advertisementMapper.findAdvertisement(bNum,rows,bt,et,name);
			if(advertisementList!=null&&advertisementList.size()>0){
				size = advertisementList.get(0).getSize();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询考勤规则报错!");
		}
		jsonObject.put("total",size);
		jsonObject.put("rows",advertisementList);
		return jsonObject;
	
	}
	/***
	 * 存储数据
	 */
	@Override
	public int uploadImg(String name, String showTime, String adUrl, String start_time, String end_time,
			String fileDirPath,String effective, String force) {
		int i = 0;
			try {
				if(force.equals("1")){//如果是强制，就取消别的强制
					advertisementMapper.updateAdvertisementForce();
				}
				i = advertisementMapper.uploadImg(name,showTime,adUrl,start_time,end_time,fileDirPath,effective,force);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("上传广告存储报错!");
			}
		return i;
	}
	/***
	 * 根据广告ID查询广告信息
	 */
	@Override
	public Advertisement findAdvertisementByID(String id) {
		Advertisement advertisement = new Advertisement();
		try {
			advertisement = advertisementMapper.findAdvertisementByID(id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("根据广告ID查询广告信息报错!");
		}
		return advertisement;
	}
	/***
	 * 修改带广告的广告信息
	 */
	@Override
	public int updateuploadImg(String name, String showTime, String adUrl, String start_time, String end_time,
			String fileDirPath, String effective, String force, String id) {
		int i = 0;
		try {
			if(force.equals("1")){//如果是强制，就取消别的强制
				advertisementMapper.updateAdvertisementForce();
			}
			i = advertisementMapper.updateuploadImg(name,showTime,adUrl,start_time,end_time,fileDirPath,effective,force,id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("上传广告存储报错!");
		}
	return i;
	}
	/***
	 * 修改不带广告的广告信息
	 */
	@Override
	public int updateuploadNotImg(String name, String showTime, String adUrl, String start_time, String end_time,
			String effective, String force, String id) {
		int i = 0;
		try {
			if(force.equals("1")){//如果是强制，就取消别的强制
				advertisementMapper.updateAdvertisementForce();
			}
			i = advertisementMapper.updateuploadNotImg(name,showTime,adUrl,start_time,end_time,effective,force,id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("上传广告存储报错!");
		}
	return i;
	
	}
}
