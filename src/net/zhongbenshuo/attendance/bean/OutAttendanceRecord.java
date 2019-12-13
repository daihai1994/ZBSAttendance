package net.zhongbenshuo.attendance.bean;

public class OutAttendanceRecord {
	private int id;//外勤打卡记录ID
	private String remarks;//外勤打卡备注
	private int status;//外勤打开状态 ，0是无人审批，1是开始审批，2是审批通过，3是审批不通过
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
	@Override
	public String toString() {
		return "OutAttendanceRecord [id=" + id + ", remarks=" + remarks + ", status=" + status + "]";
	}
	
	
}
