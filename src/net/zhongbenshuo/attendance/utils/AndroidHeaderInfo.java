package net.zhongbenshuo.attendance.utils;

public class AndroidHeaderInfo {
	private String appEncryptedKey;//安卓通过web返回的公钥进行加密的aesKey
	private String appSignature;//安卓用自己的私钥签名的
	private String appPublicKey;//安卓的rsa公钥（对于web返回的aesKey的加密）
	private String appAesKey;//通过web的私钥对appEncryptedKey进行解密
	private String encryptData;//对请求体进行aes解密
	private String body;//获取请求体的数据
	
	
	private String webEncryptedAesKey;//web对aesKey的rsa加密
	
	private String webaesKey;//web的aeskey
	
	private int code = 1001;//状态位
	
	private boolean verify;//校验是否合格
	public String getAppEncryptedKey() {
		return appEncryptedKey;
	}
	public void setAppEncryptedKey(String appEncryptedKey) {
		this.appEncryptedKey = appEncryptedKey;
	}
	public String getAppSignature() {
		return appSignature;
	}
	public void setAppSignature(String appSignature) {
		this.appSignature = appSignature;
	}
	public String getAppPublicKey() {
		return appPublicKey;
	}
	public void setAppPublicKey(String appPublicKey) {
		this.appPublicKey = appPublicKey;
	}
	public String getAppAesKey() {
		return appAesKey;
	}
	public void setAppAesKey(String appAesKey) {
		this.appAesKey = appAesKey;
	}
	public String getEncryptData() {
		return encryptData;
	}
	public void setEncryptData(String encryptData) {
		this.encryptData = encryptData;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getWebEncryptedAesKey() {
		return webEncryptedAesKey;
	}
	public void setWebEncryptedAesKey(String webEncryptedAesKey) {
		this.webEncryptedAesKey = webEncryptedAesKey;
	}
	public String getWebaesKey() {
		return webaesKey;
	}
	public void setWebaesKey(String webaesKey) {
		this.webaesKey = webaesKey;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public boolean isVerify() {
		return verify;
	}
	public void setVerify(boolean verify) {
		this.verify = verify;
	}
	@Override
	public String toString() {
		return "AndroidHeaderInfo [appEncryptedKey=" + appEncryptedKey + ", appSignature=" + appSignature
				+ ", appPublicKey=" + appPublicKey + ", appAesKey=" + appAesKey + ", encryptData=" + encryptData
				+ ", body=" + body + ", webEncryptedAesKey=" + webEncryptedAesKey + ", webaesKey=" + webaesKey
				+ ", code=" + code + ", verify=" + verify + "]";
	}
	
	
}
