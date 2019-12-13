package net.zhongbenshuo.attendance.bean;

import java.util.Arrays;

public class VisitorSubscribe {
	private int id;//访客预约ID
	private int user_id;//预约人ID
	private String user_name;//预约人的名称
	private int userInfo_id;//预约谁ID
	private String userInfo_name;//预约谁的名字
	private String theme;//预约主题
	private String breaks;//预约信息
	private int effective;//是否有效1有效0无效
	private String createTime;//创建时间
	private String startDate;//有效开始日期
	private String endDate;//有效结束日期
	private String startTime;//有效开始时间段
	private String endTime;//有效结束时间段
	private String faceUrl;//人脸图片地址
	private int status;//审核状态 0未审核，1审核通过，2审核不通过
	private byte[] faceFeature;// 人脸特征值
	private String effectiveDay;//有效星期几（以逗号分隔）
	private int carryingNumber;	//携带人数
	private int workingDay;	//法定工作日，1有效0无效
	private String auditBreaks;//审批结果
	private int size;//个数
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
	public int getUserInfo_id() {
		return userInfo_id;
	}
	public void setUserInfo_id(int userInfo_id) {
		this.userInfo_id = userInfo_id;
	}
	public String getUserInfo_name() {
		return userInfo_name;
	}
	public void setUserInfo_name(String userInfo_name) {
		this.userInfo_name = userInfo_name;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getBreaks() {
		return breaks;
	}
	public void setBreaks(String breaks) {
		this.breaks = breaks;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getFaceUrl() {
		return faceUrl;
	}
	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public byte[] getFaceFeature() {
		return faceFeature;
	}
	public void setFaceFeature(byte[] faceFeature) {
		this.faceFeature = faceFeature;
	}
	public String getEffectiveDay() {
		return effectiveDay;
	}
	public void setEffectiveDay(String effectiveDay) {
		this.effectiveDay = effectiveDay;
	}
	public int getCarryingNumber() {
		return carryingNumber;
	}
	public void setCarryingNumber(int carryingNumber) {
		this.carryingNumber = carryingNumber;
	}
	public int getWorkingDay() {
		return workingDay;
	}
	public void setWorkingDay(int workingDay) {
		this.workingDay = workingDay;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAuditBreaks() {
		return auditBreaks;
	}
	public void setAuditBreaks(String auditBreaks) {
		this.auditBreaks = auditBreaks;
	}
	@Override
	public String toString() {
		return "VisitorSubscribe [id=" + id + ", user_id=" + user_id + ", user_name=" + user_name + ", userInfo_id="
				+ userInfo_id + ", userInfo_name=" + userInfo_name + ", theme=" + theme + ", breaks=" + breaks
				+ ", effective=" + effective + ", createTime=" + createTime + ", startDate=" + startDate + ", endDate="
				+ endDate + ", startTime=" + startTime + ", endTime=" + endTime + ", faceUrl=" + faceUrl + ", status="
				+ status + ", faceFeature=" + Arrays.toString(faceFeature) + ", effectiveDay=" + effectiveDay
				+ ", carryingNumber=" + carryingNumber + ", workingDay=" + workingDay + ", auditBreaks=" + auditBreaks
				+ ", size=" + size + "]";
	}
	
}
