package net.zhongbenshuo.attendance.foreground.holidaySetting.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.holidaySetting.service.HolidaySettingService;

@Controller
@RequestMapping(value = "/HolidaySettingController", produces = "text/html;charset=UTF-8")
public class HolidaySettingController {
	@Autowired
	HolidaySettingService holidaySettingService;
	/**
	 * 查询假期设置
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(value = "/findWorkingTime.do")
	@ResponseBody
	public String findWorkingTime(String year,String month,int company_id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = holidaySettingService.findWorkingTime(year,month,company_id);
		return jsonObject.toString();
	}
	/**
	 * 修改工作状态
	 * @param id
	 * @param status
	 * @param oldStatusName
	 * @param newStatusName
	 * @param httpSession
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateWorkingInfo.do")
	@ResponseBody
	public String updateWorkingInfo(String id,String status,String oldStatusName,String newStatusName,String date,HttpSession httpSession,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = holidaySettingService.updateWorkingInfo(id,status,oldStatusName,newStatusName,date,httpSession,request);
		return jsonObject.toString();
	}
	
}
