package net.zhongbenshuo.attendance.bean;

public class OutGoing {
	private String outGoing_address;
	private String outGoing_start_time;
	private String outGoing_stop_time;
	public String getOutGoing_address() {
		return outGoing_address;
	}
	public void setOutGoing_address(String outGoing_address) {
		this.outGoing_address = outGoing_address;
	}
	public String getOutGoing_start_time() {
		return outGoing_start_time;
	}
	public void setOutGoing_start_time(String outGoing_start_time) {
		this.outGoing_start_time = outGoing_start_time;
	}
	public String getOutGoing_stop_time() {
		return outGoing_stop_time;
	}
	public void setOutGoing_stop_time(String outGoing_stop_time) {
		this.outGoing_stop_time = outGoing_stop_time;
	}
	@Override
	public String toString() {
		return "OutGoing [outGoing_address=" + outGoing_address + ", outGoing_start_time=" + outGoing_start_time
				+ ", outGoing_stop_time=" + outGoing_stop_time + "]";
	}
	
}
