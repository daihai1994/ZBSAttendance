package net.zhongbenshuo.attendance.foreground.outAttendance.bean;

public class UserBase {
	private int id;//人员的加班时间，请假时间等
	private float over_time;//加班时间
	private float leave_time;//请假时间
	private int user_id;//用户的ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getOver_time() {
		return over_time;
	}
	public void setOver_time(float over_time) {
		this.over_time = over_time;
	}
	public float getLeave_time() {
		return leave_time;
	}
	public void setLeave_time(float leave_time) {
		this.leave_time = leave_time;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "UserBase [id=" + id + ", over_time=" + over_time + ", leave_time=" + leave_time + ", user_id=" + user_id
				+ "]";
	}
	
}
