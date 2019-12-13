package net.zhongbenshuo.attendance.foreground.company.bean;

public class PositionUserJSON {
	private String user_name;
	private String id;
	private String oldpriority;
	private String priority;
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOldpriority() {
		return oldpriority;
	}
	public void setOldpriority(String oldpriority) {
		this.oldpriority = oldpriority;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "PositionUserJSON [user_name=" + user_name + ", id=" + id + ", oldpriority=" + oldpriority
				+ ", priority=" + priority + "]";
	}
	
}
