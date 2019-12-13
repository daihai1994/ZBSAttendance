package net.zhongbenshuo.attendance.foreground.company.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.Department;
import net.zhongbenshuo.attendance.bean.Position;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.PositionUser;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;

public interface CompanyMapper {
	/***
	 * 查询公司信息
	 * @param company_name
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	List<Company> findCompanyInfo(@Param("company_name")String company_name, @Param("bNum")int bNum, @Param("rows")int rows, @Param("company_id")int company_id);

	/***
	 * 修改公司名称
	 * @param company_name
	 * @param cardTypeName 
	 * @param cardType 
	 * @param bankName 
	 * @param bankCode 
	 * @param bank_account 
	 * @param mail 
	 * @param fax 
	 * @param official_website 
	 * @param landline_number 
	 * @param phone_number 
	 * @param address 
	 * @param duty_paragraph 
	 * @param compan_id
	 * @return
	 */
	int updateCompanyName(@Param("company_name")String company_name, @Param("company_id")String company_id, 
			@Param("duty_paragraph")String duty_paragraph, 
			@Param("address")String address, 
			@Param("landline_number")String landline_number, 
			@Param("official_website")String official_website, 
			@Param("fax")String fax, 
			@Param("mail")String mail, 
			@Param("bank_account")String bank_account, 
			@Param("bankCode")String bankCode, 
			@Param("bankName")String bankName, 
			@Param("cardType")String cardType, 
			@Param("cardTypeName")String cardTypeName,
			@Param("enable_application")String enable_application);
	/***
	 * 新增公司名称
	 * @param company_name
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
	 * @return
	 */
	int addCompanyName(@Param("company_name")String company_name, 
			@Param("duty_paragraph")String duty_paragraph, 
			@Param("address")String address, 
			@Param("landline_number")String landline_number, 
			@Param("official_website")String official_website, 
			@Param("fax")String fax, 
			@Param("mail")String mail, 
			@Param("bank_account")String bank_account, 
			@Param("bankCode")String bankCode, 
			@Param("bankName")String bankName, 
			@Param("cardType")String cardType, 
			@Param("cardTypeName")String cardTypeName,
			@Param("enable_application")String enable_application);
	/***
	 * 删除公司信息
	 * @param company_id
	 * @return
	 */
	int deleteCompanyInfo(@Param("company_id")String company_id);
	/***
	 * 修改删除密码
	 * @param company_id
	 * @param deletePwd
	 * @return
	 */
	int submitCompanydeletePwd_edit(@Param("company_id")String company_id, @Param("deletePwd")String deletePwd);
	/***
	 * 查询部门的信息
	 * @param company_id
	 * @return
	 */
	List<Department> findDepartMent(@Param("company_id")String company_id);
	/***
	 * 修改部门的信息
	 * @param department_name
	 * @param department_priority
	 * @param department_id
	 * @return
	 */
	int updateDepartment(@Param("department_name")String department_name, @Param("department_priority")String department_priority, @Param("department_id")String department_id);
	/***
	 * 新增部门的信息
	 * @param department_name
	 * @param department_priority
	 * @param company_id
	 * @return
	 */
	int addDepartment(@Param("department_name")String department_name, @Param("department_priority")String department_priority, @Param("company_id")String company_id);
	/***
	 * 删除部门信息
	 * @param department_id
	 * @return
	 */
	int deleteDepartMent(@Param("department_id")String department_id);
	/***
	 * 查询职位信息
	 * @param department_id
	 * @return
	 */
	List<Position> findPosition(@Param("department_id")String department_id);
	/***
	 *修改职位信息
	 * @param position_name
	 * @param position_id
	 * @return
	 */
	int updatePosition(@Param("position_name")String position_name, @Param("position_id")String position_id);
	/***
	 * 新增职位信息
	 * @param position_name
	 * @param departMent_id
	 * @return
	 */
	int addPosition(@Param("position_name")String position_name, @Param("departMent_id")String departMent_id);
	/***
	 * 删除职位信息
	 * @param position_id
	 * @param position_name
	 * @param session
	 * @param request
	 * @return
	 */
	int deletePosition(@Param("position_id")String position_id);
	/***
	 * 修改部门的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	void confirmPriority(List<Department> departmentList);
	/***
	 * 修改职位的排序
	 * @param positionList
	 */
	void positionPriority(List<Position> positionList);
	/**
	 * 查询职位和人员之间的信息
	 * @param position_id
	 * @return
	 */
	List<PositionUser> findPositionUser(@Param("position_id")String position_id);
	/**
	 * 人员排序的修改
	 * @param positionUserList
	 */
	void positionUserPriority(List<PositionUser> positionUserList);

}
