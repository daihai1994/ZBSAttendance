package net.zhongbenshuo.attendance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.Face;
import net.zhongbenshuo.attendance.bean.LoginLogInfo;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.UserInfoStatus;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.ExportReportAttendanceRecord;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.WorkingTimeUser;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;
import net.zhongbenshuo.attendance.utils.WebRsaKey;

public interface LoginMapper {

	public User checkUser(@Param("userId") String userId, @Param("userPwd") String userPwd);

	public int addPCLoginLog(LoginLogInfo loginLogInfo);

	public List<Combox> compang_main(@Param("company_id") String company_id);

	public String findDeletePwd(@Param("company_id") String company_id);

	public WebRsaKey webRsaKey();

	public void addWorkingTime(List<WorkingTime> workingTimes);

	public List<Company> findCompany();
	/**
	 * 查询用户
	 * @return
	 */
	public List<User> findUser();
	/**
	 * 新增用户没天记录
	 * @param workingTimes
	 */
	public void addWorkingTimeUser(List<WorkingTime> workingTimes);
	/**
	 * 查询上个月的所有人员的工作时间，加班时间，请假时间
	 * @param yearMonth
	 * @return
	 */
	public List<WorkingTimeUser> findWorkingTimeUser(@Param("yearMonth")String yearMonth);
	/**
	 * 新增每人每月的工作时间，加班时间，请假时间等
	 * @param recordList
	 */
	public void addUserBaseMonth(@Param("list")List<ExportReportAttendanceRecord> recordList,@Param("year")String year,@Param("month")String month);
	/**
	 * 每月的月初把当前的每个人的请假时间取消
	 */
	public void updateUserBase();
	/**
	 * 新增用户的时候新增到userBase
	 * @param user_id
	 * @param company_id
	 */
	public void addUserBase(@Param("user_id")String user_id, @Param("company_id")String company_id);
	/**
	 * 查询全部的人脸信息
	 * @return
	 */
	public List<Face> findFaceList();
	/**
	 * 修改人脸的有效
	 * @param id
	 */
	public void updateFaceEffective(@Param("id")int id);
	/**
	 * 查询今日是否是工作日
	 * @param sdate
	 * @return
	 */
	public WorkingTime findWorkingTime(@Param("sdate")String sdate);
	/**
	 * 查询用户有openiD的
	 * @return
	 */
	public List<UserInfoStatus> findUserOpenId(@Param("time")String time);

}
