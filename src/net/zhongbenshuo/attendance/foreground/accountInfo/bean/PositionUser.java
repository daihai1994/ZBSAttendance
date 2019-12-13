package net.zhongbenshuo.attendance.foreground.accountInfo.bean;

public class PositionUser {
	private int id;//职位和人员的关系表
	private int position_id;//职位id
	private int user_id;//用户ID
	private String user_name;//用户名称
	private int effective;//是否有效，1有效0无效
	private int priority;//优先级
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPosition_id() {
		return position_id;
	}
	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	@Override
	public String toString() {
		return "PositionUser [id=" + id + ", position_id=" + position_id + ", user_id=" + user_id + ", user_name="
				+ user_name + ", effective=" + effective + ", priority=" + priority + "]";
	}
	
}
