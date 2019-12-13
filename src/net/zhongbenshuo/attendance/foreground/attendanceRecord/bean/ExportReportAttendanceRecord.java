package net.zhongbenshuo.attendance.foreground.attendanceRecord.bean;

public class ExportReportAttendanceRecord {
	private int user_id;//员工ID
	private int company_id;//公司ID
	private String userName;//员工
	private String year;//年份
	private String month;//月份
	private String day;//日
	private String signInTime;//签到时间
	private String signBackTime;//签退时间
	private String signInAddress;//签到地址
	private String signBackAddress;//签退地址
	private String signInType;//签到类型
	private String signBackType;//签退类型
	private String attendance_type;//签到类型
	private String attendance_address;//签到地址
	private String attendance_time;//签到时间
	private String out_attendance;//是否外勤
	private String appeal_attendance;//是否补卡
	private String result_id;//考勤结果Id
	private String result;//考勤结果
	private String time;//时间
	private String working;//工作时间
	private String overtimeTime;//加班时间
	private float overtimeTimeAll;//加班一个月时间
	private String vacationTime;//请假时间
	private String week;//周几
	private String status;//工作日，休息日，节假日
	private int whetherWorking;//是否工作 1是，0不是
	private int whetherMealAllowance;//是否餐补 1是，0不是
	private int whetherLater;//是否迟到 1是，0不是
	private int whetherLeaveEarly;//是否早退 1是，0不是
	private int laterTime;//迟到几分钟
	private int leaveEarly;//早退几分钟
	private int whetherOverTime;//是否加班
	private int lackSignIn;//缺少签到卡 1是，0不是
	private int lackSignBack;//缺少签退卡 1是，0不是
	private Object other;//处理exl数据，好接受数据
	
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
	public float getOvertimeTimeAll() {
		return overtimeTimeAll;
	}
	public void setOvertimeTimeAll(float overtimeTimeAll) {
		this.overtimeTimeAll = overtimeTimeAll;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getSignInTime() {
		return signInTime;
	}
	public void setSignInTime(String signInTime) {
		this.signInTime = signInTime;
	}
	public String getSignBackTime() {
		return signBackTime;
	}
	public void setSignBackTime(String signBackTime) {
		this.signBackTime = signBackTime;
	}
	public String getSignInAddress() {
		return signInAddress;
	}
	public void setSignInAddress(String signInAddress) {
		this.signInAddress = signInAddress;
	}
	public String getSignBackAddress() {
		return signBackAddress;
	}
	public void setSignBackAddress(String signBackAddress) {
		this.signBackAddress = signBackAddress;
	}
	public String getSignInType() {
		return signInType;
	}
	public void setSignInType(String signInType) {
		this.signInType = signInType;
	}
	public String getSignBackType() {
		return signBackType;
	}
	public void setSignBackType(String signBackType) {
		this.signBackType = signBackType;
	}
	public String getAttendance_type() {
		return attendance_type;
	}
	public void setAttendance_type(String attendance_type) {
		this.attendance_type = attendance_type;
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
	public String getOut_attendance() {
		return out_attendance;
	}
	public void setOut_attendance(String out_attendance) {
		this.out_attendance = out_attendance;
	}
	public String getAppeal_attendance() {
		return appeal_attendance;
	}
	public void setAppeal_attendance(String appeal_attendance) {
		this.appeal_attendance = appeal_attendance;
	}
	public String getResult_id() {
		return result_id;
	}
	public void setResult_id(String result_id) {
		this.result_id = result_id;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getWorking() {
		return working;
	}
	public void setWorking(String working) {
		this.working = working;
	}
	public String getOvertimeTime() {
		return overtimeTime;
	}
	public void setOvertimeTime(String overtimeTime) {
		this.overtimeTime = overtimeTime;
	}
	public String getVacationTime() {
		return vacationTime;
	}
	public void setVacationTime(String vacationTime) {
		this.vacationTime = vacationTime;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Object getOther() {
		return other;
	}
	public void setOther(Object other) {
		this.other = other;
	}
	@Override
	public String toString() {
		return "ExportReportAttendanceRecord [user_id=" + user_id + ", company_id=" + company_id + ", userName="
				+ userName + ", year=" + year + ", month=" + month + ", day=" + day + ", signInTime=" + signInTime
				+ ", signBackTime=" + signBackTime + ", signInAddress=" + signInAddress + ", signBackAddress="
				+ signBackAddress + ", signInType=" + signInType + ", signBackType=" + signBackType
				+ ", attendance_type=" + attendance_type + ", attendance_address=" + attendance_address
				+ ", attendance_time=" + attendance_time + ", out_attendance=" + out_attendance + ", appeal_attendance="
				+ appeal_attendance + ", result_id=" + result_id + ", result=" + result + ", time=" + time
				+ ", working=" + working + ", overtimeTime=" + overtimeTime + ", overtimeTimeAll=" + overtimeTimeAll
				+ ", vacationTime=" + vacationTime + ", week=" + week + ", status=" + status + ", whetherWorking="
				+ whetherWorking + ", whetherMealAllowance=" + whetherMealAllowance + ", whetherLater=" + whetherLater
				+ ", whetherLeaveEarly=" + whetherLeaveEarly + ", laterTime=" + laterTime + ", leaveEarly=" + leaveEarly
				+ ", whetherOverTime=" + whetherOverTime + ", lackSignIn=" + lackSignIn + ", lackSignBack="
				+ lackSignBack + ", other=" + other + "]";
	}
	
}
