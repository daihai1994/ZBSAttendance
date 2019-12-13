package net.zhongbenshuo.attendance.bean;

import java.util.ArrayList;
import java.util.List;

public class AllUserInfoAddressBook {

	private int department_id_all;
	
	private String department_all;
	
	private int priority;
	
	private List<User> users = new ArrayList<User>();

	public int getDepartment_id_all() {
		return department_id_all;
	}

	public void setDepartment_id_all(int department_id_all) {
		this.department_id_all = department_id_all;
	}

	public String getDepartment_all() {
		return department_all;
	}

	public void setDepartment_all(String department_all) {
		this.department_all = department_all;
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
		return "AllUserInfoAddressBook [department_id_all=" + department_id_all + ", department_all=" + department_all
				+ ", priority=" + priority + ", users=" + users + "]";
	}

	
	
}
