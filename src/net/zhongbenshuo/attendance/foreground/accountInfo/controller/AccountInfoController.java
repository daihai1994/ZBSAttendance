package net.zhongbenshuo.attendance.foreground.accountInfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.accountInfo.service.AccountInfoService;

@Controller
@RequestMapping(value = "/AccountInfoController", produces = "text/html;charset=UTF-8")
public class AccountInfoController {
	@Autowired
	AccountInfoService accountInfoService;
	
	
	/***
	 * 查询公司下的权限信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findAuthority.do")
	@ResponseBody
	public String findAuthority(String company_id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.findAuthority(company_id);
		return jsonObject.toString();
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
	@RequestMapping(value = "/submitAuthorityAdd.do")
	@ResponseBody
	public String submitAuthorityAdd(String company_id,String authority_id,String authority_describe_old
			,String authority_describe,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.submitAuthorityAdd(company_id,authority_id,authority_describe_old,authority_describe,
				session,request);
		return jsonObject.toString();
	}
	/**
	 * 物理删除权限信息
	 * @param authority_id
	 * @param authority_describe
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteAuthority.do")
	@ResponseBody
	public String deleteAuthority(String authority_id,String authority_describe,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.deleteAuthority(authority_id,authority_describe,session,request);
		return jsonObject.toString();
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
	@RequestMapping(value = "/findAccountInfo.do")
	@ResponseBody
	public String findAccountInfo(String company_id,String user_id,String phone_number,String user_name,String jobNumber,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		User user = new User();
		user  = (User) session.getAttribute("user");
		int role  = 0;
		int id = 0;
		if(user!=null){
			role = user.getRole();
			id = user.getId();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.findAccountInfo(company_id,user_id,phone_number,user_name,role,id,jobNumber,bNum,rows);
		return jsonObject.toString();
	}
	
	/***
	 * 根据公司ID查询部门信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findDepartment.do")
	@ResponseBody
	public String findDepartment(String company_id,HttpSession session,HttpServletRequest request) {
		String res = "";
		res = accountInfoService.findDepartment(company_id);
		return res;
	}
	/***
	 * 根据部门ID查询职位信息
	 * @param department_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findPosition.do")
	@ResponseBody
	public String findPosition(String department_id,HttpSession session,HttpServletRequest request) {
		String res = "";
		res = accountInfoService.findPosition(department_id);
		return res;
	}
	/***
	 * 查询最大的user_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findUser_Id.do")
	@ResponseBody
	public String findUser_Id(HttpSession session,HttpServletRequest request) {
		String res = "";
		res = accountInfoService.findUser_Id();
		return res;
	}
	/***
	 * 查询公司下的最大的工号
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findJobNumber.do")
	@ResponseBody
	public String findJobNumber(String company_id,HttpSession session,HttpServletRequest request) {
		String res = "";
		res = accountInfoService.findJobNumber(company_id);
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
	@RequestMapping(value = "/submitAccountInfoAdd.do")
	@ResponseBody
	public String submitAccountInfoAdd(String accountInfo_id,String user_id,String company_id,String department,String position
			,String department1,String position1,String user_name,String user_pwd,String gender,String phone_number,String mail_address,
			String contact_address,String emergency_contact_name,String emergency_contact_phone,String role,
			String oldData,String job_number,String entry_time,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.submitAccountInfoAdd(accountInfo_id,user_id,company_id,department,position,department1,
				position1,user_name,user_pwd,gender,phone_number,mail_address,contact_address,emergency_contact_name,
				emergency_contact_phone,role,oldData,job_number,entry_time,session,request);
		return jsonObject.toString();
	}
	/***
	 * 物理删除账号信息
	 * @param accountInfo_id
	 * @param user_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteAccountInfo.do")
	@ResponseBody
	public String deleteAccountInfo(String accountInfo_id,String user_id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.deleteAccountInfo(accountInfo_id,user_id,session,request);
		return jsonObject.toString();
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
	@RequestMapping(value = "/updateInfo.do")
	@ResponseBody
	public String updateInfo(String accountIndfoId,String oldInfo,String newInfo,String key,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.updateInfo(accountIndfoId,oldInfo,newInfo,key,userId,session,request);
		return jsonObject.toString();
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
	@RequestMapping(value = "/addUserAndAuthority.do")
	@ResponseBody
	public String addUserAndAuthority(String authorityIdOld,String authorityId,String user_id,String id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.addUserAndAuthority(authorityIdOld,authorityId,user_id,id,session,request);
		return jsonObject.toString();
	}
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
	@RequestMapping(value = "/submitUpdatePasswork.do")
	@ResponseBody
	public String submitUpdatePasswork(String oldPasswork,String newPasswork,String user_id,String newPassworkTwo,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = accountInfoService.submitUpdatePasswork(oldPasswork,newPasswork,user_id,newPassworkTwo,session,request);
		return jsonObject.toString();
	}
	
}
