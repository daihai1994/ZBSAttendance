package net.zhongbenshuo.attendance.foreground.attendanceRecord.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.ExportReportAttendanceRecord;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.WorkingTimeUser;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;

public interface AttendanceRecordMapper {
	/**
	 * 查询时间点的考勤记录
	 * @param user_id
	 * @param et
	 * @param bt
	 * @return
	 */
	List<AttendanceRecord> findAttendanceRecord(@Param("user_id")String user_id, @Param("et")String et, @Param("bt")String bt);
	/**
	 * 查询时间段的应勤情况
	 * @param bt
	 * @param et
	 * @param valueOf
	 * @return
	 */
	List<WorkingTime> findWorkingTimes(@Param("bt")String bt, @Param("et")String et, @Param("company_id")Integer company_id);
	/**
	 * 查询员工一个月的工时，加班，请假等信息
	 * @param date
	 * @param user_id
	 * @param company_id
	 * @return
	 */
	List<WorkingTimeUser> findWorkingTimeUser(@Param("date")String date, @Param("user_id")String user_id, @Param("company_id")String company_id);
	/**
	 * 查询公司下的员工
	 * @param company_id 公司ID
	 * @return
	 */
	List<Combox> findUser(@Param("company_id")String company_id);
	/**
	 * 查询员工的考勤详情记录
	 * @param user
	 * @param year
	 * @param month
	 * @return
	 */
	List<ExportReportAttendanceRecord> exportexportReportAttendanceRecord(@Param("user")String user,@Param("bt") String bt,@Param("et") String et,@Param("company_id")String company_id);
	/**
	 * 
	 * @param company_id
	 * @return
	 */
	List<ExportReportAttendanceRecord> findUserAll(@Param("company_id")String company_id);
	/**
	 * 查询当月的考勤详情
	 * @param company_id
	 * @param user
	 * @param time
	 * @return
	 */
	List<WorkingTimeUser> findWorkingTimeUsersList(@Param("company_id")String company_id,@Param("user") String user, @Param("time")String time);

}
