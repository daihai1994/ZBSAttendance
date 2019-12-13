package net.zhongbenshuo.attendance.foreground.holidaySetting.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.holidaySetting.mapper.HolidaySettingMapper;
import net.zhongbenshuo.attendance.foreground.holidaySetting.service.HolidaySettingService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.Futil;

@Service
public class HolidaySettingServiceImpl implements HolidaySettingService {
	public static Logger logger = LogManager.getLogger(HolidaySettingServiceImpl.class);
	@Autowired
	LoggerMapper loggerMapper;
	
	@Autowired
	HolidaySettingMapper holidaySettingMapper;
	/**
	 * 查询假期设置
	 */
	@Override
	public JSONObject findWorkingTime(String year, String month,int company_id) {
		JSONObject jsonObject = new JSONObject();
		List<WorkingTime> workingTimes = new ArrayList<WorkingTime>();
		try {
			workingTimes = holidaySettingMapper.findWorkingTime(year,month,company_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("rows",workingTimes);
		jsonObject.put("total",workingTimes.size());
		return jsonObject;
	}
	/**
	 * 修改工作状态
	 */
	@Override
	public JSONObject updateWorkingInfo(String id, String status, String oldStatusName, String newStatusName,
			String date, HttpSession httpSession, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "修改失败");
		int i = 0;
		try {
			i= holidaySettingMapper.updateWorkingInfo(id,status);
			if(i>0){
				jsonObject.put("msg","修改成功");
				int user_id = Futil.getUserId(httpSession, request);
				String ip = Futil.getIpAddr(request);
				String remarks = "把"+date+"号的"+oldStatusName+"改为"+newStatusName;
				loggerMapper.addLogger(user_id,ip,remarks);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改工作状态失败");
		}
		return jsonObject;
	}
	
	
}
