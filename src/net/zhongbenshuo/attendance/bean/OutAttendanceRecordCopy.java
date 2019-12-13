package net.zhongbenshuo.attendance.bean;

public class OutAttendanceRecordCopy {
	private int id;//外勤打卡抄送人表
	private int user_id;//用户人user_id
	private int out_Attendance_Record_Id;//外勤打卡记录ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getOut_Attendance_Record_Id() {
		return out_Attendance_Record_Id;
	}
	public void setOut_Attendance_Record_Id(int out_Attendance_Record_Id) {
		this.out_Attendance_Record_Id = out_Attendance_Record_Id;
	}
	@Override
	public String toString() {
		return "OutAttendanceRecordCopy [id=" + id + ", user_id=" + user_id + ", out_Attendance_Record_Id="
				+ out_Attendance_Record_Id + "]";
	}
	
	
}
