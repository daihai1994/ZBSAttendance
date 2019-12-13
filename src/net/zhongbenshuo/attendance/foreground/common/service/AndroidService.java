package net.zhongbenshuo.attendance.foreground.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface AndroidService {

	public String uploadAndroidLog(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession);// 安卓上传日志文件

}
