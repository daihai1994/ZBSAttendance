package net.zhongbenshuo.attendance.bean;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecord implements Comparable<AttendanceRecord> {
	
	
	private int id;//主键
	private int user_id;//用户userid
	
	private String user_name;//用户名称
	
	private int attendance_type;//考勤类型（签到，签退，外勤）
	
	private double attendance_longitude;//经度
	
	private double attendance_latitude;//纬度
	
	private String attendance_address;//地址
	
	private String attendance_time;//时间
	
	private int out_attendance;//是否外勤1是，0 否
	
	private int out_attendance_id;//外勤详细信息的Id
	
	private int rule_id;//规则ID
	
	private int result_id;//考勤结果ID
	
	private String result;//考勤结果
	
	private String remarks;//外勤打卡备注
	
	private String appeal_time;//补卡时间
	
	private int appeal_attendance;//是否补卡，1是，0不是
	
	private int appeal_attendance_id;//补卡详情信息ID
	
	private List<String> audit_user = new ArrayList<String>();//审批人
	
	private List<String> copy_user = new ArrayList<String>();//抄送人
	
	private int holidayStatus;//是否假期，1是，0不是
	
	private String rule_time_work;//工作开始时间
	
	private String rule_time_off_work;//工作结束时间
	
	private String rule_rest_start;//工作休息开始时间
	
	private String rule_rest_end;//工作休息结束时间
	
	private String rule_name;//规则地址
	
	private boolean status;//true是打卡，false这是访客
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getAttendance_type() {
		return attendance_type;
	}

	public void setAttendance_type(int attendance_type) {
		this.attendance_type = attendance_type;
	}

	public double getAttendance_longitude() {
		return attendance_longitude;
	}

	public void setAttendance_longitude(double attendance_longitude) {
		this.attendance_longitude = attendance_longitude;
	}

	public double getAttendance_latitude() {
		return attendance_latitude;
	}

	public void setAttendance_latitude(double attendance_latitude) {
		this.attendance_latitude = attendance_latitude;
	}

	public String getAttendance_address() {
		return attendance_address;
	}

	public void setAttendance_address(String attendance_address) {
		this.attendance_address = attendance_address;
	}

	public String getAttendance_time() {
		return attendance_time;
	}

	public void setAttendance_time(String attendance_time) {
		this.attendance_time = attendance_time;
	}

	public int getOut_attendance() {
		return out_attendance;
	}

	public void setOut_attendance(int out_attendance) {
		this.out_attendance = out_attendance;
	}

	public int getOut_attendance_id() {
		return out_attendance_id;
	}

	public void setOut_attendance_id(int out_attendance_id) {
		this.out_attendance_id = out_attendance_id;
	}

	public int getRule_id() {
		return rule_id;
	}

	public void setRule_id(int rule_id) {
		this.rule_id = rule_id;
	}

	public int getResult_id() {
		return result_id;
	}

	public void setResult_id(int result_id) {
		this.result_id = result_id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<String> getAudit_user() {
		return audit_user;
	}

	public void setAudit_user(List<String> audit_user) {
		this.audit_user = audit_user;
	}

	public List<String> getCopy_user() {
		return copy_user;
	}

	public void setCopy_user(List<String> copy_user) {
		this.copy_user = copy_user;
	}
	public int getHolidayStatus() {
		return holidayStatus;
	}

	public void setHolidayStatus(int holidayStatus) {
		this.holidayStatus = holidayStatus;
	}

	public String getAppeal_time() {
		return appeal_time;
	}

	public void setAppeal_time(String appeal_time) {
		this.appeal_time = appeal_time;
	}

	public int getAppeal_attendance() {
		return appeal_attendance;
	}

	public void setAppeal_attendance(int appeal_attendance) {
		this.appeal_attendance = appeal_attendance;
	}

	public int getAppeal_attendance_id() {
		return appeal_attendance_id;
	}

	public void setAppeal_attendance_id(int appeal_attendance_id) {
		this.appeal_attendance_id = appeal_attendance_id;
	}

	public String getRule_time_work() {
		return rule_time_work;
	}

	public void setRule_time_work(String rule_time_work) {
		this.rule_time_work = rule_time_work;
	}

	public String getRule_time_off_work() {
		return rule_time_off_work;
	}

	public void setRule_time_off_work(String rule_time_off_work) {
		this.rule_time_off_work = rule_time_off_work;
	}

	public String getRule_rest_start() {
		return rule_rest_start;
	}

	public void setRule_rest_start(String rule_rest_start) {
		this.rule_rest_start = rule_rest_start;
	}

	public String getRule_rest_end() {
		return rule_rest_end;
	}

	public void setRule_rest_end(String rule_rest_end) {
		this.rule_rest_end = rule_rest_end;
	}

	public String getRule_name() {
		return rule_name;
	}

	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}

	@Override
	public String toString() {
		return "AttendanceRecord [id=" + id + ", user_id=" + user_id + ", user_name=" + user_name + ", attendance_type="
				+ attendance_type + ", attendance_longitude=" + attendance_longitude + ", attendance_latitude="
				+ attendance_latitude + ", attendance_address=" + attendance_address + ", attendance_time="
				+ attendance_time + ", out_attendance=" + out_attendance + ", out_attendance_id=" + out_attendance_id
				+ ", rule_id=" + rule_id + ", result_id=" + result_id + ", result=" + result + ", remarks=" + remarks
				+ ", appeal_time=" + appeal_time + ", appeal_attendance=" + appeal_attendance
				+ ", appeal_attendance_id=" + appeal_attendance_id + ", audit_user=" + audit_user + ", copy_user="
				+ copy_user + ", holidayStatus=" + holidayStatus + ", rule_time_work=" + rule_time_work
				+ ", rule_time_off_work=" + rule_time_off_work + ", rule_rest_start=" + rule_rest_start
				+ ", rule_rest_end=" + rule_rest_end + ", rule_name=" + rule_name + ", status=" + status + "]";
	}

	@Override
	public int compareTo(AttendanceRecord o) {
		// TODO Auto-generated method stub
		return this.getAttendance_time().compareTo(o.getAttendance_time());
	}
	 
}
