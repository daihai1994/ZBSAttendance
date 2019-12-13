package net.zhongbenshuo.attendance.utils;

public class Websocket {

	private int type;
	private Object object;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	@Override
	public String toString() {
		return "Websocket [type=" + type + ", object=" + object + "]";
	}
	
	

}
