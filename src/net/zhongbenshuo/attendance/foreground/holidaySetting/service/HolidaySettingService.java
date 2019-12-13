package net.zhongbenshuo.attendance.foreground.holidaySetting.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

public interface HolidaySettingService {
	/**
	 * 查询假期设置
	 * @param year
	 * @param month
	 * @return
	 */
	JSONObject findWorkingTime(String year, String month,int company_id);
	/**
	 * 修改工作状态
	 * @param id
	 * @param status
	 * @param oldStatusName
	 * @param newStatusName
	 * @param date
	 * @param httpSession
	 * @param request
	 * @return
	 */
	JSONObject updateWorkingInfo(String id, String status, String oldStatusName, String newStatusName, String date,
			HttpSession httpSession, HttpServletRequest request);	
	
	
}
