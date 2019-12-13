package net.zhongbenshuo.attendance.bean;

public class AttendanceRecordInfo {
	private int id;//返回安卓的考勤记录的信息
	private int user_id;//用户ID
	private int attendance_type;//考勤类型，1是签到，2是签退
	private double attendance_longitude;//经度
	private double attendance_latitude;//纬度
	private String attendance_adderss;//考勤地址
	private String attendance_time;//考勤时间
	private int out_attendance;//是否外勤，1是。0不是
	private int out_attendance_id;//外勤详情记录ID
	private int rule_id;//规则ID
	private int result_id;//打卡结果集
	private String user_name;//姓名
	private String result;//结果集中文
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
	public String getAttendance_adderss() {
		return attendance_adderss;
	}
	public void setAttendance_adderss(String attendance_adderss) {
		this.attendance_adderss = attendance_adderss;
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
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "AttendanceRecordInfo [id=" + id + ", user_id=" + user_id + ", attendance_type=" + attendance_type
				+ ", attendance_longitude=" + attendance_longitude + ", attendance_latitude=" + attendance_latitude
				+ ", attendance_adderss=" + attendance_adderss + ", attendance_time=" + attendance_time
				+ ", out_attendance=" + out_attendance + ", out_attendance_id=" + out_attendance_id + ", rule_id="
				+ rule_id + ", result_id=" + result_id + ", user_name=" + user_name + ", result=" + result + "]";
	}
	
}
