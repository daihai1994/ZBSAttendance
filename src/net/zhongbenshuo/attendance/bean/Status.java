package net.zhongbenshuo.attendance.bean;

import java.util.List;

public class Status {

	private String department;
	
	private List<UserStatus> realStatus;
	

	public String getDepartment() {
		return department;
	}


	public List<UserStatus> getRealStatus() {
		return realStatus;
	}


	public void setRealStatus(List<UserStatus> realStatus) {
		this.realStatus = realStatus;
	}


	public void setDepartment(String department) {
		this.department = department;
	}

}
