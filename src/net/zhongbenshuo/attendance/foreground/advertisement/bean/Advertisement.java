package net.zhongbenshuo.attendance.foreground.advertisement.bean;

public class Advertisement {
	private int id;//广告的表
	private String picUrl;//图片地址
	private String adUrl;//链接地址
	private int showTime;//显示多少时间，单位秒
	private String name;//简介
	private String start_time;//开始时间
	private String end_time;//结束时间
	private int effective;//是否有效1是有效，0是无效
	private String createTime;//创建时间
	private int force;//是否强制1是强制，0是不强制
	private int size;//个数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getAdUrl() {
		return adUrl;
	}
	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}
	public int getShowTime() {
		return showTime;
	}
	public void setShowTime(int showTime) {
		this.showTime = showTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
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
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getForce() {
		return force;
	}
	public void setForce(int force) {
		this.force = force;
	}
	
	@Override
	public String toString() {
		return "Advertisement [id=" + id + ", picUrl=" + picUrl + ", adUrl=" + adUrl + ", showTime=" + showTime
				+ ", name=" + name + ", start_time=" + start_time + ", end_time=" + end_time + ", effective="
				+ effective + ", createTime=" + createTime + ", force=" + force + ", size=" + size + "]";
	}
	
}
