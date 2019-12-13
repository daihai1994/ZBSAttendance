package net.zhongbenshuo.attendance.foreground.outAttendance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfoAuditRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.UserBase;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;

public interface OutAttendanceMapper {
	/***
	 * 外勤申请信息
	 * @param bt开始时间
	 * @param et结束时间
	 * @param user_id审批人
	 * @param applicant_user_id申请人
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutAttendanceInfo> findOutAttendance(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("applicant_user_id")String applicant_user_id, 
			@Param("bNum")int bNum,@Param("rows")int rows);
	/***
	 * 外勤审批信息提交
	 * @param outAttendanceRecordAudit_id
	 * @param audit_remarks
	 * @param audit_status
	 * @return
	 */
	int submitAuditOutAttendance(@Param("outAttendanceRecordAudit_id")String outAttendanceRecordAudit_id, @Param("audit_remarks")String audit_remarks, @Param("audit_status")String audit_status);
	/***
	 * 外勤审批 当前审批通过，修改成下一个审批人修改
	 * @param outAttendanceRecordAudit_id
	 * @return
	 */
	int updateAuditPeople(@Param("outAttendanceRecordAudit_id")String outAttendanceRecordAudit_id);
	/***
	 * 查询下一个审批人的userId
	 * @param outAttendanceRecordAudit_id
	 * @return
	 */
	String findAuditPeople(@Param("outAttendanceRecordAudit_id")String outAttendanceRecordAudit_id);
	/***
	 * 修改外勤详情信息的状态
	 * @param out_attendance_id
	 */
	void updateOutAttendanceRecordStatus(@Param("out_attendance_id")String out_attendance_id,@Param("status")int status);
	/***
	 * 修改考勤信息的状态
	 * @param out_attendance_id
	 */
	void updateAttendanceRecordresult(@Param("out_attendance_id")String out_attendance_id,@Param("result_id")String result_id);
	/**
	 * 根据外勤详细信息ID查询考勤记录
	 * @param out_attendance_id
	 * @return
	 */
	AttendanceRecord findAttendanceRecordByout_attendance_id(@Param("out_attendance_id")String out_attendance_id);
	/**
	 * 审批记录的查询
	 */
	List<OutAttendanceInfo> findauditOutAttendance_historical_records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("applicant_user_id")String applicant_user_id, 
			@Param("bNum")int bNum,@Param("rows")int rows);
	/**
	 * 查询外勤打卡审批的相关记录
	 * @param out_attendance_id
	 * @return
	 */
	List<OutAttendanceInfoAuditRecord> findOutAttendanceInfoAuditRecord(@Param("out_attendance_id")int out_attendance_id);
	/**
	 * 查询外勤打卡申请人userId
	 * @param out_attendance_id
	 * @return
	 */
	String findApplyUserId(@Param("out_attendance_id")String out_attendance_id);
	/**
	 * 查询补卡审批的相关记录
	 * @param appeal_attendacne_id
	 * @return
	 */
	List<OutAttendanceInfoAuditRecord> findAppealRecordInfoAuditRecord(@Param("appeal_attendacne_id")int appeal_attendacne_id);
	/**
	 * 查询补卡审批记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<AppealAttendanceInfo> findAppealAttendance(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("applicant_user_id")String applicant_user_id, 
			@Param("bNum")int bNum,@Param("rows")int rows);
	/**
	 * 补卡审批人提交审批信息
	 * @param appealAttendanceRecordAudit_id
	 * @param audit_remarks
	 * @param audit_status
	 * @return
	 */
	int submitAuditappealAttendance(@Param("appealAttendanceRecordAudit_id")String appealAttendanceRecordAudit_id, @Param("audit_remarks")String audit_remarks, 
			@Param("audit_status")String audit_status);
	/**
	 * 补卡审批修改下一个人
	 * @param appealAttendanceRecordAudit_id
	 * @return
	 */
	int updateAuditPeopleAppeal(@Param("appealAttendanceRecordAudit_id")String appealAttendanceRecordAudit_id);
	/**
	 * 修改补卡详情信息的状态  状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
	 * @param appeal_attendance_id
	 * @param i
	 */
	void updateAppealAttendanceRecordStatus(@Param("appeal_attendance_id")String appeal_attendance_id, @Param("status")int status);
	/**
	 * //修改考勤信息的状态 5外勤签到成功；6外勤签退成功；7外勤签到未审批;8外勤签退未审批;9外勤签到审批中;10外勤签退审批中;11外勤签到失败;12外勤签退失败
	 * @param appeal_attendance_id
	 * @param result_id
	 */
	void updateAppealAttendanceRecordresult(@Param("appeal_attendance_id")String appeal_attendance_id, @Param("result_id")String result_id);
	/**
	 * 查询下一个需要审批人的user_id
	 * @param appealAttendanceRecordAudit_id
	 * @return
	 */
	String findAuditPeopleAppeal(@Param("appealAttendanceRecordAudit_id")String appealAttendanceRecordAudit_id);
	/**
	 * 
	 * @param appeal_attendance_id
	 * @return
	 */
	String findApplyUserIdAppeal(@Param("appeal_attendance_id")String appeal_attendance_id);
	/**
	 * 根补卡详情ID查询考勤记录
	 * @param appeal_attendance_id
	 * @return
	 */
	AttendanceRecord findAttendanceRecordByAppeal_attendance_id(@Param("appeal_attendance_id")String appeal_attendance_id);
	/**
	 * 查询加班审批信息
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OverTimeRecord> findoverTime(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("applicant_user_id")String applicant_user_id, 
			@Param("bNum")int bNum,@Param("rows")int rows);
	/**
	 * 查询加班审批历史记录
	 * @param id
	 * @return
	 */
	List<OutAttendanceInfoAuditRecord> findoverTimeauditRecord(@Param("id")int id);
	/**
	 * 加班审批提交
	 * @param id
	 * @param audit_remarks
	 * @param audit_status
	 * @return
	 */
	int submitAuditoverTimeAudit(@Param("id")String id, @Param("audit_remarks")String audit_remarks, @Param("audit_status")String audit_status);
	/**
	 * 补卡审批查询历史记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<AppealAttendanceInfo> findauditappealAttendance_historical_records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 加班审批修改下一个审批的人
	 * @param audit_id
	 * @return
	 */
	int updateAuditPeopleOverTime(@Param("audit_id")String audit_id);
	/**
	 * 加班申请状态的修改
	 * @param id
	 * @param result_id
	 */
	void updateOverTimeAttendanceRecordresult(@Param("id")String id, @Param("result_id")String result_id);
	/**
	 * 加班申请查询当前需要审批的人
	 * @param audit_id
	 * @return
	 */
	String findAuditPeopleOverTime(@Param("audit_id")String audit_id);
	/**
	 * 查询加班申请人的userId
	 * @param id
	 * @return
	 */
	String findApplyUserIdOverTime(@Param("id")String id);
	/**
	 * 根据加班iD查询加班申请信息
	 * @param id
	 * @return
	 */
	OverTimeRecord findAttendanceRecordByOverTime_attendance_id(@Param("id")String id);
	/**
	 * 加班审批查询审批历史记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OverTimeRecord> findauditoverTime_historical_records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 查询加班申请的详情
	 * @param id
	 * @return
	 */
	OverTimeRecord findOverTimeById(@Param("id")String id);
	/**
	 * 查询申请的加班小时
	 * @param user_id
	 * @return
	 */
	UserBase findUserBase(@Param("user_id")int user_id);
	/**
	 * 修改申请人的加班小时
	 * @param user_id
	 * @param hours
	 */
	void updateUserBaseOverTime(@Param("user_id")int user_id, @Param("hours")float hours);
	/**
	 * 假期审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<VacationRecord> findvacation(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 假期审批历史详情
	 * @param id
	 * @return
	 */
	List<OutAttendanceInfoAuditRecord> findvacationauditRecord(@Param("id")int id);
	/**
	 * 审批人提交假期审批信息
	 * @param id
	 * @param audit_remarks
	 * @param audit_status
	 * @return
	 */
	int submitAuditvacationAudit(@Param("id")String id, @Param("audit_remarks")String audit_remarks, @Param("audit_status")String audit_status);
	/**
	 * 假期审批修改下一个审批人
	 * @param audit_id
	 * @return
	 */
	int updateAuditPeoplevacation(@Param("audit_id")String audit_id);
	/**
	 * 修改假期信息的状态
	 * @param id
	 * @param result_id
	 */
	void updatevacationAttendanceRecordresult(@Param("id")String id, @Param("result_id")String result_id);
	/**
	 * 查询下一个假期审批人
	 * @param audit_id
	 * @return
	 */
	String findAuditPeoplevacation(@Param("audit_id")String audit_id);
	/**
	 * 查询假期申请人id
	 * @param id
	 * @return
	 */
	String findApplyUserIdvacation(@Param("id")String id);
	/**
	 * 当时整个申请通过的时候，计算请假时间，新增到userBase
	 * @param id
	 * @return
	 */
	VacationRecord findvacationById(@Param("id")String id);
	/**
	 * 修改用户的请假小时
	 * @param user_id
	 * @param hours
	 */
	void updateUserBaseLeaveTime(@Param("user_id")int user_id, @Param("hours")float hours);
	/**
	 * 查询假期申请信息
	 * @param id
	 * @return
	 */
	VacationRecord findAttendanceRecordByvacation_attendance_id(@Param("id")String id);
	/**
	 * 假期审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<VacationRecord> findauditvacation_historical_records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 查询外出审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutGoingRecord> findoutGoing(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 查询外出审批历史记录
	 * @param id
	 * @return
	 */
	List<OutAttendanceInfoAuditRecord> findoutGoingauditRecord(@Param("id")int id);
	/**
	 * 提交外出审批的信息
	 * @param id
	 * @param audit_remarks
	 * @param audit_status
	 * @return
	 */
	int submitAuditoutGoingAudit(@Param("id")String id, @Param("audit_remarks")String audit_remarks, @Param("audit_status")String audit_status);
	/**
	 * 修改外出下一个需要审批的人
	 * @param audit_id
	 * @return
	 */
	int updateAuditPeopleOutGoing(@Param("audit_id")String audit_id);
	/**
	 * 修改外出审批的状态
	 * @param id
	 * @param result_id
	 */
	void updateOutGoingAttendanceRecordresult(@Param("id")String id, @Param("result_id")String result_id);
	/**
	 * 查询下一个审批user_id
	 * @param audit_id
	 * @return
	 */
	String findAuditPeopleOutGoing(@Param("audit_id")String audit_id);
	/**
	 * 查询加班申请人的userId
	 * @param id
	 * @return
	 */
	String findApplyUserIdOutGoing(@Param("id")String id);
	
	/**
	 * 查询外出申请详情
	 * @param id
	 * @return
	 */
	OutGoingRecord findAttendanceRecordByOutGoing_attendance_id(@Param("id")String id);
	/**
	 * 外出审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutGoingRecord> findauditoutGoing_historical_records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 出差审批查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<BusinessTraveIRecord> findbusinessTraveI(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 出差审批历史记录
	 * @param id
	 * @return
	 */
	List<OutAttendanceInfoAuditRecord> findbusinessTraveIauditRecord(@Param("id")int id);
	/**
	 * 出差审批人提交审批信息
	 * @param id
	 * @param audit_remarks
	 * @param audit_status
	 * @return
	 */
	int submitAuditbusinessTraveIAudit(@Param("id")String id, @Param("audit_remarks")String audit_remarks, @Param("audit_status")String audit_status);
	/**
	 * 出差修改下一个需要审批的人
	 * @param audit_id
	 * @return
	 */
	int updateAuditPeopleBusinessTraveI(@Param("audit_id")String audit_id);
	/**
	 * 出差申请状态修改
	 * @param id
	 * @param result_id
	 */
	void updateBusinessTraveIAttendanceRecordresult(@Param("id")String id, @Param("result_id")String result_id);
	/**
	 * 查询下一个需要出差审批人的user_id
	 * @param audit_id
	 * @return
	 */
	String findAuditPeopleBusinessTraveI(@Param("audit_id")String audit_id);
	/**
	 * 查询出差申请人的userId
	 * @param id
	 * @return
	 */
	String findApplyUserIdBusinessTraveI(@Param("id")String id);
	/**
	 * 查询出差申请的详情
	 * @param id
	 * @return
	 */
	BusinessTraveIRecord findAttendanceRecordByBusinessTraveI_attendance_id(@Param("id")String id);
	/**
	 * 出差审批记录查询
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<BusinessTraveIRecord> findauditbusinessTraveI_historical_records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id,
			@Param("applicant_user_id")String applicant_user_id, @Param("bNum")int bNum, @Param("rows")int rows);
}
