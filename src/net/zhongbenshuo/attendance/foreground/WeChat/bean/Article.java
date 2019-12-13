package net.zhongbenshuo.attendance.foreground.WeChat.bean;

public class Article {
	private First first ;
	private Keyword1 keyword1;
	private Keyword2 keyword2;
	private Remark remark;
	public First getFirst() {
		return first;
	}
	public void setFirst(First first) {
		this.first = first;
	}
	public Keyword1 getKeyword1() {
		return keyword1;
	}
	public void setKeyword1(Keyword1 keyword1) {
		this.keyword1 = keyword1;
	}
	public Keyword2 getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(Keyword2 keyword2) {
		this.keyword2 = keyword2;
	}
	public Remark getRemark() {
		return remark;
	}
	public void setRemark(Remark remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "Article [first=" + first + ", keyword1=" + keyword1 + ", keyword2=" + keyword2 + ", remark=" + remark
				+ "]";
	}
	
	
	
	
	
			
}
