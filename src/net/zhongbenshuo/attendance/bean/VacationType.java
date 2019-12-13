package net.zhongbenshuo.attendance.bean;

public class VacationType {

	private int vacation_type_id;
	
	private String vacation_type_name;
	
	private int effective;

	public int getVacation_type_id() {
		return vacation_type_id;
	}

	public void setVacation_type_id(int vacation_type_id) {
		this.vacation_type_id = vacation_type_id;
	}

	public String getVacation_type_name() {
		return vacation_type_name;
	}

	public void setVacation_type_name(String vacation_type_name) {
		this.vacation_type_name = vacation_type_name;
	}

	public int getEffective() {
		return effective;
	}

	public void setEffective(int effective) {
		this.effective = effective;
	}
	
}
