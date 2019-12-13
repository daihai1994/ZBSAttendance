package net.zhongbenshuo.attendance.foreground.applyRecord.bean;

public class AppealAttendanceRecordPic {
	private int appealAttendanceRecordPic_id;//补卡申请图片ID
	private String url;//图片地址
	private int appeal_attendance_record_id;//补卡申请详情ID
	public int getAppealAttendanceRecordPic_id() {
		return appealAttendanceRecordPic_id;
	}
	public void setAppealAttendanceRecordPic_id(int appealAttendanceRecordPic_id) {
		this.appealAttendanceRecordPic_id = appealAttendanceRecordPic_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getAppeal_attendance_record_id() {
		return appeal_attendance_record_id;
	}
	public void setAppeal_attendance_record_id(int appeal_attendance_record_id) {
		this.appeal_attendance_record_id = appeal_attendance_record_id;
	}
	@Override
	public String toString() {
		return "AppealAttendanceRecordPic [appealAttendanceRecordPic_id=" + appealAttendanceRecordPic_id + ", url="
				+ url + ", appeal_attendance_record_id=" + appeal_attendance_record_id + "]";
	}
	
}
