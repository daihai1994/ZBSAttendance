package net.zhongbenshuo.attendance.bean;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginLogInfo {
	private int log_Id;// 日志Id
	private String user_Id;// 登录Id
	private String ip;// ip
	private int apkTypeId;// 应用类型
	private int versionCode;// 版本编号
	private String device_Type;// 设备类型
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date login_Time;// 登录时间

	public int getLog_Id() {
		return log_Id;
	}

	public void setLog_Id(int log_Id) {
		this.log_Id = log_Id;
	}

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getApkTypeId() {
		return apkTypeId;
	}

	public void setApkTypeId(int apkTypeId) {
		this.apkTypeId = apkTypeId;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getDevice_Type() {
		return device_Type;
	}

	public void setDevice_Type(String device_Type) {
		this.device_Type = device_Type;
	}

	public Date getLogin_Time() {
		return login_Time;
	}

	public void setLogin_Time(Date login_Time) {
		this.login_Time = login_Time;
	}

}
