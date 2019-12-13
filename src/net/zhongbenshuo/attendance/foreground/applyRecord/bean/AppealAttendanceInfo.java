package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

import java.util.ArrayList;
import java.util.List;

import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfoAuditRecord;

public class AppealAttendanceInfo {
	private int id;//考勤记录id
	private int user_id;//申请人user_id
	private String user_name;//申请人name
	private String result;//申请状态
	private int result_id;//申请状态ID
	private String appeal_time;//申请时间
	private String type_name;//申请类型（签退，签到）
	private int appeal_attendance;//是否是补卡，1是，0否
	private int appeal_attendance_id;//补卡申请详情ID
	private String attendance_time;//时间
	private String attendance_type;
	private String remarks;//补卡申请详情备注
	private String audit_remarks;//审批人备注
	private int audit_status;//审批人结果 审核状态，1是当前需要审核，0是还没到这个人审核，2是审核通过，3是审核不通过
	private int appealAttendanceRecordAudit_id;//审批人记录ID
	private int appealAttendanceRecordAudit_user_id;//审批人ID 
	private String appealAttendanceRecordAudit_user_name;//审批人name
	private int size;//总个数
	private String audit_time;//审批时间
	private int rule_id;//考勤规则ID
	private String rule_name;//规则名称
	private int readNotRead;//0是未读，1是已读
	private int effective;//是否有效，1有效，0无效
	private List<AppealAttendanceRecordPic> appealAttendanceRecordPic = new ArrayList<AppealAttendanceRecordPic>();//图片地址list
	private List<OutAttendanceInfoAuditRecord> appealAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
	public String getAttendance_type() {
		return attendance_type;
	}
	public void setAttendance_type(String attendance_type) {
		this.attendance_type = attendance_type;
	}
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
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public int getResult_id() {
		return result_id;
	}
	public void setResult_id(int result_id) {
		this.result_id = result_id;
	}
	public String getAppeal_time() {
		return appeal_time;
	}
	public void setAppeal_time(String appeal_time) {
		this.appeal_time = appeal_time;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public int getAppeal_attendance() {
		return appeal_attendance;
	}
	public void setAppeal_attendance(int appeal_attendance) {
		this.appeal_attendance = appeal_attendance;
	}
	public int getAppeal_attendance_id() {
		return appeal_attendance_id;
	}
	public void setAppeal_attendance_id(int appeal_attendance_id) {
		this.appeal_attendance_id = appeal_attendance_id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAudit_remarks() {
		return audit_remarks;
	}
	public void setAudit_remarks(String audit_remarks) {
		this.audit_remarks = audit_remarks;
	}
	public int getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(int audit_status) {
		this.audit_status = audit_status;
	}
	public int getAppealAttendanceRecordAudit_id() {
		return appealAttendanceRecordAudit_id;
	}
	public void setAppealAttendanceRecordAudit_id(int appealAttendanceRecordAudit_id) {
		this.appealAttendanceRecordAudit_id = appealAttendanceRecordAudit_id;
	}
	public int getAppealAttendanceRecordAudit_user_id() {
		return appealAttendanceRecordAudit_user_id;
	}
	public void setAppealAttendanceRecordAudit_user_id(int appealAttendanceRecordAudit_user_id) {
		this.appealAttendanceRecordAudit_user_id = appealAttendanceRecordAudit_user_id;
	}
	public String getAppealAttendanceRecordAudit_user_name() {
		return appealAttendanceRecordAudit_user_name;
	}
	public void setAppealAttendanceRecordAudit_user_name(String appealAttendanceRecordAudit_user_name) {
		this.appealAttendanceRecordAudit_user_name = appealAttendanceRecordAudit_user_name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}
	public int getReadNotRead() {
		return readNotRead;
	}
	public void setReadNotRead(int readNotRead) {
		this.readNotRead = readNotRead;
	}
	public List<AppealAttendanceRecordPic> getAppealAttendanceRecordPic() {
		return appealAttendanceRecordPic;
	}
	public void setAppealAttendanceRecordPic(List<AppealAttendanceRecordPic> appealAttendanceRecordPic) {
		this.appealAttendanceRecordPic = appealAttendanceRecordPic;
	}
	public List<OutAttendanceInfoAuditRecord> getAppealAttendanceInfoAuditRecord() {
		return appealAttendanceInfoAuditRecord;
	}
	public void setAppealAttendanceInfoAuditRecord(List<OutAttendanceInfoAuditRecord> appealAttendanceInfoAuditRecord) {
		this.appealAttendanceInfoAuditRecord = appealAttendanceInfoAuditRecord;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public String getAttendance_time() {
		return attendance_time;
	}
	public void setAttendance_time(String attendance_time) {
		this.attendance_time = attendance_time;
	}
	
	public int getRule_id() {
		return rule_id;
	}
	public void setRule_id(int rule_id) {
		this.rule_id = rule_id;
	}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	@Override
	public String toString() {
		return "AppealAttendanceInfo [id=" + id + ", user_id=" + user_id + ", user_name=" + user_name + ", result="
				+ result + ", result_id=" + result_id + ", appeal_time=" + appeal_time + ", type_name=" + type_name
				+ ", appeal_attendance=" + appeal_attendance + ", appeal_attendance_id=" + appeal_attendance_id
				+ ", attendance_time=" + attendance_time + ", remarks=" + remarks + ", audit_remarks=" + audit_remarks
				+ ", audit_status=" + audit_status + ", appealAttendanceRecordAudit_id="
				+ appealAttendanceRecordAudit_id + ", appealAttendanceRecordAudit_user_id="
				+ appealAttendanceRecordAudit_user_id + ", appealAttendanceRecordAudit_user_name="
				+ appealAttendanceRecordAudit_user_name + ", size=" + size + ", audit_time=" + audit_time
				+ ", readNotRead=" + readNotRead + ", effective=" + effective + ", appealAttendanceRecordPic="
				+ appealAttendanceRecordPic + ", appealAttendanceInfoAuditRecord=" + appealAttendanceInfoAuditRecord
				+ "]";
	}
	
	
	

}
