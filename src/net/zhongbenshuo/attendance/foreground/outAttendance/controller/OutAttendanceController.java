package net.zhongbenshuo.attendance.foreground.outAttendance.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.foreground.outAttendance.service.OutAttendanceService;

@Controller
@RequestMapping(value = "/OutAttendanceController", produces = "text/html;charset=UTF-8")
public class OutAttendanceController {
	@Autowired
	OutAttendanceService outAttendanceService;
	
	/***
	 * 外勤打卡审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findOutAttendance.do")
	@ResponseBody
	public String findOutAttendance(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findOutAttendance(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 补卡审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findappealAttendance.do")
	@ResponseBody
	public String findappealAttendance(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findappealAttendance(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 加班审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findoverTime.do")
	@ResponseBody
	public String findoverTime(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findoverTime(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 外出审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findoutGoing.do")
	@ResponseBody
	public String findoutGoing(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findoutGoing(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 出差审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findbusinessTraveI.do")
	@ResponseBody
	public String findbusinessTraveI(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findbusinessTraveI(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	
	/**
	 * 假期审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findvacation.do")
	@ResponseBody
	public String findvacation(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findvacation(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	
	/***
	 * 外勤打卡审批结果处理
	 * @param outAttendanceRecordAudit_id
	 * @param out_attendance_id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitAuditOutAttendance.do")
	@ResponseBody
	public String submitAuditOutAttendance(String outAttendanceRecordAudit_id,String out_attendance_id,String result_id,
			String audit_status,String audit_resmarks,String id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.submitAuditOutAttendance(outAttendanceRecordAudit_id,out_attendance_id,result_id,
				audit_status,audit_resmarks,id,session,request);
		return jsonObject.toString();
	}
	/**
	 * 提交补卡审批的信息
	 * @param appealAttendanceRecordAudit_id
	 * @param appeal_attendance_id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitAuditappealAttendance.do")
	@ResponseBody
	public String submitAuditappealAttendance(String appealAttendanceRecordAudit_id,String appeal_attendance_id,String result_id,
			String audit_status,String audit_resmarks,String id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.submitAuditappealAttendance(appealAttendanceRecordAudit_id,appeal_attendance_id,result_id,
				audit_status,audit_resmarks,id,session,request);
		return jsonObject.toString();
	}
	/**
	 * 提交加班审批的信息
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitAuditoverTime.do")
	@ResponseBody
	public String submitAuditoverTime(String audit_id,String id,String result_id,
			String audit_status,String audit_resmarks,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.submitAuditoverTime(audit_id,id,result_id,
				audit_status,audit_resmarks,session,request);
		return jsonObject.toString();
	}
	/**
	 * 提交外出审批的信息
	 * @param audit_id
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitAuditoutGoing.do")
	@ResponseBody
	public String submitAuditoutGoing(String audit_id,String id,String result_id,
			String audit_status,String audit_resmarks,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.submitAuditoutGoing(audit_id,id,result_id,
				audit_status,audit_resmarks,session,request);
		return jsonObject.toString();
	}
	
	/**
	 * 提交出差审批的信息
	 * @param audit_id
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitAuditbusinessTraveI.do")
	@ResponseBody
	public String submitAuditbusinessTraveI(String audit_id,String id,String result_id,
			String audit_status,String audit_resmarks,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.submitAuditbusinessTraveI(audit_id,id,result_id,
				audit_status,audit_resmarks,session,request);
		return jsonObject.toString();
	}
	/**
	 * 提交假期审批的信息
	 * @param audit_id
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitAuditvacation.do")
	@ResponseBody
	public String submitAuditvacation(String audit_id,String id,String result_id,
			String audit_status,String audit_resmarks,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.submitAuditvacation(audit_id,id,result_id,
				audit_status,audit_resmarks,session,request);
		return jsonObject.toString();
	}
	/**
	 * 外勤打卡审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findauditOutAttendance_historical_records.do")
	@ResponseBody
	public String findauditOutAttendance_historical_records(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findauditOutAttendance_historical_records(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 补卡审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findauditappealAttendance_historical_records.do")
	@ResponseBody
	public String findauditappealAttendance_historical_records(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findauditappealAttendance_historical_records(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 加班审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findauditoverTime_historical_records.do")
	@ResponseBody
	public String findauditoverTime_historical_records(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findauditoverTime_historical_records(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 外出审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findauditoutGoing_historical_records.do")
	@ResponseBody
	public String findauditoutGoing_historical_records(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findauditoutGoing_historical_records(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	
	/**
	 * 出差审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findauditbusinessTraveI_historical_records.do")
	@ResponseBody
	public String findauditbusinessTraveI_historical_records(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findauditbusinessTraveI_historical_records(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 假期审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findauditvacation_historical_records.do")
	@ResponseBody
	public String findauditvacation_historical_records(String bt,String et,String user_id,String applicant_user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = outAttendanceService.findauditvacation_historical_records(bt+" 00:00:00",et+" 23:59:59",user_id,applicant_user_id,bNum,rows);
		return jsonObject.toString();
	}
	
}
