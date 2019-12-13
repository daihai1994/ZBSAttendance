package net.zhongbenshuo.attendance.foreground.homePagePicture.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.homePagePicture.bean.HomePagePicture;
import net.zhongbenshuo.attendance.foreground.homePagePicture.mapper.HomePagePictureMapper;
import net.zhongbenshuo.attendance.foreground.homePagePicture.service.HomePagePictureService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;

@Service
public class HomePagePictureServiceImpl implements HomePagePictureService {
	public static Logger logger = LogManager.getLogger(HomePagePictureServiceImpl.class);
	
	@Autowired
	HomePagePictureMapper homePagePictureMapper;
	
	@Autowired
	LoggerMapper loggerMapper;
	/**
	 * 安卓轮播图的查询
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param name
	 * @return
	 */
	@Override
	public JSONObject findhomePagePicture(int bNum, int rows, String bt, String et, String remarks,int company_id) {
		JSONObject jsonObject = new JSONObject();
		 bt = bt+" 00:00:00";
		 et = et+" 23:59:59";
		List<HomePagePicture> homePagePictureList= new ArrayList<HomePagePicture>();
		int size = 0;
		try {
			homePagePictureList = homePagePictureMapper.findhomePagePicture(bNum,rows,bt,et,remarks,company_id);
			if(homePagePictureList!=null&&homePagePictureList.size()>0){
				size = homePagePictureList.get(0).getSize();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询安卓轮播图报错!");
		}
		jsonObject.put("total",size);
		jsonObject.put("rows",homePagePictureList);
		return jsonObject;
	
	
	}
	/**
	 * 提交新增轮播图
	 * @param remarks
	 * @param addressUrl
	 * @param state
	 * @param fileDirPath
	 * @param effective
	 * @return
	 */
	@Override
	public int uploadImg(String remarks, String addressUrl, String state, String fileDirPath, String effective,String company_id) {
		int i = 0;
		try {
			i = homePagePictureMapper.uploadImg(remarks,addressUrl,state,fileDirPath,effective,company_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 查询需要修改的轮播图详情信息
	 * @param id
	 * @return homePagePicture
	 */
	@Override
	public HomePagePicture findHomePagePictureByID(String id) {
		HomePagePicture homePagePicture = new HomePagePicture();
		try {
			homePagePicture = homePagePictureMapper.findHomePagePictureByID(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return homePagePicture;
	}
	/**
	 * 修改没有改变图片的轮播图信息
	 * @param remarks
	 * @param state
	 * @param addressUrl
	 * @param effective
	 * @param id
	 * @return
	 */
	@Override
	public int updateuploadNotImg(String remarks, String state, String addressUrl, String effective, String id) {
		int i = 0;
		i = homePagePictureMapper.updateuploadNotImg(remarks,state,addressUrl,effective,id);
		return i;
	}
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
	@Override
	public int updateuploadImg(String remarks, String state, String addressUrl, String effective, String id,
			String fileDirPath) {
		int i = 0;
		try {
			i = homePagePictureMapper.updateuploadImg(remarks,state,addressUrl,effective,id,fileDirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	

}
