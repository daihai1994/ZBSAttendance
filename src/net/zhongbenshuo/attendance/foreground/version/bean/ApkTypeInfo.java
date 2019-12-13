package net.zhongbenshuo.attendance.foreground.version.bean;

public class ApkTypeInfo {

	private int apkTypeId; // type-id
	private String apkTypeName; // type-name 江苏迈拓
	private String apkTypeIcon; // type-icon jsmt
	private int apkDownloadAmount; // 下载次数

	public int getApkTypeId() {
		return apkTypeId;
	}

	public void setApkTypeId(int apkTypeId) {
		this.apkTypeId = apkTypeId;
	}

	public String getApkTypeName() {
		return apkTypeName;
	}

	public void setApkTypeName(String apkTypeName) {
		this.apkTypeName = apkTypeName;
	}

	public String getApkTypeIcon() {
		return apkTypeIcon;
	}

	public void setApkTypeIcon(String apkTypeIcon) {
		this.apkTypeIcon = apkTypeIcon;
	}

	public int getApkDownloadAmount() {
		return apkDownloadAmount;
	}

	public void setApkDownloadAmount(int apkDownloadAmount) {
		this.apkDownloadAmount = apkDownloadAmount;
	}

}
