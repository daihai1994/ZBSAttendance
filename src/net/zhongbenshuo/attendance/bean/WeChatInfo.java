package net.zhongbenshuo.attendance.bean;

public class WeChatInfo {
	private String appid;//appid
	private String secret;//secret
	private String access_token;//token
	private String access_token_time;//过期时间
	private String ticket;//jssdk调用凭据
	private String ticket_time;//jssdk票据过期时间
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getAccess_token_time() {
		return access_token_time;
	}
	public void setAccess_token_time(String access_token_time) {
		this.access_token_time = access_token_time;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTicket_time() {
		return ticket_time;
	}
	public void setTicket_time(String ticket_time) {
		this.ticket_time = ticket_time;
	}
	@Override
	public String toString() {
		return "WeChatInfo [appid=" + appid + ", secret=" + secret + ", access_token=" + access_token
				+ ", access_token_time=" + access_token_time + ", ticket=" + ticket + ", ticket_time=" + ticket_time
				+ "]";
	}
	
}
