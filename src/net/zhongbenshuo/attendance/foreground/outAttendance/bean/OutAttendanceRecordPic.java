package net.zhongbenshuo.attendance.foreground.outAttendance.bean;

public class OutAttendanceRecordPic {
	private int outAttendanceRecordPic_id;//外勤打卡申请图片ID
	private String url;//图片地址
	private int out_attendance_record_id;//外勤申请详情ID
	
	public int getOutAttendanceRecordPic_id() {
		return outAttendanceRecordPic_id;
	}
	public void setOutAttendanceRecordPic_id(int outAttendanceRecordPic_id) {
		this.outAttendanceRecordPic_id = outAttendanceRecordPic_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOut_attendance_record_id() {
		return out_attendance_record_id;
	}
	public void setOut_attendance_record_id(int out_attendance_record_id) {
		this.out_attendance_record_id = out_attendance_record_id;
	}
	@Override
	public String toString() {
		return "OutAttendanceRecordPic [outAttendanceRecordPic_id=" + outAttendanceRecordPic_id + ", url=" + url
				+ ", out_attendance_record_id=" + out_attendance_record_id + "]";
	}
	
}
