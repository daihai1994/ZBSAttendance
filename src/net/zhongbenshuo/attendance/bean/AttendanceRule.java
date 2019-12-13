package net.zhongbenshuo.attendance.bean;

public class AttendanceRule {

	private int id;
    private String rule_name;
    private String rule_address;
    private String rule_longitude;
    private String rule_latitude;
    private String rule_radius;
    private String rule_time_work;
    private String rule_time_off_work;
    private int rule_manager_id;
    private int effective;
    private String rule_rest_start;//休息起始时间
    private String rule_rest_end;//休息结束时间
    private int company_id;//公司ID
    private String rule_manager;//制定人
    
    private int rule_type;//1是地点，2是门禁机，3是wifi
    private String unique_address;//唯一地址
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRule_name() {
        return rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public String getRule_address() {
        return rule_address;
    }

    public void setRule_address(String rule_address) {
        this.rule_address = rule_address;
    }

    public String getRule_longitude() {
        return rule_longitude;
    }

    public void setRule_longitude(String rule_longitude) {
        this.rule_longitude = rule_longitude;
    }

    public String getRule_latitude() {
        return rule_latitude;
    }

    public void setRule_latitude(String rule_latitude) {
        this.rule_latitude = rule_latitude;
    }

    public String getRule_radius() {
        return rule_radius;
    }

    public void setRule_radius(String rule_radius) {
        this.rule_radius = rule_radius;
    }

    public String getRule_time_work() {
        return rule_time_work;
    }

    public void setRule_time_work(String rule_time_work) {
        this.rule_time_work = rule_time_work;
    }

    public String getRule_time_off_work() {
        return rule_time_off_work;
    }

    public void setRule_time_off_work(String rule_time_off_work) {
        this.rule_time_off_work = rule_time_off_work;
    }

	public int getRule_manager_id() {
		return rule_manager_id;
	}

	public void setRule_manager_id(int rule_manager_id) {
		this.rule_manager_id = rule_manager_id;
	}

	public int getEffective() {
		return effective;
	}

	public void setEffective(int effective) {
		this.effective = effective;
	}

	public String getRule_rest_start() {
		return rule_rest_start;
	}

	public void setRule_rest_start(String rule_rest_start) {
		this.rule_rest_start = rule_rest_start;
	}

	public String getRule_rest_end() {
		return rule_rest_end;
	}

	public void setRule_rest_end(String rule_rest_end) {
		this.rule_rest_end = rule_rest_end;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getRule_manager() {
		return rule_manager;
	}

	public void setRule_manager(String rule_manager) {
		this.rule_manager = rule_manager;
	}

	public int getRule_type() {
		return rule_type;
	}

	public void setRule_type(int rule_type) {
		this.rule_type = rule_type;
	}

	public String getUnique_address() {
		return unique_address;
	}

	public void setUnique_address(String unique_address) {
		this.unique_address = unique_address;
	}

	@Override
	public String toString() {
		return "AttendanceRule [id=" + id + ", rule_name=" + rule_name + ", rule_address=" + rule_address
				+ ", rule_longitude=" + rule_longitude + ", rule_latitude=" + rule_latitude + ", rule_radius="
				+ rule_radius + ", rule_time_work=" + rule_time_work + ", rule_time_off_work=" + rule_time_off_work
				+ ", rule_manager_id=" + rule_manager_id + ", effective=" + effective + ", rule_rest_start="
				+ rule_rest_start + ", rule_rest_end=" + rule_rest_end + ", company_id=" + company_id
				+ ", rule_manager=" + rule_manager + ", rule_type=" + rule_type + ", unique_address=" + unique_address
				+ "]";
	}

}
