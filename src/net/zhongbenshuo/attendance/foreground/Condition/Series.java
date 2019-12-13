package net.zhongbenshuo.attendance.foreground.Condition;

import java.util.ArrayList;
import java.util.List;

public class Series {//echarts series数据
	private String name;//名称
	private String type;//曲线类型
	private String stack;//
	private List<Integer> data = new ArrayList<Integer>();//data数据
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStack() {
		return stack;
	}
	public void setStack(String stack) {
		this.stack = stack;
	}
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Series [name=" + name + ", type=" + type + ", stack=" + stack + ", data=" + data + "]";
	}
	
}
