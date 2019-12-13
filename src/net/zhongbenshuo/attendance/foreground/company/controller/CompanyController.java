package net.zhongbenshuo.attendance.foreground.company.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.company.service.CompanyService;

@Controller
@RequestMapping(value = "/CompanyController", produces = "text/html;charset=UTF-8")
public class CompanyController {

	@Autowired
	CompanyService companyService;
	
	/***
	 * 查询公司信息
	 * @param company_name
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findCompanyInfo.do")
	@ResponseBody
	public String findCompanyInfo(String company_name,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		User user = new User();
		user  = (User) session.getAttribute("user");
		int company_id  = 0;
		if(user!=null){
			 company_id = user.getCompany_id();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.findCompanyInfo(company_name,bNum,rows,company_id);
		return jsonObject.toString();
	}
	/***
	 * 新增修改公司名称
	 * @param company_name
	 * @param compan_id
	 * @param company_name_old
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitCompanyAdd.do")
	@ResponseBody
	public String submitCompanyAdd(String company_name,String company_id,String company_name_old,
			String duty_paragraph,String address,String landline_number,
			String official_website,String fax,String mail,String bank_account,
			String bankCode,String bankName,String cardType,String cardTypeName,String dataCompanyStrng,String enable_application,
			HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.submitCompanyAdd(company_name,company_id,company_name_old,duty_paragraph,address,landline_number,
				official_website,fax,mail,bank_account,bankCode,bankName,cardType,cardTypeName,dataCompanyStrng,enable_application,session,request);
		return jsonObject.toString();
	}
	/***
	 * 删除公司信息
	 * @param company_name
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteCompanyInfo.do")
	@ResponseBody
	public String deleteCompanyInfo(String company_name,String company_id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.deleteCompanyInfo(company_name,company_id,session,request);
		return jsonObject.toString();
	}
	
	/***
	 * 修改删除密码
	 * @param company_id
	 * @param old_deletePwd
	 * @param deletePwd
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitCompanydeletePwd_edit.do")
	@ResponseBody
	public String submitCompanydeletePwd_edit(String company_id,String old_deletePwd,String deletePwd,String company_name,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.submitCompanydeletePwd_edit(company_id,old_deletePwd,deletePwd,company_name,session,request);
		return jsonObject.toString();
	}
	/***
	 * 查询部门的信息
	 * @param company_id
	 * @return
	 */
	@RequestMapping(value = "/findDepartMent.do")
	@ResponseBody
	public String findDepartMent(String company_id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.findDepartMent(company_id);
		return jsonObject.toString();
	}
	
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
	@RequestMapping(value = "/submitDepartmentAdd.do")
	@ResponseBody
	public String submitDepartmentAdd(String department_id,String department_name_old,String department_priority_old,String department_name,
			String department_priority,String company_id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.submitDepartmentAdd(department_id,department_name_old,department_priority_old,department_name,department_priority,company_id,session,request);
		return jsonObject.toString();
	}
	
	
	/***
	 * 删除部门信息
	 * @param department_id
	 * @param department_name
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteDepartMent.do")
	@ResponseBody
	public String deleteDepartMent(String department_id,String department_name,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.deleteDepartMent(department_id,department_name,session,request);
		return jsonObject.toString();
	}
	
	/***
	 * 修改部门的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/confirmPriority.do")
	@ResponseBody
	public String confirmPriority(String liststring,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.confirmPriority(liststring,session,request);
		return jsonObject.toString();
	}
	/**
	 * 职位的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/positionPriority.do")
	@ResponseBody
	public String positionPriority(String liststring,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.positionPriority(liststring,session,request);
		return jsonObject.toString();
	}
	/**
	 * 员工的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/positionUserPriority.do")
	@ResponseBody
	public String positionUserPriority(String liststring,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.positionUserPriority(liststring,session,request);
		return jsonObject.toString();
	}
	
	/***
	 * 查询职位信息
	 * @param department_id
	 * @return
	 */
	@RequestMapping(value = "/findPosition.do")
	@ResponseBody
	public String findPosition(String department_id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.findPosition(department_id);
		return jsonObject.toString();
	}
	/**
	 * 查询职位和人员之间的信息
	 * @param position_id
	 * @return
	 */
	@RequestMapping(value = "/findPositionUser.do")
	@ResponseBody
	public String findPositionUser(String position_id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.findPositionUser(position_id);
		return jsonObject.toString();
	}
	
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
	@RequestMapping(value = "/submitPositionAdd.do")
	@ResponseBody
	public String submitPositionAdd(String position_id,String position_name_old,String position_name,String departMent_id,
			HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.submitPositionAdd(position_id,position_name_old,position_name,departMent_id,session,request);
		return jsonObject.toString();
	}
	
	/***
	 * 删除职位信息
	 * @param position_id
	 * @param position_name
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deletePosition.do")
	@ResponseBody
	public String deletePosition(String position_id,String position_name,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = companyService.deletePosition(position_id,position_name,session,request);
		return jsonObject.toString();
	}
}
