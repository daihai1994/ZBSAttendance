package net.zhongbenshuo.attendance.foreground.logger.bean;

public class LoggerInfo {
	private int id;
	private String ip;
	private String remarks;
	private String user_id;
	private String user_name;
	private String  createTime;
	private String company_name;
	private int size;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	@Override
	public String toString() {
		return "LoggerInfo [id=" + id + ", ip=" + ip + ", remarks=" + remarks + ", user_id=" + user_id + ", user_name="
				+ user_name + ", createTime=" + createTime + ", company_name=" + company_name + ", size=" + size + "]";
	}
	
}
