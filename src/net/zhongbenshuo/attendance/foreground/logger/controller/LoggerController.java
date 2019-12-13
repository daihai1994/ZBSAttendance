package net.zhongbenshuo.attendance.foreground.logger.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.logger.service.LoggerService;

@Controller
@RequestMapping(value = "/LoggerController", produces = "text/html;charset=UTF-8")
public class LoggerController {
	@Autowired
	LoggerService loggerService;
	/***
	 * 查询日志信息
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param remarks
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findLoggerInfo.do")
	@ResponseBody
	public String findLoggerInfo(String bt,String et,String user_id,String remarks,int bNum,int rows,String company_id,HttpSession session,HttpServletRequest request) {
		User user = new User();
		user  = (User) session.getAttribute("user");
		int company_id_user  = 0;
		if(user!=null){
			company_id_user = user.getCompany_id();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject = loggerService.findLoggerInfo(bt,et,bNum,rows,user_id,remarks,company_id,company_id_user);
		return jsonObject.toString();
	}
}
