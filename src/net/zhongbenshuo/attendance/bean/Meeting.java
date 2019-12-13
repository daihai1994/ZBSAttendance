package net.zhongbenshuo.attendance.bean;

public class Meeting {
	
	private int id;
	
	private String theme;
	
	private String time_start_plan;
	
	private String time_end_plan;
	
	private int room_id;
	
	private String room_name;
	
	private String agenda;
	
	private int organizer_id;
	
	private String organizer;
	
	private String time_start_real;
	
	private String time_end_real;
	
	private String record;
	
	private int recorder_id;
	
	private String create_time;
	
	private int metting_status_id;
	
	private String metting_status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTime_start_plan() {
		return time_start_plan;
	}

	public void setTime_start_plan(String time_start_plan) {
		this.time_start_plan = time_start_plan;
	}

	public String getTime_end_plan() {
		return time_end_plan;
	}

	public void setTime_end_plan(String time_end_plan) {
		this.time_end_plan = time_end_plan;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public int getOrganizer_id() {
		return organizer_id;
	}

	public void setOrganizer_id(int organizer_id) {
		this.organizer_id = organizer_id;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getTime_start_real() {
		return time_start_real;
	}

	public void setTime_start_real(String time_start_real) {
		this.time_start_real = time_start_real;
	}

	public String getTime_end_real() {
		return time_end_real;
	}

	public void setTime_end_real(String time_end_real) {
		this.time_end_real = time_end_real;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public int getRecorder_id() {
		return recorder_id;
	}

	public void setRecorder_id(int recorder_id) {
		this.recorder_id = recorder_id;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getMetting_status_id() {
		return metting_status_id;
	}

	public void setMetting_status_id(int metting_status_id) {
		this.metting_status_id = metting_status_id;
	}

	public String getMetting_status() {
		return metting_status;
	}

	public void setMetting_status(String metting_status) {
		this.metting_status = metting_status;
	}

}
