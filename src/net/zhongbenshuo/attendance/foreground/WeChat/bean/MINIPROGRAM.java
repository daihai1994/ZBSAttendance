package net.zhongbenshuo.attendance.foreground.WeChat.bean;

public class MINIPROGRAM {
	private String type;
	private String name;
	private String url;
	private String appid;
	private String pagepath;
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
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPagepath() {
		return pagepath;
	}
	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}
	@Override
	public String toString() {
		return "MINIPROGRAM [type=" + type + ", name=" + name + ", url=" + url + ", appid=" + appid + ", pagepath="
				+ pagepath + "]";
	}
	
}
