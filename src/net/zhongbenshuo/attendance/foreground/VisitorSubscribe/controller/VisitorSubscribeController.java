package net.zhongbenshuo.attendance.foreground.VisitorSubscribe.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.foreground.VisitorSubscribe.service.VisitorSubscribeService;

@Controller
@RequestMapping(value = "/VisitorSubscribeController", produces = "text/html;charset=UTF-8")
public class VisitorSubscribeController {
	public static Logger logger = LogManager.getLogger(VisitorSubscribeController.class);
	@Autowired
	VisitorSubscribeService visitorSubscribeService;
	/**
	 * 查询预约访客的审批
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findvisitorSubscribe.do")
	@ResponseBody
	public String findvisitorSubscribe(String bt,String et,String user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = visitorSubscribeService.findvisitorSubscribe(bt+" 00:00:00",et+" 23:59:59",user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 提交访客预约审批信息
	 * @param id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitAuditvisitorSubscribe.do")
	@ResponseBody
	public String submitAuditvisitorSubscribe(String id,String audit_status,String audit_resmarks,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = visitorSubscribeService.submitAuditvisitorSubscribe(id,audit_status,audit_resmarks);
		return jsonObject.toString();
	}
}
