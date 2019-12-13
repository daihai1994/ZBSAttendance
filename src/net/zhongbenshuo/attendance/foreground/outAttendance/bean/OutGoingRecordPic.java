package net.zhongbenshuo.attendance.foreground.outAttendance.bean;

public class OutGoingRecordPic {
	private int outGoingRecordPicId;//外出打卡申请图片ID
	private String url;//图片地址
	private int outGoing_record_id;//外出申请详情ID
	public int getOutGoingRecordPicId() {
		return outGoingRecordPicId;
	}
	public void setOutGoingRecordPicId(int outGoingRecordPicId) {
		this.outGoingRecordPicId = outGoingRecordPicId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOutGoing_record_id() {
		return outGoing_record_id;
	}
	public void setOutGoing_record_id(int outGoing_record_id) {
		this.outGoing_record_id = outGoing_record_id;
	}
	@Override
	public String toString() {
		return "OutGoingRecordPic [outGoingRecordPicId=" + outGoingRecordPicId + ", url=" + url
				+ ", outGoing_record_id=" + outGoing_record_id + "]";
	}
	
}
