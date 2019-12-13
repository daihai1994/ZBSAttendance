package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

public class OutGoingRecordCopy {
	private int id;//外出抄送人记录信息ID
	private int user_id;//外出抄送人ID
	private int outGoing_record_id;//外出申请详情信息
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
	public int getOutGoing_record_id() {
		return outGoing_record_id;
	}
	public void setOutGoing_record_id(int outGoing_record_id) {
		this.outGoing_record_id = outGoing_record_id;
	}
	@Override
	public String toString() {
		return "OutGoingRecordCopy [id=" + id + ", user_id=" + user_id + ", outGoing_record_id=" + outGoing_record_id
				+ "]";
	}
	
}
