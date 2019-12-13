package net.zhongbenshuo.attendance.bean;

/**
 * 用户实体类
 * 
 * @author liyul
 * 
 */

public class User {
	private int id;
	private int user_id = 0;//user_id
	private String user_pwd="";//密码
	private int gender_id = 0;//性别
	private String gender = "";//性别
	private String user_name = "";//姓名
	private String phone_number = "";//号码
	private String mail_address = "";//邮箱
	private String contact_address  = "";//联系地址
	private String emergency_contact_name  = "";//紧急联系人
	private String emergency_contact_phone = "";//紧急联系电话
	private String icon_url = "";//头像地址
	private int role = 0;//是不是管理员，1是管理员，0是普通用户
	private int effective = 0;//是不是有效1是有效，0是无效
	private String time_register;//创建时间
	private String ip_register = "";//注册IP
	
	private int company_id = -1;//公司ID
	private String company_name;//公司名称
	private int enable_application;//是否启用申请0不启用，1启用
	
	private String department_id;//部门ID
	private String position_id;//职位ID
	private String department = "";//部门
	private String position = "";//职位
	private String job_number;//工号

	private String authority_id;//权限ID
	private String authority;//权限
	private int size;//多少条
	
	private String entry_time;//入职时间
	
	private String open_id;//openid
	private String unionid;//unionid
	private int employeeTypeId = 0;//员工类型
	private int nationalRestSystem;//是否是国家休息制度 1是，0不是
	private int monthVacationDay;//月休息天数
	private int overTimeLimit;//加班界限
	private int lateLimit;//迟到早退界限
	private int mealAllowance;//是否有餐补，1有，0没有
	
	private String status;
	private String remarks;
	
	private int faceCount;//人脸图片的个数
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public int getGender_id() {
		return gender_id;
	}

	public void setGender_id(int gender_id) {
		this.gender_id = gender_id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getMail_address() {
		return mail_address;
	}

	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
	}

	public String getContact_address() {
		return contact_address;
	}

	public void setContact_address(String contact_address) {
		this.contact_address = contact_address;
	}

	public String getEmergency_contact_name() {
		return emergency_contact_name;
	}

	public void setEmergency_contact_name(String emergency_contact_name) {
		this.emergency_contact_name = emergency_contact_name;
	}

	public String getEmergency_contact_phone() {
		return emergency_contact_phone;
	}

	public void setEmergency_contact_phone(String emergency_contact_phone) {
		this.emergency_contact_phone = emergency_contact_phone;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getEffective() {
		return effective;
	}

	public void setEffective(int effective) {
		this.effective = effective;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime_register() {
		return time_register;
	}

	public void setTime_register(String time_register) {
		this.time_register = time_register;
	}

	public String getIp_register() {
		return ip_register;
	}

	public void setIp_register(String ip_register) {
		this.ip_register = ip_register;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	
	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getPosition_id() {
		return position_id;
	}

	public void setPosition_id(String position_id) {
		this.position_id = position_id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getJob_number() {
		return job_number;
	}

	public void setJob_number(String job_number) {
		this.job_number = job_number;
	}

	

	public String getAuthority_id() {
		return authority_id;
	}

	public void setAuthority_id(String authority_id) {
		this.authority_id = authority_id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	
	public String getEntry_time() {
		return entry_time;
	}

	public void setEntry_time(String entry_time) {
		this.entry_time = entry_time;
	}

	public int getFaceCount() {
		return faceCount;
	}

	public void setFaceCount(int faceCount) {
		this.faceCount = faceCount;
	}
	public int getEnable_application() {
		return enable_application;
	}
	public void setEnable_application(int enable_application) {
		this.enable_application = enable_application;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public int getEmployeeTypeId() {
		return employeeTypeId;
	}

	public void setEmployeeTypeId(int employeeTypeId) {
		this.employeeTypeId = employeeTypeId;
	}

	public int getNationalRestSystem() {
		return nationalRestSystem;
	}

	public void setNationalRestSystem(int nationalRestSystem) {
		this.nationalRestSystem = nationalRestSystem;
	}

	public int getMonthVacationDay() {
		return monthVacationDay;
	}

	public void setMonthVacationDay(int monthVacationDay) {
		this.monthVacationDay = monthVacationDay;
	}

	public int getOverTimeLimit() {
		return overTimeLimit;
	}

	public void setOverTimeLimit(int overTimeLimit) {
		this.overTimeLimit = overTimeLimit;
	}

	public int getLateLimit() {
		return lateLimit;
	}

	public void setLateLimit(int lateLimit) {
		this.lateLimit = lateLimit;
	}

	public int getMealAllowance() {
		return mealAllowance;
	}

	public void setMealAllowance(int mealAllowance) {
		this.mealAllowance = mealAllowance;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", user_id=" + user_id + ", user_pwd=" + user_pwd + ", gender_id=" + gender_id
				+ ", gender=" + gender + ", user_name=" + user_name + ", phone_number=" + phone_number
				+ ", mail_address=" + mail_address + ", contact_address=" + contact_address
				+ ", emergency_contact_name=" + emergency_contact_name + ", emergency_contact_phone="
				+ emergency_contact_phone + ", icon_url=" + icon_url + ", role=" + role + ", effective=" + effective
				+ ", time_register=" + time_register + ", ip_register=" + ip_register + ", company_id=" + company_id
				+ ", company_name=" + company_name + ", enable_application=" + enable_application + ", department_id="
				+ department_id + ", position_id=" + position_id + ", department=" + department + ", position="
				+ position + ", job_number=" + job_number + ", authority_id=" + authority_id + ", authority="
				+ authority + ", size=" + size + ", entry_time=" + entry_time + ", open_id=" + open_id + ", unionid="
				+ unionid + ", employeeTypeId=" + employeeTypeId + ", nationalRestSystem=" + nationalRestSystem
				+ ", monthVacationDay=" + monthVacationDay + ", overTimeLimit=" + overTimeLimit + ", lateLimit="
				+ lateLimit + ", mealAllowance=" + mealAllowance + ", status=" + status + ", remarks=" + remarks
				+ ", faceCount=" + faceCount + "]";
	}
	
}
