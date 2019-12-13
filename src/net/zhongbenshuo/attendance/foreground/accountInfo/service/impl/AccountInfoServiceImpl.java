package net.zhongbenshuo.attendance.foreground.accountInfo.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import net.zhongbenshuo.attendance.bean.Department;
import net.zhongbenshuo.attendance.bean.Position;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.Authority;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.PositionUser;
import net.zhongbenshuo.attendance.foreground.accountInfo.mapper.AccountInfoMapper;
import net.zhongbenshuo.attendance.foreground.accountInfo.service.AccountInfoService;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;
import net.zhongbenshuo.attendance.mapper.IAttendanceMapper;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.mapper.LoginMapper;
import net.zhongbenshuo.attendance.service.IUserService;
import net.zhongbenshuo.attendance.utils.AesCBC;
import net.zhongbenshuo.attendance.utils.Encrypt;
import net.zhongbenshuo.attendance.utils.Futil;
import net.zhongbenshuo.attendance.utils.HttpClientUtil;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
	public static Logger logger = LogManager.getLogger(AccountInfoServiceImpl.class);
	
	@Autowired
	AccountInfoMapper accountInfoMapper;
	
	@Autowired
	private IAttendanceMapper iWalkieTalkieMapper;
	
	@Autowired
	private  IUserService iuserService;
	
	@Autowired
	LoggerMapper loggerMapper;
	
	@Autowired
	LoginMapper  loginMapper;
	/***
	 * 查询公司下的权限信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject findAuthority(String company_id) {
		JSONObject jsonObject = new JSONObject();
		List<Authority> authorityList = new ArrayList<Authority>();
		try {
			authorityList = accountInfoMapper.findAuthority(company_id);
			jsonObject.put("rows", authorityList);
			jsonObject.put("total", authorityList.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("查询公司下权限信息报错"+e);
		}
		return jsonObject;
	
	}
	/***
	 * 新增/修改公司下权限
	 * @param company_id
	 * @param authority_id
	 * @param authority_describe_old
	 * @param authority_code_old
	 * @param authority_describe
	 * @param authority_code
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitAuthorityAdd(String company_id, String authority_id, String authority_describe_old,
			 String authority_describe,  HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isNotBlank(authority_id)){
			jsonObject.put("msg","修改失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = accountInfoMapper.updateAuthority(authority_id,authority_describe);
				 if(i>0){
						jsonObject.put("msg","修改成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "把权限"+authority_describe_old+"改成"+authority_describe+";";
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("修改公司信息报错"+e);
			}
			
		}else{
			jsonObject.put("msg","新增失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = accountInfoMapper.addAuthority(company_id,authority_describe);
				 if(i>0){
						jsonObject.put("msg","新增成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "新增权限"+authority_describe;
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("新增公司信息报错"+e);
			}
		}
		return jsonObject;
	
	}
	/**
	 * 物理删除权限信息
	 * @param authority_id
	 * @param authority_describe
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject deleteAuthority(String authority_id, String authority_describe, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","删除失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = accountInfoMapper.deleteAuthority(authority_id);
			 if(i>0){
					jsonObject.put("msg","删除成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把"+authority_describe+"删除";
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("删除权限信息报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 查询账号信息
	 * @param company_id
	 * @param user_id
	 * @param phone_number
	 * @param user_name
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject findAccountInfo(String company_id, String user_id, String phone_number, String user_name,int role,int id,String jobNumber,int bNum,int rows) {
		JSONObject jsonObject = new JSONObject();
		List<User> userList = new ArrayList<User>();
		
		List<Authority> authorityList = new ArrayList<Authority>();
		Map<Integer,String> authorityMap = new HashMap<Integer, String>();
		
		
		List<Department> departmentList = new ArrayList<Department>();
		Map<Integer,String> departmentMap = new HashMap<Integer, String>();
		
		List<Position> positionList = new ArrayList<Position>();
		Map<Integer,String> positionMap = new HashMap<Integer, String>();
		try {
			userList = accountInfoMapper.findAccountInfo(company_id,user_id,phone_number,user_name,role,id,jobNumber,bNum,rows);
			
			authorityList = iWalkieTalkieMapper.findAuthorityList(Integer.valueOf(company_id));//查询全部的权限
			authorityList.forEach(xx ->authorityMap.put(xx.id, xx.authority_describe));
			
			departmentList = iWalkieTalkieMapper.findDepartmentList(Integer.valueOf(company_id));//查询全部的部门
			departmentList.forEach(xx ->departmentMap.put(xx.department_id, xx.department));
			
			positionList = iWalkieTalkieMapper.findPositionList(Integer.valueOf(company_id));//查询全部的职位
			positionList.forEach(xx ->positionMap.put(xx.position_id, xx.position));
			int size = 0;
			if(userList!=null){
				if(userList.size()>0){
					size = userList.get(0).getSize();
				}
				for(User user : userList){
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
					}
					
				}
			}
			jsonObject.put("rows", userList);
			jsonObject.put("total",size);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("查询账号信息报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 根据公司ID查询部门信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public String findDepartment(String company_id) {
		List<Combox> comboxList = new ArrayList<Combox>();
		comboxList = accountInfoMapper.findDepartment(company_id);
		return JSON.toJSONString(comboxList);
	}
	/***
	 * 根据部门ID查询职位信息
	 * @param department_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public String findPosition(String department_id) {
		
		List<Combox> comboxList = new ArrayList<Combox>();
		comboxList = accountInfoMapper.findPosition(department_id);
		return JSON.toJSONString(comboxList);
	}
	/***
	 * 查询最大的user_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public String findUser_Id() {
		String res = "10000001";
			res = accountInfoMapper.findUser_Id();
		return res;
	}
	/***
	 * 新增/修改账号信息
	 * @param accountInfo_id
	 * @param company_id
	 * @param department
	 * @param position
	 * @param department1
	 * @param position1
	 * @param user_name
	 * @param user_pwd
	 * @param gender
	 * @param phone_number
	 * @param mail_address
	 * @param contact_address
	 * @param emergency_contact_name
	 * @param emergency_contact_phone
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitAccountInfoAdd(String accountInfo_id,String user_id, String company_id, String department, String position,
			String department1, String position1, String user_name, String user_pwd, String gender, String phone_number,
			String mail_address, String contact_address, String emergency_contact_name, String emergency_contact_phone,String role,String oldData,
			String job_number,String entry_time,
			HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String ip = Futil.getIpAddr(request);
		try {
			user_pwd = Encrypt.aesEncrypt(user_pwd,Encrypt.KEY);
		} catch (Exception e1) {
			e1.printStackTrace();
		}//aes密码加密
		if(StringUtils.isNotBlank(accountInfo_id)){
			jsonObject.put("msg","修改失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				int index = 0;
				index = accountInfoMapper.findJobNumberCount(job_number,accountInfo_id,company_id);
				if(index>0){
					jsonObject.put("msg","工号已存在！");
					jsonObject.put("result","fail");
					return jsonObject;
				}
				String departmentList = department;
				if(StringUtils.isNotBlank(department1)){
					departmentList= departmentList+","+department1;
				}
				List<String> positionUser = new ArrayList<String>();
				positionUser.add(position);
				String positionList = position;
				if(StringUtils.isNotBlank(position1)){
					positionList= positionList+","+position1;
					positionUser.add(position1);
				}
				 i = accountInfoMapper.updateAccountInfo(job_number,accountInfo_id,user_name,gender,phone_number,mail_address,contact_address,emergency_contact_name,
							emergency_contact_phone,departmentList,positionList,role,entry_time);
				 if(i>0){
					 List<String> positionUserAdd = new ArrayList<String>();//需要新增的
					 List<String> positionUserDelete = new ArrayList<String>();//需要删除的
					 
					 List<PositionUser> positionUsers = new ArrayList<PositionUser>();
					 positionUsers = accountInfoMapper.findPositionUser(user_id);
						 for(PositionUser positionUser2 : positionUsers){//原有的
							 boolean exist = false;
							 for(String string : positionUser){//现有的
							 		if(positionUser2.getPosition_id()==Integer.valueOf(string)){//如果原有的和现在的相同，不处理
							 			exist = true;
							 			break;
							 		}
							 }
							 if(!exist){
								 positionUserDelete.add(String.valueOf(positionUser2.getId()));
							 }
						 }
						 try {
							 if(positionUserDelete.size()>0){
								 accountInfoMapper.deletePositionUser(positionUserDelete);
							 }
						} catch (Exception e) {
						}
						 for(String string : positionUser){//现有的
							 boolean exist = false;
							 for(PositionUser positionUser2 : positionUsers){//原有的
							 		if(positionUser2.getPosition_id()==Integer.valueOf(string)){//如果原有的和现在的相同，不处理
							 			exist = true;
							 			break;
							 		}
							 }
							 if(!exist){
								 positionUserAdd.add(string);
							 }
						 }
						 try {
							 accountInfoMapper.addPositionUser(positionUserAdd,user_id);
						} catch (Exception e) {
						}
						jsonObject.put("msg","修改成功");
						jsonObject.put("result","success");
						int user_ids = Futil.getUserId(session, request);
						String remarks = "原有数据："+oldData+",改成:user_name:"+user_name+"gender:"
								+gender+"phone_number:"+phone_number+"mail_address:"+mail_address+"contact_address:"+contact_address+"emergency_contact_name:"
								+emergency_contact_name+"emergency_contact_phone:"+emergency_contact_phone+"departmentList:"+departmentList+"positionList:"
								+positionList+"ip:"+ip;
						loggerMapper.addLogger(user_ids,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("修改账号信息报错"+e);
			}
			
		}else{
			jsonObject.put("msg","新增失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				int index = 0;
				index = accountInfoMapper.findJobNumberCount(job_number,accountInfo_id,company_id);
				if(index>0){
					jsonObject.put("msg","工号已存在！");
					jsonObject.put("result","fail");
					return jsonObject;
				}
				String departmentList = department;
				if(StringUtils.isNotBlank(department1)){
					departmentList= departmentList+","+department1;
				}
				List<String> positionUser = new ArrayList<String>();
				positionUser.add(position);
				String positionList = position;
				if(StringUtils.isNotBlank(position1)){
					positionList= positionList+","+position1;
					positionUser.add(position1);
				}
				 i = accountInfoMapper.addAccountInfo(job_number,user_id,company_id,user_name,user_pwd,gender,phone_number,mail_address,contact_address,emergency_contact_name,
							emergency_contact_phone,departmentList,positionList,ip,role,entry_time);
				 if(i>0){
						 try {
							 accountInfoMapper.addPositionUser(positionUser,user_id);
							 addWorkingTimeUser(user_id,company_id);//新增到workingTimeUser
							 addUserBase(user_id,company_id);//新增到userbase
						} catch (Exception e) {
						}
						jsonObject.put("msg","新增成功");
						jsonObject.put("result","success");
						int user_ids= Futil.getUserId(session, request);
						String remarks = "新增账号"+user_name+"公司ID"+company_id+"用户ID"+user_id+"部门ID"+department+"职位ID"
						+position+"部门1ID"+department1+"职位1ID"+position1+"用户姓名"+user_name+"性别"+gender+
						"手机号码"+phone_number+"邮箱"+mail_address+"联系地址"+contact_address+"紧急联系人"+emergency_contact_name+"紧急联系号码"+emergency_contact_phone;
						loggerMapper.addLogger(user_ids,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("新增公司信息报错"+e);
			}
		}
		return jsonObject;
	
	}
	
	private void addUserBase(String user_id, String company_id) {
		loginMapper.addUserBase(user_id,company_id);
	}
	private void addWorkingTimeUser(String user_id, String company_id) {
		Map<String,String> resultMap = new HashMap<String, String>();
	 	List<String> dateStr = getInitMonthMapWithZero();
        String apiURL = "http://www.easybots.cn/api/holiday.php?m=" + dateStr.stream().collect(Collectors.joining(","));
        System.out.println(apiURL);
        String result = HttpClientUtil.httpGetRequest(apiURL);
        System.out.println(result);
        if (StringUtils.isNotBlank(result)) {
            Map<String, Object> map = JSON.parseObject(result);
            Map<String, Object> orderByResult = new LinkedHashMap<>();
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered( x -> orderByResult.put(x.getKey(), x.getValue()));
            for (Map.Entry<String, Object> entry : orderByResult.entrySet()) {
                Map<String, Object> mapValue = JSON.parseObject(orderByResult.get(entry.getKey()).toString());
                Map<String, Object> orderByMapValueKeyResult = new LinkedHashMap<>();
                mapValue.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> orderByMapValueKeyResult.put(x.getKey(), x.getValue()));
                for (Map.Entry<String, Object> entryValue : orderByMapValueKeyResult.entrySet()) {
                    String holiday=LocalDate.parse(entry.getKey() + "" + entryValue.getKey(), DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
                    resultMap.put(holiday,entryValue.getValue().toString());
                }
            }

        }
		LocalDate localDate = LocalDate.now();
		int year = localDate.getYear();
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		c.set(year, 0, 1);
		while(true){
	    	if(c.get(Calendar.YEAR)!=year){
	    		break;
	    	}else{
		    	if(!resultMap.containsKey(df.format(c.getTime()))){
		    		resultMap.put(df.format(c.getTime()), "0");
		    	}
		    	c.set(Calendar.DATE,c.get(Calendar.DATE)+1);
	    	}
		}
		List<WorkingTime> workingTimes = new ArrayList<WorkingTime>();
		for(String key: resultMap.keySet()){
			WorkingTime time = new WorkingTime();
			time.setDate(key);
			time.setWeek(getDayofweek(key));
			time.setStatus(Integer.valueOf(resultMap.get(key)));
			time.setYear(Integer.valueOf(key.substring(0, 4)));
			time.setMonth(Integer.valueOf(key.substring(5, 7)));
			workingTimes.add(time);
		}
		for(WorkingTime time : workingTimes){
			time.setCompany_id(Integer.parseInt(company_id));
			time.setUser_id(Integer.parseInt(user_id));
		}
		try {
			loginMapper.addWorkingTimeUser(workingTimes);
		} catch (Exception e) {
		}
	}
	public static String getDayofweek(String date){
		  Calendar cal = Calendar.getInstance();
		  if (date.equals("")) {
		   cal.setTime(new Date(System.currentTimeMillis()));
		  }else {
		   cal.setTime(new Date(getDateByStr2(date).getTime()));
		  }
		  String week = "";
		  switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				week = "周日";
				break;
			case 2:
				week = "周一";
				break;
			case 3:
				week = "周二";
				break;
			case 4:
				week = "周三";
				break;
			case 5:
				week = "周四";
				break;
			case 6:
				week = "周五";
				break;
			case 7:
				week = "周六";
				break;
			default:
				break;
			}
	   return week;
	 }
		public static Date getDateByStr2(String dd)
		 {
		 
		  SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		  Date date;
		  try {
		   date = sd.parse(dd);
		  } catch (Exception e) {
		   date = null;
		   e.printStackTrace();
		  }
		  return date;
		 }
	
	private static List<String> getInitMonthMapWithZero() {
      List<String> list = Lists.newArrayList();
      LocalDate localDate = LocalDate.now();
      int month = 12;
      for (int j = 1; j <= month; j++) {
          String date = "";
          date = localDate.getYear() + (StringUtils.leftPad(String.valueOf(j), 2, "0"));
          list.add(date);
      }

      return list;
  }
	/***
	 * 物理删除账号信息
	 * @param accountInfo_id
	 * @param user_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject deleteAccountInfo(String accountInfo_id, String user_id, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","删除失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = accountInfoMapper.deleteAccountInfo(accountInfo_id);
			 accountInfoMapper.deletePositionByUser(user_id);
			 if(i>0){
					jsonObject.put("msg","删除成功");
					jsonObject.put("result","success");
					int user_ids = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把账号"+user_id+"删除";
					loggerMapper.addLogger(user_ids,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("删除权限信息报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 查询公司下的最大的工号
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public String findJobNumber(String company_id) {
		String res = "";
		res = accountInfoMapper.findJobNumber(company_id);
		if(StringUtils.isBlank(res)){
			res = "100001";	
		}
		return res;
	}
	/***
	 * 提交配置权限的信息
	 * @param authorityIdOld
	 * @param authorityId
	 * @param user_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject addUserAndAuthority(String authorityIdOld, String authorityId, String user_id,String id,
			HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","配置失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = accountInfoMapper.addUserAndAuthority(authorityId,id);
			 if(i>0){
					jsonObject.put("msg","配置成功");
					jsonObject.put("result","success");
					int user_ids = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把账号"+user_id+"的权限从:"+authorityIdOld+"改成:"+authorityId;
					loggerMapper.addLogger(user_ids,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("配置账号权限信息报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 根据key修改用户的某一个信息
	 * @param accountIndfoId
	 * @param oldInfo
	 * @param newInfo
	 * @param key
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject updateInfo(String accountIndfoId, String oldInfo, String newInfo, String key, String userId,
			HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","修改失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = accountInfoMapper.updateInfo(accountIndfoId,newInfo,key);
			 if(i>0){
					jsonObject.put("msg","修改成功");
					jsonObject.put("result","success");
					int user_ids = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把账号"+userId+"的:"+key+":"+oldInfo+"改成:"+newInfo;
					loggerMapper.addLogger(user_ids,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("账号修改信息报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 修改登录密码
	 */
	@Override
	public JSONObject submitUpdatePasswork(String oldpassword, String newpassword, String userId,
			String newPassworkTwo, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","修改失败");
		jsonObject.put("result","fail");
		try {
			int c = iuserService.findUserInfoByUserId(userId);
			if(c<1){
				jsonObject.put("msg","用户不存在");
				jsonObject.put("result","fail");
			}else{
				oldpassword = AesCBC.encrypt(oldpassword,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
				newpassword = AesCBC.encrypt(newpassword,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
				int b = iuserService.findUserInfoByUserIdAndPassWord(userId,oldpassword);
				if(b<1){
					jsonObject.put("msg","用户密码错误");
					jsonObject.put("result","fail");
				}else{
					int n = iuserService.updatePassword(userId,oldpassword,newpassword);
					System.out.println("操作结果：" + n);
					switch (n) {
					case 0:
						jsonObject.put("msg","修改失败");
						jsonObject.put("result","fail");
						break;
					case 1:
						jsonObject.put("msg","修改成功");
						jsonObject.put("result","success");
						break;
					case 2:
						jsonObject.put("msg","用户密码错误");
						jsonObject.put("result","fail");
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("修改密码报错"+e);
		}
		return jsonObject;
	}
	
}
