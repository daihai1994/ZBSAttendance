package net.zhongbenshuo.attendance.bean;

import java.util.ArrayList;
import java.util.List;

import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;

public class UserInfoStatus {
	private int user_id;//名称ID
	private String user_name;//名称
	private String remarks;//备注
	private String status;//状态
	private String color;//颜色
	private String phone_number;//电话
	private String icon_url;//头像地址
	private String open_id;
	private String unionid;
	private List<BusinessTraveIRecord> businessTraveIRecords = new ArrayList<BusinessTraveIRecord>();//出差
	private List<OutGoing> outGoing = new ArrayList<OutGoing>();//外出
	private List<Vacation> vacation = new ArrayList<Vacation>();//假期
	private List<Overtime> overtime = new ArrayList<Overtime>();//加班
	private List<AttendanceRecord> attendanceRecords = new ArrayList<AttendanceRecord>();//考勤记录表
	
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getIcon_url() {
		return icon_url;
	}
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public List<BusinessTraveIRecord> getBusinessTraveIRecords() {
		return businessTraveIRecords;
	}
	public void setBusinessTraveIRecords(List<BusinessTraveIRecord> businessTraveIRecords) {
		this.businessTraveIRecords = businessTraveIRecords;
	}
	public List<OutGoing> getOutGoing() {
		return outGoing;
	}
	public void setOutGoing(List<OutGoing> outGoing) {
		this.outGoing = outGoing;
	}
	public List<Vacation> getVacation() {
		return vacation;
	}
	public void setVacation(List<Vacation> vacation) {
		this.vacation = vacation;
	}
	public List<Overtime> getOvertime() {
		return overtime;
	}
	public void setOvertime(List<Overtime> overtime) {
		this.overtime = overtime;
	}
	public List<AttendanceRecord> getAttendanceRecords() {
		return attendanceRecords;
	}
	public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
		this.attendanceRecords = attendanceRecords;
	}
	
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	@Override
	public String toString() {
		return "UserInfoStatus [user_id=" + user_id + ", user_name=" + user_name + ", remarks=" + remarks + ", status="
				+ status + ", color=" + color + ", phone_number=" + phone_number + ", icon_url=" + icon_url
				+ ", open_id=" + open_id + ", unionid=" + unionid + ", businessTraveIRecords=" + businessTraveIRecords
				+ ", outGoing=" + outGoing + ", vacation=" + vacation + ", overtime=" + overtime
				+ ", attendanceRecords=" + attendanceRecords + "]";
	}
	
	
}
