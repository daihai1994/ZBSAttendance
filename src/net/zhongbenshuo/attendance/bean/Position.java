package net.zhongbenshuo.attendance.bean;

public class Position {
	
	public int department_id;//部门ID

	public int position_id;//主键
	
	public String position;//职位名称
	
	public int effective;//是否有效，1有效，0无效
	
	public int priority;//职位排序

	public int getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}

	public int getPosition_id() {
		return position_id;
	}

	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getEffective() {
		return effective;
	}

	public void setEffective(int effective) {
		this.effective = effective;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Position [department_id=" + department_id + ", position_id=" + position_id + ", position=" + position
				+ ", effective=" + effective + ", priority=" + priority + "]";
	}


	
}
