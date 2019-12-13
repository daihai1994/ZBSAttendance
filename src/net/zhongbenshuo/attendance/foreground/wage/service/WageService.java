package net.zhongbenshuo.attendance.foreground.wage.service;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;

public interface WageService {
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
	JSONObject findwage(int bNum, int rows, int year, int month, String userName, int company_id);
	/**
	 * 下载导入模板
	 * @param titles
	 * @param outputStream
	 */
	void uploadTemplate(String[] titles, ServletOutputStream outputStream,int company_id,String department,String company);
	/**
	 * 导入工资
	 * @param session
	 * @param excelFileWage
	 * @return
	 */
	List<String> uploadWage(HttpSession session, MultipartFile excelFileWage,int company_id,String department_id);
	/**
	 * 查询工资和个人邮件
	 * @param company_id
	 * @param year
	 * @param month
	 * @return
	 */
	List<Wage> findWageMail(int company_id, int year, int month);
	/**
	 * 查询选择时间段的工资信息
	 * @param bt_year
	 * @param bt_month
	 * @param et_year
	 * @param et_month
	 * @return
	 */
	List<Wage> findWage(String bt_year, String bt_month, String et_year, String et_month,String company_id);
	/**
	 * 工资报表，导出全部的月工资
	 * @param month_year
	 * @param company_id
	 * @return
	 */
	List<Wage> findWageMonthAll(String month_year, String company_id);
	/**
	 * 工资报表，导出月工资
	 * @param month_year
	 * @param company_id
	 * @param month_month
	 * @return
	 */
	List<Wage> findWageMonth(String month_year, String company_id, String month_month);
	/**
	 * 工资报表，导出季度工资
	 * @param quarter_year
	 * @param company_id
	 * @param btMonth
	 * @param etMonth
	 * @return
	 */
	List<Wage> findWageQuarter(String quarter_year, String company_id, String btMonth, String etMonth);
	/**
	 * 导出工资报表
	 * @param titles
	 * @param outputStream
	 * @param wageList
	 */
	void exportReportCustom(String[] titles, ServletOutputStream outputStream, List<Wage> wageList);
	/**
	 * 导出工资报表（按全部月份计算）
	 * @param titles
	 * @param outputStream
	 * @param wageList
	 */
	void exportReportMonth(String[] titles, ServletOutputStream outputStream, List<Wage> wageList);
	/**
	 * 导出工资报表（全部季度计算）
	 * @param titles
	 * @param outputStream
	 * @param wageList
	 */
	void exportReportQuarter(String[] titles, ServletOutputStream outputStream, List<Wage> wageList);
	

}
