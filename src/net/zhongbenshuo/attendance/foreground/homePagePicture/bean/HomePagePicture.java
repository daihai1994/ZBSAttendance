package net.zhongbenshuo.attendance.foreground.homePagePicture.bean;

public class HomePagePicture {
	private int id;//轮播图ID
	private int company_id;//公司ID
	private String remarks;//图片描述
	private int level;//图片显示级别
	private String createTime;//创建时间
	private int effective;//是否有效1有效0无效
	private String addressUrl;//链接地址
	private int force;//是否强制，1是强制，0不是
	private int state;//1是第三方链接，0是本地链接
	private String pictureUrl;//图片地址
	private int size;//个数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public String getAddressUrl() {
		return addressUrl;
	}
	public void setAddressUrl(String addressUrl) {
		this.addressUrl = addressUrl;
	}
	public int getForce() {
		return force;
	}
	public void setForce(int force) {
		this.force = force;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	@Override
	public String toString() {
		return "HomePagePicture [id=" + id + ", company_id=" + company_id + ", remarks=" + remarks 
				+ ", level=" + level + ", createTime=" + createTime + ", effective=" + effective + ", addressUrl="
				+ addressUrl + ", force=" + force + ", state=" + state + ", pictureUrl=" + pictureUrl + ", size=" + size
				+ "]";
	}
	
}
