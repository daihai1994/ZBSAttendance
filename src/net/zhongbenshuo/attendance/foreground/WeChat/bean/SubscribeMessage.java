package net.zhongbenshuo.attendance.foreground.WeChat.bean;

public class SubscribeMessage {
	private String phrase4;
	private String date2;
	public String getPhrase4() {
		return phrase4;
	}
	public void setPhrase4(String phrase4) {
		this.phrase4 = phrase4;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = date2;
	}
	@Override
	public String toString() {
		return "SubscribeMessage [phrase4=" + phrase4 + ", date2=" + date2 + "]";
	}
	
	
}
