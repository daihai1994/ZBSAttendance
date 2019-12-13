package net.zhongbenshuo.attendance.foreground.accountInfo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

public interface AccountInfoService {
	/***
	 * 查询公司下的权限信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject findAuthority(String company_id);
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
	JSONObject submitAuthorityAdd(String company_id, String authority_id, String authority_describe_old,
			 String authority_describe,  HttpSession session,
			HttpServletRequest request);
	/**
	 * 物理删除权限信息
	 * @param authority_id
	 * @param authority_describe
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject deleteAuthority(String authority_id, String authority_describe, HttpSession session,
			HttpServletRequest request);
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
	JSONObject findAccountInfo(String company_id, String user_id, String phone_number, String user_name,int role,int id,String jobNumber,int bNum,int rows);
	/***
	 * 根据公司ID查询部门信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	String findDepartment(String company_id);
	/***
	 * 根据部门ID查询职位信息
	 * @param department_id
	 * @param session
	 * @param request
	 * @return
	 */
	String findPosition(String department_id);
	/***
	 * 查询最大的user_id
	 * @param session
	 * @param request
	 * @return
	 */
	String findUser_Id();
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
	JSONObject submitAccountInfoAdd(String accountInfo_id,String user_id, String company_id, String department, String position,
			String department1, String position1, String user_name, String user_pwd, String gender, String phone_number,
			String mail_address, String contact_address, String emergency_contact_name, String emergency_contact_phone,String role,String oldData,String job_number,
			String entry_time,
			HttpSession session, HttpServletRequest request);
	/***
	 * 物理删除账号信息
	 * @param accountInfo_id
	 * @param user_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject deleteAccountInfo(String accountInfo_id, String user_id, HttpSession session,
			HttpServletRequest request);
	/***
	 * 查询公司下的最大的工号
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	String findJobNumber(String company_id);
	/***
	 * 提交配置权限的信息
	 * @param authorityIdOld
	 * @param authorityId
	 * @param user_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject addUserAndAuthority(String authorityIdOld, String authorityId, String user_id,String id, HttpSession session,
			HttpServletRequest request);
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
	JSONObject updateInfo(String accountIndfoId, String oldInfo, String newInfo, String key, String userId,
			HttpSession session, HttpServletRequest request);
	/**
	 * 修改登录密码
	 * @param oldPasswork
	 * @param newPasswork
	 * @param user_id
	 * @param newPassworkTwo
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitUpdatePasswork(String oldPasswork, String newPasswork, String user_id, String newPassworkTwo,
			HttpSession session, HttpServletRequest request);

}
