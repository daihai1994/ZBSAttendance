package net.zhongbenshuo.attendance.foreground.version.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.version.bean.ApkTypeInfo;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;
import net.zhongbenshuo.attendance.foreground.version.service.VersionService;

@Controller
@RequestMapping(value = "/VersionController", produces = "text/html;charset=UTF-8")
public class VersionController {

	public static Logger logger = LogManager.getLogger(VersionController.class);

	@Autowired
	VersionService versionService;

	// 安卓查询最近十条更新记录
	@RequestMapping(value = "/getNewVersionUpdateLog.do")
	@ResponseBody
	public String getNewVersionUpdateLog() {
		String returnData = "";
		returnData = versionService.getNewVersionUpdateLog();
		return returnData;
	}

	// 下载新版本jsmt.apk
	@RequestMapping(value = "/exportNewVersion.do")
	public String exportNewVersion(HttpServletResponse response, HttpServletRequest request, HttpSession session,
			String versionFileName) {
		response.setContentType("application/binary;charset=ISO8859_1");
		try {
			
			versionService.exportNewVersion(request, response, versionFileName);
		} catch (Exception e) {
			// logger.info("下载新版本失败："+e.getMessage());
		}
		return null;
	}

	// 下载新版本jsmt_xhs.apk
	@RequestMapping(value = "/downloadNewVersion.do")
	public String downloadNewVersion(HttpServletResponse response, HttpServletRequest request, HttpSession session,
			String versionFileName,  String loginId) {
		response.setContentType("application/binary;charset=ISO8859_1");
		try {
			versionService.exportNewVersion(request, response, versionFileName);
		} catch (Exception e) {
			// logger.info("下载新版本失败："+e.getMessage());
		}
		return null;
	}

	// 下载
	@RequestMapping(value = "/downloadNewVersionByLoginId.do")
	public String downloadNewVersionByLoginId(HttpServletResponse response, HttpServletRequest request,
			HttpSession session, String versionFileName, String loginId) {
		response.setContentType("application/binary;charset=ISO8859_1");
		try {
			versionService.exportNewVersionByLoginId(request, response, versionFileName,  loginId);
		} catch (Exception e) {
			// logger.info("下载新版本失败："+e.getMessage());
		}
		return null;
	}

	// 上传文件会自动绑定到MultipartFile中
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
	@ResponseBody
	public String upload(HttpServletRequest request, int apkTypeId,
			@RequestParam("versionCode") int versionCode, @RequestParam("versionName") String versionName, @RequestParam("versionLog") String versionLog,
			@RequestParam("file") MultipartFile file) {

		String flag = "error";
		flag = versionService.uploadNewFile(request, versionCode, versionName, apkTypeId, versionLog,
				file);
		return flag;
	}

	// 查询版本信息
	@RequestMapping(value = "/findVersionLog.do")
	@ResponseBody
	public String findVersionLog(int bNum, int rows, String versionType) {
		logger.info("网页查询版本信息");
		JSONObject jObject = new JSONObject();
		List<VersionInfo> list = new ArrayList<VersionInfo>();
		try {
			list = versionService.findVersionLog(versionType);
			int totalCount = list.size();
			if (bNum + rows <= totalCount) {
				list = list.subList(bNum, rows + bNum);
			} else if (totalCount == 0) {

			} else if (bNum + rows >= totalCount) {
				if (bNum > totalCount && rows >= totalCount) {
					list = list.subList(0, totalCount);
				} else if (bNum > totalCount && rows < totalCount) {
					list = list.subList(0, rows);
				} else {
					list = list.subList(bNum, totalCount);
				}
			}
			jObject.put("total", totalCount);
			jObject.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jObject.toString();// JSONArray.fromObject(list).toString();
	}

	// 删除版本信息
	@RequestMapping(value = "/deleteVersion.do")
	@ResponseBody
	public String deleteVersion(HttpServletRequest request, int versionId, String versionFileName) {
		logger.info("网页删除版本信息");
		String flag = "flase";
		flag = versionService.deleteVersion(request, versionId, versionFileName);
		return flag;
	}

	// 查询版本种类信息
	@RequestMapping(value = "/findApkInfo.do")
	@ResponseBody
	public String findApkInfo(int bNum, int rows) {
		logger.info("网页查询版本种类信息");
		JSONObject jObject = new JSONObject();
		List<ApkTypeInfo> list = new ArrayList<ApkTypeInfo>();
		try {
			list = versionService.findApkInfo();
			int totalCount = list.size();
			if (bNum + rows <= totalCount) {
				list = list.subList(bNum, rows + bNum);
			} else if (totalCount == 0) {

			} else if (bNum + rows >= totalCount) {
				if (bNum > totalCount && rows >= totalCount) {
					list = list.subList(0, totalCount);
				} else if (bNum > totalCount && rows < totalCount) {
					list = list.subList(0, rows);
				} else {
					list = list.subList(bNum, totalCount);
				}
			}
			jObject.put("total", totalCount);
			jObject.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jObject.toString();// JSONArray.fromObject(list).toString();
	}

	// 新增版本种类信息
	@RequestMapping(value = "/addApkInfo.do")
	@ResponseBody
	public String addApkInfo(String apkTypeName, String apkTypeIcon) {
		logger.info("网页新增版本种类信息");
		String flag = "flase";
		try {
			flag = versionService.addApkInfo(apkTypeName, apkTypeIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	// 修改版本种类信息
	@RequestMapping(value = "/editApkInfo.do")
	@ResponseBody
	public String editApkInfo( String apkTypeName, String apkTypeId) {
		logger.info("网页新增版本种类信息");
		String flag = "flase";
		try {
			flag = versionService.editApkInfo( apkTypeName, apkTypeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	// 删除版本种类信息
	@RequestMapping(value = "/deleteApkInfo.do")
	@ResponseBody
	public String deleteApkInfo(int apkTypeId) {
		logger.info("网页删除版本种类信息");
		String flag = "flase";
		try {
			flag = versionService.deleteApkInfo(apkTypeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	// 查询app种类
	@RequestMapping(value = "/findApkTypeId.do")
	@ResponseBody
	public String findApkTypeId() {
		String result = "";
		result = versionService.findApkTypeId();
		return result;
	}

}
