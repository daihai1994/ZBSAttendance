package net.zhongbenshuo.attendance.bean;

public class Vacation {
	private String vacation_type_name;
	private String vacation_start_time;
	private String vacation_stop_time;
	public String getVacation_type_name() {
		return vacation_type_name;
	}
	public void setVacation_type_name(String vacation_type_name) {
		this.vacation_type_name = vacation_type_name;
	}
	public String getVacation_start_time() {
		return vacation_start_time;
	}
	public void setVacation_start_time(String vacation_start_time) {
		this.vacation_start_time = vacation_start_time;
	}
	public String getVacation_stop_time() {
		return vacation_stop_time;
	}
	public void setVacation_stop_time(String vacation_stop_time) {
		this.vacation_stop_time = vacation_stop_time;
	}
	@Override
	public String toString() {
		return "Vacation [vacation_type_name=" + vacation_type_name + ", vacation_start_time=" + vacation_start_time
				+ ", vacation_stop_time=" + vacation_stop_time + "]";
	}
	
}
