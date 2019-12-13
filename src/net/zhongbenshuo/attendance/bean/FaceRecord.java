package net.zhongbenshuo.attendance.bean;

public class FaceRecord {
	private int id;//
	private int user_id;//用户ID
	private String createTime;//创建时间
	private String url;//图片地址
	private String remarks;//备注
	private String user_name;//名称
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	@Override
	public String toString() {
		return "FaceRecord [id=" + id + ", user_id=" + user_id + ", createTime=" + createTime + ", url=" + url
				+ ", remarks=" + remarks + ", user_name=" + user_name + "]";
	}
	
}
