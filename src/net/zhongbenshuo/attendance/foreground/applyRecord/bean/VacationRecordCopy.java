package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

public class VacationRecordCopy {
	private int id;//假期申请抄送表ID
	private int user_id;//抄送人
	private int vacation_record_id;//假期申请详情表ID
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
	public int getVacation_record_id() {
		return vacation_record_id;
	}
	public void setVacation_record_id(int vacation_record_id) {
		this.vacation_record_id = vacation_record_id;
	}
	@Override
	public String toString() {
		return "VacationRecordCopy [id=" + id + ", user_id=" + user_id + ", vacation_record_id=" + vacation_record_id
				+ "]";
	}
	
}
