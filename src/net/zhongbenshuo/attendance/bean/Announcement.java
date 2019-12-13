package net.zhongbenshuo.attendance.bean;

public class Announcement {
	
	private int id;//公告的主键
	
	private int type_id;//公告类型ID
	
	private String type;//公告类型
	
	private String title;//标题
	
	private String picture;//图片
	
	private String external_link;//连接类型
	
	private String url;//点击后跳转的地址
	
	private String author;//发布人
	
	private int author_id;//发布人ID
	
	private int priority_id;//优先级ID
	
	private String priority;//优先级
	
	private String time;//发布时间
	
	private int effective;//1有效，0无效
	
	private String effectiveTime;//失效时间
	
	private int company_id;//公司ID
	
	private int size;//个数

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getExternal_link() {
		return external_link;
	}

	public void setExternal_link(String external_link) {
		this.external_link = external_link;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPriority_id() {
		return priority_id;
	}

	public void setPriority_id(int priority_id) {
		this.priority_id = priority_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	@Override
	public String toString() {
		return "Announcement [id=" + id + ", type_id=" + type_id + ", type=" + type + ", title=" + title + ", picture="
				+ picture + ", external_link=" + external_link + ", url=" + url + ", author=" + author + ", author_id="
				+ author_id + ", priority_id=" + priority_id + ", priority=" + priority + ", time=" + time
				+ ", effective=" + effective + ", effectiveTime=" + effectiveTime + ", company_id=" + company_id
				+ ", size=" + size + "]";
	}
	
}
