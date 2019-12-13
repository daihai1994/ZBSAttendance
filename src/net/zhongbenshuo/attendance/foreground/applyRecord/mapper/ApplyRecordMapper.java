package net.zhongbenshuo.attendance.foreground.applyRecord.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;

public interface ApplyRecordMapper {
	/**
	 * 查询外勤打卡需要审批信息
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutAttendanceInfo> findApply_Records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 标记为已读
	 * @param list
	 */
	void markRead(List<String> list);
	/**
	 * 全部标记已读
	 * @param userId
	 */
	void allTagsRead(@Param("userId")String userId);
	/**
	 * 查询外勤需要审批
	 * @param userId
	 * @return
	 */
	int findOutAttendanceApproval(@Param("userId")String userId);
	/**
	 * 查询外勤申请
	 * @param userId
	 * @return
	 */
	int findOutAttendanceApply(@Param("userId")String userId);
	/**
	 * 查询补卡申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<AppealAttendanceInfo> findAppeal_Records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 标记为无效
	 * @param list
	 */
	void updateApplyEffective(List<String> list);
	/**
	 * 补卡申请标记为无效
	 * @param list
	 */
	void updateAppealEffective(List<String> list);
	/**
	 * 查询补卡审批的个数
	 * @param userId
	 * @return
	 */
	int findAppealAttendanceApproval(@Param("userId")String userId);
	/**
	 * 查询补卡申请的个数
	 * @param userId
	 * @return
	 */
	int findAppealAttendanceApply(@Param("userId")String userId);
	/**
	 * 申请人查询加班申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OverTimeRecord> findOverTime_Record(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 加班申请标记无效
	 * @param list
	 */
	void updateOverTimeEffective(List<String> list);
	/**
	 * 加班标记已读
	 * @param list
	 */
	void markReadoverTime(List<String> list);
	/**
	 * 加班申请全部标记已读
	 * @param userId
	 */
	void allTagsReadoverTime(@Param("userId")String userId);
	/**
	 * 加班审批的个数
	 * @param userId
	 * @return
	 */
	int findOverTimeApproval(@Param("userId")String userId);
	/**
	 * 加班申请的个数
	 * @param userId
	 * @return
	 */
	int findOverTimeApply(@Param("userId")String userId);
	/**
	 * 申请人查询假期申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<VacationRecord> findVacation_Record(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 假期申请标记无效
	 * @param list
	 */
	void updatevacationEffective(List<String> list);
	/**
	 * 假期全部标记已读
	 * @param userId
	 */
	void allTagsReadvacation(@Param("userId")String userId);
	/**
	 * 假期标记为已读
	 * @param list
	 */
	void markReadvacation(List<String> list);
	/**
	 * 申请人查询外出申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutGoingRecord> findOutGoing_Record(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 外出申请标记无效
	 * @param list
	 */
	void updateoutGoingEffective(List<String> list);
	/**
	 * 外出标记为已读
	 * @param list
	 */
	void markReadoutGoing(List<String> list);
	/**
	 * 外出全部标记已读
	 * @param userId
	 */
	void allTagsReadoutGoing(@Param("userId")String userId);
	/**
	 * 审批过的历史记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<BusinessTraveIRecord> findbusinessTraveI_Record(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 出差申请标记无效
	 * @return
	 */
	void updatebusinessTraveIEffective(List<String> list);
	/**
	 * 出差标记为已读
	 * @param list
	 */
	void markReadbusinessTraveI(List<String> list);
	/**
	 * 出差全部标记已读
	 * @param userId
	 */
	void allTagsReadbusinessTraveI(@Param("userId")String userId);
	/**
	 * 休假审批个数
	 * @param userId
	 * @return
	 */
	int findVacationApproval(@Param("userId")String userId);
	/**
	 * 休假申请个数
	 * @param userId
	 * @return
	 */
	int findVacationApply(@Param("userId")String userId);
	/**
	 * 外出审批个数
	 * @param userId
	 * @return
	 */
	int findOutGoingApproval(@Param("userId")String userId);
	/**
	 * 外出申请
	 * @param userId
	 * @return
	 */
	int findOutGoingApply(@Param("userId")String userId);
	/**
	 * 出差审批个数
	 * @param userId
	 * @return
	 */
	int findBusinessTraveIApproval(@Param("userId")String userId);
	/**
	 * 出差申请个数
	 * @param userId
	 * @return
	 */
	int findBusinessTraveIApply(@Param("userId")String userId);

}
