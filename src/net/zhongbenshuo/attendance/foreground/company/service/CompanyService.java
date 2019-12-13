package net.zhongbenshuo.attendance.foreground.company.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

public interface CompanyService {
	/***
	 * 查询公司信息
	 * @param company_name
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject findCompanyInfo(String company_name, int bNum, int rows, int company_id);
	/***
	 * 新增修改公司名称
	 * @param company_name
	 * @param compan_id
	 * @param company_name_old
	 * @param cardTypeName 
	 * @param cardType 
	 * @param bankName 
	 * @param bankCode 
	 * @param resBank 
	 * @param bank_account 
	 * @param mail 
	 * @param fax 
	 * @param official_website 
	 * @param landline_number 
	 * @param phone_number 
	 * @param address 
	 * @param duty_paragraph 
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitCompanyAdd(String company_name, String company_id,String company_name_old,
			String duty_paragraph, String address,  
			String landline_number, String official_website, 
			String fax, String mail, String bank_account,  
			String bankCode, String bankName, String cardType, 
			String cardTypeName,String dataCompanyStrng,String enable_application, HttpSession session,HttpServletRequest request);
	/***
	 * 删除公司信息
	 * @param company_name
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject deleteCompanyInfo(String company_name, String company_id, HttpSession session,
			HttpServletRequest request);
	/***
	 * 修改删除密码
	 * @param company_id
	 * @param old_deletePwd
	 * @param deletePwd
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitCompanydeletePwd_edit(String company_id, String old_deletePwd, String deletePwd,String company_name,
			HttpSession session, HttpServletRequest request);
	/***
	 * 查询部门的信息
	 * @param company_id
	 * @return
	 */
	JSONObject findDepartMent(String company_id);
	/**
	 * 新增，修改部门的信息
	 * @param department_id
	 * @param department_name_old
	 * @param department_priority_old
	 * @param department_name
	 * @param department_priority
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitDepartmentAdd(String department_id, String department_name_old, String department_priority_old,
			String department_name, String department_priority, String company_id, HttpSession session,
			HttpServletRequest request);
	/***
	 * 删除部门信息
	 * @param department_id
	 * @param department_name
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject deleteDepartMent(String department_id, String department_name, HttpSession session,
			HttpServletRequest request);
	/***
	 * 查询职位信息
	 * @param department_id
	 * @return
	 */
	JSONObject findPosition(String department_id);
	/***
	 * 新增修改职位信息
	 * @param position_id
	 * @param position_name_old
	 * @param position_name
	 * @param departMent_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject submitPositionAdd(String position_id, String position_name_old, String position_name,
			String departMent_id, HttpSession session, HttpServletRequest request);
	/***
	 * 删除职位信息
	 * @param position_id
	 * @param position_name
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject deletePosition(String position_id, String position_name, HttpSession session,
			HttpServletRequest request);
	/***
	 * 修改部门的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject confirmPriority(String liststring, HttpSession session, HttpServletRequest request);
	/***
	 * 修改职位的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject positionPriority(String liststring, HttpSession session, HttpServletRequest request);
	/**
	 * 查询职位和人员之间的信息
	 * @param position_id
	 * @return
	 */
	JSONObject findPositionUser(String position_id);
	/***
	 * 员工的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject positionUserPriority(String liststring, HttpSession session, HttpServletRequest request);

}
