package net.zhongbenshuo.attendance.foreground.common.service.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.constant.Constants;
import net.zhongbenshuo.attendance.foreground.common.service.AndroidService;

@Service
public class AndroidServiceImpl implements AndroidService {

	public static Logger logger = LogManager.getLogger(AndroidServiceImpl.class);

	/**
	 * 安卓上传日志文件
	 */
	@Override
	public String uploadAndroidLog(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		JSONObject jObject = new JSONObject();
		jObject.put("message", "上传失败");
		jObject.put("result", "fail");
		try {
			// 安卓日志文件放路径
			String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
			int p = path.indexOf("webapps");
			String realpath = path.substring(0, p + 8) + Constants.ANDROID_LOGS_FILE + "\\";
			realpath = realpath.replaceAll("%20", " ");
			logger.info("安卓日志存放路径：" + realpath);

			if (files != null) { // 有文件
				for (int i = 0; i < files.length; i++) {
					// 存入文件夹
					File tempFile = new File(new File(realpath), files[i].getOriginalFilename());
					if (!tempFile.getParentFile().exists()) {
						tempFile.getParentFile().mkdirs();
					}
					if (!tempFile.exists()) {
						tempFile.createNewFile();
					}
					files[i].transferTo(tempFile);
				}
			}
			jObject.put("message", "上传成功");
			jObject.put("result", "success");
			logger.info("安卓上传日志成功");
		} catch (Exception e) {
			logger.info("安卓上传日志出错：" + e.getMessage());
		}
		return jObject.toString();
	}

}
