package net.zhongbenshuo.attendance.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.bean.AllUserInfo;
import net.zhongbenshuo.attendance.bean.AllUserInfoAddressBook;
import net.zhongbenshuo.attendance.bean.AllUserInfoStatus;
import net.zhongbenshuo.attendance.bean.Announcement;
import net.zhongbenshuo.attendance.bean.AppealAttendanceRecord;
import net.zhongbenshuo.attendance.bean.AppealAttendanceRecordAudit;
import net.zhongbenshuo.attendance.bean.AttendanceFaceRecord;
import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.bean.AttendanceRule;
import net.zhongbenshuo.attendance.bean.Department;
import net.zhongbenshuo.attendance.bean.Face;
import net.zhongbenshuo.attendance.bean.FaceRecord;
import net.zhongbenshuo.attendance.bean.OpenAndCloseDoorRecord;
import net.zhongbenshuo.attendance.bean.OutAttendanceRecord;
import net.zhongbenshuo.attendance.bean.OutAttendanceRecordAudit;
import net.zhongbenshuo.attendance.bean.OutAttendanceRecordCopy;
import net.zhongbenshuo.attendance.bean.OvertimeType;
import net.zhongbenshuo.attendance.bean.Position;
import net.zhongbenshuo.attendance.bean.Status;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.VacationType;
import net.zhongbenshuo.attendance.bean.VisitorInfo;
import net.zhongbenshuo.attendance.bean.VisitorSubscribe;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.advertisement.bean.Advertisement;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.BusinessTraveIRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.BusinessTraveIRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OutGoingRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OutGoingRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.VacationRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.VacationRecordCopy;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.WorkingTimeUser;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;
import net.zhongbenshuo.attendance.foreground.homePagePicture.bean.HomePagePicture;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;
import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;
import net.zhongbenshuo.attendance.utils.AndroidHeaderInfo;
import net.zhongbenshuo.attendance.utils.JPushData;

public interface IUserService {

	/**
	 * 提供注册服务
	 * 
	 * @param userId
	 * @param userPwd
	 * @return
	 */
	int registerUser(String userPhoneNumber, String userPwd, String ipAddress);

	/**
	 * 提供登录服务
	 * 
	 * @param userName
	 * @param userPwd
	 * @return
	 */
	User loginUser(String userId, String userPwd);

	/**
	 * 提供查找用户服务
	 * 
	 * @param userName
	 * @return
	 */
	User searchUser(String key, String value);

	/**
	 * 查找所有用户
	 * @param companyId 
	 * 
	 * @param userName
	 * @return
	 */
	List<AllUserInfoAddressBook> searchAllUser(int companyId,String authority);

	/**
	 * 提供更新用户信息服务
	 * 
	 * @param userName
	 * @return
	 */
	int updateUser(String userId, String key, String value);

	/**
	 * 提供更新用户部门与职位信息
	 * 
	 * @param userName
	 * @return
	 */
	int updateDepartment(String userId, int departmentIdOne, int positionIdOne, int departmentIdTwo, int positionIdTwo);

	/**
	 * 更新用户紧急联系人
	 * 
	 * @param userName
	 * @return
	 */
	int updateEmergencyContact(String userId, String emergencyUserName, String emergencyPhone);

	/**
	 * 提供查询部门信息的服务
	 * 
	 * @return
	 */
	List<Department> searchDepartment(int companyId);

	/**
	 * 查询人员状态
	 * 
	 * @return
	 */
	List<Status> searchUserStatus();

	/**
	 * 查询职位
	 * 
	 * @return
	 */
	List<Position> searchPosition(int companyId);

	/**
	 * 查询考勤规则
	 * 
	 * @return
	 */
	List<AttendanceRule> searchAttendanceRules(int manager,int company_id,int id);

	/**
	 * 根据规则ID查询考勤规则
	 * 
	 * @return
	 */
	AttendanceRule searchAttendanceRuleByRuleId(int ruleId);

	/**
	 * 用户更新头像
	 * 
	 * @param files
	 * @param request
	 * @param response
	 * @param httpSession
	 * @return
	 */
	public String uploadHeadPortrait(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession);

	/**
	 * 删除考勤规则
	 * 
	 * @return
	 */
	int deleteAttendanceRule(int ruleId);

	/**
	 * 添加考勤规则
	 * 
	 * @return
	 */
	int addAttendanceRule(AttendanceRule rule);

	/**
	 * 修改考勤规则
	 * 
	 * @return
	 */
	int modifyAttendanceRule(AttendanceRule rule);

	/**
	 * 添加考勤记录
	 * 
	 * @return
	 */
	void addAttendanceRecord(AttendanceRecord attendanceRecord);

	/**
	 * 查询考勤记录
	 * 
	 * @return
	 */
	List<AttendanceRecord> searchAttendanceRecord(int userId, String startTime, String endTime);

	/**
	 * 查询考勤排行记录
	 * 
	 * @return
	 */
	List<AttendanceRecord> searchAttendanceTopRecord(String startTime, String endTime);

	/**
	 * 查询假期类型
	 * 
	 * @return
	 */
	List<VacationType> searchVacationTypes(int manager);

	/**
	 * 查询加班类型
	 * 
	 * @return
	 */
	List<OvertimeType> searchOvertimeTypes(int manager);

	/**
	 * 查询工资
	 * 
	 * @return
	 */
	List<Wage> searchWageByUserId(int userId, int startMonth, int endMonth);

	/**
	 * 查询通知公告
	 * @param rows 
	 * @param bNum 
	 * 
	 * @return
	 */
	List<Announcement> searchAnnouncement(int companyId, int bNum, int rows);
	/***
	 * 登入账号查询公司，（如果是admin就查询全部的公司返回到安卓）
	 * @param companyid
	 * @return
	 */
	List<Company> findCompanyList(int companyid);
	/***
	 * 重置密码
	 * @param phoneNumber
	 * @param password
	 * @return
	 */
	int updateUserPassword(String phoneNumber, String password);
	/***
	 * 修改密码
	 * @param userId
	 * @param oldpassword
	 * @param newpassword
	 * @return
	 */
	int updatePassword(String userId, String oldpassword, String newpassword);
	/***
	 * 查询广告
	 * @return
	 */
	List<Advertisement> findAdvertisementList(String time);
	/***
	 * 新增外勤打卡详情记录
	 * @param outAttendanceRecord
	 */
	void insertOutAttendanceRecord(OutAttendanceRecord outAttendanceRecord);
	/***
	 * 新增外勤打卡审批人
	 * @param outAttendanceRecordAudit
	 */
	void insertOutAttendanceRecordAudit(OutAttendanceRecordAudit outAttendanceRecordAudit);
	/***
	 * 新增外勤打卡抄送人
	 * @param attendanceRecordCopies
	 */
	void insertOutAttendanceRecordCopy(List<OutAttendanceRecordCopy> attendanceRecordCopies);
	/***
	 * 查询用户是否存在
	 * @param userId
	 * @return
	 */
	int findUserInfoByUserId(String userId);
	/***
	 * 查询用户账户密码是否正确
	 * @param userId
	 * @param oldpassword
	 * @return
	 */
	int findUserInfoByUserIdAndPassWord(String userId, String oldpassword);
	/**
	 * 存储图片sql
	 * @param out_attendance_id
	 * @param fileDirPath
	 * @return
	 */
	int OutAttendanceRecordPic(int out_attendance_id, String fileDirPath);
	/**
	 * 根据安卓提供日期查询工作时间表
	 * @param optString
	 * @param optString2
	 * @return
	 */
	List<WorkingTime> searchHolidayRecord(String bt, String et,int companyId);
	/**
	 * 查询是否是休息日
	 * @param date
	 * @return
	 */
	int searchHoliday(String date,int companyId);
	/**
	 * 根据用户iD查询公司ID
	 * @param user_id
	 * @return
	 */
	int getCompangIdByUserId(int user_id);
	/**
	 * 新增激光推送消息
	 * @param jpushData
	 */
	void insertJPushData(JPushData jpushData);
	/**
	 * 查询外勤打卡审批
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<OutAttendanceInfo> searchOutAttendance(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 查询外勤审批记录
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<OutAttendanceInfo> serachauditOutAttendanceHistoricalRecords(int userId, int bNum, int rows, String bt,
			String et, String applicant_user_id);
	/**
	 * 提交外勤打卡审批记录
	 * @param outAttendanceRecordAudit_id
	 * @param out_attendance_id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitApprovalOutAttendance(String outAttendanceRecordAudit_id, String out_attendance_id,
			String result_id, String audit_status, String audit_resmarks,int userId,int id, HttpSession session,
			HttpServletRequest request);
	/**
	 * 查询申请人查询申请记录
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @return
	 */
	List<OutAttendanceInfo> searchApprovalAttendanceRecord(int userId, int bNum, int rows, String bt, String et);
	/**
	 * 撤销外勤申请详情记录
	 * @param id
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitRevokeOutAttendance(int id, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 新增补卡详情记录
	 * @param appealAttendanceRecord
	 */
	void insertAppealAttendanceRecord(AppealAttendanceRecord appealAttendanceRecord);
	/**
	 * 新增补卡详情审批人
	 * @param appealAttendanceRecordAudit
	 */
	void insertAppealAttendanceRecordAudit(AppealAttendanceRecordAudit appealAttendanceRecordAudit);
	/**
	 * 新增补卡的图片
	 * @param appeal_attendance_id
	 * @param fileDirPath
	 * @return
	 */
	int AppealAttendanceRecordPic(int appeal_attendance_id, String fileDirPath);
	/**
	 * 查询补卡需要审批信息
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<AppealAttendanceInfo> serachauditAppealAttendanceHistoricalRecords(int userId, int bNum, int rows, String bt,
			String et, String applicant_user_id);
	/**
	 * 查询补卡审批的历史记录
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<AppealAttendanceInfo> searchAppealAttendance(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 新增补卡
	 * @param record
	 * @return
	 */
	void addAppealAttendanceRecord(AttendanceRecord record);
	/**
	 * 提交补卡审批结果
	 * @param appealAttendanceRecordAuditId
	 * @param appealAttendanceId
	 * @param resultId
	 * @param auditStatus
	 * @param auditResmarks
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitApprovalAppealAttendance(String appealAttendanceRecordAuditId, String appealAttendanceId, String resultId, String auditStatus,
			String auditResmarks, int userId,int id, HttpSession session, HttpServletRequest request);
	/**
	 * 撤销补卡申请记录
	 * @param id
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitRevokeAppealAttendance(int id, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @return
	 */
	List<AppealAttendanceInfo> searchApprovalAppealAttendanceRecord(int userId, int bNum, int rows, String bt, String et);
	/**
	 * 新增加班申请记录
	 * @param overTimeRecord
	 */
	void insertOverTimeRecord(OverTimeRecord overTimeRecord);
	/**
	 * 新增加班审批人记录
	 * @param overTimeRecordAudit
	 */
	void insertOverTimeRecordAudit(OverTimeRecordAudit overTimeRecordAudit);
	/**
	 * 新增加班抄送人记录
	 * @param overTimeRecordCopies
	 */
	void insertOverTimeRecordCopy(List<OverTimeRecordCopy> overTimeRecordCopies);
	/**
	 * 新增加班申请图片
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int OverTimeRecordPic(int id, String fileDirPath);
	/**
	 * 查询加班已经审批过的
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<OverTimeRecord> searchOverTimeHistoricalRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 查询加班需要审批
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<OverTimeRecord> serachOverTimeRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 申请人查询加班申请记录
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @return
	 */
	List<OverTimeRecord> searchApprovalOverTime(int userId, int bNum, int rows, String bt, String et);
	/**
	 * 申请人撤销加班申请
	 * @param id
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitRevokeOverTime(int id, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 审批人提交审批记录
	 * @param audit_id
	 * @param id
	 * @param resultId
	 * @param auditStatus
	 * @param auditResmarks
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitApprovalOverTime(String audit_id, String id, String resultId, String auditStatus,
			String auditResmarks, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 查询用户的加班时间
	 * @param userId
	 * @return
	 */
	float findUserBaseOvertime(int userId,int company_id);
	/**
	 * 新增假期详情信息
	 * @param vacationRecord
	 */
	void insertVacationRecord(VacationRecord vacationRecord);
	/**
	 * 新增假期审批信息
	 * @param vacationRecordAudit
	 */
	void insertVacationRecordAudit(VacationRecordAudit vacationRecordAudit);
	/**
	 * 新增假期抄送人
	 * @param vacationRecordCopies
	 */
	void insertVacationRecordCopy(List<VacationRecordCopy> vacationRecordCopies);
	/**
	 * 新增假期图片
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int VacationRecordPic(int id, String fileDirPath);
	/**
	 * 查询假期已经审批过的
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<VacationRecord> searchVacationHistoricalRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 查询假期需要审批信息
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<VacationRecord> serachVacationRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 提交假期审批结果
	 * @param audit_id
	 * @param id
	 * @param resultId
	 * @param auditStatus
	 * @param auditRemarks
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitApprovalVacation(String audit_id, String id, String resultId, String auditStatus,
			String auditRemarks, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 申请人撤销假期申请信息
	 * @param id
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitRevokeVacation(int id, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 申请人查询假期申请
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @return
	 */
	List<VacationRecord> searchApprovalVacation(int userId, int bNum, int rows, String bt, String et);
	/**
	 * 查询时间段的额人员工时，加班，请假等信息
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param companyId
	 * @return
	 */
	List<WorkingTimeUser> searchWorkingTimeUser(int userId, String startTime, String endTime, int companyId);
	/**
	 * 新增外出申请
	 * @param outGoingRecord
	 */
	void insertOutGoingRecord(OutGoingRecord outGoingRecord);
	/**
	 * 新增外出审批人信息
	 * @param outGoingRecordAudit
	 */
	void insertOutGoingRecordAudit(OutGoingRecordAudit outGoingRecordAudit);
	/**
	 * 新增外出抄送人信息
	 * @param outGoingRecordCopies
	 */
	void insertOutGoingRecordCopy(List<OutGoingRecordCopy> outGoingRecordCopies);
	/**
	 * 查询外出已审批
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<OutGoingRecord> searchOutGoingHistoricalRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 查询外出未审批的
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<OutGoingRecord> serachOutGoingRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 申请人查询外出申请记录
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @return
	 */
	List<OutGoingRecord> searchApprovalOutGoing(int userId, int bNum, int rows, String bt, String et);
	/**
	 * 提交外出审批结果
	 * @param valueOf
	 * @param valueOf2
	 * @param valueOf3
	 * @param valueOf4
	 * @param auditRemarks
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitApprovalOutGoing(String valueOf, String valueOf2, String valueOf3, String valueOf4,
			String auditRemarks, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 申请人撤销外出申请记录
	 * @param id
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitRevokeOutGoing(int id, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 新增出差详情信息
	 * @param businessTraveIRecord
	 */
	void insertBusinessTraveIRecord(BusinessTraveIRecord businessTraveIRecord);
	/**
	 * 新增出差审批人信息
	 * @param businessTraveIRecordAudit
	 */
	void insertBusinessTraveIRecordAudit(BusinessTraveIRecordAudit businessTraveIRecordAudit);
	/**
	 * 新增出差抄送人信息
	 * @param businessTraveIRecordCopies
	 */
	void insertBusinessTraveIRecordCopy(List<BusinessTraveIRecordCopy> businessTraveIRecordCopies);
	/**
	 * 新增出差图片
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int BusinessTraveIRecordPic(int id, String fileDirPath);
	/**
	 * 新增外出图片
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int OutGoingRecordPic(int id, String fileDirPath);
	/**
	 * 查询出差已经审批过的
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<BusinessTraveIRecord> searchBusinessTraveIHistoricalRecords(int userId, int bNum, int rows, String bt,
			String et, String applicant_user_id);
	/**
	 * 查询出差未审批过的
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param applicant_user_id
	 * @return
	 */
	List<BusinessTraveIRecord> serachBusinessTraveIRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id);
	/**
	 * 申请人查询出差申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<BusinessTraveIRecord> searchApprovalBusinessTraveI(int userId, int bNum, int rows, String bt, String et);
	/**
	 * 提交出差审批结果
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	JSONObject submitApprovalBusinessTraveI(String valueOf, String valueOf2, String valueOf3, String valueOf4,
			String auditRemarks, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 申请人撤销出差申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	JSONObject submitRevokeBusinessTraveI(int id, int userId, HttpSession session, HttpServletRequest request);
	/**
	 * 查询外勤详情记录
	 * @param id 主键
	 * @return
	 */
	OutAttendanceInfo serachOutAttendanceById(int id,int status,int userId);
	/**
	 * 查询假期详情记录
	 * @param id 主键
	 * @return
	 */
	VacationRecord serachVacationById(int id,int type,int userId);
	/**
	 * 查询加班详情记录
	 * @param id 主键
	 * @return
	 */
	OverTimeRecord serachOvertimeById(int id,int type,int userId);
	/**
	 * 查询外出详情记录
	 * @param id 主键
	 * @return
	 */
	OutGoingRecord serachOutGoingById(int id,int type,int userId);
	/**
	 * 查询出差详情记录
	 * @param id 主键
	 * @return
	 */
	BusinessTraveIRecord serachBusinessTraveIById(int id,int type,int userId);
	/**
	 * 查询补卡详情记录
	 * @param id 主键
	 * @return
	 */
	AppealAttendanceInfo serachAppealAttendanceById(int id,int type,int userId);

	JSONObject serachUserInfoAll();

	List<AllUserInfoStatus> getEmployeeStatus(int companyId);

	List<VersionInfo> getNewVersionUpdateLog(int apkTypeId);
	/**
	 * 查询轮播图
	 * @param companyId
	 * @return
	 */
	List<HomePagePicture> searchHomePagePicture(int companyId);
	/**
	 * 存人脸
	 * @param face
	 * @return
	 */
	int AddFace(Face face);
	/**
	 * 查询openID绑定的用户
	 * @param openId
	 * @return
	 */
	List<User> searchOpenId(String openId);
	/**
	 * 用户绑定openiD
	 * @param openId
	 * @param userId
	 * @return
	 */
	int bindingOpenId(String openId, int userId);
	/**
	 * 查询人脸识别
	 * @param userId
	 * @return
	 */
	List<Face> searchFace(int userId);
	/**
	 * 取消关注
	 * @param openId
	 */
	void unSubscribe(String openId);
	/***
	 * 查询全部的人脸
	 * @return
	 */
	List<Face> searchAllFace();

	/**
	 * 存人脸的记录
	 * @param fileDirPath
	 * @param userId
	 * @return
	 */
	int addFaceRecord(String fileDirPath, int userId,String createTime,String remarks,String user_name);
	/**
	 * 查询人脸图片的个数
	 * @param user_id
	 * @return
	 */
	int findFaceCountByUserId(int user_id);
	/**
	 * 删除人脸图片
	 * @param id
	 * @return
	 */
	int deleteFace(int id);
	/**
	 * 人脸终端查询所有人和每个人图片的个数
	 * @return
	 */
	List<AllUserInfo> serachAllFaceOfTerminal(int companyId);
	/**
	 * 查询访客的userid最大的一个
	 * @return
	 */
	int getVisitorInfoUserId();
	/**
	 * 新增访客的基本信息
	 * @param info
	 */
	void addVisitorInfo(VisitorInfo info);
	/**
	 * 查询全部的访客基本信息
	 * @return
	 */
	List<VisitorInfo> serachVisitorInfoAll(int company_id);
	/**
	 * 修改访客的基本信息
	 * @param info
	 * @return
	 */
	int updateVisitorInfo(VisitorInfo info);
	/**
	 * 删除访客的基本信息
	 * @param user_id
	 * @return
	 */
	int deleteVisitorInfo(int user_id);
	/**
	 * 删除访客的人脸库信息
	 * @param user_id
	 */
	void deleteVisitorFace(int user_id);
	/**
	 * 修改face的有效期时间
	 * @param info
	 * @return
	 */
	int updateFace(Face info);
	/**
	 * 查询访客的所有预约
	 * @param user_id
	 * @return
	 */
	List<VisitorSubscribe> searchVisitorSubscribe(int user_id);
	/**
	 * 查询访客的人脸信息
	 */
	VisitorInfo findVistorInfo(int user_id);
	/**
	 * 新增访客预约
	 * @param visitorSubscribe
	 * @return
	 */
	int addVisitorSubscribe(VisitorSubscribe visitorSubscribe);
	/**
	 * 新增修改人脸图像
	 * @param fileDirPath
	 * @param userId
	 * @return
	 */
	int addVisitorSubscribeFace(String fileDirPath, int userId,byte[] faceFeature);
	/**
	 * 查询人脸信息
	 * @param id
	 * @return
	 */
	Face findface(int id);
	/**
	 * 修改人脸的状态
	 * @param id
	 */
	void updateFaceStatus(int id);
	/**
	 * 查询访客的信息
	 * @param openId
	 * @return
	 */
	List<VisitorInfo> searchVisitorOpenId(String openId);
	/**
	 * 查询全部没有处理的人脸
	 * @return
	 */
	List<Face> searchAllFaceUntreated();
	/**
	 * 查询一整年的假期情况
	 * @param year
	 * @return
	 */
	List<WorkingTime> searchWorkingTimeByYear(String year,int companyId);
	/**
	 * 新增人脸机打卡
	 * @param record
	 */
	void addAttendanceFaceRecord(AttendanceFaceRecord record);
	/**
	 * 撤销预约
	 * @param id
	 * @return
	 */
	int deleteVisitorSubscribe(int id);
	/**
	 * 修改访客预约信息
	 * @param visitorSubscribe
	 * @return
	 */
	int updateVisitorSubscribe(VisitorSubscribe visitorSubscribe);
	/**
	 * 查询人脸进出情况
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FaceRecord> searchFaceRecord(int userId, String startTime, String endTime);
	/**
	 * 真的删除人脸信息
	 * @param id
	 * @return
	 */
	int deleteFaceTrue(int id);
	/**
	 * 修改访客信息没有特征值
	 * @param userId
	 * @param faceFeature
	 * @return
	 */
	int updateVisitorInfoFaceFeature(int userId, byte[] faceFeature);
	/**
	 * 新增开关门记录
	 * @param openAndCloseDoorRecord
	 * @return
	 */
	int openAndCloseDoorRecord(OpenAndCloseDoorRecord openAndCloseDoorRecord);
	
	void inserAddface(Face face);
	/**
	 * 查询最新版本
	 * @param apkTypeId
	 * @return
	 */
	VersionInfo searchNewVersion(int apkTypeId);
	/**
	 * 小程序登录接口
	 * @param user_id
	 * @param password
	 * @return
	 */
	User serachUserByUserAndPasswork(String user_id, String password);
	/**
	 * 小程序更新openid
	 * @param user_id
	 * @param openId
	 */
	void adduserInfoByOpenId(String unionid,String user_id, String openId);

	void updateUnoinid(String openId, String unionid);
	
}
