package net.zhongbenshuo.attendance.bean;

public class WorkingTime {
	private int id;
	private String date;//年月日
	private String week;//星期几
	private int status;// 是工作日，1是休息日，2是节假日
	private int year;//年
	private int month;//月
	private int company_id;//公司ID
	private int user_id;//用户ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "WorkingTime [id=" + id + ", date=" + date + ", week=" + week + ", status=" + status + ", year=" + year
				+ ", month=" + month + ", company_id=" + company_id + ", user_id=" + user_id + "]";
	}
	
}
