package net.zhongbenshuo.attendance.bean;

import java.util.Arrays;

public class Face {
	private int id;                 // 人脸识别ID
    private int user_id;            // 用户ID
    private String name;            // 用户姓名
    private String url;             // 图片地址
    private byte[] faceFeature;     // 人脸特征值
    private int identity;           // 身份（0：公司员工  1：访客）
    private String createTime;      // 创建时间
	private String updateTime;      // 修改时间
	private String startDate;       // 有效开始日期
    private String endDate;   		// 有效结束日期
	private String effectiveDay;    // 每周星期几（以逗号分隔）
	private String startTime;       // 每日有效起始时间
    private String endTime;         // 每日有效截止时间
    private int effective;	 		//是否有效。1有效，0无效
    private int workingDay;			//法定工作日，1有效0无效
    private int serverStatus;		//0是没有处理，1是处理
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public byte[] getFaceFeature() {
		return faceFeature;
	}
	public void setFaceFeature(byte[] faceFeature) {
		this.faceFeature = faceFeature;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
	public String getEffectiveDay() {
		return effectiveDay;
	}
	public void setEffectiveDay(String effectiveDay) {
		this.effectiveDay = effectiveDay;
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
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getWorkingDay() {
		return workingDay;
	}
	public void setWorkingDay(int workingDay) {
		this.workingDay = workingDay;
	}
	public int getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(int serverStatus) {
		this.serverStatus = serverStatus;
	}
	@Override
	public String toString() {
		return "Face [id=" + id + ", user_id=" + user_id + ", name=" + name + ", url=" + url + ", faceFeature="
				+ Arrays.toString(faceFeature) + ", identity=" + identity + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", startDate=" + startDate + ", endDate=" + endDate + ", effectiveDay="
				+ effectiveDay + ", startTime=" + startTime + ", endTime=" + endTime + ", effective=" + effective
				+ ", workingDay=" + workingDay + ", serverStatus=" + serverStatus + "]";
	}

    
}
