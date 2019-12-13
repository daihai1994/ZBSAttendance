package net.zhongbenshuo.attendance.foreground.WeChat.bean;

public class MiniprogramSend {
	private String appid;
	private String pagepath;
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
		return "MiniprogramSend [appid=" + appid + ", pagepath=" + pagepath + "]";
	}
	
}
