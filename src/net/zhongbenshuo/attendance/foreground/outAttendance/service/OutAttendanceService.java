package net.zhongbenshuo.attendance.foreground.outAttendance.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

public interface OutAttendanceService {
	/***
	 * 查询外勤打卡审批信息
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findOutAttendance(String bt, String et, String user_id, String applicant_user_id, int bNum, int rows);
	/***
	 * 外勤打卡审批处理
	 * @param outAttendanceRecordAudit_id
	 * @param out_attendance_id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitAuditOutAttendance(String outAttendanceRecordAudit_id, String out_attendance_id, String result_id,
			String audit_status, String audit_resmarks,String id, HttpSession session, HttpServletRequest request);
	/**
	 * 审批记录
	 * @param outAttendanceRecordAudit_id
	 * @param out_attendance_id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject findauditOutAttendance_historical_records(String string, String string2, String user_id,
			String applicant_user_id, int bNum, int rows);
	/**
	 * 补卡审批查询
	 * @param string
	 * @param string2
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findappealAttendance(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows);
	/**
	 * 补卡审批提交
	 * @param appealAttendanceRecordAudit_id
	 * @param appeal_attendance_id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitAuditappealAttendance(String appealAttendanceRecordAudit_id, String appeal_attendance_id,
			String result_id, String audit_status, String audit_resmarks,String id, HttpSession session,
			HttpServletRequest request);
	/**
	 * 加班审批查询
	 * @param string
	 * @param string2
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findoverTime(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows);
	/**
	 * 加班审批提交
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitAuditoverTime(String audit_id,String id, String result_id, String audit_status, String audit_resmarks,
			HttpSession session, HttpServletRequest request);
	/**
	 * 补卡审批查询历史记录
	 * @param string
	 * @param string2
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findauditappealAttendance_historical_records(String string, String string2, String user_id,
			String applicant_user_id, int bNum, int rows);
	/**
	 * 加班审批查询历史记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findauditoverTime_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows);
	/**
	 * 假期审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findvacation(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows);
	/**
	 * 假期审批的提交
	 * @param audit_id
	 * @param id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitAuditvacation(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, HttpSession session, HttpServletRequest request);
	/**
	 * 假期审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findauditvacation_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows);
	/**
	 * 外出审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findoutGoing(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows);
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
	JSONObject submitAuditoutGoing(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, HttpSession session, HttpServletRequest request);
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
	JSONObject findauditoutGoing_historical_records(String bt, String et, String user_id,
			String applicant_user_id, int bNum, int rows);
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
	JSONObject findbusinessTraveI(String bt, String et, String user_id, String applicant_user_id, int bNum,
			int rows);
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
	JSONObject submitAuditbusinessTraveI(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, HttpSession session, HttpServletRequest request);
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
	JSONObject findauditbusinessTraveI_historical_records(String string, String string2, String user_id,
			String applicant_user_id, int bNum, int rows);
	

}
