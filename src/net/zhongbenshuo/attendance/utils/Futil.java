package net.zhongbenshuo.attendance.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zhongbenshuo.attendance.bean.User;

public class Futil {
	/**
	 * 获取ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ip = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}
	/***
	 * 返回user_id
	 * @param session
	 * @param request
	 * @return
	 */
	public static int getUserId(HttpSession session,HttpServletRequest request) {
		User user = new User();
		user = (User) session.getAttribute("user");
		int user_id = 00000000;
		try {
			 user_id = user.getUser_id();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取session用户user_id错误");
		}
		
		return user_id;
	}


	/**
	 * 传入request，返回请求体内容 application/json请求时,获取请求体body内容
	 * 
	 * @param request
	 * @return 请求体 body的byte数组，可自行转成string后解析json
	 * @throws IOException
	 */
	public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength <= 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = request.getInputStream().read(buffer, i, contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

	// 算校验和
	public static String getsum(String S, int Start) {
		String r = "";
		int sumL = 0;
		for (int i = (Start * 2); i < S.length(); i = i + 2) {
			sumL = (sumL + HexS2ToInt(S.substring(i, i + 2))) % 256;
		}
		r = Integer.toHexString(sumL / 16) + Integer.toHexString(sumL % 16);
		r = r.toUpperCase();
		return r;
	}

	static int HexS2ToInt(String S) {
		int r = 0;
		char a[] = S.toCharArray();
		r = HexS1ToInt(a[0]) * 16 + HexS1ToInt(a[1]);
		return r;
	}

	static int HexS1ToInt(char ch) {
		if ('a' <= ch && ch <= 'f') {
			return ch - 'a' + 10;
		}
		if ('A' <= ch && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		}
		throw new IllegalArgumentException(String.valueOf(ch));
	}
}
