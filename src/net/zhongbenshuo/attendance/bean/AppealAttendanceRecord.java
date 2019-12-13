package net.zhongbenshuo.attendance.bean;

public class AppealAttendanceRecord {
	private int id;//补卡申请详情表ID
	private String remarks;//补卡申请详情备注
	private int status;//补卡状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
	private String appeal_time;//补卡的哪天
	private int effective;//是否有效，1有效，0无效
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAppeal_time() {
		return appeal_time;
	}
	public void setAppeal_time(String appeal_time) {
		this.appeal_time = appeal_time;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	@Override
	public String toString() {
		return "AppealAttendanceRecord [id=" + id + ", remarks=" + remarks + ", status=" + status + ", appeal_time="
				+ appeal_time + ", effective=" + effective + "]";
	}
	
}
