package net.zhongbenshuo.attendance.foreground.wage.bean;

public class Wage {
	private int id;//工资表主键
	private String mail_address;//邮箱地址
	private int user_id;//用户ID
	private String user_name;//用户姓名
	private int month;//月份
	private float wage_basic;//基本工资
	private float working;//出勤小时
	private float attendance_days;//出勤天数
	private float subsidy;//补助（餐补）
	private float overtime_hours;//加班小时
	private int mealAllowanceDay;//餐补天数
	private int workingDay;//工作天数
	private int laterSize;//迟到次数
	private int leaveEarlySize;//早退次数
	private int lackSignInSize;//缺签到卡
	private int lackSignBackSize;//缺签退卡
	private float social_security_fund_persional;//个人承担社保
	private float social_security_fund_company;//公司承担社保
	private float individual_income_tax;//个人所得税
	private float wage_real;//实发工资
	private float agent_distribution_boc;//中国银行代发
	private float agent_distribution_cmbc;//民生银行代发
	private int issuing_department_id;//发布部门ID
	private String department;//发布部门
	private String time;//发布时间
	private int year;//年份
	private float reimbursement;//报销
	private float achievement;//业绩
	private int company_id;//公司ID
	private int size;//个数
	
	public int getMealAllowanceDay() {
		return mealAllowanceDay;
	}
	public void setMealAllowanceDay(int mealAllowanceDay) {
		this.mealAllowanceDay = mealAllowanceDay;
	}
	public int getWorkingDay() {
		return workingDay;
	}
	public void setWorkingDay(int workingDay) {
		this.workingDay = workingDay;
	}
	public int getLaterSize() {
		return laterSize;
	}
	public void setLaterSize(int laterSize) {
		this.laterSize = laterSize;
	}
	public int getLeaveEarlySize() {
		return leaveEarlySize;
	}
	public void setLeaveEarlySize(int leaveEarlySize) {
		this.leaveEarlySize = leaveEarlySize;
	}
	public int getLackSignInSize() {
		return lackSignInSize;
	}
	public void setLackSignInSize(int lackSignInSize) {
		this.lackSignInSize = lackSignInSize;
	}
	public int getLackSignBackSize() {
		return lackSignBackSize;
	}
	public void setLackSignBackSize(int lackSignBackSize) {
		this.lackSignBackSize = lackSignBackSize;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getMail_address() {
		return mail_address;
	}
	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
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
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public float getWage_basic() {
		return wage_basic;
	}
	public void setWage_basic(float wage_basic) {
		this.wage_basic = wage_basic;
	}
	public float getAttendance_days() {
		return attendance_days;
	}
	public void setAttendance_days(float attendance_days) {
		this.attendance_days = attendance_days;
	}
	public float getSubsidy() {
		return subsidy;
	}
	public void setSubsidy(float subsidy) {
		this.subsidy = subsidy;
	}
	public float getOvertime_hours() {
		return overtime_hours;
	}
	public void setOvertime_hours(float overtime_hours) {
		this.overtime_hours = overtime_hours;
	}
	public float getSocial_security_fund_persional() {
		return social_security_fund_persional;
	}
	public void setSocial_security_fund_persional(float social_security_fund_persional) {
		this.social_security_fund_persional = social_security_fund_persional;
	}
	public float getSocial_security_fund_company() {
		return social_security_fund_company;
	}
	public void setSocial_security_fund_company(float social_security_fund_company) {
		this.social_security_fund_company = social_security_fund_company;
	}
	public float getIndividual_income_tax() {
		return individual_income_tax;
	}
	public void setIndividual_income_tax(float individual_income_tax) {
		this.individual_income_tax = individual_income_tax;
	}
	public float getWage_real() {
		return wage_real;
	}
	public void setWage_real(float wage_real) {
		this.wage_real = wage_real;
	}
	public float getAgent_distribution_boc() {
		return agent_distribution_boc;
	}
	public void setAgent_distribution_boc(float agent_distribution_boc) {
		this.agent_distribution_boc = agent_distribution_boc;
	}
	public float getAgent_distribution_cmbc() {
		return agent_distribution_cmbc;
	}
	public void setAgent_distribution_cmbc(float agent_distribution_cmbc) {
		this.agent_distribution_cmbc = agent_distribution_cmbc;
	}
	public int getIssuing_department_id() {
		return issuing_department_id;
	}
	public void setIssuing_department_id(int issuing_department_id) {
		this.issuing_department_id = issuing_department_id;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public float getReimbursement() {
		return reimbursement;
	}
	public void setReimbursement(float reimbursement) {
		this.reimbursement = reimbursement;
	}
	public float getAchievement() {
		return achievement;
	}
	public void setAchievement(float achievement) {
		this.achievement = achievement;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public float getWorking() {
		return working;
	}
	public void setWorking(float working) {
		this.working = working;
	}
	@Override
	public String toString() {
		return "Wage [id=" + id + ", mail_address=" + mail_address + ", user_id=" + user_id + ", user_name=" + user_name
				+ ", month=" + month + ", wage_basic=" + wage_basic + ", working=" + working + ", attendance_days="
				+ attendance_days + ", subsidy=" + subsidy + ", overtime_hours=" + overtime_hours
				+ ", mealAllowanceDay=" + mealAllowanceDay + ", workingDay=" + workingDay + ", laterSize=" + laterSize
				+ ", leaveEarlySize=" + leaveEarlySize + ", lackSignInSize=" + lackSignInSize + ", lackSignBackSize="
				+ lackSignBackSize + ", social_security_fund_persional=" + social_security_fund_persional
				+ ", social_security_fund_company=" + social_security_fund_company + ", individual_income_tax="
				+ individual_income_tax + ", wage_real=" + wage_real + ", agent_distribution_boc="
				+ agent_distribution_boc + ", agent_distribution_cmbc=" + agent_distribution_cmbc
				+ ", issuing_department_id=" + issuing_department_id + ", department=" + department + ", time=" + time
				+ ", year=" + year + ", reimbursement=" + reimbursement + ", achievement=" + achievement
				+ ", company_id=" + company_id + ", size=" + size + "]";
	}
	
}
