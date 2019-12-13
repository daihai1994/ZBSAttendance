package net.zhongbenshuo.attendance.bean;

public class OutAttendanceRecordAudit {
	private int id;//外勤考勤审核人表
	private int out_Attendance_Record_Id;//外勤考勤记录ID
	private int user_id;//审核人ID
	private int father_audit_id;//父级审核记录ID如果是第一个，那就是0
	private int audit_status;//审核状态，1是当前需要审核，0是还没到这个人审核，2是审核通过，3是审核不通过
	private String audit_remarks;//审核人备注
	private String audit_time;//审批时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOut_Attendance_Record_Id() {
		return out_Attendance_Record_Id;
	}
	public void setOut_Attendance_Record_Id(int out_Attendance_Record_Id) {
		this.out_Attendance_Record_Id = out_Attendance_Record_Id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getFather_audit_id() {
		return father_audit_id;
	}
	public void setFather_audit_id(int father_audit_id) {
		this.father_audit_id = father_audit_id;
	}
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
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}
	@Override
	public String toString() {
		return "OutAttendanceRecordAudit [id=" + id + ", out_Attendance_Record_Id=" + out_Attendance_Record_Id
				+ ", user_id=" + user_id + ", father_audit_id=" + father_audit_id + ", audit_status=" + audit_status
				+ ", audit_remarks=" + audit_remarks + ", audit_time=" + audit_time + "]";
	}
	
}
