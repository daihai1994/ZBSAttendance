package net.zhongbenshuo.attendance.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 计算MD5值
 * 
 * @author User
 * 
 */
public class CheckHashValue {

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16).toUpperCase();
	}

	public static void main(String[] args) {
		File file = new File("D:\\Tools\\apache-tomcat-7.0.65\\webapps\\DownloadApk\\jsmt.apk");
		String value = getFileMD5(file);
		System.out.println(value);
	}
}
