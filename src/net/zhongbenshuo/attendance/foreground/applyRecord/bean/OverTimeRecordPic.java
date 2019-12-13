package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

public class OverTimeRecordPic {
	private int overTimeRecordPicid;//图片ID
	private String url;//图片地址
	private int over_time_record_id;//加班详情表ID
	public int getOverTimeRecordPicid() {
		return overTimeRecordPicid;
	}
	public void setOverTimeRecordPicid(int overTimeRecordPicid) {
		this.overTimeRecordPicid = overTimeRecordPicid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOver_time_record_id() {
		return over_time_record_id;
	}
	public void setOver_time_record_id(int over_time_record_id) {
		this.over_time_record_id = over_time_record_id;
	}
	@Override
	public String toString() {
		return "OverTimeRecordPic [overTimeRecordPicid=" + overTimeRecordPicid + ", url=" + url + ", over_time_record_id=" + over_time_record_id + "]";
	}
	
}
