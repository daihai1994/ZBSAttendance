package net.zhongbenshuo.attendance.utils;

public class WebRsaKey {
	private String rsaPublicKey;
	private String rsaPrivateKey;
	public String getRsaPublicKey() {
		return rsaPublicKey;
	}
	public void setRsaPublicKey(String rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}
	public String getRsaPrivateKey() {
		return rsaPrivateKey;
	}
	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}
	@Override
	public String toString() {
		return "WebRsaKey [rsaPublicKey=" + rsaPublicKey + ", rsaPrivateKey=" + rsaPrivateKey + "]";
	}
	
}
