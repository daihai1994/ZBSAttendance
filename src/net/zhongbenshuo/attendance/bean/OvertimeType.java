package net.zhongbenshuo.attendance.bean;

public class OvertimeType {

	private int overtime_type_id;
	
	private String overtime_type_name;
	
	private int effective;
	
	private float coefficient;//系数

	public int getOvertime_type_id() {
		return overtime_type_id;
	}

	public void setOvertime_type_id(int overtime_type_id) {
		this.overtime_type_id = overtime_type_id;
	}

	public String getOvertime_type_name() {
		return overtime_type_name;
	}

	public void setOvertime_type_name(String overtime_type_name) {
		this.overtime_type_name = overtime_type_name;
	}

	public int getEffective() {
		return effective;
	}

	public void setEffective(int effective) {
		this.effective = effective;
	}

	public float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}
	
}
