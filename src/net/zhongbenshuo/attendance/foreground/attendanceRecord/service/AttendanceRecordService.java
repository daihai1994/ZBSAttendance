package net.zhongbenshuo.attendance.foreground.attendanceRecord.service;

import javax.servlet.ServletOutputStream;

import net.sf.json.JSONObject;

public interface AttendanceRecordService {
	/**
	 * 查询某一个人的考勤记录和考勤状态
	 * @param user_id
	 * @param year
	 * @param month
	 * @return
	 */
	JSONObject findAttendanceRecord(String user_id, String year, String month,String company_id);
	/**
	 * 查询公司下的所有员工
	 * @param company_id 公司ID
	 * @return
	 */
	String findUser(String company_id);
	/**
	 * 
	 * @param titles 表格抬头
	 * @param outputStream
	 * @param user 员工
	 * @param year 年
	 * @param month 月
	 */
	void exportexportReportAttendanceRecord(String[] titles, ServletOutputStream outputStream,String user,String year,String month,String company_id);
	void exportexportReportAttendanceAll(String[] titles,  ServletOutputStream outputStream, String user, String year,
			String month, String company_id);

}
