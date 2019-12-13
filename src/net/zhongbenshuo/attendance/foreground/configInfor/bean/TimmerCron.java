package net.zhongbenshuo.attendance.foreground.configInfor.bean;

public class TimmerCron {
	private String id;
	private String mode;//类型
	private String ttime;//时间
	private String createTime;//创建时间
	private String cron;//cron
	private String jobName;//名称
	private int user_id;//谁设定的
	private int effective;//是否有效1是有效0是无效
	private String cronName;//名称
	private String timerName;//计算工时
	private String group_id;//分组ID
	private String timmer_id;//定时任务器ID
	private String company_id;//哪家公司的
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getTtime() {
		return ttime;
	}
	public void setTtime(String ttime) {
		this.ttime = ttime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public String getTimerName() {
		return timerName;
	}
	public void setTimerName(String timerName) {
		this.timerName = timerName;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	
	public String getTimmer_id() {
		return timmer_id;
	}
	public void setTimmer_id(String timmer_id) {
		this.timmer_id = timmer_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getCronName() {
		return cronName;
	}
	public void setCronName(String cronName) {
		this.cronName = cronName;
	}
	@Override
	public String toString() {
		return "TimmerCron [id=" + id + ", mode=" + mode + ", ttime=" + ttime + ", createTime=" + createTime + ", cron="
				+ cron + ", jobName=" + jobName + ", user_id=" + user_id + ", effective=" + effective + ", cronName="
				+ cronName + ", timerName=" + timerName + ", group_id=" + group_id + ", timmer_id=" + timmer_id
				+ ", company_id=" + company_id + "]";
	}
	
	
}
