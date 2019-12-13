package net.zhongbenshuo.attendance.bean;

import java.util.ArrayList;
import java.util.List;

public class AllUserInfo {

	private int department_id;
	
	private String department;
	
	private int priority;
	
	private List<User> users = new ArrayList<User>();

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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "AllUserInfo [department_id=" + department_id + ", department=" + department + ", priority=" + priority
				+ ", users=" + users + "]";
	}
	
}
