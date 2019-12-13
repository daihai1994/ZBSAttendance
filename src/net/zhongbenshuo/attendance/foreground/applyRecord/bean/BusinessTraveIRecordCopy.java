package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

public class BusinessTraveIRecordCopy {
	private int id;//出差抄送人记录信息ID
	private int user_id;//出差抄送人ID
	private int businessTraveI_record_id;//出差申请详情信息
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
	public int getBusinessTraveI_record_id() {
		return businessTraveI_record_id;
	}
	public void setBusinessTraveI_record_id(int businessTraveI_record_id) {
		this.businessTraveI_record_id = businessTraveI_record_id;
	}
	@Override
	public String toString() {
		return "BusinessTraveIRecordCopy [id=" + id + ", user_id=" + user_id + ", businessTraveI_record_id="
				+ businessTraveI_record_id + "]";
	}
	
}
