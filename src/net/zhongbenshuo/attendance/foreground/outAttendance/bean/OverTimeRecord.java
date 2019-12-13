package net.zhongbenshuo.attendance.foreground.outAttendance.bean;

import java.util.ArrayList;
import java.util.List;

import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordPic;

public class OverTimeRecord {
	private int id;//加班申请详情ID
	private int overTimeType_id;//加班类型ID
	private String overTimeType;//加班类型
	private float coefficient;//加班系数
	private String start_time;//开始时间
	private String stop_time;//结束时间
	private float day;//天数
	private float hour;//小时
	private String remarks;//备注
	private int effective;//是否有效1有效，0无效
	private String createTime;//创建时间
	private int readNotRead;//0是未读，1是已读
	private int size;//个数
	private int user_id;//申请人ID
	private String user_name;//申请人名称
	private int audit_id;//审批人记录ID
	private int audit_status;//审核状态，1是当前需要审核，0是还没到这个人审核，2是审核通过，3是审核不通过
	private String audit_remarks;//审批人备注
	private String audit_time;//审批时间
	private int result_id;//申请状态
	private String result;//申请结果
	private List<OverTimeRecordPic> overTimeRecordPic = new ArrayList<OverTimeRecordPic>();//图片list
	private List<OutAttendanceInfoAuditRecord> overTimeAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
	private List<String> audit_user = new ArrayList<String>();//审批人
	private List<String> copy_user = new ArrayList<String>();//抄送人
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOverTimeType_id() {
		return overTimeType_id;
	}
	public void setOverTimeType_id(int overTimeType_id) {
		this.overTimeType_id = overTimeType_id;
	}
	public String getOverTimeType() {
		return overTimeType;
	}
	public void setOverTimeType(String overTimeType) {
		this.overTimeType = overTimeType;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getStop_time() {
		return stop_time;
	}
	public void setStop_time(String stop_time) {
		this.stop_time = stop_time;
	}
	public float getDay() {
		return day;
	}
	public void setDay(float day) {
		this.day = day;
	}
	public float getHour() {
		return hour;
	}
	public void setHour(float hour) {
		this.hour = hour;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getReadNotRead() {
		return readNotRead;
	}
	public void setReadNotRead(int readNotRead) {
		this.readNotRead = readNotRead;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
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
	public List<OverTimeRecordPic> getOverTimeRecordPic() {
		return overTimeRecordPic;
	}
	public void setOverTimeRecordPic(List<OverTimeRecordPic> overTimeRecordPic) {
		this.overTimeRecordPic = overTimeRecordPic;
	}
	public List<OutAttendanceInfoAuditRecord> getOverTimeAuditRecord() {
		return overTimeAuditRecord;
	}
	public void setOverTimeAuditRecord(List<OutAttendanceInfoAuditRecord> overTimeAuditRecord) {
		this.overTimeAuditRecord = overTimeAuditRecord;
	}
	public int getResult_id() {
		return result_id;
	}
	public void setResult_id(int result_id) {
		this.result_id = result_id;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public int getAudit_id() {
		return audit_id;
	}
	public void setAudit_id(int audit_id) {
		this.audit_id = audit_id;
	}
	public List<String> getAudit_user() {
		return audit_user;
	}
	public void setAudit_user(List<String> audit_user) {
		this.audit_user = audit_user;
	}
	public List<String> getCopy_user() {
		return copy_user;
	}
	public void setCopy_user(List<String> copy_user) {
		this.copy_user = copy_user;
	}
	public float getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}
	@Override
	public String toString() {
		return "OverTimeRecord [id=" + id + ", overTimeType_id=" + overTimeType_id + ", overTimeType=" + overTimeType
				+ ", coefficient=" + coefficient + ", start_time=" + start_time + ", stop_time=" + stop_time + ", day="
				+ day + ", hour=" + hour + ", remarks=" + remarks + ", effective=" + effective + ", createTime="
				+ createTime + ", readNotRead=" + readNotRead + ", size=" + size + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", audit_id=" + audit_id + ", audit_status=" + audit_status
				+ ", audit_remarks=" + audit_remarks + ", audit_time=" + audit_time + ", result_id=" + result_id
				+ ", result=" + result + ", overTimeRecordPic=" + overTimeRecordPic + ", overTimeAuditRecord="
				+ overTimeAuditRecord + ", audit_user=" + audit_user + ", copy_user=" + copy_user + "]";
	}
	
	
}
