package net.zhongbenshuo.attendance.foreground.outAttendance.bean;

public class BusinessTraveIRecordPic {
	private int businessTraveIRecordPicId;//出差打卡申请图片ID
	private String url;//图片地址
	private int businessTraveI_record_id;//出差申请详情ID
	public int getBusinessTraveIRecordPicId() {
		return businessTraveIRecordPicId;
	}
	public void setBusinessTraveIRecordPicId(int businessTraveIRecordPicId) {
		this.businessTraveIRecordPicId = businessTraveIRecordPicId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getBusinessTraveI_record_id() {
		return businessTraveI_record_id;
	}
	public void setBusinessTraveI_record_id(int businessTraveI_record_id) {
		this.businessTraveI_record_id = businessTraveI_record_id;
	}
	@Override
	public String toString() {
		return "BusinessTraveIRecordPic [businessTraveIRecordPicId=" + businessTraveIRecordPicId + ", url=" + url
				+ ", businessTraveI_record_id=" + businessTraveI_record_id + "]";
	}
	
}
