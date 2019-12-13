package net.zhongbenshuo.attendance.foreground.announcement.bean;

public class AnnouncementType {
	private int type_id;
	private String type;
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
	@Override
	public String toString() {
		return "AnnouncementType [type_id=" + type_id + ", type=" + type + "]";
	}
	
}
