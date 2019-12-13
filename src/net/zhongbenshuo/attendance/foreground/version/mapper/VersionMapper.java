package net.zhongbenshuo.attendance.foreground.version.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.foreground.Condition.StationInfo;
import net.zhongbenshuo.attendance.foreground.version.bean.ApkTypeInfo;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;

public interface VersionMapper {

	public List<VersionInfo> getNewVersionUpdateLog(@Param("apkTypeId")int apkTypeId);

	public VersionInfo findVersionInfo(); // 根据apkTypeId查询稳定版本信息

	public int updateDownloadAmount(ApkTypeInfo ai); // 根据apkTypeId更新apk下载次数

	public int uploadNewVersion(VersionInfo pic); // 更新table_version相关信息

	public ApkTypeInfo findApkTypeInfo(); // 根据apkTypeId查询apk信息

	public List<VersionInfo> findVersionLog(@Param("versionType") String versionType); // 查询版本信息

	public int deleteVersion(int versionId); // 删除版本信息

	public List<ApkTypeInfo> findApkInfo(); // 查询版本种类信息

	public int addApkInfo(ApkTypeInfo ai); // 新增版本种类信息

	public int editApkInfo(ApkTypeInfo ai); // 修改版本种类信息

	public int deleteApkInfo(@Param("apkTypeId")int apkTypeId); // 删除版本信息种类

	public VersionInfo findVersionInfo2(int apkTypeId);// 查询最新预览版更新

	public List<Combox> findApkTypeId();// 查询app种类

	public void updateNumberOfShareByLoginId(@Param("loginId") String loginId);// 分享次数加一

	public List<VersionInfo> findVersionInfoList();// 查询最新发布稳定版APP信息
	/**
	 * 查询环境检测仪基本信息
	 * @return
	 */
	public List<StationInfo> findStationInfoList();

}
