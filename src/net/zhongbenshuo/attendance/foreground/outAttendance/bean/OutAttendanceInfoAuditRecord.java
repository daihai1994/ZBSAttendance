package net.zhongbenshuo.attendance.foreground.outAttendance.bean;

public class OutAttendanceInfoAuditRecord {
	private int audit_status;//审批结果 2是通过，3是不通过
	private String audit_remarks;//审批备注
	private String user_name;//审批人名称
	private String audit_time;//审批时间
	private int user_id;//审批人user_id
	public int getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(int audit_status) {
		this.audit_status = audit_status;
	}
	public String getAudit_remarks() {
		return audit_remarks;
	}
	public void setAudit_remarks(String audit_remarks) {
		this.audit_remarks = audit_remarks;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "OutAttendanceInfoAuditRecord [audit_status=" + audit_status + ", audit_remarks=" + audit_remarks
				+ ", user_name=" + user_name + ", audit_time=" + audit_time + ", user_id=" + user_id + "]";
	}
	
}
