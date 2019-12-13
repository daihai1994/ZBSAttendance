package net.zhongbenshuo.attendance.foreground.configInfor.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.bean.OvertimeType;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.WeChatInfo;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.configInfor.bean.TimmerCron;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.UserBase;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;

public interface ConfigInforMapper {
	/***
	 * 查询定时器任务
	 * @param company_id
	 * @return
	 */
	List<TimmerCron> findTimmerCron();
	/***
	 * 查询是否存在一样的定时任务
	 * @param cron
	 * @return
	 */
	int findCron(@Param("cron")String cron);
	/***
	 * 保存定时任务
	 * @param mode
	 * @param ttime
	 * @param cron
	 * @param jobName
	 * @return
	 */
	int save_timmer(@Param("mode")String mode, @Param("ttime")String ttime, @Param("cron")String cron, @Param("jobName")String jobName, @Param("user_ids")int user_ids);
	
	/**
	 * 根据jobname查询定时器任务
	 * @param jobName
	 * @return
	 */
	TimmerCron findByName(@Param("jobName")String jobName);
	/**
	 * 根据id查询定时器任务
	 * @param id
	 * @return
	 */
	TimmerCron findById(@Param("id")String id);
	/***
	 * 物理删除定时任务器
	 * @param id
	 * @return
	 */
	int deleteTimmerCron(@Param("id")String id);
	/***
	 * 修改定时任务器
	 * @param mode
	 * @param ttime
	 * @param cron
	 * @param jobName
	 * @param user_ids
	 * @param id
	 * @return
	 */
	int update_timmer(@Param("mode")String mode, @Param("ttime")String ttime, @Param("cron")String cron, @Param("jobName")String jobName, @Param("user_ids")int user_ids,
			@Param("id")String id);
	/***
	 * 查询公司下的计算工时定时任务
	 * @param company_id
	 * @param group_id
	 * @return
	 */
	List<TimmerCron> findTimmerCron_configure(@Param("company_id")int company_id, @Param("group_id")int group_id);
	/***
	 * 下拉框查询定时任务器
	 * @return
	 */
	List<Combox> findTimmer();
	/***
	 * 新增定时任务
	 * @param company_id
	 * @param timmer_id
	 * @param timer_name
	 * @param group_id
	 * @return
	 */
	int configureAdd_timmer(@Param("company_id")String company_id, @Param("timmer_id")String timmer_id, @Param("timer_name")String timer_name, @Param("group_id")String group_id);
	/***
	 * 查询定时任务BYID
	 * @param id
	 * @return
	 */
	TimmerCron findTimmerByWorkId(@Param("id")String id);
	/***
	 * 修改定时任务
	 * @param id
	 * @param timmer_id
	 * @param timer_name
	 * @return
	 */
	int configureupdate_timmer(@Param("id")String id, @Param("timmer_id")String timmer_id, @Param("timer_name")String timer_name);
	/***
	 * 删除定时任务
	 * @param id
	 * @return
	 */
	int deleteTimmerCron_configure(@Param("id")String id);
	/**
	 * 查询定时任务
	 * @param c
	 * @return
	 */
	List<TimmerCron> findTimer(@Param("timmer_id")String timmer_id);
	/**
	 * 查询公司下的所有用户
	 * @param company_id
	 * @return
	 */
	List<User> findUserList(@Param("company_id")String company_id);
	/**
	 * 查询用户的一天上班时长
	 * @param user_id
	 * @param bt
	 * @param et
	 * @return
	 */
	List<AttendanceRecord> findAttendanceRecordByUserId(@Param("user_id")int user_id, @Param("bt")String bt, @Param("et")String et);
	/**
	 * 修改人员的每日工作时间
	 * @param user_id
	 * @param hours
	 * @param bt
	 * @param lackSignBack 
	 * @param lackSignIn 
	 * @param qiantuiMinute 
	 * @param qiandaoMinute 
	 * @param whetherLeaveEarly 
	 * @param whetherLater 
	 * @param whetherMealAllowance 
	 * @param whetherWorking 
	 */
	void updateWorkingUser(@Param("user_id")int user_id, @Param("hours")float hours, @Param("bt")String bt,
			@Param("whetherWorking")int whetherWorking, @Param("whetherMealAllowance")int whetherMealAllowance, @Param("whetherLater")int whetherLater, @Param("whetherLeaveEarly")int whetherLeaveEarly, 
			@Param("qiandaoMinute")int qiandaoMinute, @Param("qiantuiMinute")int qiantuiMinute, @Param("lackSignIn")int lackSignIn,@Param("lackSignBack") int lackSignBack);
	/**
	 * 查询人员还没有处理的已经审核过的加班申请
	 * @param user_id
	 * @return
	 */
	List<OverTimeRecord> findOverTimeRecords(@Param("user_id")int user_id);
	/**
	 * 修改人员的加班时间
	 * @param user_id
	 * @param overtimeHours
	 * @param whetherOverTimes 
	 * @param substring
	 */
	void updateUserOvertime(@Param("user_id")int user_id, 
			@Param("overtimeHours")float overtimeHours, @Param("date")String date, @Param("whetherOverTime")int whetherOverTimes);
	/**
	 * 修改加班申请的状态
	 * @param id
	 */
	void updateOvertimeStatus(@Param("id")int id);
	/**
	 * 查询人员还没有处理的已经审批过的假期申请
	 * @param user_id
	 * @return
	 */
	List<VacationRecord> findVacationRecords(@Param("user_id")int user_id);
	/**
	 * 修改人员的休假时间
	 * @param user_id
	 * @param vacationHours
	 * @param substring
	 */
	void updateUservacation(@Param("user_id")int user_id, 
			@Param("vacationHours")float vacationHours, @Param("date")String date);
	/**
	 * 修改假期申请的状态
	 * @param id
	 */
	void updateVacationStatus(@Param("id")int id);
	/**
	 * 查询人员的所有加班时间
	 * @param user_id
	 * @return
	 */
	String findOvertimeHour(@Param("user_id")int user_id);
	/**
	 * 修改人员的加班时间
	 * @param user_id
	 * @param allovertimeHour
	 */
	void updateOvertimeUser(@Param("user_id")int user_id, @Param("allovertimeHour")float allovertimeHour);
	/**
	 * 查询人员的所有请假时间
	 * @param user_id
	 * @return
	 */
	float findAllUserBaseVacationHours(@Param("user_id")int user_id);
	/**
	 * 修改人员的所有请假时间
	 * @param user_id
	 * @param alluserBasevacationHour
	 */
	void updateAllUserBaseVacationHours(@Param("user_id")int user_id, @Param("alluserBasevacationHour")float alluserBasevacationHour);
	/**
	 * 查询公司下的所有用户的总调休，加班信息
	 * @param company_id
	 * @return
	 */
	List<UserBase> findUserBaseList(@Param("company_id")String company_id);
	
	void updateFaceStatus(@Param("id")int id);
	/**
	 * 批量修改人脸同步状态
	 * @param updateList
	 */
	void updateFaceStatusList(List<Integer> updateList);
	/**
	 * 删除人脸信息
	 * @param id
	 */
	void deleteFace(@Param("id")int id);
	/**
	 * 批量删除人脸信息
	 * @param updateList
	 */
	void deleteFaceList(List<Integer> updateList);
	/**
	 * 查询当天日期的状态
	 * @param date
	 * @return
	 */
	WorkingTime findworkingTime(@Param("date")String date);
	/**
	 * 查询加班系数
	 * @return
	 */
	List<OvertimeType> findOvertimeType();
	/**
	 * 查询全年的假期
	 * @param time
	 * @return
	 */
	List<WorkingTime> findWorkingTimeList(@Param("time")String time);
	/**
	 * 获取微信基本信息
	 * @return
	 */
	WeChatInfo findWeChatInfo();
	/**
	 * 修改微信的accesstoken
	 * @param expires
	 * @param access_token
	 */
	void updateWeChatInfoAccessToken(@Param("expires")String expires, @Param("access_token")String access_token);
	/**
	 * 修改微信的ticket
	 * @param expires
	 * @param ticket
	 */
	void updateWeChatInfoTicket(@Param("expires")String expires, @Param("ticket")String ticket);
	/**
	 * 新增unionid
	 * @param openId
	 * @param unionid
	 */
	void addUnionId(@Param("openId")String openId, @Param("unionid")String unionid);
	/**
	 * 删除unionid
	 * @param openId
	 */
	void deleteUnionId(@Param("openId")String openId);

}
