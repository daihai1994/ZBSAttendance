package net.zhongbenshuo.attendance.foreground.version.bean;

public class Combox {
	private int id;
	private String text;
	private String deletePwd;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDeletePwd() {
		return deletePwd;
	}
	public void setDeletePwd(String deletePwd) {
		this.deletePwd = deletePwd;
	}
	@Override
	public String toString() {
		return "Combox [id=" + id + ", text=" + text + ", deletePwd=" + deletePwd + "]";
	}
	
}
