package net.zhongbenshuo.attendance.foreground.version.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import net.zhongbenshuo.attendance.foreground.version.bean.ApkTypeInfo;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;

public interface VersionService {

	public String getNewVersionUpdateLog();// 安卓查询最近十条更新记录

	public void exportNewVersion(HttpServletRequest request, HttpServletResponse response, String versionFileName) throws Exception; // 下载新版本jsmt.apk

	public String uploadNewFile(HttpServletRequest request,  int versionCode, String versionName,
			int apkTypeId, String versionLog, MultipartFile file); // 上传新版本文件

	public List<VersionInfo> findVersionLog(String versionType); // 查询上传版本信息

	public String deleteVersion(HttpServletRequest request, int versionId, String versionFileName); // 删除版本信息和文件

	public List<ApkTypeInfo> findApkInfo(); // 查询版本种类信息

	public String addApkInfo(String apkTypeName, String apkTypeIcon); // 新增版本种类信息

	public String editApkInfo( String apkTypeName, String apkTypeId); // 修改版本种类信息

	public String deleteApkInfo(int apkTypeId); // 删除版本种类信息

	public String findApkTypeId();// 查询app种类

	public void exportNewVersionByLoginId(HttpServletRequest request, HttpServletResponse response,
			String versionFileName,  String loginId);

	public VersionInfo findVersionInfo();

}
