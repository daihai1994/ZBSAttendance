package net.zhongbenshuo.attendance.foreground.WeChat.bean;

public class VIEW {
	private String type;
	private String name;
	private String url;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	@Override
	public String toString() {
		return "VIEW [type=" + type + ", name=" + name + ", url=" + url + "]";
	}
	
}
