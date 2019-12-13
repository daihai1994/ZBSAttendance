package net.zhongbenshuo.attendance.foreground.attendanceRecord.bean;

public class WorkingTimeUser {
	private int id;//每人每日的工作
	private String date;//几号
	private int company_id;//公司ID
	private int user_id;//用户ID
	private String user_name;//用户名称
	private float working;//工作时间
	private float overtimeTime;//加班时间
	private float vacationTime;//休假时间
	private int status;//工作日对于的结果0，休息日1，节假日2
	private float realityOvertimeTime;//实际加班时间
	private String week;//周几
	private int whetherWorking;//是否工作 1是，0不是
	private int whetherMealAllowance;//是否餐补 1是，0不是
	private int whetherLater;//是否迟到 1是，0不是
	private int whetherLeaveEarly;//是否早退 1是，0不是
	private int laterTime;//迟到几分钟
	private int leaveEarly;//早退几分钟
	private int whetherOverTime;//是否加班
	private int lackSignIn;//缺少签到卡 1是，0不是
	private int lackSignBack;//缺少签退卡 1是，0不是
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
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public float getWorking() {
		return working;
	}
	public void setWorking(float working) {
		this.working = working;
	}
	public float getOvertimeTime() {
		return overtimeTime;
	}
	public void setOvertimeTime(float overtimeTime) {
		this.overtimeTime = overtimeTime;
	}
	public float getVacationTime() {
		return vacationTime;
	}
	public void setVacationTime(float vacationTime) {
		this.vacationTime = vacationTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public float getRealityOvertimeTime() {
		return realityOvertimeTime;
	}
	public void setRealityOvertimeTime(float realityOvertimeTime) {
		this.realityOvertimeTime = realityOvertimeTime;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public int getWhetherWorking() {
		return whetherWorking;
	}
	public void setWhetherWorking(int whetherWorking) {
		this.whetherWorking = whetherWorking;
	}
	public int getWhetherMealAllowance() {
		return whetherMealAllowance;
	}
	public void setWhetherMealAllowance(int whetherMealAllowance) {
		this.whetherMealAllowance = whetherMealAllowance;
	}
	public int getWhetherLater() {
		return whetherLater;
	}
	public void setWhetherLater(int whetherLater) {
		this.whetherLater = whetherLater;
	}
	public int getWhetherLeaveEarly() {
		return whetherLeaveEarly;
	}
	public void setWhetherLeaveEarly(int whetherLeaveEarly) {
		this.whetherLeaveEarly = whetherLeaveEarly;
	}
	public int getLaterTime() {
		return laterTime;
	}
	public void setLaterTime(int laterTime) {
		this.laterTime = laterTime;
	}
	public int getLeaveEarly() {
		return leaveEarly;
	}
	public void setLeaveEarly(int leaveEarly) {
		this.leaveEarly = leaveEarly;
	}
	public int getWhetherOverTime() {
		return whetherOverTime;
	}
	public void setWhetherOverTime(int whetherOverTime) {
		this.whetherOverTime = whetherOverTime;
	}
	public int getLackSignIn() {
		return lackSignIn;
	}
	public void setLackSignIn(int lackSignIn) {
		this.lackSignIn = lackSignIn;
	}
	public int getLackSignBack() {
		return lackSignBack;
	}
	public void setLackSignBack(int lackSignBack) {
		this.lackSignBack = lackSignBack;
	}
	@Override
	public String toString() {
		return "WorkingTimeUser [id=" + id + ", date=" + date + ", company_id=" + company_id + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", working=" + working + ", overtimeTime=" + overtimeTime
				+ ", vacationTime=" + vacationTime + ", status=" + status + ", realityOvertimeTime="
				+ realityOvertimeTime + ", week=" + week + ", whetherWorking=" + whetherWorking
				+ ", whetherMealAllowance=" + whetherMealAllowance + ", whetherLater=" + whetherLater
				+ ", whetherLeaveEarly=" + whetherLeaveEarly + ", laterTime=" + laterTime + ", leaveEarly=" + leaveEarly
				+ ", whetherOverTime=" + whetherOverTime + ", lackSignIn=" + lackSignIn + ", lackSignBack="
				+ lackSignBack + "]";
	}
	
	
}
