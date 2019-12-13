package net.zhongbenshuo.attendance.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;

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
import net.zhongbenshuo.attendance.bean.OutGoing;
import net.zhongbenshuo.attendance.bean.Overtime;
import net.zhongbenshuo.attendance.bean.OvertimeType;
import net.zhongbenshuo.attendance.bean.Position;
import net.zhongbenshuo.attendance.bean.Status;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.UserInfoStatus;
import net.zhongbenshuo.attendance.bean.Vacation;
import net.zhongbenshuo.attendance.bean.VacationType;
import net.zhongbenshuo.attendance.bean.VisitorInfo;
import net.zhongbenshuo.attendance.bean.VisitorSubscribe;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.constant.Constants;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.Authority;
import net.zhongbenshuo.attendance.foreground.accountInfo.mapper.AccountInfoMapper;
import net.zhongbenshuo.attendance.foreground.advertisement.bean.Advertisement;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceRecordPic;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.BusinessTraveIRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.BusinessTraveIRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OutGoingRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OutGoingRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordPic;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.VacationRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.VacationRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.VacationRecordPic;
import net.zhongbenshuo.attendance.foreground.applyRecord.mapper.ApplyRecordMapper;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.WorkingTimeUser;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;
import net.zhongbenshuo.attendance.foreground.homePagePicture.bean.HomePagePicture;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecordPic;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfoAuditRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceRecordPic;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecordPic;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.mapper.OutAttendanceMapper;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;
import net.zhongbenshuo.attendance.foreground.version.mapper.VersionMapper;
import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;
import net.zhongbenshuo.attendance.mapper.IAttendanceMapper;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.netty.WebSocketFrameHandler;
import net.zhongbenshuo.attendance.service.IUserService;
import net.zhongbenshuo.attendance.utils.AndroidHeaderInfo;
import net.zhongbenshuo.attendance.utils.Futil;
import net.zhongbenshuo.attendance.utils.GsonUtils;
import net.zhongbenshuo.attendance.utils.JPushData;
import net.zhongbenshuo.attendance.utils.JiguangPush;
import net.zhongbenshuo.attendance.utils.PushData;
import net.zhongbenshuo.attendance.utils.PushType;
import net.zhongbenshuo.attendance.utils.TimeUtils;
@Service("userService")
public class UserServiceImpl implements IUserService {

	public static Logger logger = LogManager.getLogger(UserServiceImpl.class);
	private static final String color_out = "#00FF00";
	private static final String color_vacation = "#65656F";
	private static final String color_overtime = "#782DFF";
	private static final String color_absence = "#FFAB00";
	private static final String color_working = "#02B2FF";
	private static final String color_late = "#7D6900";
	@Autowired
	VersionMapper versionMapper;
	@Autowired
	private IAttendanceMapper iWalkieTalkieMapper;
	
	@Autowired
	private  IUserService iuserService;
	
	@Autowired
	AccountInfoMapper accountInfoMapper;
	
	@Autowired
	OutAttendanceMapper outAttendanceMapper;
	
	@Autowired
	LoggerMapper loggerMapper;
	
	@Autowired
	ApplyRecordMapper applyRecordMapper;

	@Override
	public int registerUser(String userPhoneNumber, String userPwd, String ipAddress) {
		int i = 0;
		if (searchUser("PhoneNumber", userPhoneNumber) == null) {
			try {
				i = iWalkieTalkieMapper.addUser(userPhoneNumber, userPwd, ipAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 用户已存在
			i = 2;
		}
		return i;
	}

	@Override
	public User loginUser(String userId, String userPwd) {
		User user = null;
		try {
			int user_id = -1;
			try {
				if (userId.length() == 8) {
					user_id = Integer.valueOf(userId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user_id != -1) {
				user = iWalkieTalkieMapper.loginUserByUserId(userId, userPwd);
			} else {
				user = iWalkieTalkieMapper.searchUserByOthers(userId, userPwd);
			}
			if(user!=null){
				if(StringUtils.isNotBlank(user.getDepartment_id())){
					String department = accountInfoMapper.findDepartmentName(user.getDepartment_id());
					if(StringUtils.isNotBlank(department)){
						department = department.substring(1, department.length());
						List<String> result = Arrays.asList(user.getDepartment_id().split(","));
						if(result.size()>1){
							if(result.get(0).equals(result.get(1))){
								department = department+","+department;
							}
						}
						user.setDepartment(department);
					}
				}
				
				if(StringUtils.isNotBlank(user.getPosition_id())){
					String position = accountInfoMapper.findPositionName(user.getPosition_id());
					if(StringUtils.isNotBlank(position)){
						position = position.substring(1, position.length());
						user.setPosition(position);
					}
				}
				
				if(StringUtils.isNotBlank(user.getAuthority_id())){
					String authority = accountInfoMapper.findAuthorityName(user.getAuthority_id());
					if(StringUtils.isNotBlank(authority)){
						authority = authority.substring(1, authority.length());
						user.setAuthority(authority);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User searchUser(String key, String value) {
		User user = null;
		try {
			switch (key) {
			case "PhoneNumber":
				key = "phone_number";
				break;
			case "UserId":
				key = "user_id";
				break;
			default:
				break;
			}
			user = iWalkieTalkieMapper.searchUser(key, value);
			if(user!=null){
				if(StringUtils.isNotBlank(user.getDepartment_id())){
					String department = accountInfoMapper.findDepartmentName(user.getDepartment_id());
					if(StringUtils.isNotBlank(department)){
						department = department.substring(1, department.length());
						List<String> result = Arrays.asList(user.getDepartment_id().split(","));
						if(result.size()>1){
							if(result.get(0).equals(result.get(1))){
								department = department+","+department;
							}
						}
						user.setDepartment(department);
					}
				}
				
				if(StringUtils.isNotBlank(user.getPosition_id())){
					String position = accountInfoMapper.findPositionName(user.getPosition_id());
					if(StringUtils.isNotBlank(position)){
						position = position.substring(1, position.length());
						user.setPosition(position);
					}
				}
				
				if(StringUtils.isNotBlank(user.getAuthority_id())){
					String authority = accountInfoMapper.findAuthorityName(user.getAuthority_id());
					if(StringUtils.isNotBlank(authority)){
						authority = authority.substring(1, authority.length());
						user.setAuthority(authority);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<AllUserInfoAddressBook> searchAllUser(int companyId,String authorityName) {
		List<AllUserInfoAddressBook> userList = new ArrayList<AllUserInfoAddressBook>();
		List<Authority> authorityList = new ArrayList<Authority>();
		Map<Integer,String> authorityMap = new HashMap<Integer, String>();
		
		
		List<Department> departmentList = new ArrayList<Department>();
		Map<Integer,String> departmentMap = new HashMap<Integer, String>();
		
		List<Position> positionList = new ArrayList<Position>();
		Map<Integer,String> positionMap = new HashMap<Integer, String>();
		try {
			authorityList = iWalkieTalkieMapper.findAuthorityList(companyId);//查询全部的权限
			authorityList.forEach(xx ->authorityMap.put(xx.id, xx.authority_describe));
			
			departmentList = iWalkieTalkieMapper.findDepartmentList(companyId);//查询全部的部门
			departmentList.forEach(xx ->departmentMap.put(xx.department_id, xx.department));
			
			positionList = iWalkieTalkieMapper.findPositionList(companyId);//查询全部的职位
			positionList.forEach(xx ->positionMap.put(xx.position_id, xx.position));
			userList = iWalkieTalkieMapper.findDepartmentByCompanyId(companyId);
			for(AllUserInfoAddressBook allUserInfo : userList){
				List<User> list = new ArrayList<User>();
				list = allUserInfo.getUsers();
				List<User> listAll = new ArrayList<User>();
				for(User user : list){
					if(StringUtils.isNotBlank(user.getAuthority_id())){
						String [] authorityarra = null;
						String authoritystring = "";
						authorityarra = user.getAuthority_id().split(",");
						for(int i = 0;i<authorityarra.length;i++){
							if(authorityMap.containsKey(Integer.valueOf(authorityarra[i]))){
								authoritystring+=(authorityMap.get(Integer.valueOf(authorityarra[i]))+",");
							}
						}
						if(authoritystring.length()>0){
							authoritystring = authoritystring.substring(0,authoritystring.length()-1);
						}
						user.setAuthority(authoritystring);
						if(StringUtils.isNotBlank(authorityName)){
							 if(user.getAuthority().contains(authorityName)){
								 listAll.add(user);
							 }
						}else{
							listAll.add(user);
						}
					}else{
						if(StringUtils.isBlank(authorityName)){
							listAll.add(user);
						}
					}
				}
				allUserInfo.setUsers(listAll);
			}
			for(AllUserInfoAddressBook allUserInfo : userList){
				List<User> list = new ArrayList<User>();
				list = allUserInfo.getUsers();
				for(User user : list){
					if(StringUtils.isNotBlank(user.getDepartment_id())){
						String [] departmentarra = null;
						String departmentstring = "";
						departmentarra = user.getDepartment_id().split(",");
						for(int i = 0;i<departmentarra.length;i++){
							if(departmentMap.containsKey(Integer.valueOf(departmentarra[i]))){
								departmentstring+=(departmentMap.get(Integer.valueOf(departmentarra[i]))+",");
							}
						}
						if(departmentstring.length()>0){
							departmentstring = departmentstring.substring(0,departmentstring.length()-1);
						}
						user.setDepartment(departmentstring);
					}
					
					if(StringUtils.isNotBlank(user.getPosition_id())){
						String [] positionarra = null;
						String positionstring = "";
						positionarra = user.getPosition_id().split(",");
						for(int i = 0;i<positionarra.length;i++){
							if(positionMap.containsKey(Integer.valueOf(positionarra[i]))){
								System.out.println(Integer.valueOf(positionarra[i]));
								positionstring+=(positionMap.get(Integer.valueOf(positionarra[i]))+",");
							}
						}
						if(positionstring.length()>0){
							positionstring = positionstring.substring(0,positionstring.length()-1);
						}
						user.setPosition(positionstring);
					}
					//部门ID去重复
//					String departmentId = user.getDepartment_id();
//					String authoritys = user.getAuthority();//获取所有的权限
//					if(StringUtils.isNotBlank(departmentId)){
//						List<String> result = Arrays.asList(departmentId.split(","));
//						for(String string : result){
//							if(){
//								
//							}
//						}
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(userList.toString());
		return userList;
	}

	@Override
	public int updateUser(String userId, String key, String value) {
		int i = 0;
		if (searchUser("UserId", userId) != null) {
			try {
				switch (key) {
				case "UserName":
					// 修改用户名
					key = "user_name";
					break;
				case "Gender":
					// 修改性别
					key = "gender_id";
					break;
				case "PhoneNumber":
					// 修改手机号码
					key = "phone_number";
					
					int ii = 0;
					ii = iWalkieTalkieMapper.findUserByPhoneNumber(userId,value);
					if(ii>0){
						i = 3;
						return i;
					}
					break;
				case "EmailAddress":
					// 修改邮箱地址
					key = "mail_address";
					
					int iii = 0;
					iii = iWalkieTalkieMapper.findUserByEmailAddress(userId,value);
					if(iii>0){
						i = 4;
						return i;
					}
					break;
				case "ContactAddress":
					// 修改联系地址
					key = "contact_address";
					break;
				case "EntryTime":
					//修改入职时间
					key = "Entry_Time";
					break;
				default:
					break;
				}
				i = iWalkieTalkieMapper.updateUser(userId, key, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			i = 2;
		}
		return i;
	}

	@Override
	public int updateDepartment(String userId, int departmentIdOne, int positionIdOne, int departmentIdTwo,
			int positionIdTwo) {
		int i = 0;
		if (searchUser("UserId", userId) != null) {
			try {
				String departmentId = "";
				String positionId = "";
				if(departmentIdTwo!=-1){
					departmentId = departmentIdOne+","+departmentIdTwo;
				}else{
					departmentId = String.valueOf(departmentIdOne);
				}
				if(positionIdTwo!=-1){
					 positionId = positionIdOne+","+positionIdTwo;
				}else{
					positionId = String.valueOf(positionIdOne);
				}
				i = iWalkieTalkieMapper.updateUserDepartmentAndPosition(userId,departmentId,positionId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			i = 2;
		}
		return i;
	}

	@Override
	public int updateEmergencyContact(String userId, String emergencyUserName, String emergencyPhone) {
		int i = 0;
		if (searchUser("UserId", userId) != null) {
			try {
				i = iWalkieTalkieMapper.updateEmergencyContact(userId, emergencyUserName, emergencyPhone);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			i = 2;
		}
		return i;
	}

	@Override
	public List<Department> searchDepartment(int companyId) {
		List<Department> departmentList = null;
		try {
			departmentList = iWalkieTalkieMapper.searchDepartment(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return departmentList;
	}

	@Override
	public List<Position> searchPosition(int companyId) {
		List<Position> positionList = null;
		try {
			positionList = iWalkieTalkieMapper.searchPosition(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return positionList;
	}

	@Override
	public List<AttendanceRule> searchAttendanceRules(int manager,int company_id,int id) {
		List<AttendanceRule> ruleList = null;
		try {
			ruleList = iWalkieTalkieMapper.searchAttendanceRules(manager,company_id,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ruleList;
	}

	@Override
	public AttendanceRule searchAttendanceRuleByRuleId(int ruleId) {
		AttendanceRule rule = null;
		try {
			rule = iWalkieTalkieMapper.searchAttendanceRuleByRuleId(ruleId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rule;
	}

	@Override
	public List<Status> searchUserStatus() {
		List<Status> recordList = null;
		try {
			recordList = iWalkieTalkieMapper.searchUserStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordList;
	}

	@Override
	public int deleteAttendanceRule(int ruleId) {
		int i = -1;
		try {
			i = iWalkieTalkieMapper.deleteAttendanceRule(ruleId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 安卓更新头像
	 */
	@Override
	public String uploadHeadPortrait(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		Map<String, Object> map = new HashMap<>();
		String message = "";
		JSONObject jsonObject = new JSONObject();
		try {
			// 头像图片存放路径
			String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
			int p = path.indexOf("webapps");
			String realpath = path.substring(0, p + 8) + Constants.HeadPortrait_Folder + "\\";
			realpath = realpath.replaceAll("%20", " ");
			logger.info("头像图片存放路径：" + realpath);
			MultipartFile file = files[0];
			String user_id = request.getParameter("information");
			logger.info("安卓用户更新头像信息：" + user_id);
			// 存入文件夹
			File tempFile = new File(new File(realpath), file.getOriginalFilename());
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
			if (!tempFile.exists()) {
				tempFile.createNewFile();
			}
			file.transferTo(tempFile);

			User user = searchUser("UserId", user_id);
			String oldPath = "";
			int m = 0;
			if (user.getIcon_url() != null && !user.getIcon_url().equals("")) {
				// 有旧图片，需要删除旧图片并更新数据
				oldPath = path.substring(0, p + 8) + user.getIcon_url();
				logger.info("旧的头像文件路径：" + oldPath);
			}

			user.setIcon_url((realpath + file.getOriginalFilename()).split("webapps")[1]);
			m = iWalkieTalkieMapper.updateUserHeadPortraitInfo(user);

			if (m == 1) {
				map.put("code", 1001);
				message = user.getIcon_url();
				// 如果有旧图片地址，则删除
				if (oldPath != "" && oldPath != null) {
					File delfile = new File(oldPath);
					// 路径为文件且不为空则进行删除
					if (delfile.isFile() && delfile.exists()) {
						delfile.delete();
						logger.info("用户" + user.getUser_id() + "删除旧头像成功！");
					}
				}
			} else if (m == 0) {
				map.put("code", 1010);
			}
			jsonObject.put("url", message);
			map.put("data", jsonObject);
			logger.info(m == 1 ? "头像上传成功!" : "头像上传失败!");
			logger.info("回复内容kkk：" + GsonUtils.convertJSON(map));
		} catch (Exception e) {
			map.put("code", 1010);
			jsonObject.put("url", "");
			map.put("data", jsonObject);
			logger.info("更新头像失败：" + e.getMessage());
		}
		return GsonUtils.convertJSON(map);
	}
	@Override
	public void addAttendanceRecord(AttendanceRecord attendanceRecord)  {
		try {
			 iWalkieTalkieMapper.addAttendanceRecord(attendanceRecord);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<AttendanceRecord> searchAttendanceRecord(int userId, String startTime, String endTime) {
		List<AttendanceRecord> recordList = null;
		try {
			recordList = iWalkieTalkieMapper.searchAttendanceRecord(userId, startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordList;
	}

	@Override
	public List<AttendanceRecord> searchAttendanceTopRecord(String startTime, String endTime) {
		List<AttendanceRecord> recordList = null;
		try {
			recordList = iWalkieTalkieMapper.searchAttendanceTopRecord(startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordList;
	}

	@Override
	public int addAttendanceRule(AttendanceRule rule) {
		int i = -1;
		try {
			i = iWalkieTalkieMapper.addAttendanceRule(rule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public int modifyAttendanceRule(AttendanceRule rule) {
		int i = -1;
		try {
			i = iWalkieTalkieMapper.modifyAttendanceRule(rule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<VacationType> searchVacationTypes(int manager) {
		List<VacationType> typeList = null;
		try {
			typeList = iWalkieTalkieMapper.searchVacationTypes(manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return typeList;
	}

	@Override
	public List<OvertimeType> searchOvertimeTypes(int manager) {
		List<OvertimeType> typeList = null;
		try {
			typeList = iWalkieTalkieMapper.searchOvertimeTypes(manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return typeList;
	}

	@Override
	public List<Wage> searchWageByUserId(int userId, int startMonth, int endMonth) {
		List<Wage> wageList = null;
		try {
			wageList = iWalkieTalkieMapper.searchWageByUserId(userId, startMonth, endMonth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wageList;
	}
	
	@Override
	public List<Announcement> searchAnnouncement(int companyId,int bNum, int rows) {
		List<Announcement> announcementList = null;
		try {
			announcementList = iWalkieTalkieMapper.searchAnnouncement(companyId,bNum,rows);
			for(Announcement announcement : announcementList){
				if(!announcement.getExternal_link().equals("external_links")){
					String pictureUrl = announcement.getUrl().split("webapps")[1];
					announcement.setUrl(pictureUrl);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return announcementList;
	}
	/***
	 * 登入账号查询公司，（如果是admin就查询全部的公司返回到安卓）
	 * @param companyid
	 * @return
	 */
	@Override
	public List<Company> findCompanyList(int companyid) {
		List<Company> company = new ArrayList<Company>();
		try {
			company = iWalkieTalkieMapper.findCompanyList(companyid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return company;
	}
	/***
	 * 重置密码
	 * @param phoneNumber
	 * @param password
	 * @return
	 */
	@Override
	public int updateUserPassword(String phoneNumber, String password) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.updateUserPassword(phoneNumber,password);
		} catch (Exception e) {
			e.printStackTrace();
			i = 0;
		}
		return i;
	}
	/***
	 * 修改密码
	 * @param userId
	 * @param oldpassword
	 * @param newpassword
	 * @return
	 */
	@Override
	public int updatePassword(String userId, String oldpassword, String newpassword) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.updatePassword(userId,oldpassword,newpassword);
		} catch (Exception e) {
			e.printStackTrace();
			i = 0;
		}
		return i;
	}
	/**
	 * 查询广告
	 */
	@Override
	public List<Advertisement> findAdvertisementList(String time) {
		List<Advertisement> advertisements = new ArrayList<Advertisement>();
		try {
			advertisements = iWalkieTalkieMapper.findAdvertisementList(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return advertisements;
	}
	/***
	 * 外勤打卡详情信息
	 */
	@Override
	public void insertOutAttendanceRecord(OutAttendanceRecord outAttendanceRecord) {
		try {
			iWalkieTalkieMapper.insertOutAttendanceRecord(outAttendanceRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 新增外勤打卡审批人
	 */
	@Override
	public void insertOutAttendanceRecordAudit(OutAttendanceRecordAudit outAttendanceRecordAudit) {
		try {
			iWalkieTalkieMapper.insertOutAttendanceRecordAudit(outAttendanceRecordAudit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 *  新增外勤打卡抄送人
	 */
	@Override
	public void insertOutAttendanceRecordCopy(List<OutAttendanceRecordCopy> attendanceRecordCopies) {
		try {
			iWalkieTalkieMapper.insertOutAttendanceRecordCopy(attendanceRecordCopies);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 查询用户是否存在
	 */
	@Override
	public int findUserInfoByUserId(String userId) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.findUserInfoByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/***
	 * 查询用户账户密码是否正确
	 */
	@Override
	public int findUserInfoByUserIdAndPassWord(String userId, String oldpassword) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.findUserInfoByUserIdAndPassWord(userId,oldpassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/***
	 * 存储外勤打卡图片
	 */
	@Override
	public int OutAttendanceRecordPic(int out_attendance_id, String fileDirPath) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.OutAttendanceRecordPic(out_attendance_id,fileDirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 根据安卓提供日期查询工作表
	 */
	@Override
	public List<WorkingTime> searchHolidayRecord(String bt, String et,int companyId) {
		List<WorkingTime> workingTimes = new ArrayList<WorkingTime>();
		try {
			workingTimes = iWalkieTalkieMapper.searchHolidayRecord(bt,et,companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workingTimes;
	}
	/**
	 * 查询是否是休息日
	 */
	@Override
	public int searchHoliday(String date,int companyId) {
		int status = 0;
		 try {
			 status = iWalkieTalkieMapper.searchHoliday(date,companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	/**
	 * 根据用户ID查询公司iD
	 */
	@Override
	public int getCompangIdByUserId(int user_id) {
		int companyId = 0;
		companyId = iWalkieTalkieMapper.getCompangIdByUserId(user_id);
		return companyId;
	}
	/**
	 * 新增激光推送记录消息
	 */
	@Override
	public void insertJPushData(JPushData jpushData) {
		iWalkieTalkieMapper.insertJPushData(jpushData);
	}
	/**
	 * 查询外勤打卡审批
	 */
	@Override
	public List<OutAttendanceInfo> searchOutAttendance(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outAttendanceInfoList = iWalkieTalkieMapper.findOutAttendance(bt,et,userId,applicant_user_id,bNum,rows);
			if(outAttendanceInfoList!=null&&outAttendanceInfoList.size()>0){
				for(OutAttendanceInfo attendanceInfo : outAttendanceInfoList){
					List<OutAttendanceRecordPic> outAttendanceRecordPic = new ArrayList<OutAttendanceRecordPic>();
					outAttendanceRecordPic = attendanceInfo.getOutAttendanceRecordPic();
					for(OutAttendanceRecordPic attendanceRecordPic : outAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = iWalkieTalkieMapper.findOutAttendanceInfoAuditRecord(attendanceInfo.getOut_attendance_id());
					attendanceInfo.setOutAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤打卡需要审批信息报错");
		}
		return outAttendanceInfoList;
	}
	/**
	 * 查询外勤审批记录
	 */
	@Override
	public List<OutAttendanceInfo> serachauditOutAttendanceHistoricalRecords(int userId, int bNum, int rows, String bt,
			String et, String applicant_user_id) {
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outAttendanceInfoList = iWalkieTalkieMapper.findauditOutAttendance_historical_records(bt,et,userId,applicant_user_id,bNum,rows);
			if(outAttendanceInfoList!=null&&outAttendanceInfoList.size()>0){
				for(OutAttendanceInfo attendanceInfo : outAttendanceInfoList){
					List<OutAttendanceRecordPic> outAttendanceRecordPic = new ArrayList<OutAttendanceRecordPic>();
					outAttendanceRecordPic = attendanceInfo.getOutAttendanceRecordPic();
					for(OutAttendanceRecordPic attendanceRecordPic : outAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = iWalkieTalkieMapper.findOutAttendanceInfoAuditRecord(attendanceInfo.getOut_attendance_id());//查询外勤打卡审批的相关记录
					attendanceInfo.setOutAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤打卡需要审批信息报错");
		}
		return outAttendanceInfoList;
	}
	/**
	 * 提交外勤打卡审批记录
	 */
	@Override
	public JSONObject submitApprovalOutAttendance(String outAttendanceRecordAudit_id, String out_attendance_id,
			String result_id, String audit_status, String audit_resmarks,int userId,int id, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditOutAttendance(outAttendanceRecordAudit_id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeople(outAttendanceRecordAudit_id);//修改下一个需要审批的人
					if(x>0){
						outAttendanceMapper.updateOutAttendanceRecordStatus(out_attendance_id,1);//修改外勤详情信息的状态  状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateAttendanceRecordresult(out_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeople(outAttendanceRecordAudit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.OUT_ATTENDANCE_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OUT_ATTENDANCE_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.OUT_ATTENDANCE_APPLY);
						 pushData.setData(id);
						 String message = "您收到了一条外勤打卡申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserId(out_attendance_id);
						 websocket = new JSONObject();
						websocket.put("key",PushType.OUT_ATTENDANCE_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.OUT_ATTENDANCE_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.OUT_ATTENDANCE_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条外勤打卡申请已有人处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						outAttendanceMapper.updateOutAttendanceRecordStatus(out_attendance_id,2);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateAttendanceRecordresult(out_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserId(out_attendance_id);
						JSONObject websocket = new JSONObject();
						websocket = new JSONObject();
						websocket.put("key",PushType.OUT_ATTENDANCE_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						PushData pushData = new PushData();
						 pushData.setType(PushType.OUT_ATTENDANCE_APPROVAL);
						 pushData.setData(id);
						String  message = "您的一条外勤打卡申请已有人处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPROVAL),message);
						
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OUT_ATTENDANCE_APPROVAL);
						 alert.put("data", id);
						insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					outAttendanceMapper.updateOutAttendanceRecordStatus(out_attendance_id,3);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateAttendanceRecordresult(out_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserId(out_attendance_id);
					JSONObject websocket = new JSONObject();
					websocket = new JSONObject();
					websocket.put("key",PushType.OUT_ATTENDANCE_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					PushData pushData = new PushData();
					 pushData.setType(PushType.OUT_ATTENDANCE_APPROVAL);
					 pushData.setData(id);
					String  message = "您的一条外勤打卡申请已有人处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPROVAL),message);
					
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.OUT_ATTENDANCE_APPROVAL);
					 alert.put("data", id);
					insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = userId;
					String ip = Futil.getIpAddr(request);
					AttendanceRecord attendanceRecord = new AttendanceRecord();
					attendanceRecord = outAttendanceMapper.findAttendanceRecordByout_attendance_id(out_attendance_id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = "安卓："+JSONObject.fromObject(attendanceRecord).toString()+"申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("外勤打卡审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 查询公司ID
	 * @param user_id
	 * @return
	 */
	public  int getCompangId(int user_id) {
		int comPanyId = 0;
		comPanyId = iuserService.getCompangIdByUserId(user_id);
		return comPanyId;
	}
	/***
	 * 新增推送消息
	 * @param alert 内容
	 * @param outAttendanceApply 推送类型
	 * @param companyId 公司ID
	 * @param currentFormatDateTime 时间
	 * @param pushType  别名推送，全部推送，标签推送
	 * @param resDate 激光返回结果
	 * @param setResultCode 激光返回码
	 * @param userIdList 推送给谁的list
	 */
	public   void insertJPushData(String alert, int outAttendanceApply, int companyId, String currentFormatDateTime,
			String pushType, String resDate, int setResultCode, String userIdList) {
		JPushData jpushData = new JPushData();
		jpushData.setContent(alert);
		jpushData.setDataType(outAttendanceApply);
		jpushData.setPusher(companyId);
		jpushData.setPushTime(currentFormatDateTime);
		jpushData.setPushType(pushType);
		jpushData.setResultCode(setResultCode);
		jpushData.setResultContent(resDate);
		jpushData.setReceiver(userIdList);
		iuserService.insertJPushData(jpushData);
	}
	/**
	 * 查询申请人申请记录
	 */
	@Override
	public List<OutAttendanceInfo> searchApprovalAttendanceRecord(int userId, int bNum, int rows, String bt,
			String et) {
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outAttendanceInfoList = iWalkieTalkieMapper.searchApprovalAttendanceRecord(bt,et,userId,bNum,rows);
			if(outAttendanceInfoList!=null&&outAttendanceInfoList.size()>0){
				for(OutAttendanceInfo attendanceInfo : outAttendanceInfoList){
					List<OutAttendanceRecordPic> outAttendanceRecordPic = new ArrayList<OutAttendanceRecordPic>();
					outAttendanceRecordPic = attendanceInfo.getOutAttendanceRecordPic();
					for(OutAttendanceRecordPic attendanceRecordPic : outAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findOutAttendanceInfoAuditRecord(attendanceInfo.getOut_attendance_id());//查询外勤打卡审批的相关记录
					attendanceInfo.setOutAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤打卡需要审批信息报错");
		}
		return outAttendanceInfoList;
	}
	/**
	 * 撤销外勤申请详情
	 */
	@Override
	public JSONObject submitRevokeOutAttendance(int id, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","撤销失败");
		jsonObject.put("result","fail");
		try {
			iWalkieTalkieMapper.submitRevokeOutAttendance(id,userId);
			jsonObject.put("msg","撤销成功");
			jsonObject.put("result","success");
			String ip = Futil.getIpAddr(request);
			String remarks = "安卓："+userId+"撤销外勤打卡"+id;
			loggerMapper.addLogger(userId,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 撤销外勤申请详情信息报错");
		}
		return jsonObject;
	}
	/**
	 * 新增补卡详情记录
	 */
	@Override
	public void insertAppealAttendanceRecord(AppealAttendanceRecord appealAttendanceRecord) {
		try {
			iWalkieTalkieMapper.insertAppealAttendanceRecord(appealAttendanceRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	/**
	 * 新增补卡审批人信息
	 */
	@Override
	public void insertAppealAttendanceRecordAudit(AppealAttendanceRecordAudit appealAttendanceRecordAudit) {
		try {
			iWalkieTalkieMapper.insertAppealAttendanceRecordAudit(appealAttendanceRecordAudit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int AppealAttendanceRecordPic(int appeal_attendance_id, String fileDirPath) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.AppealAttendanceRecordPic(appeal_attendance_id,fileDirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 补卡审批查询
	 */
	@Override
	public List<AppealAttendanceInfo> serachauditAppealAttendanceHistoricalRecords(int userId, int bNum, int rows,
			String bt, String et, String applicant_user_id) {
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			appealAttendanceInfoList = outAttendanceMapper.findAppealAttendance(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(appealAttendanceInfoList!=null&&appealAttendanceInfoList.size()>0){
				for(AppealAttendanceInfo attendanceInfo : appealAttendanceInfoList){
					List<AppealAttendanceRecordPic> appealAttendanceRecordPic = new ArrayList<AppealAttendanceRecordPic>();
					appealAttendanceRecordPic = attendanceInfo.getAppealAttendanceRecordPic();
					for(AppealAttendanceRecordPic attendanceRecordPic : appealAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					attendanceInfo.setAttendance_time(attendanceInfo.getAttendance_time().substring(0,19));
					outAttendanceInfoAuditRecord = outAttendanceMapper.findAppealRecordInfoAuditRecord(attendanceInfo.getAppeal_attendance_id());
					attendanceInfo.setAppealAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询补卡需要审批信息报错");
		}
		return appealAttendanceInfoList;
	
	}
	/**
	 * 补卡审批的历史记录
	 */
	@Override
	public List<AppealAttendanceInfo> searchAppealAttendance(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			appealAttendanceInfoList = outAttendanceMapper.findauditappealAttendance_historical_records(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(appealAttendanceInfoList!=null&&appealAttendanceInfoList.size()>0){
				for(AppealAttendanceInfo attendanceInfo : appealAttendanceInfoList){
					List<AppealAttendanceRecordPic> appealAttendanceRecordPic = new ArrayList<AppealAttendanceRecordPic>();
					appealAttendanceRecordPic = attendanceInfo.getAppealAttendanceRecordPic();
					for(AppealAttendanceRecordPic attendanceRecordPic : appealAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					attendanceInfo.setAttendance_time(attendanceInfo.getAttendance_time().substring(0,19));
					attendanceInfo.setAudit_time(attendanceInfo.getAudit_time().substring(0,19));
					outAttendanceInfoAuditRecord = outAttendanceMapper.findAppealRecordInfoAuditRecord(attendanceInfo.getAppeal_attendance_id());
					attendanceInfo.setAppealAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询补卡需要审批信息报错");
		}
		return appealAttendanceInfoList;
	}
	/**
	 * 新增补卡信息
	 */
	@Override
	public void addAppealAttendanceRecord(AttendanceRecord record) {
		try {
			iWalkieTalkieMapper.addAppealAttendanceRecord(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 提交补卡审批结果
	 */
	@Override
	public JSONObject submitApprovalAppealAttendance(String appealAttendanceRecordAudit_id, String appeal_attendance_id,
			String result_id, String audit_status, String audit_resmarks, int userId,int id, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditappealAttendance(appealAttendanceRecordAudit_id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleAppeal(appealAttendanceRecordAudit_id);//修改下一个需要审批的人
					if(x>0){
						outAttendanceMapper.updateAppealAttendanceRecordStatus(appeal_attendance_id,1);//修改外勤详情信息的状态  状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateAppealAttendanceRecordresult(appeal_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleAppeal(appealAttendanceRecordAudit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.ATTENDANCE_APPEAL_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.ATTENDANCE_APPEAL_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.ATTENDANCE_APPEAL_APPLY);
						 pushData.setData(id);
						 String message = "您收到了一条补卡申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdAppeal(appeal_attendance_id);
						 websocket = new JSONObject();
						websocket.put("key",PushType.ATTENDANCE_APPEAL_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.ATTENDANCE_APPEAL_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.ATTENDANCE_APPEAL_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条补卡申请已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						outAttendanceMapper.updateAppealAttendanceRecordStatus(appeal_attendance_id,2);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateAppealAttendanceRecordresult(appeal_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdAppeal(appeal_attendance_id);
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.ATTENDANCE_APPEAL_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.ATTENDANCE_APPEAL_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条补卡申请已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.ATTENDANCE_APPEAL_APPROVAL);
						 alert.put("data", id);
						insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					outAttendanceMapper.updateOutAttendanceRecordStatus(appeal_attendance_id,3);//修改外勤详情信息的状态 状态，0是未审批，1是审批中，2是审批通过，3是审批不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateAppealAttendanceRecordresult(appeal_attendance_id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdAppeal(appeal_attendance_id);
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.ATTENDANCE_APPEAL_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.ATTENDANCE_APPEAL_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条补卡申请已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.ATTENDANCE_APPEAL_APPROVAL);
					 alert.put("data", id);
					insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					AttendanceRecord attendanceRecord = new AttendanceRecord();
					attendanceRecord = outAttendanceMapper.findAttendanceRecordByAppeal_attendance_id(appeal_attendance_id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(attendanceRecord).toString()+"补卡申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("补卡审批处理报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 撤销补卡申请记录
	 */
	@Override
	public JSONObject submitRevokeAppealAttendance(int id, int userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","撤销失败");
		jsonObject.put("result","fail");
		try {
			iWalkieTalkieMapper.submitRevokeAppealAttendance(id,userId);
			jsonObject.put("msg","撤销成功");
			jsonObject.put("result","success");
			String ip = Futil.getIpAddr(request);
			String remarks = "安卓："+userId+"撤销补卡"+id;
			loggerMapper.addLogger(userId,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 撤销外勤申请详情信息报错");
		}
		return jsonObject;
	}
	/**
	 * 申请人查询补卡申请记录
	 */
	@Override
	public List<AppealAttendanceInfo> searchApprovalAppealAttendanceRecord(int userId, int bNum, int rows, String bt,
			String et) {
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			appealAttendanceInfoList = applyRecordMapper.findAppeal_Records(bt,et,String.valueOf(userId),bNum,rows);
			if(appealAttendanceInfoList!=null&&appealAttendanceInfoList.size()>0){
				for(AppealAttendanceInfo attendanceInfo : appealAttendanceInfoList){
					List<AppealAttendanceRecordPic> appealAttendanceRecordPic = new ArrayList<AppealAttendanceRecordPic>();
					appealAttendanceRecordPic = attendanceInfo.getAppealAttendanceRecordPic();
					for(AppealAttendanceRecordPic attendanceRecordPic : appealAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					attendanceInfo.setAttendance_time(attendanceInfo.getAttendance_time().substring(0,19));
					outAttendanceInfoAuditRecord = outAttendanceMapper.findAppealRecordInfoAuditRecord(attendanceInfo.getAppeal_attendance_id());//查询补卡审批的相关记录
					attendanceInfo.setAppealAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询补卡申请人申请信息报错");
		}
		
		return appealAttendanceInfoList;
	}
	/**
	 * 新增加班申请记录信息
	 */
	@Override
	public void insertOverTimeRecord(OverTimeRecord overTimeRecord) {
		try {
			iWalkieTalkieMapper.insertOverTimeRecord(overTimeRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增加班申请记录信息报错");
		}
	}
	/**
	 * 新增加班审批人记录信息
	 */
	@Override
	public void insertOverTimeRecordAudit(OverTimeRecordAudit overTimeRecordAudit) {
		try {
			iWalkieTalkieMapper.insertOverTimeRecordAudit(overTimeRecordAudit);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增加班审批人记录信息报错");
		}
	}
	/**
	 * 新增加班抄送人记录信息
	 */
	@Override
	public void insertOverTimeRecordCopy(List<OverTimeRecordCopy> overTimeRecordCopies) {
		try {
			iWalkieTalkieMapper.insertOverTimeRecordCopy(overTimeRecordCopies);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增加班抄送人记录信息报错");
		}
	}
	/**
	 * 新增加班申请图片
	 */
	@Override
	public int OverTimeRecordPic(int id, String fileDirPath) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.OverTimeRecordPic(id,fileDirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 加班查询已经审批过的
	 */
	@Override
	public List<OverTimeRecord> searchOverTimeHistoricalRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			overTimeRecordList = outAttendanceMapper.findauditoverTime_historical_records(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(overTimeRecordList!=null&&overTimeRecordList.size()>0){
				for(OverTimeRecord overTimeRecord : overTimeRecordList){
					List<OverTimeRecordPic> overTimeRecordPic = new ArrayList<OverTimeRecordPic>();
					overTimeRecordPic = overTimeRecord.getOverTimeRecordPic();
					for(OverTimeRecordPic attendanceRecordPic : overTimeRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoverTimeauditRecord(overTimeRecord.getId());
					overTimeRecord.setOverTimeAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询加班需要审批信息报错");
		}
		return overTimeRecordList;
	}
	/**
	 * 查询加班需要审批信息
	 */
	@Override
	public List<OverTimeRecord> serachOverTimeRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			overTimeRecordList = outAttendanceMapper.findoverTime(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(overTimeRecordList!=null&&overTimeRecordList.size()>0){
				for(OverTimeRecord overTimeRecord : overTimeRecordList){
					List<OverTimeRecordPic> overTimeRecordPic = new ArrayList<OverTimeRecordPic>();
					overTimeRecordPic = overTimeRecord.getOverTimeRecordPic();
					for(OverTimeRecordPic attendanceRecordPic : overTimeRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoverTimeauditRecord(overTimeRecord.getId());
					overTimeRecord.setOverTimeAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询加班需要审批信息报错");
		}
		return overTimeRecordList;
	}
	/**
	 * 申请人查询加班申请记录
	 */
	@Override
	public List<OverTimeRecord> searchApprovalOverTime(int userId, int bNum, int rows, String bt, String et) {
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			overTimeRecordList = applyRecordMapper.findOverTime_Record(bt,et,String.valueOf(userId),bNum,rows);
			if(overTimeRecordList!=null&&overTimeRecordList.size()>0){
				for(OverTimeRecord overTimeRecord : overTimeRecordList){
					List<OverTimeRecordPic> overTimeRecordPic = new ArrayList<OverTimeRecordPic>();
					overTimeRecordPic = overTimeRecord.getOverTimeRecordPic();
					for(OverTimeRecordPic attendanceRecordPic : overTimeRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoverTimeauditRecord(overTimeRecord.getId());//查询加班审批的相关记录
					overTimeRecord.setOverTimeAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询申请人查询加班申请记录信息报错");
		}
		return overTimeRecordList;
	}
	/**
	 * 申请人撤销加班申请
	 */
	@Override
	public JSONObject submitRevokeOverTime(int id, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","撤销失败");
		jsonObject.put("result","fail");
		try {
			iWalkieTalkieMapper.submitRevokeOverTime(id,userId);
			jsonObject.put("msg","撤销成功");
			jsonObject.put("result","success");
			String ip = Futil.getIpAddr(request);
			String remarks = "安卓："+userId+"撤销加班"+id;
			loggerMapper.addLogger(userId,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 撤销加班申请详情信息报错");
		}
		return jsonObject;
	}
	/**
	 * 加班审批提交
	 */
	@Override
	public JSONObject submitApprovalOverTime(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditoverTimeAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleOverTime(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateOverTimeAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleOverTime(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.OVERTIME_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OVERTIME_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.OVERTIME_APPLY);
						 pushData.setData(id);
						 String message = "您收到了一条加班申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.OVERTIME_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdOverTime(id);//查询加班申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.OVERTIME_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.OVERTIME_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.OVERTIME_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条加班申请已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.OVERTIME_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateOverTimeAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdOverTime(id);//查询加班申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.OVERTIME_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.OVERTIME_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.OVERTIME_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条加班申请已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.OVERTIME_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateOverTimeAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdOverTime(id);//查询加班申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.OVERTIME_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.OVERTIME_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.OVERTIME_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条加班申请已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.OVERTIME_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					OverTimeRecord overTimeRecord = new OverTimeRecord();
					overTimeRecord = outAttendanceMapper.findAttendanceRecordByOverTime_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(overTimeRecord).toString()+"加班补卡申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("加班审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 查询用户的加班时间
	 */
	@Override
	public float findUserBaseOvertime(int userId,int company_id) {
		float hour = 0;
		try {
			hour = iWalkieTalkieMapper.findUserBaseOvertime(userId,company_id);
			if(hour<0){
				hour = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hour;
	}
	/**
	 * 新增假期详情信息
	 */
	@Override
	public void insertVacationRecord(VacationRecord vacationRecord) {
		try {
			iWalkieTalkieMapper.insertVacationRecord(vacationRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增假期申请记录信息报错");
		}
	}
	/**
	 * 新增假期审批人记录
	 */
	@Override
	public void insertVacationRecordAudit(VacationRecordAudit vacationRecordAudit) {
		try {
			iWalkieTalkieMapper.insertVacationRecordAudit(vacationRecordAudit);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增假期审批人记录信息报错");
		}
	}
	/**
	 * 新增假期抄送人记录
	 */
	@Override
	public void insertVacationRecordCopy(List<VacationRecordCopy> vacationRecordCopies) {
		try {
			iWalkieTalkieMapper.insertVacationRecordCopy(vacationRecordCopies);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增假期抄送人记录信息报错");
		}
	}
	/**
	 * 新增假期图片
	 */
	@Override
	public int VacationRecordPic(int id, String fileDirPath) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.VacationRecordPic(id,fileDirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 查询假期已经审批过的
	 */
	@Override
	public List<VacationRecord> searchVacationHistoricalRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			vacationRecordList = outAttendanceMapper.findauditvacation_historical_records(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(vacationRecordList!=null&&vacationRecordList.size()>0){
				for(VacationRecord vacationRecord : vacationRecordList){
					List<VacationRecordPic> vacationRecordPic = new ArrayList<VacationRecordPic>();
					vacationRecordPic = vacationRecord.getVacationRecordPic();
					for(VacationRecordPic attendanceRecordPic : vacationRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findvacationauditRecord(vacationRecord.getId());
					vacationRecord.setVacationAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询加班已经审批信息报错");
		}
		return vacationRecordList;
	}
	/**
	 * 查询假期审批信息
	 */
	@Override
	public List<VacationRecord> serachVacationRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			vacationRecordList = outAttendanceMapper.findvacation(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(vacationRecordList!=null&&vacationRecordList.size()>0){
				for(VacationRecord vacationRecord : vacationRecordList){
					List<VacationRecordPic> vacationRecordPic = new ArrayList<VacationRecordPic>();
					vacationRecordPic = vacationRecord.getVacationRecordPic();
					for(VacationRecordPic attendanceRecordPic : vacationRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findvacationauditRecord(vacationRecord.getId());
					vacationRecord.setVacationAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询假期需要审批信息报错");
		}
		return vacationRecordList;
	}
	/**
	 * 提交假期审批结果
	 */
	@Override
	public JSONObject submitApprovalVacation(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditvacationAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeoplevacation(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updatevacationAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeoplevacation(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.VACATION_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.VACATION_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.VACATION_APPLY);
						 pushData.setData(id);
						 String message = "您收到一条假期审批!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.VACATION_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdvacation(id);//查询假期申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.VACATION_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.VACATION_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.VACATION_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条假期审批已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.VACATION_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updatevacationAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdvacation(id);//查询假期申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.VACATION_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.VACATION_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.VACATION_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条假期审批已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.VACATION_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updatevacationAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdvacation(id);//查询假期申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.VACATION_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.VACATION_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.VACATION_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条假期审批已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.VACATION_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					VacationRecord vacationRecord = new VacationRecord();
					vacationRecord = outAttendanceMapper.findAttendanceRecordByvacation_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(vacationRecord).toString()+"假期申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("假期审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 申请人撤销假期申请
	 */
	@Override
	public JSONObject submitRevokeVacation(int id, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","撤销失败");
		jsonObject.put("result","fail");
		try {
			iWalkieTalkieMapper.submitRevokeVacation(id,userId);
			jsonObject.put("msg","撤销成功");
			jsonObject.put("result","success");
			String ip = Futil.getIpAddr(request);
			String remarks = "安卓："+userId+"撤销假期"+id;
			loggerMapper.addLogger(userId,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 撤销假期申请详情信息报错");
		}
		return jsonObject;
	}
	/**
	 * 申请人查询假期申请
	 */
	@Override
	public List<VacationRecord> searchApprovalVacation(int userId, int bNum, int rows, String bt, String et) {
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			vacationRecordList = applyRecordMapper.findVacation_Record(bt,et,String.valueOf(userId),bNum,rows);
			if(vacationRecordList!=null&&vacationRecordList.size()>0){
				for(VacationRecord vacationRecord : vacationRecordList){
					List<VacationRecordPic> vacationRecordPic = new ArrayList<VacationRecordPic>();
					vacationRecordPic = vacationRecord.getVacationRecordPic();
					for(VacationRecordPic attendanceRecordPic : vacationRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findvacationauditRecord(vacationRecord.getId());//查询假期审批的相关记录
					vacationRecord.setVacationAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询申请人查询假期申请记录信息报错");
		}
		return vacationRecordList;
	
	}
	/**
	 * 查询人员的时间段的工时，加班，请假等信息
	 */
	@Override
	public List<WorkingTimeUser> searchWorkingTimeUser(int userId, String startTime, String endTime, int companyId) {
		List<WorkingTimeUser> list = new ArrayList<WorkingTimeUser>();
		try {
			list = iWalkieTalkieMapper.searchWorkingTimeUser(userId,startTime,endTime,companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 新增外出申请
	 * @param outGoingRecord
	 */
	@Override
	public void insertOutGoingRecord(OutGoingRecord outGoingRecord) {
		try {
			iWalkieTalkieMapper.insertOutGoingRecord(outGoingRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增外出申请记录信息报错");
		}
	}
	/**
	 * 新增外出审批人信息
	 */
	@Override
	public void insertOutGoingRecordAudit(OutGoingRecordAudit outGoingRecordAudit) {
		try {
			iWalkieTalkieMapper.insertOutGoingRecordAudit(outGoingRecordAudit);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增外出审批人信息报错");
		}
	}
	/**
	 * 新增外出抄送人信息
	 */
	@Override
	public void insertOutGoingRecordCopy(List<OutGoingRecordCopy> outGoingRecordCopies) {
		try {
			iWalkieTalkieMapper.insertOutGoingRecordCopy(outGoingRecordCopies);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增外出抄送人信息报错");
		}
	}
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
	@Override
	public List<OutGoingRecord> searchOutGoingHistoricalRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outGoingRecordList = outAttendanceMapper.findauditoutGoing_historical_records(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(outGoingRecordList!=null&&outGoingRecordList.size()>0){
				for(OutGoingRecord outGoingRecord : outGoingRecordList){
					List<OutGoingRecordPic> outGoingRecordPic = new ArrayList<OutGoingRecordPic>();
					outGoingRecordPic = outGoingRecord.getOutGoingRecordPic();
					for(OutGoingRecordPic attendanceRecordPic : outGoingRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoutGoingauditRecord(outGoingRecord.getId());
					outGoingRecord.setOutGoingAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外出已审批信息报错");
		}
		return outGoingRecordList;
	}
	/**
	 * 查询外出需要审批
	 */
	@Override
	public List<OutGoingRecord> serachOutGoingRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outGoingRecordList = outAttendanceMapper.findoutGoing(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(outGoingRecordList!=null&&outGoingRecordList.size()>0){
				for(OutGoingRecord outGoingRecord : outGoingRecordList){
					List<OutGoingRecordPic> outGoingRecordPic = new ArrayList<OutGoingRecordPic>();
					outGoingRecordPic = outGoingRecord.getOutGoingRecordPic();
					for(OutGoingRecordPic attendanceRecordPic : outGoingRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoutGoingauditRecord(outGoingRecord.getId());
					outGoingRecord.setOutGoingAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外出需要审批信息报错");
		}
		return outGoingRecordList;
	}
	/**
	 * 申请人查询外出申请记录
	 */
	@Override
	public List<OutGoingRecord> searchApprovalOutGoing(int userId, int bNum, int rows, String bt, String et) {
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outGoingRecordList = applyRecordMapper.findOutGoing_Record(bt,et,String.valueOf(userId),bNum,rows);
			if(outGoingRecordList!=null&&outGoingRecordList.size()>0){
				for(OutGoingRecord outGoingRecord : outGoingRecordList){
					List<OutGoingRecordPic> outGoingRecordPic = new ArrayList<OutGoingRecordPic>();
					outGoingRecordPic = outGoingRecord.getOutGoingRecordPic();
					for(OutGoingRecordPic attendanceRecordPic : outGoingRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoutGoingauditRecord(outGoingRecord.getId());
					outGoingRecord.setOutGoingAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("申请人查询外出申请记录信息报错");
		}
		return outGoingRecordList;
	}
	/**
	 * 提交外出审批结果
	 */
	@Override
	public JSONObject submitApprovalOutGoing(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditoutGoingAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleOutGoing(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateOutGoingAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleOutGoing(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.GO_OUT_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.GO_OUT_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.GO_OUT_APPLY);
						 pushData.setData(id);
						 String message = "您收到一条外出审批!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.GO_OUT_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdOutGoing(id);//查询加班申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.GO_OUT_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.GO_OUT_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.GO_OUT_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条外出审批已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.GO_OUT_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateOutGoingAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdOutGoing(id);//查询加班申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.GO_OUT_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.GO_OUT_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.GO_OUT_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条外出审批已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.GO_OUT_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateOutGoingAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdOutGoing(id);//查询加班申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.GO_OUT_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.GO_OUT_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.GO_OUT_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条外出审批已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.GO_OUT_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					OutGoingRecord outGoingRecord = new OutGoingRecord();
					outGoingRecord = outAttendanceMapper.findAttendanceRecordByOutGoing_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(outGoingRecord).toString()+"外出申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("外出审批处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 申请人撤销外出申请记录
	 */
	@Override
	public JSONObject submitRevokeOutGoing(int id, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","撤销失败");
		jsonObject.put("result","fail");
		try {
			iWalkieTalkieMapper.submitRevokeOutGoing(id,userId);
			jsonObject.put("msg","撤销成功");
			jsonObject.put("result","success");
			String ip = Futil.getIpAddr(request);
			String remarks = "安卓："+userId+"撤销加班"+id;
			loggerMapper.addLogger(userId,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 申请人撤销外出申请记录报错");
		}
		return jsonObject;
	}
	/**
	 * 新增出差详情信息
	 */
	@Override
	public void insertBusinessTraveIRecord(BusinessTraveIRecord businessTraveIRecord) {
		try {
			iWalkieTalkieMapper.insertBusinessTraveIRecord(businessTraveIRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增出差详情信息报错");
		}
	}
	/**
	 * 新增出差审批人信息
	 */
	@Override
	public void insertBusinessTraveIRecordAudit(BusinessTraveIRecordAudit businessTraveIRecordAudit) {
		try {
			iWalkieTalkieMapper.insertBusinessTraveIRecordAudit(businessTraveIRecordAudit);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增出差审批人信息报错");
		}
	}
	/**
	 * 新增出差抄送人信息
	 */
	@Override
	public void insertBusinessTraveIRecordCopy(List<BusinessTraveIRecordCopy> businessTraveIRecordCopies) {
		try {
			iWalkieTalkieMapper.insertBusinessTraveIRecordCopy(businessTraveIRecordCopies);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增出差抄送人信息报错");
		}
	}
	/**
	 * 新增出差图片
	 */
	@Override
	public int BusinessTraveIRecordPic(int id, String fileDirPath) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.BusinessTraveIRecordPic(id,fileDirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 新增外出图片
	 */
	@Override
	public int OutGoingRecordPic(int id, String fileDirPath) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.OutGoingRecordPic(id,fileDirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
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
	@Override
	public List<BusinessTraveIRecord> searchBusinessTraveIHistoricalRecords(int userId, int bNum, int rows, String bt,
			String et, String applicant_user_id) {
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			businessTraveIRecordList = outAttendanceMapper.findauditbusinessTraveI_historical_records(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(businessTraveIRecordList!=null&&businessTraveIRecordList.size()>0){
				for(BusinessTraveIRecord businessTraveIRecord : businessTraveIRecordList){
					List<BusinessTraveIRecordPic> businessTraveIRecordPic = new ArrayList<BusinessTraveIRecordPic>();
					businessTraveIRecordPic = businessTraveIRecord.getBusinessTraveIRecordPic();
					for(BusinessTraveIRecordPic attendanceRecordPic : businessTraveIRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findbusinessTraveIauditRecord(businessTraveIRecord.getId());
					businessTraveIRecord.setBusinessTraveIAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询出差已经审批过的信息报错");
		}
		return businessTraveIRecordList;
	}
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
	@Override
	public List<BusinessTraveIRecord> serachBusinessTraveIRecords(int userId, int bNum, int rows, String bt, String et,
			String applicant_user_id) {
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			businessTraveIRecordList = outAttendanceMapper.findbusinessTraveI(bt,et,String.valueOf(userId),applicant_user_id,bNum,rows);
			if(businessTraveIRecordList!=null&&businessTraveIRecordList.size()>0){
				for(BusinessTraveIRecord businessTraveIRecord : businessTraveIRecordList){
					List<BusinessTraveIRecordPic> businessTraveIRecordPic = new ArrayList<BusinessTraveIRecordPic>();
					businessTraveIRecordPic = businessTraveIRecord.getBusinessTraveIRecordPic();
					for(BusinessTraveIRecordPic attendanceRecordPic : businessTraveIRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findbusinessTraveIauditRecord(businessTraveIRecord.getId());
					businessTraveIRecord.setBusinessTraveIAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询出差未审批过的信息报错");
		}
		return businessTraveIRecordList;
	}
	/**
	 * 申请人查询出差申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<BusinessTraveIRecord> searchApprovalBusinessTraveI(int userId, int bNum, int rows, String bt,
			String et) {
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			businessTraveIRecordList = applyRecordMapper.findbusinessTraveI_Record(bt,et,String.valueOf(userId),bNum,rows);
			if(businessTraveIRecordList!=null&&businessTraveIRecordList.size()>0){
				for(BusinessTraveIRecord businessTraveIRecord : businessTraveIRecordList){
					List<BusinessTraveIRecordPic> businessTraveIRecordPic = new ArrayList<BusinessTraveIRecordPic>();
					businessTraveIRecordPic = businessTraveIRecord.getBusinessTraveIRecordPic();
					for(BusinessTraveIRecordPic attendanceRecordPic : businessTraveIRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findbusinessTraveIauditRecord(businessTraveIRecord.getId());
					businessTraveIRecord.setBusinessTraveIAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("申请人查询出差申请记录");
		}
		return businessTraveIRecordList;
	
	}
	/**
	 * 提交出差审批结果
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject submitApprovalBusinessTraveI(String audit_id, String id, String result_id, String audit_status,
			String audit_resmarks, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","审批失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			i = outAttendanceMapper.submitAuditbusinessTraveIAudit(id,audit_resmarks,audit_status);//审批人提交审批信息
			if(i>0){
				if(audit_status.equals("2")){//审批通过
					int x = 0;
					x = outAttendanceMapper.updateAuditPeopleBusinessTraveI(audit_id);//修改下一个需要审批的人
					if(x>0){
						if(result_id.equals("5")){
							result_id = "6";
						}
						outAttendanceMapper.updateBusinessTraveIAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						
						String user_id = outAttendanceMapper.findAuditPeopleBusinessTraveI(audit_id);//查询下一个需要审批人的user_id
						JSONObject websocket = new JSONObject();
						websocket.put("key",PushType.BUSINESS_TRIP_APPLY);
						websocket.put("user_id", user_id);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(user_id);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.BUSINESS_TRIP_APPLY);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.BUSINESS_TRIP_APPLY);
						 pushData.setData(id);
						 String message = "您收到一条出差申请!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPLY),message);
						int companyId = getCompangId(Integer.valueOf(user_id));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(user_id));
						insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
						
						String applyUserId = outAttendanceMapper.findApplyUserIdBusinessTraveI(id);//查询加班申请人的userId
						 websocket = new JSONObject();
						websocket.put("key",PushType.BUSINESS_TRIP_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						 audience = new JSONObject();
						 alias1 = new JSONArray();
						 alias1.add(applyUserId);
						 audience.put("alias", alias1);
						  alert = new JSONObject();
						 alert.put("type",PushType.BUSINESS_TRIP_APPROVAL);
						 alert.put("data", id);
						  pushData = new PushData();
						 pushData.setType(PushType.BUSINESS_TRIP_APPROVAL);
						 pushData.setData(id);
						  message = "您的一条出差申请已经处理!";
						 resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPROVAL),message);
						 companyId = getCompangId(Integer.valueOf(applyUserId));
						 setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}else{
						if(result_id.equals("5")||result_id.equals("6")){
							result_id = "7";
						}
						outAttendanceMapper.updateBusinessTraveIAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
						String applyUserId = outAttendanceMapper.findApplyUserIdBusinessTraveI(id);//查询加班申请人的userId
						JSONObject websocket = new JSONObject();
						 websocket = new JSONObject();
						websocket.put("key",PushType.BUSINESS_TRIP_APPROVAL);
						websocket.put("user_id", applyUserId);
						WebSocketFrameHandler.sendData(websocket);
						JSONObject audience = new JSONObject();
						JSONArray alias1 = new JSONArray();
				        alias1.add(applyUserId);
						 audience.put("alias", alias1);
						 JSONObject alert = new JSONObject();
						 alert.put("type",PushType.BUSINESS_TRIP_APPROVAL);
						 alert.put("data", id);
						 PushData pushData = new PushData();
						 pushData.setType(PushType.BUSINESS_TRIP_APPROVAL);
						 pushData.setData(id);
						 String message = "您的一条出差申请已经处理!";
						JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPROVAL),message);
						int companyId = getCompangId(Integer.valueOf(applyUserId));
						int setResultCode = 400;
						if(resDate.containsKey("error")){
							setResultCode = 400;
						}else{
							setResultCode = 200;
						}
						List<Integer> userIdList = new ArrayList<Integer>();
						userIdList.add(Integer.valueOf(applyUserId));
						insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
								JSONArray.toJSONString(userIdList).toString());
					}
				}else if(audit_status.equals("3")){//审核不通过
					if(result_id.equals("5")||result_id.equals("6")){
						result_id = "8";
					}
					outAttendanceMapper.updateBusinessTraveIAttendanceRecordresult(id,result_id);//修改考勤信息的状态 5是待审批，6是审批中，7是审批通过，8是审批不通过
					String applyUserId = outAttendanceMapper.findApplyUserIdBusinessTraveI(id);//查询加班申请人的userId
					JSONObject websocket = new JSONObject();
					 websocket = new JSONObject();
					websocket.put("key",PushType.BUSINESS_TRIP_APPROVAL);
					websocket.put("user_id", applyUserId);
					WebSocketFrameHandler.sendData(websocket);
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
			        alias1.add(applyUserId);
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.BUSINESS_TRIP_APPROVAL);
					 alert.put("data", id);
					 PushData pushData = new PushData();
					 pushData.setType(PushType.BUSINESS_TRIP_APPROVAL);
					 pushData.setData(id);
					 String message = "您的一条出差申请已经处理!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPROVAL),message);
					int companyId = getCompangId(Integer.valueOf(applyUserId));
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(Integer.valueOf(applyUserId));
					insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPROVAL,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
				}
				jsonObject.put("msg","审批成功");
				jsonObject.put("result","success");
			}
			
			 if(i>0){
				 	jsonObject.put("msg","审批成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					BusinessTraveIRecord overTimeRecord = new BusinessTraveIRecord();
					overTimeRecord = outAttendanceMapper.findAttendanceRecordByBusinessTraveI_attendance_id(id);
					String statuss = "";
					if(audit_status.equals("2")){
						statuss  ="通过";
					}else if(audit_status.equals("3")){
						statuss  ="不通过";
					}
					String remarks = JSONObject.fromObject(overTimeRecord).toString()+"出差申请"+statuss+"审批人备注:"+audit_resmarks;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("提交出差审批结果处理报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 申请人撤销出差申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject submitRevokeBusinessTraveI(int id, int userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","撤销失败");
		jsonObject.put("result","fail");
		try {
			iWalkieTalkieMapper.submitRevokeBusinessTraveI(id,userId);
			jsonObject.put("msg","撤销成功");
			jsonObject.put("result","success");
			String ip = Futil.getIpAddr(request);
			String remarks = "安卓："+userId+"撤销加班"+id;
			loggerMapper.addLogger(userId,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 申请人撤销出差申请记录报错");
		}
		return jsonObject;
	}
	@Override
	public JSONObject serachUserInfoAll(){
		JSONObject jsonObject = new JSONObject();
		String bt = TimeUtils.getCurrentDateTime();
		System.out.println(bt+"系统时间开始");
		List<AllUserInfoStatus> userList = new ArrayList<AllUserInfoStatus>();
		String time = TimeUtils.getCurrentDateTime();
		try {
			userList = iWalkieTalkieMapper.findDepartmentByCompanyIds(1,time,time.substring(0, 10)+" 00:00:00",time.substring(0, 10)+" 23:59:59");
			for(AllUserInfoStatus allUserInfo : userList){
				List<UserInfoStatus> list = new ArrayList<UserInfoStatus>();
				list = allUserInfo.getUsers();
				for(UserInfoStatus user : list){
					try {
						int user_id = user.getUser_id();
						int status = 0;
						List<BusinessTraveIRecord> businessTraveIRecords = new ArrayList<BusinessTraveIRecord>();//出差
						//businessTraveIRecords = iWalkieTalkieMapper.findBusinessTraveIByUserId(user_id,time);//出差查询
						businessTraveIRecords = user.getBusinessTraveIRecords();//出差查询
						List<OutGoing> goingRecords = new ArrayList<OutGoing>();//外出
						List<AttendanceRecord> attendanceRecords = new ArrayList<AttendanceRecord>();//考勤记录表
						List<Vacation> vacationRecords = new ArrayList<Vacation>();//假期、
						List<Overtime> overtime = new ArrayList<Overtime>();//加班
						if(businessTraveIRecords.size()>0){//当出差中
							user.setStatus("出差中");
							user.setColor(color_out);
							user.setRemarks(businessTraveIRecords.get(0).getAddress()+"  ("+TimeUtils.getSimpleMonthDate(businessTraveIRecords.get(0).getStart_time())+"---"+TimeUtils.getSimpleMonthDate(businessTraveIRecords.get(0).getStop_time())
									+")");
						}else{
							//goingRecords = iWalkieTalkieMapper.findbusinessTraveIRecords(user_id,time);//外出查询
							goingRecords = user.getOutGoing();//外出
							if(goingRecords.size()>0){
								user.setStatus("外出中");
								user.setColor(color_out);
								user.setRemarks(goingRecords.get(0).getOutGoing_address()+"  ("+TimeUtils.getSimpleMonthDate(goingRecords.get(0).getOutGoing_start_time())+"---"+TimeUtils.getSimpleMonthDate(goingRecords.get(0).getOutGoing_stop_time())
										+")");
							}else {
								//vacationRecords = iWalkieTalkieMapper.findVacationByUserId(user_id,time);//假期查询
								vacationRecords = user.getVacation();//假期
								if(vacationRecords.size()>0){//当时休假中
									user.setStatus("休假中");
									user.setColor(color_vacation);
									user.setRemarks(vacationRecords.get(0).getVacation_type_name()+"  ("+TimeUtils.getSimpleMonthDate(vacationRecords.get(0).getVacation_start_time())
											+"---"+TimeUtils.getSimpleMonthDate(vacationRecords.get(0).getVacation_stop_time())+")");
								}else {
									//status = iWalkieTalkieMapper.findOvertimeByUserId(user_id,time);//加班查询
									overtime = user.getOvertime();
									if(overtime.size()>0){//当时加班中
										user.setStatus("加班中");
										user.setRemarks("加班中");
										user.setColor(color_overtime);
									}else{
										//status = iWalkieTalkieMapper.findOvertimeByUserId(user_id,time);//加班查询
										overtime = user.getOvertime();
										if(overtime.size()>0){//当时加班中
											user.setStatus("加班中");
											user.setRemarks("加班中");
											user.setColor(color_overtime);
										}else{
											//attendanceRecords = iWalkieTalkieMapper.findAttendanceByUserId(user_id,time.substring(0, 10)+" 00:00:00",time.substring(0, 10)+" 23:59:59");//考勤记录查询
											attendanceRecords = user.getAttendanceRecords();
											Collections.sort(attendanceRecords);
											user.setStatus("旷工");
											user.setColor(color_absence);
											user.setRemarks("暂无信息");
											boolean statusvaction = false;
											if(OnLineInitial.workingTime!=null){
												switch (OnLineInitial.workingTime.getStatus()) {
												case 0:
													user.setStatus("旷工");
													statusvaction = false;
													break;
												case 1:
													user.setStatus("休息日");
													user.setColor(color_vacation);
													statusvaction = true;
													break;
												case 2:
													user.setStatus("休息日");
													user.setColor(color_vacation);
													statusvaction = true;
													break;
												default:
													break;
												}
											}
											List<AttendanceRecord> attendanceRecordQIANDAO = new ArrayList<AttendanceRecord>();//每人每天的签到考勤详情
											List<AttendanceRecord> attendanceRecordQIANTUI = new ArrayList<AttendanceRecord>();//每人每天的签退考勤详情
											AttendanceRecord attendanceRecordSignIn = new AttendanceRecord();//签到
											AttendanceRecord attendanceRecordSignBack = new AttendanceRecord();//签退
											for(AttendanceRecord attendanceRecord : attendanceRecords){//遍历全部的，分为签到和签退两个list
												if(attendanceRecord.getAttendance_type()==1){
													attendanceRecordQIANDAO.add(attendanceRecord);
												}else if(attendanceRecord.getAttendance_type()==2){
													attendanceRecordQIANTUI.add(attendanceRecord);
												}
											}
											if(attendanceRecordQIANDAO.size()>0){
												attendanceRecordSignIn = attendanceRecordQIANDAO.get(0);
											}
											if(attendanceRecordQIANTUI.size()>0){
												attendanceRecordSignBack = attendanceRecordQIANTUI.get(attendanceRecordQIANTUI.size()-1);
											}
											if(attendanceRecordSignIn.getResult_id()==1){//1是签到成功
												if(statusvaction){
													user.setStatus("加班中");
													user.setRemarks("加班中");
													user.setColor(color_overtime);
												}else{
													user.setStatus("工作中");
													user.setColor(color_working);
													user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
												}
											}else if(attendanceRecordSignIn.getResult_id()==3){//3是签到迟到
												if(statusvaction){
													user.setStatus("加班中");
													user.setRemarks("加班中");
													user.setColor(color_overtime);
												}else{
													user.setStatus("迟到");
													user.setColor(color_late);
													user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
												}
											}else if(attendanceRecordSignIn.getResult_id()==7){//7是审核通过
												if(attendanceRecordSignIn.getOut_attendance()==1){//外勤
													if(statusvaction){
														user.setStatus("加班中");
														user.setRemarks("加班中");
														user.setColor(color_overtime);
													}else{
														user.setStatus("工作中");
														user.setColor(color_working);
														user.setRemarks(attendanceRecordSignIn.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
													}
												}else if(attendanceRecordSignIn.getAppeal_attendance()==1){//补卡
													if(statusvaction){
														user.setStatus("加班中");
														user.setRemarks("加班中");
														user.setColor(color_overtime);
													}else{
														if (TimeUtils.getTimeCompareSize(attendanceRecordSignIn.getAttendance_time(), attendanceRecordSignIn.getRule_time_work()) > 1) {
															// 正常签到
															user.setStatus("工作中");
															user.setColor(color_working);
															user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
														} else {
															// 迟到打卡
															user.setStatus("迟到");
															user.setColor(color_late);
															user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
														}
													}
												}
											}
											if(attendanceRecordSignBack.getResult_id()==2){//2是签退成功
												user.setStatus("休息中");
												user.setColor(color_vacation);
												user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
											}else if(attendanceRecordSignBack.getResult_id()==4){//3是早退打卡
												if(statusvaction){
													user.setStatus("休息中");
													user.setColor(color_vacation);
													user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
												}else{
													user.setStatus("早退");
													user.setColor(color_late);
													user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
												}
											}else if(attendanceRecordSignBack.getResult_id()==7){//7是审核通过
												if(attendanceRecordSignBack.getOut_attendance()==1){//外勤
													user.setStatus("休息中");
													user.setColor(color_vacation);
													user.setRemarks(attendanceRecordSignBack.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
												}else if(attendanceRecordSignBack.getAppeal_attendance()==1){//补卡
													if(statusvaction){
														user.setStatus("休息中");
														user.setColor(color_vacation);
														user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
													}else{
														if (TimeUtils.getTimeCompareSize(attendanceRecordSignBack.getAttendance_time(), attendanceRecordSignBack.getRule_time_off_work()) < 3) {
															// 正常签退
															user.setStatus("休息中");
															user.setColor(color_vacation);
															user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
														} else {
															// 早退打卡
															user.setStatus("早退");
															user.setColor(color_late);
															user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
														}
													}
													
												}
											}
//											for(AttendanceRecord attendanceRecord : attendanceRecords){
//												if(attendanceRecord.getAttendance_type()==1){//1是签到，2是签退
//													if(attendanceRecord.getResult_id()==1){//1是签到成功
//														if(statusvaction){
//															user.setStatus("加班中");
//															user.setRemarks("加班中");
//															user.setColor(color_overtime);
//														}else{
//															user.setStatus("工作中");
//															user.setColor(color_working);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}
//														break;
//													}else if(attendanceRecord.getResult_id()==3){//3是签到迟到
//														if(statusvaction){
//															user.setStatus("加班中");
//															user.setRemarks("加班中");
//															user.setColor(color_overtime);
//														}else{
//															user.setStatus("迟到");
//															user.setColor(color_late);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}
//													}else if(attendanceRecord.getResult_id()==7){//7是审核通过
//														if(attendanceRecord.getOut_attendance()==1){//外勤
//															if(statusvaction){
//																user.setStatus("加班中");
//																user.setRemarks("加班中");
//																user.setColor(color_overtime);
//															}else{
//																user.setStatus("工作中");
//																user.setColor(color_working);
//																user.setRemarks(attendanceRecord.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																break;
//															}
//														}else if(attendanceRecord.getAppeal_attendance()==1){//补卡
//															if(statusvaction){
//																user.setStatus("加班中");
//																user.setRemarks("加班中");
//																user.setColor(color_overtime);
//															}else{
//																if (TimeUtils.getTimeCompareSize(attendanceRecord.getAttendance_time(), attendanceRecord.getRule_time_work()) > 1) {
//																	// 正常签到
//																	user.setStatus("工作中");
//																	user.setColor(color_working);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																	break;
//																} else {
//																	// 迟到打卡
//																	user.setStatus("迟到");
//																	user.setColor(color_late);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																}
//															}
//														}
//													}
//												}
//											}
//											for(AttendanceRecord attendanceRecord : attendanceRecords){
//												if(attendanceRecord.getAttendance_type()==2){//1是签到，2是签退
//													if(attendanceRecord.getResult_id()==2){//2是签退成功
//														user.setStatus("休息中");
//														user.setColor(color_vacation);
//														user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														break;
//													}else if(attendanceRecord.getResult_id()==4){//3是早退打卡
//														if(statusvaction){
//															user.setStatus("休息中");
//															user.setColor(color_vacation);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}else{
//															user.setStatus("早退");
//															user.setColor(color_late);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}
//													}else if(attendanceRecord.getResult_id()==7){//7是审核通过
//														if(attendanceRecord.getOut_attendance()==1){//外勤
//															user.setStatus("休息中");
//															user.setColor(color_vacation);
//															user.setRemarks(attendanceRecord.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//															break;
//														}else if(attendanceRecord.getAppeal_attendance()==1){//补卡
//															if(statusvaction){
//																user.setStatus("休息中");
//																user.setColor(color_vacation);
//																user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//															}else{
//																if (TimeUtils.getTimeCompareSize(attendanceRecord.getAttendance_time(), attendanceRecord.getRule_time_off_work()) < 3) {
//																	// 正常签退
//																	user.setStatus("休息中");
//																	user.setColor(color_vacation);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																	break;
//																} else {
//																	// 早退打卡
//																	user.setStatus("早退");
//																	user.setColor(color_late);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																}
//															}
//															
//														}
//													}
//												}
//											}
										}
									}
								}
							
							}
						}
						} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("rows", userList);
		jsonObject.put("total", userList.size());	
		System.out.println(bt+"-------------"+TimeUtils.getCurrentDateTime()+"系统时间结束");
		return jsonObject;
	}
	/**
	 * 查询外勤详情记录
	 * @param id 主键
	 * @return
	 */
	@Override
	public OutAttendanceInfo serachOutAttendanceById(int id,int status,int userId) {
		OutAttendanceInfo outAttendanceInfoList = new OutAttendanceInfo();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			if(status==PushType.OUT_ATTENDANCE_APPLY){//外勤审批
				outAttendanceInfoList = iWalkieTalkieMapper.serachOutAttendanceById(id,userId);
			}else if(status==PushType.OUT_ATTENDANCE_APPROVAL){//外勤申请
				outAttendanceInfoList = iWalkieTalkieMapper.serachOutAttendanceByIdApproval(id);
			}
			
			if(outAttendanceInfoList!=null){
					List<OutAttendanceRecordPic> outAttendanceRecordPic = new ArrayList<OutAttendanceRecordPic>();
					outAttendanceRecordPic = outAttendanceInfoList.getOutAttendanceRecordPic();
					for(OutAttendanceRecordPic attendanceRecordPic : outAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = iWalkieTalkieMapper.findOutAttendanceInfoAuditRecord(outAttendanceInfoList.getOut_attendance_id());
					outAttendanceInfoList.setOutAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤详情记录报错");
		}
		return outAttendanceInfoList;
	}
	/**
	 * 查询假期详情记录
	 * @param id 主键
	 * @return
	 */
	@Override
	public VacationRecord serachVacationById(int id,int type,int userId) {
		VacationRecord vacationRecordList = new VacationRecord();//假期
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			if(type==PushType.VACATION_APPLY){//休假申请（审批）
				vacationRecordList = iWalkieTalkieMapper.serachVacationById(id,userId);
			}else{//休假申请（申请）
				vacationRecordList = iWalkieTalkieMapper.serachVacationByIdApproval(id);
			}
			
			if(vacationRecordList!=null){
				List<VacationRecordPic> vacationRecordPic = new ArrayList<VacationRecordPic>();
				vacationRecordPic = vacationRecordList.getVacationRecordPic();
				for(VacationRecordPic attendanceRecordPic : vacationRecordPic){
					String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
					attendanceRecordPic.setUrl(pictureUrl);
				}
				outAttendanceInfoAuditRecord = outAttendanceMapper.findvacationauditRecord(vacationRecordList.getId());
				vacationRecordList.setVacationAuditRecord(outAttendanceInfoAuditRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询假期详情记录报错");
		}
		return vacationRecordList;
	}
	/**
	 * 查询加班详情记录
	 * @param id 主键
	 * @return
	 */
	@Override
	public OverTimeRecord serachOvertimeById(int id,int type,int userId) {
		OverTimeRecord overTimeRecordList = new OverTimeRecord();//加班
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			if(type==PushType.OVERTIME_APPLY){//加班详情（审批）
				overTimeRecordList = iWalkieTalkieMapper.serachOvertimeById(id,userId);
			}else{//加班详情（申请）
				overTimeRecordList = iWalkieTalkieMapper.serachOvertimeByIdApproval(id);
			}
			
			if(overTimeRecordList!=null){
				List<OverTimeRecordPic> overTimeRecordPic = new ArrayList<OverTimeRecordPic>();
				overTimeRecordPic = overTimeRecordList.getOverTimeRecordPic();
				for(OverTimeRecordPic attendanceRecordPic : overTimeRecordPic){
					String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
					attendanceRecordPic.setUrl(pictureUrl);
				}
				outAttendanceInfoAuditRecord = outAttendanceMapper.findoverTimeauditRecord(overTimeRecordList.getId());
				overTimeRecordList.setOverTimeAuditRecord(outAttendanceInfoAuditRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询加班详情记录报错");
		}
		return overTimeRecordList;
	}
	/**
	 * 查询外出详情记录
	 * @param id 主键
	 * @return
	 */
	@Override
	public OutGoingRecord serachOutGoingById(int id,int type,int userId) {
		OutGoingRecord outGoingRecordList = new OutGoingRecord();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			if(type==PushType.GO_OUT_APPLY){//外出详情（审批）
				outGoingRecordList = iWalkieTalkieMapper.serachOutGoingById(id,userId);
			}else{//外出详情（申请）
				outGoingRecordList = iWalkieTalkieMapper.serachOutGoingByIdApproval(id);
			}
			
			if(outGoingRecordList!=null){
					List<OutGoingRecordPic> outGoingRecordPic = new ArrayList<OutGoingRecordPic>();
					outGoingRecordPic = outGoingRecordList.getOutGoingRecordPic();
					for(OutGoingRecordPic attendanceRecordPic : outGoingRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoutGoingauditRecord(outGoingRecordList.getId());
					outGoingRecordList.setOutGoingAuditRecord(outAttendanceInfoAuditRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外出详情记录报错");
		}
		return outGoingRecordList;
	}
	/**
	 * 查询出差详情记录
	 * @param id 主键
	 * @return
	 */
	@Override
	public BusinessTraveIRecord serachBusinessTraveIById(int id,int type,int userId) {
		BusinessTraveIRecord businessTraveIRecordList = new BusinessTraveIRecord();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			if(type==PushType.BUSINESS_TRIP_APPLY){//出差详情（审批）
				businessTraveIRecordList = iWalkieTalkieMapper.serachBusinessTraveIById(id,userId);
			}else{//出差详情（申请）
				businessTraveIRecordList = iWalkieTalkieMapper.serachBusinessTraveIByIdApproval(id);
			}
			
			if(businessTraveIRecordList!=null){
					List<BusinessTraveIRecordPic> businessTraveIRecordPic = new ArrayList<BusinessTraveIRecordPic>();
					businessTraveIRecordPic = businessTraveIRecordList.getBusinessTraveIRecordPic();
					for(BusinessTraveIRecordPic attendanceRecordPic : businessTraveIRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findbusinessTraveIauditRecord(businessTraveIRecordList.getId());
					businessTraveIRecordList.setBusinessTraveIAuditRecord(outAttendanceInfoAuditRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询出差详情记录报错");
		}
		return businessTraveIRecordList;
	}
	/**
	 * 查询补卡详情记录
	 * @param id 主键
	 * @return
	 */
	@Override
	public AppealAttendanceInfo serachAppealAttendanceById(int id,int type,int userId) {
		AppealAttendanceInfo appealAttendanceInfoList = new AppealAttendanceInfo();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			if(type==PushType.ATTENDANCE_APPEAL_APPLY){//考勤补卡（审批）
				appealAttendanceInfoList = iWalkieTalkieMapper.serachAppealAttendanceById(id,userId);
			}else{//考勤补卡（申请）
				appealAttendanceInfoList = iWalkieTalkieMapper.serachAppealAttendanceByIdApproval(id);
			}
			
			if(appealAttendanceInfoList!=null){
					List<AppealAttendanceRecordPic> appealAttendanceRecordPic = new ArrayList<AppealAttendanceRecordPic>();
					appealAttendanceRecordPic = appealAttendanceInfoList.getAppealAttendanceRecordPic();
					for(AppealAttendanceRecordPic attendanceRecordPic : appealAttendanceRecordPic){
						String pictureUrl = attendanceRecordPic.getUrl().split("webapps")[1];
						attendanceRecordPic.setUrl(pictureUrl);
					}
					outAttendanceInfoAuditRecord = outAttendanceMapper.findAppealRecordInfoAuditRecord(appealAttendanceInfoList.getAppeal_attendance_id());
					appealAttendanceInfoList.setAppealAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询补卡需要审批信息报错");
		}
		return appealAttendanceInfoList;
	}

	@Override
	public List<AllUserInfoStatus> getEmployeeStatus(int companyId) {
		String bt = TimeUtils.getCurrentDateTime();
		System.out.println(bt+"系统时间开始");
		List<AllUserInfoStatus> userList = new ArrayList<AllUserInfoStatus>();
		String time = TimeUtils.getCurrentDateTime();
		try {
			userList = iWalkieTalkieMapper.findDepartmentByCompanyIds(1,time,time.substring(0, 10)+" 00:00:00",time.substring(0, 10)+" 23:59:59");
			for(AllUserInfoStatus allUserInfo : userList){
				List<UserInfoStatus> list = new ArrayList<UserInfoStatus>();
				list = allUserInfo.getUsers();
				for(UserInfoStatus user : list){
					try {
						int user_id = user.getUser_id();
						int status = 0;
						List<BusinessTraveIRecord> businessTraveIRecords = new ArrayList<BusinessTraveIRecord>();//出差
						//businessTraveIRecords = iWalkieTalkieMapper.findBusinessTraveIByUserId(user_id,time);//出差查询
						businessTraveIRecords = user.getBusinessTraveIRecords();//出差查询
						List<OutGoing> goingRecords = new ArrayList<OutGoing>();//外出
						List<AttendanceRecord> attendanceRecords = new ArrayList<AttendanceRecord>();//考勤记录表
						List<Vacation> vacationRecords = new ArrayList<Vacation>();//假期、
						List<Overtime> overtime = new ArrayList<Overtime>();//加班
						if(businessTraveIRecords.size()>0){//当出差中
							user.setStatus("出差中");
							user.setColor(color_out);
							user.setRemarks(businessTraveIRecords.get(0).getAddress()+"  ("+TimeUtils.getSimpleMonthDate(businessTraveIRecords.get(0).getStart_time())+"---"+TimeUtils.getSimpleMonthDate(businessTraveIRecords.get(0).getStop_time())
									+")");
						}else{
							//goingRecords = iWalkieTalkieMapper.findbusinessTraveIRecords(user_id,time);//外出查询
							goingRecords = user.getOutGoing();//外出
							if(goingRecords.size()>0){
								user.setStatus("外出中");
								user.setColor(color_out);
								user.setRemarks(goingRecords.get(0).getOutGoing_address()+"  ("+TimeUtils.getSimpleMonthDate(goingRecords.get(0).getOutGoing_start_time())+"---"+TimeUtils.getSimpleMonthDate(goingRecords.get(0).getOutGoing_stop_time())
										+")");
							}else {
								//vacationRecords = iWalkieTalkieMapper.findVacationByUserId(user_id,time);//假期查询
								vacationRecords = user.getVacation();//假期
								if(vacationRecords.size()>0){//当时休假中
									user.setStatus("休假中");
									user.setColor(color_vacation);
									user.setRemarks(vacationRecords.get(0).getVacation_type_name()+"  ("+TimeUtils.getSimpleMonthDate(vacationRecords.get(0).getVacation_start_time())
											+"---"+TimeUtils.getSimpleMonthDate(vacationRecords.get(0).getVacation_stop_time())+")");
								}else {
									//status = iWalkieTalkieMapper.findOvertimeByUserId(user_id,time);//加班查询
									overtime = user.getOvertime();
									if(overtime.size()>0){//当时加班中
										user.setStatus("加班中");
										user.setRemarks("加班中");
										user.setColor(color_overtime);
									}else{
										//status = iWalkieTalkieMapper.findOvertimeByUserId(user_id,time);//加班查询
										overtime = user.getOvertime();
										if(overtime.size()>0){//当时加班中
											user.setStatus("加班中");
											user.setRemarks("加班中");
											user.setColor(color_overtime);
										}else{
											//attendanceRecords = iWalkieTalkieMapper.findAttendanceByUserId(user_id,time.substring(0, 10)+" 00:00:00",time.substring(0, 10)+" 23:59:59");//考勤记录查询
											attendanceRecords = user.getAttendanceRecords();
											Collections.sort(attendanceRecords);
											user.setStatus("旷工");
											user.setColor(color_absence);
											user.setRemarks("暂无信息");
											boolean statusvaction = false;
											if(OnLineInitial.workingTime!=null){
												switch (OnLineInitial.workingTime.getStatus()) {
												case 0:
													user.setStatus("旷工");
													statusvaction = false;
													break;
												case 1:
													user.setStatus("休息日");
													user.setColor(color_vacation);
													statusvaction = true;
													break;
												case 2:
													user.setStatus("休息日");
													user.setColor(color_vacation);
													statusvaction = true;
													break;
												default:
													break;
												}
											}
											List<AttendanceRecord> attendanceRecordQIANDAO = new ArrayList<AttendanceRecord>();//每人每天的签到考勤详情
											List<AttendanceRecord> attendanceRecordQIANTUI = new ArrayList<AttendanceRecord>();//每人每天的签退考勤详情
											AttendanceRecord attendanceRecordSignIn = new AttendanceRecord();//签到
											AttendanceRecord attendanceRecordSignBack = new AttendanceRecord();//签退
											for(AttendanceRecord attendanceRecord : attendanceRecords){//遍历全部的，分为签到和签退两个list
												if(attendanceRecord.getAttendance_type()==1){
													attendanceRecordQIANDAO.add(attendanceRecord);
												}else if(attendanceRecord.getAttendance_type()==2){
													attendanceRecordQIANTUI.add(attendanceRecord);
												}
											}
											if(attendanceRecordQIANDAO.size()>0){
												attendanceRecordSignIn = attendanceRecordQIANDAO.get(0);
											}
											if(attendanceRecordQIANTUI.size()>0){
												attendanceRecordSignBack = attendanceRecordQIANTUI.get(attendanceRecordQIANTUI.size()-1);
											}
											if(attendanceRecordSignIn.getResult_id()==1){//1是签到成功
												if(statusvaction){
													user.setStatus("加班中");
													user.setRemarks("加班中");
													user.setColor(color_overtime);
												}else{
													user.setStatus("工作中");
													user.setColor(color_working);
													user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
												}
											}else if(attendanceRecordSignIn.getResult_id()==3){//3是签到迟到
												if(statusvaction){
													user.setStatus("加班中");
													user.setRemarks("加班中");
													user.setColor(color_overtime);
												}else{
													user.setStatus("迟到");
													user.setColor(color_late);
													user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
												}
											}else if(attendanceRecordSignIn.getResult_id()==7){//7是审核通过
												if(attendanceRecordSignIn.getOut_attendance()==1){//外勤
													if(statusvaction){
														user.setStatus("加班中");
														user.setRemarks("加班中");
														user.setColor(color_overtime);
													}else{
														user.setStatus("工作中");
														user.setColor(color_working);
														user.setRemarks(attendanceRecordSignIn.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
													}
												}else if(attendanceRecordSignIn.getAppeal_attendance()==1){//补卡
													if(statusvaction){
														user.setStatus("加班中");
														user.setRemarks("加班中");
														user.setColor(color_overtime);
													}else{
														if (TimeUtils.getTimeCompareSize(attendanceRecordSignIn.getAttendance_time(), attendanceRecordSignIn.getRule_time_work()) > 1) {
															// 正常签到
															user.setStatus("工作中");
															user.setColor(color_working);
															user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
														} else {
															// 迟到打卡
															user.setStatus("迟到");
															user.setColor(color_late);
															user.setRemarks(attendanceRecordSignIn.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignIn.getAttendance_time().substring(0, 19))+")");
														}
													}
												}
											}
											if(attendanceRecordSignBack.getResult_id()==2){//2是签退成功
												user.setStatus("休息中");
												user.setColor(color_vacation);
												user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
											}else if(attendanceRecordSignBack.getResult_id()==4){//3是早退打卡
												if(statusvaction){
													user.setStatus("休息中");
													user.setColor(color_vacation);
													user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
												}else{
													user.setStatus("早退");
													user.setColor(color_late);
													user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
												}
											}else if(attendanceRecordSignBack.getResult_id()==7){//7是审核通过
												if(attendanceRecordSignBack.getOut_attendance()==1){//外勤
													user.setStatus("休息中");
													user.setColor(color_vacation);
													user.setRemarks(attendanceRecordSignBack.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
												}else if(attendanceRecordSignBack.getAppeal_attendance()==1){//补卡
													if(statusvaction){
														user.setStatus("休息中");
														user.setColor(color_vacation);
														user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
													}else{
														if (TimeUtils.getTimeCompareSize(attendanceRecordSignBack.getAttendance_time(), attendanceRecordSignBack.getRule_time_off_work()) < 3) {
															// 正常签退
															user.setStatus("休息中");
															user.setColor(color_vacation);
															user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
														} else {
															// 早退打卡
															user.setStatus("早退");
															user.setColor(color_late);
															user.setRemarks(attendanceRecordSignBack.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecordSignBack.getAttendance_time().substring(0, 19))+")");
														}
													}
													
												}
											}
//											for(AttendanceRecord attendanceRecord : attendanceRecords){
//												if(attendanceRecord.getAttendance_type()==1){//1是签到，2是签退
//													if(attendanceRecord.getResult_id()==1){//1是签到成功
//														if(statusvaction){
//															user.setStatus("加班中");
//															user.setRemarks("加班中");
//															user.setColor(color_overtime);
//														}else{
//															user.setStatus("工作中");
//															user.setColor(color_working);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}
//														break;
//													}else if(attendanceRecord.getResult_id()==3){//3是签到迟到
//														if(statusvaction){
//															user.setStatus("加班中");
//															user.setRemarks("加班中");
//															user.setColor(color_overtime);
//														}else{
//															user.setStatus("迟到");
//															user.setColor(color_late);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}
//													}else if(attendanceRecord.getResult_id()==7){//7是审核通过
//														if(attendanceRecord.getOut_attendance()==1){//外勤
//															if(statusvaction){
//																user.setStatus("加班中");
//																user.setRemarks("加班中");
//																user.setColor(color_overtime);
//															}else{
//																user.setStatus("工作中");
//																user.setColor(color_working);
//																user.setRemarks(attendanceRecord.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																break;
//															}
//														}else if(attendanceRecord.getAppeal_attendance()==1){//补卡
//															if(statusvaction){
//																user.setStatus("加班中");
//																user.setRemarks("加班中");
//																user.setColor(color_overtime);
//															}else{
//																if (TimeUtils.getTimeCompareSize(attendanceRecord.getAttendance_time(), attendanceRecord.getRule_time_work()) > 1) {
//																	// 正常签到
//																	user.setStatus("工作中");
//																	user.setColor(color_working);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																	break;
//																} else {
//																	// 迟到打卡
//																	user.setStatus("迟到");
//																	user.setColor(color_late);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																}
//															}
//														}
//													}
//												}
//											}
//											for(AttendanceRecord attendanceRecord : attendanceRecords){
//												if(attendanceRecord.getAttendance_type()==2){//1是签到，2是签退
//													if(attendanceRecord.getResult_id()==2){//2是签退成功
//														user.setStatus("休息中");
//														user.setColor(color_vacation);
//														user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														break;
//													}else if(attendanceRecord.getResult_id()==4){//3是早退打卡
//														if(statusvaction){
//															user.setStatus("休息中");
//															user.setColor(color_vacation);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}else{
//															user.setStatus("早退");
//															user.setColor(color_late);
//															user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//														}
//													}else if(attendanceRecord.getResult_id()==7){//7是审核通过
//														if(attendanceRecord.getOut_attendance()==1){//外勤
//															user.setStatus("休息中");
//															user.setColor(color_vacation);
//															user.setRemarks(attendanceRecord.getAttendance_address()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//															break;
//														}else if(attendanceRecord.getAppeal_attendance()==1){//补卡
//															if(statusvaction){
//																user.setStatus("休息中");
//																user.setColor(color_vacation);
//																user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//															}else{
//																if (TimeUtils.getTimeCompareSize(attendanceRecord.getAttendance_time(), attendanceRecord.getRule_time_off_work()) < 3) {
//																	// 正常签退
//																	user.setStatus("休息中");
//																	user.setColor(color_vacation);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																	break;
//																} else {
//																	// 早退打卡
//																	user.setStatus("早退");
//																	user.setColor(color_late);
//																	user.setRemarks(attendanceRecord.getRule_name()+"   ("+TimeUtils.getSimpleMonthDate(attendanceRecord.getAttendance_time().substring(0, 19))+")");
//																}
//															}
//															
//														}
//													}
//												}
//											}
										}
									}
								}
							
							}
						}
						} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(bt+"-------------"+TimeUtils.getCurrentDateTime()+"系统时间结束");
		return userList;
	}

	@Override
	public List<VersionInfo> getNewVersionUpdateLog(int apkTypeId) {
		List<VersionInfo> list = new ArrayList<VersionInfo>();
		list = versionMapper.getNewVersionUpdateLog(apkTypeId);
		return list;
	}

	@Override
	public List<HomePagePicture> searchHomePagePicture(int companyId) {
		List<HomePagePicture> homePagePictures = new ArrayList<HomePagePicture>();
		try {
			homePagePictures = iWalkieTalkieMapper.searchHomePagePicture(companyId);
			for(HomePagePicture homePagePicture : homePagePictures){
				if(homePagePicture.getState()==0){
					if(StringUtils.isNotBlank(homePagePicture.getAddressUrl())){
						homePagePicture.setAddressUrl(homePagePicture.getAddressUrl().split("webapps")[1]);
					}
				}
				homePagePicture.setPictureUrl(homePagePicture.getPictureUrl().split("webapps")[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return homePagePictures;
	}
	/**
	 * 存储人脸
	 */
	@Override
	public int AddFace(Face face) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.AddFace(face);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 查询openid绑定用户
	 */
	@Override
	public List<User> searchOpenId(String openId) {
		List<User> userList = new ArrayList<User>();
		try {
			userList = iWalkieTalkieMapper.searchOpenId(openId);
			for(User user: userList){
				if(StringUtils.isNotBlank(user.getDepartment_id())){
					String department = accountInfoMapper.findDepartmentName(user.getDepartment_id());
					if(StringUtils.isNotBlank(department)){
						department = department.substring(1, department.length());
						List<String> result = Arrays.asList(user.getDepartment_id().split(","));
						if(result.size()>1){
							if(result.get(0).equals(result.get(1))){
								department = department+","+department;
							}
						}
						user.setDepartment(department);
					}
				}
				
				if(StringUtils.isNotBlank(user.getPosition_id())){
					String position = accountInfoMapper.findPositionName(user.getPosition_id());
					if(StringUtils.isNotBlank(position)){
						position = position.substring(1, position.length());
						user.setPosition(position);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
	/**
	 * 用户绑定openiD
	 */
	@Override
	public int bindingOpenId(String openId, int userId) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.bindingOpenId(openId,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 查询人脸识别
	 */
	@Override
	public List<Face> searchFace(int userId) {
		List<Face> face = new ArrayList<Face>();
		try {
			face = iWalkieTalkieMapper.searchFace(userId);
			for(Face faces : face){
				if(StringUtils.isNotBlank(faces.getUrl())){
					faces.setUrl(faces.getUrl().split("webapps")[1]);
				}
				faces.setCreateTime(TimeUtils.toYYYYMMDDHHMMSS(faces.getCreateTime()));
				faces.setStartDate(TimeUtils.toYYYYMMDD(faces.getStartDate()));
				faces.setEndDate(TimeUtils.toYYYYMMDD(faces.getEndDate()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return face;
	}
	/**
	 * 取消关注
	 */
	@Override
	public void unSubscribe(String openId) {
		try {
			iWalkieTalkieMapper.unSubscribe(openId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 查询全部的人脸
	 */
	@Override
	public List<Face> searchAllFace() {
		List<Face> face = new ArrayList<Face>();
		try {
			face = iWalkieTalkieMapper.searchAllFace();
			for(Face faces : face){
				if(StringUtils.isNotBlank(faces.getUrl())){
					faces.setUrl(faces.getUrl().split("webapps")[1]);
				}
				faces.setCreateTime(TimeUtils.toYYYYMMDDHHMMSS(faces.getCreateTime()));
				faces.setStartDate(TimeUtils.toYYYYMMDD(faces.getStartDate()));
				faces.setEndDate(TimeUtils.toYYYYMMDD(faces.getEndDate()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return face;
	}
	/**
	 * 存人脸记录
	 */
	@Override
	public int addFaceRecord(String fileDirPath, int userId,String createTime,String remarks,String user_name) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.addFaceRecord(fileDirPath,userId,createTime,remarks,user_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 查询人脸图片的个数
	 */
	@Override
	public int findFaceCountByUserId(int user_id) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.findFaceCountByUserId(user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 删除人脸图片
	 */
	@Override
	public int deleteFace(int id) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.deleteFace(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 人脸终端查询所有人和每个人图片的个数
	 */
	@Override
	public List<AllUserInfo> serachAllFaceOfTerminal(int companyId) {
		List<AllUserInfo> userList = new ArrayList<>();
		try {
			userList = iWalkieTalkieMapper.serachAllFaceOfTerminal(companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
	/**
	 *  查询访客userid的末次
	 */
	@Override
	public int getVisitorInfoUserId() {
		int userId = 0;
		try {
			userId = iWalkieTalkieMapper.getVisitorInfoUserId();
			userId++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}
	/**
	 * 新增访客的基本信息
	 */
	@Override
	public void addVisitorInfo(VisitorInfo info) {
		iWalkieTalkieMapper.addVisitorInfo(info);
	}
	/**
	 * 查询全部的访客信息
	 */
	@Override
	public List<VisitorInfo> serachVisitorInfoAll(int company_id) {
		List<VisitorInfo> infos = new ArrayList<>();
		try {
			infos = iWalkieTalkieMapper.serachVisitorInfoAll(company_id);
			for(VisitorInfo info : infos){
				if(StringUtils.isNotBlank(info.getUrl())){
					String pictureUrl = info.getUrl().split("webapps")[1];
					info.setUrl(pictureUrl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	/**
	 * 修改访客的基本信息
	 */
	@Override
	public int updateVisitorInfo(VisitorInfo info) {
		int i = 0;
		i = iWalkieTalkieMapper.updateVisitorInfo(info);
		return i;
	}
	/**
	 * 删除访客的基本信息
	 */
	@Override
	public int deleteVisitorInfo(int user_id) {
		int i = 0;
		i = iWalkieTalkieMapper.deleteVisitorInfo(user_id);
		return i;
	}
	/**
	 * 删除访客人脸库的信息
	 */
	@Override
	public void deleteVisitorFace(int user_id) {
		iWalkieTalkieMapper.deleteVisitorFace(user_id);
	}
	/**
	 * 修改face的有效期时间
	 */
	@Override
	public int updateFace(Face info) {
		int i = 0;
		i = iWalkieTalkieMapper.updateFace(info);
		return i;
	}
	/**
	 * 查询访客的所有预约
	 */
	@Override
	public List<VisitorSubscribe> searchVisitorSubscribe(int user_id) {
		List<VisitorSubscribe> list = new ArrayList<VisitorSubscribe>();
		try {
			list = iWalkieTalkieMapper.searchVisitorSubscribe(user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 查询访客的人脸信息
	 */
	@Override
	public VisitorInfo findVistorInfo(int user_id) {
		VisitorInfo info = new VisitorInfo();
		try {
			info = iWalkieTalkieMapper.findVistorInfo(user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	/**
	 * 新增访客预约
	 */
	@Override
	public int addVisitorSubscribe(VisitorSubscribe visitorSubscribe) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.addVisitorSubscribe(visitorSubscribe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 新增修改访客人脸
	 */
	@Override
	public int addVisitorSubscribeFace(String fileDirPath, int userId,byte[] faceFeature) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.addVisitorSubscribeFace(fileDirPath,userId,faceFeature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Face findface(int id) {
		Face face = new Face();
		try {
			face = iWalkieTalkieMapper.findface(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return face;
	}
	/**
	 * 修改人脸的状态
	 */
	@Override
	public void updateFaceStatus(int id) {
		iWalkieTalkieMapper.updateFaceStatus(id);
	}

	@Override
	public List<VisitorInfo> searchVisitorOpenId(String openId) {
		List<VisitorInfo> infos = new ArrayList<VisitorInfo>();
			try {
				infos = iWalkieTalkieMapper.searchVisitorOpenId(openId);
				for(VisitorInfo info : infos){
					String pictureUrl = info.getUrl().split("webapps")[1];
					info.setUrl(pictureUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return infos;
	}
	/**
	 * 查询全部没有处理过的人脸
	 */
	@Override
	public List<Face> searchAllFaceUntreated() {
		List<Face> facList = new ArrayList<Face>();
		try {
			facList = iWalkieTalkieMapper.searchAllFaceUntreated();
			for(Face info : facList){
				String pictureUrl = info.getUrl().split("webapps")[1];
				info.setUrl(pictureUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return facList;
	}
	/**
	 * 查询一整年的假期情况
	 */
	@Override
	public List<WorkingTime> searchWorkingTimeByYear(String year,int companyId) {
		List<WorkingTime> workingTimes = new ArrayList<>();
		try {
			workingTimes = iWalkieTalkieMapper.searchWorkingTimeByYear(year,companyId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workingTimes;
	}
	/**
	 * 新增人脸机打卡
	 */
	@Override
	public void addAttendanceFaceRecord(AttendanceFaceRecord record) {
		iWalkieTalkieMapper.addAttendanceFaceRecord(record);
	}
	/**
	 * 撤销预约
	 */
	@Override
	public int deleteVisitorSubscribe(int id) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.deleteVisitorSubscribe(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 修改访客预约
	 */
	@Override
	public int updateVisitorSubscribe(VisitorSubscribe visitorSubscribe) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.updateVisitorSubscribe(visitorSubscribe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 查询人脸进出情况
	 */
	@Override
	public List<FaceRecord> searchFaceRecord(int userId, String startTime, String endTime) {
		List<FaceRecord> faceRecords = new ArrayList<FaceRecord>();
		try {
			faceRecords = iWalkieTalkieMapper.searchFaceRecord(userId,startTime,endTime);
			for(FaceRecord faceRecord : faceRecords){
				if(StringUtils.isNotBlank(faceRecord.getUrl())){
					String pictureUrl = faceRecord.getUrl().split("webapps")[1];
					faceRecord.setUrl(pictureUrl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return faceRecords;
	}
	/**
	 * 删除人脸信息
	 */
	@Override
	public int deleteFaceTrue(int id) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.deleteFaceTrue(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 修改访客没有特征值
	 */
	@Override
	public int updateVisitorInfoFaceFeature(int userId, byte[] faceFeature) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.updateVisitorInfoFaceFeature(userId,faceFeature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 新增开关门记录
	 */
	@Override
	public int openAndCloseDoorRecord(OpenAndCloseDoorRecord openAndCloseDoorRecord) {
		int i = 0;
		try {
			i = iWalkieTalkieMapper.openAndCloseDoorRecord(openAndCloseDoorRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public void inserAddface(Face face) {
		iWalkieTalkieMapper.inserAddface(face);
	}
	/**
	 * 查询最新版本
	 */
	@Override
	public VersionInfo searchNewVersion(int apkTypeId) {
		VersionInfo info = new VersionInfo();
		try {
			info = iWalkieTalkieMapper.searchNewVersion(apkTypeId);
			info.setVersionFileUrl(info.getVersionFileUrl().split("webapps")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	/***
	 * 小程序登录接口
	 */
	@Override
	public User serachUserByUserAndPasswork(String userId, String userPwd) {
		User user = new User();
		try {
			int user_id = -1;
			try {
				if (userId.length() == 8) {
					user_id = Integer.valueOf(userId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user_id != -1) {
				user = iWalkieTalkieMapper.loginUserByUserId(userId, userPwd);
			} else {
				user = iWalkieTalkieMapper.searchUserByOthers(userId, userPwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 小程序跟新openid
	 */
	@Override
	public void adduserInfoByOpenId(String unionid ,String user_id, String openId) {
		try {
			if(user_id.length()== 8){
				iWalkieTalkieMapper.adduserInfoByOpenId(unionid,user_id,openId);
			}else{
				iWalkieTalkieMapper.adduserInfoByOthers(unionid,user_id,openId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("更新小程序openid报错");
		}
	}

	@Override
	public void updateUnoinid(String openId, String unionid) {
		try {
			iWalkieTalkieMapper.updateUnoinid(unionid,openId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("更新小程序unionid报错");
		}
	}

}
