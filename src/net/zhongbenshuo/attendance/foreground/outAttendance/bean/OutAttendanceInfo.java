package net.zhongbenshuo.attendance.foreground.outAttendance.bean;

import java.util.ArrayList;
import java.util.List;

public class OutAttendanceInfo {
	private int id;//考勤记录id
	private int user_id;//申请人user_id
	private String user_name;//申请人name
	private String result;//申请状态
	private int result_id;//申请状态ID
	private double attendance_longitude;//经度
	private double attendance_latitude;//纬度
	private String attendance_address;//地址
	private String attendance_time;//申请时间
	private String type_name;//申请类型（签退，签到）
	private int out_attendance_id;//外勤申请详情ID
	private String remarks;//外勤申请详情备注
	private String audit_remarks;//审批人备注
	private int audit_status;//审批人结果 审核状态，1是当前需要审核，0是还没到这个人审核，2是审核通过，3是审核不通过
	private int outAttendanceRecordAudit_id;//审批人记录ID
	private int outAttendanceRecordAudit_user_id;//审批人ID 
	private String outAttendanceRecordAudit_user_name;//审批人name
	private int effective;//是否有效，1有效，0无效
	private int size;//总个数
	private String audit_time;//审批时间
	private int readNotRead;//0是未读，1是已读
	private List<OutAttendanceRecordPic> outAttendanceRecordPic = new ArrayList<OutAttendanceRecordPic>();//图片地址list
	private List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
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
	public double getAttendance_longitude() {
		return attendance_longitude;
	}
	public void setAttendance_longitude(double attendance_longitude) {
		this.attendance_longitude = attendance_longitude;
	}
	public double getAttendance_latitude() {
		return attendance_latitude;
	}
	public void setAttendance_latitude(double attendance_latitude) {
		this.attendance_latitude = attendance_latitude;
	}
	public String getAttendance_address() {
		return attendance_address;
	}
	public void setAttendance_address(String attendance_address) {
		this.attendance_address = attendance_address;
	}
	public String getAttendance_time() {
		return attendance_time;
	}
	public void setAttendance_time(String attendance_time) {
		this.attendance_time = attendance_time;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public int getOut_attendance_id() {
		return out_attendance_id;
	}
	public void setOut_attendance_id(int out_attendance_id) {
		this.out_attendance_id = out_attendance_id;
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
	public int getOutAttendanceRecordAudit_id() {
		return outAttendanceRecordAudit_id;
	}
	public void setOutAttendanceRecordAudit_id(int outAttendanceRecordAudit_id) {
		this.outAttendanceRecordAudit_id = outAttendanceRecordAudit_id;
	}
	public int getOutAttendanceRecordAudit_user_id() {
		return outAttendanceRecordAudit_user_id;
	}
	public void setOutAttendanceRecordAudit_user_id(int outAttendanceRecordAudit_user_id) {
		this.outAttendanceRecordAudit_user_id = outAttendanceRecordAudit_user_id;
	}
	public String getOutAttendanceRecordAudit_user_name() {
		return outAttendanceRecordAudit_user_name;
	}
	public void setOutAttendanceRecordAudit_user_name(String outAttendanceRecordAudit_user_name) {
		this.outAttendanceRecordAudit_user_name = outAttendanceRecordAudit_user_name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<OutAttendanceRecordPic> getOutAttendanceRecordPic() {
		return outAttendanceRecordPic;
	}
	public void setOutAttendanceRecordPic(List<OutAttendanceRecordPic> outAttendanceRecordPic) {
		this.outAttendanceRecordPic = outAttendanceRecordPic;
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
	public List<OutAttendanceInfoAuditRecord> getOutAttendanceInfoAuditRecord() {
		return outAttendanceInfoAuditRecord;
	}
	public void setOutAttendanceInfoAuditRecord(List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord) {
		this.outAttendanceInfoAuditRecord = outAttendanceInfoAuditRecord;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	@Override
	public String toString() {
		return "OutAttendanceInfo [id=" + id + ", user_id=" + user_id + ", user_name=" + user_name + ", result="
				+ result + ", result_id=" + result_id + ", attendance_longitude=" + attendance_longitude
				+ ", attendance_latitude=" + attendance_latitude + ", attendance_address=" + attendance_address
				+ ", attendance_time=" + attendance_time + ", type_name=" + type_name + ", out_attendance_id="
				+ out_attendance_id + ", remarks=" + remarks + ", audit_remarks=" + audit_remarks + ", audit_status="
				+ audit_status + ", outAttendanceRecordAudit_id=" + outAttendanceRecordAudit_id
				+ ", outAttendanceRecordAudit_user_id=" + outAttendanceRecordAudit_user_id
				+ ", outAttendanceRecordAudit_user_name=" + outAttendanceRecordAudit_user_name + ", effective="
				+ effective + ", size=" + size + ", audit_time=" + audit_time + ", readNotRead=" + readNotRead
				+ ", outAttendanceRecordPic=" + outAttendanceRecordPic + ", outAttendanceInfoAuditRecord="
				+ outAttendanceInfoAuditRecord + "]";
	}
	
	
}
