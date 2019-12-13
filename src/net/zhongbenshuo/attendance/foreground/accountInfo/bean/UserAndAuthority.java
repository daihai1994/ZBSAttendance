package net.zhongbenshuo.attendance.foreground.accountInfo.bean;

public class UserAndAuthority {
	private int id;//主键
	private int user_id;//用户ID
	private String authority;//权限
	private int authority_id;//权限ID
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

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public int getAuthority_id() {
		return authority_id;
	}

	public void setAuthority_id(int authority_id) {
		this.authority_id = authority_id;
	}

	

	@Override
	public String toString() {
		return "UserAndAuthority [id=" + id + ", user_id=" + user_id + ", authority=" + authority + ", authority_id="
				+ authority_id  + "]";
	}
	
}
