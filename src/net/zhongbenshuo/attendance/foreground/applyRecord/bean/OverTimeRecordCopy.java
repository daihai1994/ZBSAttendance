package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

public class OverTimeRecordCopy {
	private int id;//加班抄送人记录信息ID
	private int user_id;//加班抄送人ID
	private int over_time_record_id;//加班申请详情信息
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getOver_time_record_id() {
		return over_time_record_id;
	}
	public void setOver_time_record_id(int over_time_record_id) {
		this.over_time_record_id = over_time_record_id;
	}
	@Override
	public String toString() {
		return "OverTimeRecordCopy [id=" + id + ", user_id=" + user_id + ", over_time_record_id=" + over_time_record_id
				+ "]";
	}
	
}
