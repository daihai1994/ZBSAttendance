package net.zhongbenshuo.attendance.bean;

public class AttendanceFaceRecord {
	private int id;
	
	private int user_id;//用户ID
	
	private String user_name;//用户姓名
	
	private double attendance_longitude;//经度
	
	private double attendance_latitude;//纬度 
	
	private String attendance_address;//地址
	
	private String attendance_time;//时间
	
	private int rule_id;//规则ID
	
	private int attendance_type;//签到状态
	
	private String remarks;//备注
	
	private boolean status;//状态，是否是考勤，true是考勤，false不是考勤

	
	public int getAttendance_type() {
		return attendance_type;
	}

	public void setAttendance_type(int attendance_type) {
		this.attendance_type = attendance_type;
	}

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

	public int getRule_id() {
		return rule_id;
	}

	public void setRule_id(int rule_id) {
		this.rule_id = rule_id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AttendanceFaceRecord [id=" + id + ", user_id=" + user_id + ", user_name=" + user_name
				+ ", attendance_longitude=" + attendance_longitude + ", attendance_latitude=" + attendance_latitude
				+ ", attendance_address=" + attendance_address + ", attendance_time=" + attendance_time + ", rule_id="
				+ rule_id + ", remarks=" + remarks + ", status=" + status + "]";
	}
	
	
	
}
