package net.zhongbenshuo.attendance.foreground.version.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.constant.Constants;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.foreground.version.bean.ApkTypeInfo;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;
import net.zhongbenshuo.attendance.foreground.version.mapper.VersionMapper;
import net.zhongbenshuo.attendance.foreground.version.service.VersionService;
import net.zhongbenshuo.attendance.utils.CheckHashValue;

@Service
public class VersionServiceImpl implements VersionService {

	public static Logger logger = LogManager.getLogger(VersionServiceImpl.class);

	@Autowired
	VersionMapper versionMapper;

	/**
	 * 查询最近十条版本更新日志
	 */
	@Override
	public String getNewVersionUpdateLog() {
		JSONObject jObject = new JSONObject();
		List<VersionInfo> list = new ArrayList<VersionInfo>();
		//list = versionMapper.getNewVersionUpdateLog();
		jObject.put("version", JSONArray.parseArray(JSON.toJSONString(list)));
		if (list.size() > 0) {
			jObject.put("versionCode", list.get(0).getVersionCode());
		} else {
			jObject.put("versionCode", "0");
		}
		return jObject.toString();
	}

	/**
	 * 下载新版本jsmt.apk
	 */
	@Override
	public void exportNewVersion(HttpServletRequest request, HttpServletResponse response, String versionFileName) throws Exception {
		logger.info("开始下载jsmt.apk文件");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		// 获取项目根目录
		// String ctxPath = request.getSession().getServletContext()
		// .getRealPath("");
		String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
		int p = path.indexOf("webapps");
		String realpath = path.substring(0, p + 8) + Constants.ApkVersion_Folder + "\\";
		realpath = realpath.replaceAll("%20", " ");
		logger.info("apk文件存放路径：" + realpath);

		// 获取下载文件路径
		// String downLoadPath = ctxPath+"/uploadFile/"+ storeName;
		VersionInfo vi = new VersionInfo();
		vi = versionMapper.findVersionInfo();
		String storeName = vi.getVersionFileName();
		if (versionFileName != null && versionFileName != "") {
			storeName = versionFileName;
		}
		String downLoadPath = realpath + storeName;

		// 获取文件的长度
		long fileLength = new File(downLoadPath).length();

		// 设置文件输出类型
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String(storeName.getBytes("utf-8"), "ISO8859-1"));
		// 设置输出长度
		response.setHeader("Content-Length", String.valueOf(fileLength));
		// 获取输入流
		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		// 输出流
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		// 关闭流
		bis.close();
		bos.close();

		// 下载文件次数+1
		int count = -1;
		int num = -1;
		// int apkTypeId = Constants.apkTypeId1; // jsmt的apkTypeId
		ApkTypeInfo ai = new ApkTypeInfo();
		ai = versionMapper.findApkTypeInfo();
		num = ai.getApkDownloadAmount();
		count = num + 1;
		ai.setApkDownloadAmount(count);
		if (count > 0) {
			versionMapper.updateDownloadAmount(ai);
			num = count;
		}
		logger.info("下载次数count：" + num);
	}

	/**
	 * 上传apk新版本
	 */
	@Override
	public String uploadNewFile(HttpServletRequest request,  int versionCode, String versionName,
			int apkTypeId, String versionLog, MultipartFile file) {
		logger.info("开始上传新版本文件");
		String flag = "error";
		try {
			long startTime = System.currentTimeMillis();
			// 如果文件不为空，写入上传路径
			if (!file.isEmpty() && !versionName.equals("") &&  !versionLog.equals("")) {
				// 上传文件路径
				// String path =
				// request.getServletContext().getRealPath("/images/");
				String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
				int p = path.indexOf("webapps");
				String realpath = path.substring(0, p + 8) + Constants.ApkVersion_Folder + "\\";
				realpath = realpath.replaceAll("%20", " ");
				logger.info("apk文件存放路径：" + realpath);
				// 上传文件名
				String filename = file.getOriginalFilename();
				String versionUrl = Constants.ApkVersion_Folder + "\\" + filename;
				// 上传文件大小
				long filesize = file.getSize();
				File filepath = new File(realpath, filename);
				// 判断路径是否存在，如果不存在就创建一个
				if (!filepath.getParentFile().exists()) {
					filepath.getParentFile().mkdirs();
				}
				// 将上传文件保存到一个目标文件当中
				file.transferTo(new File(realpath + File.separator + filename));

				// 读取apk文件MD5值
				String md5Value = CheckHashValue.getFileMD5(filepath);
				logger.info("上传文件MD5值为：" + md5Value);

				// 更新数据库信息
				VersionInfo vi = new VersionInfo();
				// vi = versionMapper.findVersionInfo(Constants.apkTypeId1);
				vi = versionMapper.findVersionInfo();
				int versionCount = 1;
				if (vi != null) {
					versionCount = vi.getVersionCount() + 1;
				} else {
					vi = new VersionInfo();
				}
				vi.setVersionCode(versionCode);
				vi.setVersionName(versionName);
				vi.setVersionUrl(versionUrl);
				vi.setApkTypeId(apkTypeId);
				vi.setVersionLog(versionLog);
				vi.setVersionSize(filesize);
				vi.setMd5Value(md5Value);
				vi.setVersionCount(versionCount);
				vi.setVersionFileName(filename);
				vi.setVersionFileUrl(realpath);

				versionMapper.uploadNewVersion(vi);

				List<VersionInfo> currentVersionInfo = OnLineInitial.versionInfoList;
				if (currentVersionInfo != null) {
					for (VersionInfo v : currentVersionInfo) {
							v.setVersionCode(versionCode);
					}
				}

				long endTime = System.currentTimeMillis();
				flag = "success";
				logger.info("上传新版本成功，运行时间：" + String.valueOf(endTime - startTime) + "ms");
			} else {
				logger.info("上传新版本失败");
			}
		} catch (Exception e) {
			logger.info("上传新版本出错：" + e.getMessage());
		}

		return flag;
	}

	/**
	 * 查询版本信息
	 */
	@Override
	public List<VersionInfo> findVersionLog(String versionType) {
		logger.info("浏览器查询版本信息");
		List<VersionInfo> list = new ArrayList<VersionInfo>();
		try {
			list = versionMapper.findVersionLog(versionType);
		} catch (Exception e) {
			logger.info("浏览器查询版本信息出错" + e.getMessage());
		}
		return list;
	}

	/**
	 * 删除版本信息和文件
	 */
	@Override
	public String deleteVersion(HttpServletRequest request, int versionId, String versionFileName) {
		String flag = "false";
		try {
			String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
			int p = path.indexOf("webapps");
			String realpath = path.substring(0, p + 8) + Constants.ApkVersion_Folder + "\\";
			realpath = realpath.replaceAll("%20", " ");
			logger.info("apk文件存放路径：" + realpath);
			if (versionFileName != null && versionFileName != "") {
				String storeName = versionFileName;
				String delPath = realpath + storeName;
				File delfile = new File(delPath);
				// 路径为文件且不为空则进行删除
				if (delfile.isFile() && delfile.exists()) {
					delfile.delete();
					logger.info("删除文件成功！");
				}
			}
			versionMapper.deleteVersion(versionId);
			flag = "true";
			logger.info("浏览器删除版本信息成功");
		} catch (Exception e) {
			logger.info("浏览器删除版本信息出错：" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 查询版本种类信息
	 */
	@Override
	public List<ApkTypeInfo> findApkInfo() {
		logger.info("浏览器查询版本种类信息");
		List<ApkTypeInfo> list = new ArrayList<ApkTypeInfo>();
		try {
			list = versionMapper.findApkInfo();
		} catch (Exception e) {
			logger.info("浏览器查询版本种类信息出错" + e.getMessage());
		}
		return list;
	}

	/**
	 * 新增版本种类信息
	 */
	@Override
	public String addApkInfo(String apkTypeName, String apkTypeIcon) {
		String flag = "flase";
		try {
			ApkTypeInfo ai = new ApkTypeInfo();
			ai.setApkTypeName(apkTypeName);
			ai.setApkTypeIcon(apkTypeIcon);
			int i = versionMapper.addApkInfo(ai);
			if (i == 1)
				flag = "true";
		} catch (Exception e) {
			logger.info("浏览器新增版本种类出错：" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 修改版本种类信息
	 */
	@Override
	public String editApkInfo( String apkTypeName, String apkTypeId) {
		String flag = "false";
		try {
			ApkTypeInfo ai = new ApkTypeInfo();
			ai.setApkTypeName(apkTypeName);
			ai.setApkTypeId(Integer.valueOf(apkTypeId));
			int i = versionMapper.editApkInfo(ai);
			if (i == 1)
				flag = "true";
		} catch (Exception e) {
			logger.info("浏览器修改版本种类信息：" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 删除版本信息种类
	 */
	@Override
	public String deleteApkInfo(int apkTypeId) {
		String flag = "false";
		try {
			int i = versionMapper.deleteApkInfo(apkTypeId);
			if (i == 1)
				flag = "true";
		} catch (Exception e) {
			logger.info("浏览器删除版本种类信息：" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 查询app种类
	 */
	@Override
	public String findApkTypeId() {
		String result = "";
		try {
			List<Combox> list = new ArrayList<Combox>();
			list = versionMapper.findApkTypeId();
			result = JSON.toJSONString(list);
		} catch (Exception e) {
			logger.info("" + e.getMessage());
		}
		return result;
	}

	@Override
	public void exportNewVersionByLoginId(HttpServletRequest request, HttpServletResponse response,
			String versionFileName,  String loginId) {
		logger.info("开始下载jsmt.apk文件");
		try {
			request.setCharacterEncoding("UTF-8");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			// 获取项目根目录
			// String ctxPath = request.getSession().getServletContext()
			// .getRealPath("");
			String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
			int p = path.indexOf("webapps");
			String realpath = path.substring(0, p + 8) + Constants.ApkVersion_Folder + "\\";
			realpath = realpath.replaceAll("%20", " ");
			logger.info("apk文件存放路径：" + realpath);

			// 获取下载文件路径
			// String downLoadPath = ctxPath+"/uploadFile/"+ storeName;
			VersionInfo vi = new VersionInfo();
			vi = versionMapper.findVersionInfo();
			String storeName = vi.getVersionFileName();
			if (versionFileName != null && versionFileName != "") {
				storeName = versionFileName;
			}
			String downLoadPath = realpath + storeName;

			// 获取文件的长度
			long fileLength = new File(downLoadPath).length();

			// 设置文件输出类型
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(storeName.getBytes("utf-8"), "ISO8859-1"));
			// 设置输出长度
			response.setHeader("Content-Length", String.valueOf(fileLength));
			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			// 输出流
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			// 关闭流
			bis.close();
			bos.close();

			// 下载文件次数+1
			int count = -1;
			int num = -1;
			// int apkTypeId = Constants.apkTypeId1; // jsmt的apkTypeId
			ApkTypeInfo ai = new ApkTypeInfo();
			ai = versionMapper.findApkTypeInfo();
			num = ai.getApkDownloadAmount();
			count = num + 1;
			ai.setApkDownloadAmount(count);
			if (count > 0) {
				versionMapper.updateDownloadAmount(ai);
				num = count;
			}
			logger.info("下载次数count：" + num);
			versionMapper.updateNumberOfShareByLoginId(loginId);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 查询最新的稳定版
	 */
	@Override
	public VersionInfo findVersionInfo() {
		VersionInfo vi = new VersionInfo();
		vi = versionMapper.findVersionInfo();
		return vi;
	}
}
