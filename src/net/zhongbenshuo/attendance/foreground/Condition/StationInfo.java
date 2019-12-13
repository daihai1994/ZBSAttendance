package net.zhongbenshuo.attendance.foreground.Condition;

public class StationInfo {
	private int id;//环境检测仪信息表
	private int station_id;//环境检测仪ID（人为的设置）
	private String station_name;//环境检测仪的名称
	private String station_remarks;//备注
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStation_id() {
		return station_id;
	}
	public void setStation_id(int station_id) {
		this.station_id = station_id;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}
	public String getStation_remarks() {
		return station_remarks;
	}
	public void setStation_remarks(String station_remarks) {
		this.station_remarks = station_remarks;
	}
	@Override
	public String toString() {
		return "StationInfo [id=" + id + ", station_id=" + station_id + ", station_name=" + station_name
				+ ", station_remarks=" + station_remarks + "]";
	}
	
}
