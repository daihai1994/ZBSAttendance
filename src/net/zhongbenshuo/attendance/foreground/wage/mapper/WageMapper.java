package net.zhongbenshuo.attendance.foreground.wage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;


public interface WageMapper {
	/**
	 * 查询工资
	 * @param bNum
	 * @param rows
	 * @param year
	 * @param month
	 * @param userName
	 * @param company_id
	 * @return
	 */
	List<Wage> findwage(@Param("bNum")int bNum, @Param("rows")int rows, @Param("year")int year, @Param("month")int month, 
			@Param("userName")String userName, @Param("company_id")int company_id);
	/**
	 * 导入工资，查询用户ID是否存在
	 * @param user_id
	 * @return
	 */
	int findUserSize(@Param("user_id")String user_id);
	/**
	 * 导入工资，查询部门是否存在
	 * @param issuing_department_id
	 * @return
	 */
	int findDempartmentSize(@Param("issuing_department_id")String issuing_department_id);
	/**
	 * 查询需要导入的所有的人员
	 * @param company_id
	 * @return
	 */
	List<Wage> findAllUser(@Param("company_id")int company_id,@Param("year")String year,@Param("month")String month);
	/**
	 * 查询公司是否存在
	 * @param company
	 * @return
	 */
	int findCompany_id(String company);
	/**
	 * 新增工资
	 * @param wageList
	 */
	void addWageList(List<Wage> wageList);
	/**
	 * 查询工资和邮件地址
	 * @param company_id
	 * @param year
	 * @param month
	 * @return
	 */
	List<Wage> findWageMail(@Param("company_id")int company_id, @Param("year")int year, @Param("month")int month);
	/**
	 * 查询工资报表，自定义时间段
	 * @param bt_year
	 * @param bt_month
	 * @param et_year
	 * @param et_month
	 * @return
	 */
	List<Wage> findWage(@Param("bt_year")String bt_year, @Param("bt_month")String bt_month, @Param("et_year")String et_year, 
			@Param("et_month")String et_month,@Param("company_id")String company_id);
	/**
	 * 工资报表，导出全部的月工资
	 * @param month_year
	 * @param company_id
	 * @return
	 */
	List<Wage> findWageMonthAll(@Param("month_year")String month_year, @Param("company_id")String company_id);
	/**
	 * 工资报表，导出月工资
	 * @param month_year
	 * @param company_id
	 * @param month_month
	 * @return
	 */
	List<Wage> findWageMonth(@Param("month_year")String month_year, @Param("company_id")String company_id, @Param("month_month")String month_month);
	/**
	 * 工资报表，导出季度工资
	 * @param quarter_year
	 * @param company_id
	 * @param btMonth
	 * @param etMonth
	 * @return
	 */
	List<Wage> findWageQuarter(@Param("quarter_year")String quarter_year, @Param("company_id")String company_id, @Param("btMonth")String btMonth, 
			@Param("etMonth")String etMonth);
	
}
