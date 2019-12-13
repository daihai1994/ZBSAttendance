package net.zhongbenshuo.attendance.foreground.company.bean;

public class PositionJSON {
	private String position;
	private String id;
	private String oldpriority;
	private String priority;
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
		return "PositionJSON [position=" + position + ", id=" + id + ", oldpriority=" + oldpriority + ", priority="
				+ priority + "]";
	}
	
}
