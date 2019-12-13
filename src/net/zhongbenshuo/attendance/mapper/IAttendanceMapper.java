package net.zhongbenshuo.attendance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
import net.zhongbenshuo.attendance.bean.UserInfoStatus;
import net.zhongbenshuo.attendance.bean.VacationType;
import net.zhongbenshuo.attendance.bean.VisitorInfo;
import net.zhongbenshuo.attendance.bean.VisitorSubscribe;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.Authority;
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
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfoAuditRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;
import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;
import net.zhongbenshuo.attendance.utils.JPushData;

public interface IAttendanceMapper {

	int addUser(@Param("userPhoneNumber") String userPhoneNumber, @Param("userPwd") String userPwd,
			@Param("ipAddress") String ipAddress);

	int getMaxUserId();

	User loginUserByUserId(@Param("userId") String userId, @Param("userPwd") String userPwd);

	User searchUserByOthers(@Param("userId") String userId, @Param("userPwd") String userPwd);

	User searchUser(@Param("key") String key, @Param("value") String value);

	int updateUser(@Param("userId") String userId, @Param("key") String key, @Param("value") String value);

	int deleteRelationship(@Param("userId") String userId);

	int insertRelationship(@Param("userId") String userId, @Param("departmentId") int departmentId,
			@Param("positionId") int positionId);

	int updateEmergencyContact(@Param("userId") String userId, @Param("emergencyUserName") String emergencyUserName,
			@Param("emergencyPhone") String emergencyPhone);

	List<Department> searchDepartment(@Param("companyId")int companyId);

	List<Position> searchPosition(@Param("companyId")int companyId);

	List<Status> searchUserStatus();

	List<AllUserInfo> searchAllUser();

	List<AttendanceRule> searchAttendanceRules(@Param("manager") int manager,@Param("company_id")int company_id,@Param("id")int id);

	int deleteAttendanceRule(@Param("ruleId") int ruleId);

	int updateUserHeadPortraitInfo(User user);

	void addAttendanceRecord(AttendanceRecord attendanceRecord);

	List<AttendanceRecord> searchAttendanceRecord(@Param("userId") int userId, @Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<AttendanceRecord> searchAttendanceTopRecord(@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	int addAttendanceRule(@Param("attendanceRule") AttendanceRule rule);

	int modifyAttendanceRule(@Param("attendanceRule") AttendanceRule rule);

	AttendanceRule searchAttendanceRuleByRuleId(@Param("ruleId") int ruleId);

	List<VacationType> searchVacationTypes(@Param("manager") int manager);

	List<OvertimeType> searchOvertimeTypes(@Param("manager") int manager);

	List<Wage> searchWageByUserId(@Param("userId") int userId, @Param("startMonth") int startMonth,
			@Param("endMonth") int endMonth);

	List<Announcement> searchAnnouncement(@Param("companyId")int companyId,@Param("bNum")int bNum, @Param("rows")int rows);
	/***
	 * 登入账号查询公司，（如果是admin就查询全部的公司返回到安卓）
	 * @param companyid
	 * @return
	 */
	List<Company> findCompanyList(@Param("companyid")int companyid);
	/***
	 * 根据用户ID更新账号的权限
	 * @param userId
	 * @param departmentId
	 * @param positionId
	 * @return
	 */
	int updateUserDepartmentAndPosition(@Param("userId")String userId, @Param("departmentId")String departmentId, @Param("positionId")String positionId);
	/***
	 * 根据company查询department
	 * @param companyId
	 * @return
	 */
	List<AllUserInfoAddressBook> findDepartmentByCompanyId(@Param("companyId")int companyId);
	
	List<AllUserInfoStatus> findDepartmentByCompanyIds(@Param("companyId")int companyId,@Param("time")String time,@Param("bt")String bt,@Param("et")String et);
	/***
	 * 根据company查询userInfo
	 * @param companyId
	 * @return
	 */
	List<User> findUserInfoByCompanyId(@Param("companyId")int companyId);
	/***
	 * 根据userId和手机号，查询号码是否已经被用
	 * @param userId
	 * @param value
	 * @return
	 */
	int findUserByPhoneNumber(@Param("userId")String userId, @Param("value")String value);
	/***
	 * 重置密码
	 * @param phoneNumber
	 * @param password
	 * @return
	 */
	int updateUserPassword(@Param("phoneNumber")String phoneNumber, @Param("password")String password);
	/***
	 * 修改密码
	 * @param userId
	 * @param oldpassword
	 * @param newpassword
	 * @return
	 */
	int updatePassword(@Param("userId")String userId, @Param("oldpassword")String oldpassword, @Param("newpassword")String newpassword);
	/***
	 * 根据userId和邮箱，查询邮箱是否已经被用
	 * @param userId
	 * @param value
	 * @return
	 */
	int findUserByEmailAddress(@Param("userId")String userId, @Param("value")String value);
	/***
	 * 查询广告
	 * @return
	 */
	List<Advertisement> findAdvertisementList(@Param("time")String time);
	/***
	 * 外勤打卡详情信息
	 * @param outAttendanceRecord
	 */
	void insertOutAttendanceRecord(OutAttendanceRecord outAttendanceRecord);
	/***
	 * 新增外勤打卡审批人
	 * @param outAttendanceRecordAudit
	 */
	void insertOutAttendanceRecordAudit(OutAttendanceRecordAudit outAttendanceRecordAudit);
	/**
	 * 新增外勤打卡抄送人
	 * @param attendanceRecordCopies
	 */
	void insertOutAttendanceRecordCopy(List<OutAttendanceRecordCopy> attendanceRecordCopies);
	/***
	 * 查询用户是否存在
	 * @param userId
	 * @return
	 */
	int findUserInfoByUserId(@Param("userId")String userId);
	/***
	 * 查询用户账号密码是否存在
	 * @param userId
	 * @param oldpassword
	 * @return
	 */
	int findUserInfoByUserIdAndPassWord(@Param("userId")String userId, @Param("oldpassword")String oldpassword);
	/***
	 *  存储外勤打卡图片
	 * @param out_attendance_id
	 * @param fileDirPath
	 * @return
	 */
	int OutAttendanceRecordPic(@Param("out_attendance_id")int out_attendance_id, @Param("fileDirPath")String fileDirPath);
	/**
	 * 根据安卓提供的日期查询工作表
	 * @param bt
	 * @param et
	 * @return
	 */
	List<WorkingTime> searchHolidayRecord(@Param("bt")String bt, @Param("et")String et,@Param("company_id")int companyId);
	/**
	 * 是不是假期
	 * @param date
	 * @return
	 */
	int searchHoliday(@Param("date")String date,@Param("companyId")int companyId);
	
	
	/**
	 * 查询公司ID根据用过id
	 * @param user_id
	 * @return
	 */
	int getCompangIdByUserId(@Param("user_id")int user_id);
	/**
	 * 新增激光推送消息
	 * @param jpushData
	 */
	void insertJPushData(JPushData jpushData);
	/**
	 * 审批过的历史记录
	 * @param bt
	 * @param et
	 * @param userId
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutAttendanceInfo> findOutAttendance(@Param("bt")String bt, @Param("et")String et, @Param("user_id")int userId, @Param("applicant_user_id")String applicant_user_id,
			@Param("bNum")int bNum,
			@Param("rows")int rows);
	/**
	 * 查询外勤审批记录
	 * @param out_attendance_id
	 * @return
	 */
	List<OutAttendanceInfoAuditRecord> findOutAttendanceInfoAuditRecord( @Param("out_attendance_id")int out_attendance_id);
	/**
	 * 查询外勤审批记录
	 * @param bt
	 * @param et
	 * @param userId
	 * @param applicant_user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutAttendanceInfo> findauditOutAttendance_historical_records(@Param("bt")String bt, @Param("et")String et, @Param("user_id")int userId, @Param("applicant_user_id")String applicant_user_id,
			@Param("bNum")int bNum,
			@Param("rows")int rows);
	/**
	 * 申请人查询申请记录
	 * @param bt
	 * @param et
	 * @param userId
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<OutAttendanceInfo> searchApprovalAttendanceRecord(@Param("bt")String bt, @Param("et")String et, @Param("user_id")int userId, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 新增补卡详情记录
	 * @param appealAttendanceRecord
	 */
	void insertAppealAttendanceRecord(AppealAttendanceRecord appealAttendanceRecord);
	/**
	 * 新增补卡审批人信息
	 * @param appealAttendanceRecordAudit
	 */
	void insertAppealAttendanceRecordAudit(AppealAttendanceRecordAudit appealAttendanceRecordAudit);
	/**
	 * 新增补卡图片
	 * @param appeal_attendance_id
	 * @param fileDirPath
	 * @return
	 */
	int AppealAttendanceRecordPic(@Param("appeal_attendance_id")int appeal_attendance_id, @Param("fileDirPath")String fileDirPath);
	/**
	 *  新增补卡信息
	 * @param record
	 * @return
	 */
	void addAppealAttendanceRecord(AttendanceRecord attendanceRecord);
	/**
	 * 外勤打卡撤销
	 * @param id
	 * @param userId
	 */
	void submitRevokeOutAttendance(@Param("id")int id, @Param("userId")int userId);
	/**
	 * 补卡撤销
	 * @param id
	 * @param userId
	 */
	void submitRevokeAppealAttendance(@Param("id")int id, @Param("userId")int userId);
	/**
	 * 新增加班申请信息
	 * @param overTimeRecord
	 */
	void insertOverTimeRecord(OverTimeRecord overTimeRecord);
	/**
	 * 新增加班审批人信息
	 * @param overTimeRecordAudit
	 */
	void insertOverTimeRecordAudit(OverTimeRecordAudit overTimeRecordAudit);
	/**
	 * 新增加班抄送人记录信息
	 * @param overTimeRecordCopies
	 */
	void insertOverTimeRecordCopy(List<OverTimeRecordCopy> overTimeRecordCopies);
	/**
	 * 新增加班申请图片
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int OverTimeRecordPic(@Param("id")int id, @Param("fileDirPath")String fileDirPath);
	/**
	 * 申请人撤销加班申请
	 * @param id
	 * @param userId
	 */
	void submitRevokeOverTime(@Param("id")int id, @Param("userId")int userId);
	/**
	 * 查询用户可用的调休时间
	 * @param userId
	 * @return
	 */
	float findUserBaseOvertime(@Param("userId")int userId,@Param("company_id")int company_id);
	/**
	 * 新增假期详情信息
	 * @param vacationRecord
	 */
	void insertVacationRecord(VacationRecord vacationRecord);
	/**
	 * 新增假期审批人信息
	 * @param vacationRecordAudit
	 */
	void insertVacationRecordAudit(VacationRecordAudit vacationRecordAudit);
	/**
	 * 新增假期抄送人记录
	 * @param vacationRecordCopies
	 */
	void insertVacationRecordCopy(List<VacationRecordCopy> vacationRecordCopies);
	/**
	 * 新增假期图片
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int VacationRecordPic(@Param("id")int id, @Param("fileDirPath")String fileDirPath);
	/**
	 * 申请人撤销假期申请
	 * @param id
	 * @param userId
	 */
	void submitRevokeVacation(@Param("id")int id, @Param("userId")int userId);
	/**
	 * 查询人员的一段时间的工时，加班，请假等信息
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param companyId
	 * @return
	 */
	List<WorkingTimeUser> searchWorkingTimeUser(@Param("userId")int userId, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("companyId")int companyId);
	/**
	 * 查询职位
	 * @param departmentId
	 * @return
	 */
	List<User> findPositionUser(@Param("departmentId")int departmentId);
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
	 * 申请人撤销外出申请记录
	 * @param id
	 * @param userId
	 */
	void submitRevokeOutGoing(@Param("id")int id, @Param("userId")int userId);
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
	int BusinessTraveIRecordPic(@Param("id")int id, @Param("fileDirPath")String fileDirPath);
	/**
	 * 新增外出图片
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int OutGoingRecordPic(@Param("id")int id, @Param("fileDirPath")String fileDirPath);
	/**
	 * 申请人撤销出差申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	void submitRevokeBusinessTraveI(@Param("id")int id, @Param("userId")int userId);
	/**
	 * 大屏幕显示人员
	 * @param company_id
	 * @return
	 */
	List<UserInfoStatus> serachUserInfoAll(@Param("company_id")int company_id,@Param("rows")int rows,@Param("bNum")int bNum);
	/**
	 * 大屏幕人员查询是否休假
	 * @param user_id
	 * @param time
	 * @return
	 */
	List<VacationRecord> findVacationByUserId(@Param("user_id")int user_id, @Param("time")String time);
	/**
	 * 大屏幕人员查询是否加班
	 * @param user_id
	 * @param time
	 * @return
	 */
	int findOvertimeByUserId(@Param("user_id")int user_id, @Param("time")String time);
	/**
	 * 大屏幕人员查询是否正常上班，下班
	 * @param user_id
	 * @param time
	 * @return
	 */
	List<AttendanceRecord> findAttendanceByUserId(@Param("user_id")int user_id, @Param("bt")String bt, @Param("et")String et);
	/**
	 * 查询外勤详情记录(审批)
	 * @param id 主键
	 * @return
	 */
	OutAttendanceInfo serachOutAttendanceById(@Param("id")int id,@Param("user_id")int userId);
	
	/**
	 * 查询外勤详情记录(申请)
	 * @param id主键
	 * @return
	 */
	OutAttendanceInfo serachOutAttendanceByIdApproval(@Param("id")int id);
	/**
	 * 休假申请（审批）
	 * @return
	 */
	VacationRecord serachVacationById(@Param("id")int id,@Param("user_id")int userId);
	/**
	 * 休假申请（申请）
	 * @param id
	 * @return
	 */
	VacationRecord serachVacationByIdApproval(@Param("id")int id);
	/**
	 * 加班详情（审批）
	 * @param id 主键
	 * @return
	 */
	OverTimeRecord serachOvertimeById(@Param("id")int id,@Param("user_id")int userId);
	/**
	 * 加班详情（申请）
	 * @param id
	 * @param i
	 * @return
	 */
	OverTimeRecord serachOvertimeByIdApproval(@Param("id")int id);
	/**
	 *外出详情（审批）
	 * @param id 主键
	 * @return
	 */
	OutGoingRecord serachOutGoingById(@Param("id")int id,@Param("user_id")int userId);
	/**
	 * 外出详情（申请）
	 * @param id
	 * @return
	 */
	OutGoingRecord serachOutGoingByIdApproval(@Param("id")int id);
	/**
	 *出差详情（审批）
	 * @param id 主键
	 * @return
	 */
	BusinessTraveIRecord serachBusinessTraveIById(@Param("id")int id,@Param("user_id")int userId);
	/**
	 * 出差详情（申请）
	 * @param id
	 * @return
	 */
	BusinessTraveIRecord serachBusinessTraveIByIdApproval(@Param("id")int id );
	/**
	 * 考勤补卡（审批）
	 * @param id 主键
	 * @return
	 */
	AppealAttendanceInfo serachAppealAttendanceById(@Param("id")int id,@Param("user_id")int userId);
	/**
	 * 考勤补卡（申请）
	 * @param id
	 * @return
	 */
	AppealAttendanceInfo serachAppealAttendanceByIdApproval(@Param("id")int id);

	List<UserInfoStatus> findPositionUsers(@Param("departmentId")int departmentId);
	/**
	 * 大屏幕出差查询
	 * @param user_id
	 * @param time
	 * @return
	 */
	List<BusinessTraveIRecord> findBusinessTraveIByUserId(@Param("user_id")int user_id, @Param("time")String time);
	/**
	 * 大屏幕外出查询
	 * @param user_id
	 * @param time
	 * @return
	 */
	List<OutGoingRecord> findbusinessTraveIRecords(@Param("user_id")int user_id, @Param("time")String time);
	/**
	 * 查询安卓轮播
	 * @param companyId
	 * @return
	 */
	List<HomePagePicture> searchHomePagePicture(@Param("companyId")int companyId);
	/**
	 * 存储人脸
	 * @param face
	 * @return
	 */
	int AddFace(Face face);
	/**
	 * 查询openid绑定用户
	 * @param openId
	 * @return
	 */
	List<User> searchOpenId(@Param("openId")String openId);
	/**
	 * 用户绑定openId
	 * @param openId
	 * @param userId
	 * @return
	 */
	int bindingOpenId(@Param("openId")String openId, @Param("userId")int userId);
	/**
	 * 查询人脸识别
	 * @param userId
	 * @return
	 */
	List<Face> searchFace(@Param("userId")int userId);
	/**
	 * 取消关注
	 * @param openId
	 */
	void unSubscribe(@Param("openId")String openId);
	/**
	 * 查询全部的人脸
	 * @return
	 */
	List<Face> searchAllFace();

	int addFaceRecord(@Param("fileDirPath")String fileDirPath, @Param("userId")int userId,@Param("createTime")String createTime,@Param("remarks")String remarks,@Param("user_name")String user_name);
	/**
	 * 查询人脸图片的个数
	 * @param user_id
	 * @return
	 */
	int findFaceCountByUserId(@Param("userId")int userId);
	/**
	 * 删除人脸图片
	 * @param id
	 * @return
	 */
	int deleteFace(@Param("id")int id);
	/**
	 * 查询全部的权限
	 * @return
	 */
	List<Authority> findAuthorityList(@Param("companyId")int companyId);
	/**
	 * 查询全部的部门
	 * @return
	 */
	List<Department> findDepartmentList(@Param("companyId")int companyId);
	/**
	 * 查询全部的职位
	 * @return
	 */
	List<Position> findPositionList(@Param("companyId")int companyId);
	/**
	 * 查询全部的人员的个人脸的个数
	 * @param companyId
	 * @return
	 */
	List<AllUserInfo> serachAllFaceOfTerminal(@Param("companyId")int companyId);
	/**
	 * 查询访客的末次useriD
	 * @return
	 */
	int getVisitorInfoUserId();
	/**
	 * 新增访客的基本信息
	 * @param info
	 */
	void addVisitorInfo(VisitorInfo info);
	/**
	 * 查询全部访客的基本信息
	 * @return
	 */
	List<VisitorInfo> serachVisitorInfoAll(@Param("company_id")int company_id);
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
	int deleteVisitorInfo(@Param("user_id")int user_id);
	/**
	 * 删除访客的人脸库信息
	 * @param user_id
	 */
	void deleteVisitorFace(@Param("user_id")int user_id);
	/**
	 * 修改face的有效期时间
	 * @param info
	 * @return
	 */
	int updateFace(Face info);
	/**
	 * 查询全部的预约信息
	 * @param user_id
	 * @return
	 */
	List<VisitorSubscribe> searchVisitorSubscribe(@Param("user_id")int user_id);
	/**
	 * 查询访客的人脸信息
	 * @param user_id
	 * @return
	 */
	VisitorInfo findVistorInfo(@Param("user_id")int user_id);
	/**
	 * 新增访客预约
	 * @param visitorSubscribe
	 * @return
	 */
	int addVisitorSubscribe(VisitorSubscribe visitorSubscribe);
	/**
	 * 新增修改访客人脸
	 * @param fileDirPath
	 * @param userId
	 * @return
	 */
	int addVisitorSubscribeFace(@Param("fileDirPath")String fileDirPath, @Param("userId")int userId,@Param("faceFeature")byte[] faceFeature);
	/**
	 * 查询人脸信息
	 */
	Face findface(@Param("id")int id);
	/**
	 * 修改人脸的状态
	 * @param id
	 */
	void updateFaceStatus(@Param("id")int id);
	/**
	 * 查询访客的基本信息
	 * @param openId
	 * @return
	 */
	List<VisitorInfo> searchVisitorOpenId(@Param("openId")String openId);
	/**
	 * 查询全部没有处理过的人脸
	 * @return
	 */
	List<Face> searchAllFaceUntreated();
	/**
	 * 查询全年的假期情况
	 * @param year
	 * @return
	 */
	List<WorkingTime> searchWorkingTimeByYear(@Param("year")String year,@Param("companyId")int companyId);
	/**
	 * 人脸打卡机，新增打卡记录
	 * @param record
	 */
	void addAttendanceFaceRecord(AttendanceFaceRecord record);
	/**
	 * 撤销预约
	 * @param id
	 * @return
	 */
	int deleteVisitorSubscribe(@Param("id")int id);
	/**
	 * 修改访客预约信息
	 * @param visitorSubscribe
	 * @return
	 */
	int updateVisitorSubscribe(VisitorSubscribe visitorSubscribe);
	/**
	 * 查询人脸进出信息
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FaceRecord> searchFaceRecord(@Param("userId")int userId, @Param("startTime")String startTime, @Param("endTime")String endTime);
	/**
	 * 删除人脸信息
	 * @param id
	 * @return
	 */
	int deleteFaceTrue(@Param("id")int id);
	/**
	 * 修改访客没有特征值
	 * @param userId
	 * @param faceFeature
	 * @return
	 */
	int updateVisitorInfoFaceFeature(@Param("userId")int userId, @Param("faceFeature")byte[] faceFeature);
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
	VersionInfo searchNewVersion(@Param("apkTypeId")int apkTypeId);
	/***
	 * 小程序登录接口
	 * @param user_id
	 * @param password
	 * @return
	 */
	User serachUserByUserAndPasswork(@Param("user_id")String user_id, @Param("password")String password);
	/**
	 * 小程序跟新openid
	 * @param user_id
	 * @param openId
	 */
	void adduserInfoByOpenId(@Param("unionid")String unionid,@Param("user_id")String user_id, @Param("openId")String openId);

	void updateUnoinid(@Param("unionid")String unionid, @Param("openId")String openId);

	void adduserInfoByOthers(@Param("unionid")String unionid,@Param("user_id")String user_id, @Param("openId")String openId);
}
