package net.zhongbenshuo.attendance.foreground.announcement.bean;

public class Priority {
	private int priority_id;//优先级ID
	private String priority;//优先级
	public int getPriority_id() {
		return priority_id;
	}
	public void setPriority_id(int priority_id) {
		this.priority_id = priority_id;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "Priority [priority_id=" + priority_id + ", priority=" + priority + "]";
	}
	
}
