package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

public class VacationRecordPic {
	private int vacationRecordPicid;//图片ID
	private String url;//图片地址
	private int vacation_record_id;//休假详情表ID
	public int getVacationRecordPicid() {
		return vacationRecordPicid;
	}
	public void setVacationRecordPicid(int vacationRecordPicid) {
		this.vacationRecordPicid = vacationRecordPicid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getVacation_record_id() {
		return vacation_record_id;
	}
	public void setVacation_record_id(int vacation_record_id) {
		this.vacation_record_id = vacation_record_id;
	}
	@Override
	public String toString() {
		return "VacationRecordPic [vacationRecordPicid=" + vacationRecordPicid + ", url=" + url
				+ ", vacation_record_id=" + vacation_record_id + "]";
	}
	
}
