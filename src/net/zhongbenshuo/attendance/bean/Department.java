package net.zhongbenshuo.attendance.bean;

public class Department {
	public int department_id;//部门iD
	public String department;//部门名称
	public int priority;//部门优先级
	public int effective;//是否有效，1有效，0无效
	public int company_id;//公司ID
	public int getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	@Override
	public String toString() {
		return "Department [department_id=" + department_id + ", department=" + department + ", priority=" + priority
				+ ", effective=" + effective + ", company_id=" + company_id + "]";
	}
	
	
}
