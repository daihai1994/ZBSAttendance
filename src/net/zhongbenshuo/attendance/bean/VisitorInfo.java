package net.zhongbenshuo.attendance.bean;

import java.util.Arrays;

public class VisitorInfo {
	private int id;//访客信息ID
	private int user_id;//访客id
	private String user_name;//访客名称
	private String company;//访客的公司名称
	private String phone;//电话号码
	private String position;//职务
	private String open_id;//微信openid
	private int effective;//是否有效 1有效0无效
	private int company_id;//公司ID
	private String url;	//人脸地址
	private byte[] faceFeature;     // 人脸特征值
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
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
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
	@Override
	public String toString() {
		return "VisitorInfo [id=" + id + ", user_id=" + user_id + ", user_name=" + user_name + ", company=" + company
				+ ", phone=" + phone + ", position=" + position + ", open_id=" + open_id + ", effective=" + effective
				+ ", company_id=" + company_id + ", url=" + url + ", faceFeature=" + Arrays.toString(faceFeature) + "]";
	}
	
}
