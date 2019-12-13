package net.zhongbenshuo.attendance.foreground.wage.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;
import net.zhongbenshuo.attendance.foreground.wage.mapper.WageMapper;
import net.zhongbenshuo.attendance.foreground.wage.service.WageService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.ExcelUtil;
import net.zhongbenshuo.attendance.utils.ImportExcelUtil;
import net.zhongbenshuo.attendance.utils.Utils;

@Service
public class WageServiceImpl implements WageService {
	public static Logger logger = LogManager.getLogger(WageServiceImpl.class);
	
	@Autowired
	WageMapper wageMapper;
	
	@Autowired
	LoggerMapper loggerMapper;

	@Override
	public JSONObject findwage(int bNum, int rows, int year, int month, String userName, int company_id) {
		JSONObject jsonObject = new JSONObject();
		List<Wage> wageList = new ArrayList<Wage>();
		int size = 0;
		try {
			wageList = wageMapper.findwage(bNum,rows,year,month,userName,company_id);
			if(wageList!=null&&wageList.size()>0){
				size = wageList.get(0).getSize();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.put("rows", wageList);
		jsonObject.put("total",size);
		return jsonObject;
	}
	
	@Override
	public void uploadTemplate(String[] titles, ServletOutputStream outputStream,int company_id,String department,String company) {

		// 创建一个workbook 对应一个excel应用文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = workBook.createSheet("工资导入模板");
		ExcelUtil exportUtil = new ExcelUtil(workBook, sheet);
		HSSFCellStyle headStyle = exportUtil.getHeadStyle();
		HSSFCellStyle timeStyle = exportUtil.getTimeStyle();
		HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
		HSSFCellStyle titleStyle = exportUtil.getTitleStyle();
		HSSFCellStyle lastStyle = exportUtil.getLastStyle();
		HSSFCellStyle titleRightStyle = exportUtil.getTitleRightStyle();
		HSSFCellStyle bodyrightStyle = exportUtil.getBodyRightStyle();
		HSSFCellStyle LastBodyRightStyle = exportUtil.getLastBodyRightStyle();
		HSSFCellStyle titleLeftStyle = exportUtil.getTitleLeftStyle();
		HSSFCellStyle bodyLeftStyle = exportUtil.getBodyLeftStyle();
		HSSFCellStyle bodyLeftLastStyle = exportUtil.getBodyLeftLastStyle();
		for (int h = 0; h <= 32; h++) {
			sheet.setColumnWidth((short) h, (short) 5600);
		}
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) 500);
		HSSFCell titlecell = titleRow.createCell(1);
		titlecell.setCellValue("工资导入模板");
		HSSFRow timeRow = sheet.createRow(1);
		timeRow.setHeight((short) 450);
		HSSFCell timecell = timeRow.createCell(1);
		timecell.setCellValue("工资导入模板");
		sheet.createFreezePane( 0, 1, 0, 1 );  
		sheet.createFreezePane( 0, 2, 0, 2 ); 
		sheet.createFreezePane( 0, 3, 0, 3 ); 
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 10));
		titlecell.setCellStyle(headStyle);
		timecell.setCellStyle(timeStyle);
		// 构建表头
		HSSFRow headRow = sheet.createRow(2);
		headRow.setHeight((short) 450);
		HSSFCell cell = null;
		for (int i = 0; i < titles.length; i++) {
			if (i == 0) {
				cell = headRow.createCell(i);
				cell.setCellValue(titles[i]);
				cell.setCellStyle(titleLeftStyle);
			} else if (i == titles.length - 1) {
				cell = headRow.createCell(i);
				cell.setCellValue(titles[i]);
				cell.setCellStyle(titleRightStyle);
			} else {
				cell = headRow.createCell(i);
				cell.setCellValue(titles[i]);
				cell.setCellStyle(titleStyle);
			}
		}
		Calendar newdate = Calendar.getInstance();
		String year = String.valueOf(newdate.get(Calendar.YEAR));
		String month = String.valueOf(newdate.get(Calendar.MONTH));
		//String month = "12";
		List<Wage> wageList = new ArrayList<Wage>();
		wageList = wageMapper.findAllUser(company_id,year,month);
		DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		// 构建表体数据
				for (int j = 0; j < wageList.size(); j++) {
					Wage wage = wageList.get(j);
					if (j == wageList.size()-1) {
						HSSFRow bodyRow = sheet.createRow(j + 3);
						bodyRow.setHeight((short) 450);
						cell = bodyRow.createCell(0);
						cell.setCellValue(wage.getUser_id());
						cell.setCellStyle(bodyLeftLastStyle);

						cell = bodyRow.createCell(1);
						cell.setCellValue(wage.getUser_name());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(2);
						cell.setCellValue(year);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(3);
						cell.setCellValue(month);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(4);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(5);
						cell.setCellValue(wage.getWorkingDay());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(6);
						cell.setCellValue(wage.getMealAllowanceDay());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(7);
						cell.setCellValue(Utils.removeZero(wage.getOvertime_hours(),2));
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(8);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(9);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(10);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(11);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);
						
						cell = bodyRow.createCell(12);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(13);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);
						
						cell = bodyRow.createCell(14);
						cell.setCellValue(0);
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(15);
						cell.setCellValue(0);
						cell.setCellStyle(LastBodyRightStyle);
					} else {
						HSSFRow bodyRow = sheet.createRow(j + 3);
						bodyRow.setHeight((short) 450);
						cell = bodyRow.createCell(0);
						cell.setCellValue(wage.getUser_id());
						cell.setCellStyle(bodyLeftStyle);

						cell = bodyRow.createCell(1);
						cell.setCellValue(wage.getUser_name());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(2);
						cell.setCellValue(year);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(3);
						cell.setCellValue(month);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(4);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(5);
						cell.setCellValue(wage.getWorkingDay());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(6);
						cell.setCellValue(wage.getMealAllowanceDay());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(7);
						cell.setCellValue(Utils.removeZero(wage.getOvertime_hours(),2));
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(8);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(9);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(10);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(11);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);
						
						cell = bodyRow.createCell(12);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(13);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);

						
						cell = bodyRow.createCell(14);
						cell.setCellValue(0);
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(15);
						cell.setCellValue(0);
						cell.setCellStyle(bodyrightStyle);
					}

				}
		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	@Override
	public List<String> uploadWage(HttpSession session, MultipartFile excelFileWage,int company_id,String department_id) {
		// 创建集合，存放录入失败的各种信息
		List<String> enterFailure = new ArrayList<String>();

		String result = "";
		if (null == excelFileWage || excelFileWage.isEmpty()) {
			result = "模板文件为空,请选择文件";
			enterFailure.add(result);
			return enterFailure;
		}
		System.out.println("通过传统方式form表单提交方式导入excel文件！");

		InputStream in = null;
		List<List<Object>> listob = new ArrayList<List<Object>>();
		ImportExcelUtil util = new ImportExcelUtil();
		try {
			in = excelFileWage.getInputStream();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		String originalFilename = excelFileWage.getOriginalFilename();
		// 获取整个文件的集合
		try {
			listob = util.getBankListByExcel(in, originalFilename);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		// 关闭流
		try {
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		List<Wage> wageList = new ArrayList<Wage>();
		for (int i = 3; i < listob.size(); i++) {
			try {
				// 获取每一行的信息
				List<Object> lo = listob.get(i);
				System.out.println(lo);
				// 得到文档中各项信息
			String user_id = lo.get(0).toString();// 用户ID
			String year = lo.get(2).toString();// 年份
			String month = lo.get(3).toString();// 月份
			String wage_basic = lo.get(4).toString();// 基本工资
			String attendance_days = lo.get(5).toString();// 出勤天数
			String subsidy = lo.get(6).toString();// 补助(餐补)
			String overtime_hours = lo.get(7).toString();// 加班时间
			String social_security_fund_persional = lo.get(8).toString();// 个人承担社保
			String social_security_fund_company = lo.get(9).toString();// 公司承担社保
			String individual_income_tax = lo.get(10).toString();// 个人所得税
			String wage_real = lo.get(11).toString();// 实发工资
			String agent_distribution_boc = lo.get(12).toString();// 中国银行代发
			String agent_distribution_cmbc = lo.get(13).toString();// 民生银行代发
			String reimbursement = lo.get(14).toString();// 报销
			String achievement = lo.get(15).toString();// 业绩
			String msg = "第"+(i-2)+"条数据";
			if(lo.size()!=16){
				enterFailure.add(msg+",少了参数，请检查（如有疑问，请先导出数据）！");
			}
			try {
				int userSize = wageMapper.findUserSize(user_id);//检测人员id是否存在，或者存在多个
				if(userSize!=1){
					enterFailure.add(msg+",用户ID不存在或者存在多个请检测！");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",用户ID不存在或者存在多个请检测！");
			}
			
			
			//年份
			try {
				boolean status = getInteger(year);
				if(!status){
					enterFailure.add(msg+",年份不是正规的年份!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",年份不是正规的年份!");
			}
			//月份
			try {
				boolean status = getInteger(month);
				if(!status){
					enterFailure.add(msg+",月份不是正规的月份!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",月份不是正规的月份!");
			}
			//基本工资
			try {
				boolean status = getFloat(wage_basic);
				if(!status){
					enterFailure.add(msg+",基本工资为空或者格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",基本工资为空或者格式不正确!");
			}
			//出勤天数
			try {
				boolean status = getFloat(attendance_days);
				if(!status){
					enterFailure.add(msg+",出勤天数不能为空或者格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",出勤天数不能为空或者格式不正确!");
			}
			//（补助）餐补
			if(StringUtils.isNotBlank(subsidy)){
				try {
					boolean status = getFloat(subsidy);
					if(!status){
						enterFailure.add(msg+",餐补的格式不正确!");
					}
				} catch (Exception e) {
					enterFailure.add(msg+",餐补的格式不正确!");
				}
			}
			//加班时间
			if(StringUtils.isNotBlank(overtime_hours)){
				try {
					boolean status = getFloat(overtime_hours);
					if(!status){
						enterFailure.add(msg+",加班时间的格式不正确!");
					}
				} catch (Exception e) {
					enterFailure.add(msg+",加班时间的格式不正确!");
				}
			}
			//个人承担社保
			try {
				boolean status = getFloat(social_security_fund_persional);
				if(!status){
					enterFailure.add(msg+",个人承担社保格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",个人承担社保格式不正确!");
			}
			//公司承担社保
			try {
				boolean status = getFloat(social_security_fund_company);
				if(!status){
					enterFailure.add(msg+",公司承担社保格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",公司承担社保格式不正确!");
			}
			//个人所得税
			try {
				boolean status = getFloat(individual_income_tax);
				if(!status){
					enterFailure.add(msg+",个人所得税格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",个人所得税格式不正确!");
			}
			//实发工资
			try {
				boolean status = getFloat(wage_real);
				if(!status){
					enterFailure.add(msg+",工资格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",工资格式不正确!");
			}
			//中国银行代发
			try {
				boolean status = getFloat(agent_distribution_boc);
				if(!status){
					enterFailure.add(msg+",中国银行代发格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",中国银行代发格式不正确!");
			}
			//民生银行代发
			try {
				boolean status = getFloat(agent_distribution_cmbc);
				if(!status){
					enterFailure.add(msg+",民生银行代发格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",民生银行代发格式不正确!");
			}
			//报销
			try {
				boolean status = getFloat(reimbursement);
				if(!status){
					enterFailure.add(msg+",报销格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",报销格式不正确!");
			}
			//业绩
			try {
				boolean status = getFloat(achievement);
				if(!status){
					enterFailure.add(msg+",业绩格式不正确!");
				}
			} catch (Exception e) {
				enterFailure.add(msg+",业绩格式不正确!");
			}
			Wage wage = new Wage();
			wage.setUser_id(Integer.parseInt(user_id));
			wage.setMonth(Integer.parseInt(month));
			wage.setWage_basic(Float.parseFloat(wage_basic));
			wage.setAttendance_days(Float.parseFloat(attendance_days));
			wage.setSubsidy(Float.parseFloat(subsidy));
			wage.setOvertime_hours(Float.parseFloat(overtime_hours));
			wage.setSocial_security_fund_persional(Float.parseFloat(social_security_fund_persional));
			wage.setSocial_security_fund_company(Float.parseFloat(social_security_fund_company));
			wage.setIndividual_income_tax(Float.parseFloat(individual_income_tax));
			wage.setWage_real(Float.parseFloat(wage_real));
			wage.setAgent_distribution_boc(Float.parseFloat(agent_distribution_boc));
			wage.setAgent_distribution_cmbc(Float.parseFloat(agent_distribution_cmbc));
			wage.setIssuing_department_id(Integer.parseInt(department_id));
			wage.setYear(Integer.parseInt(year));
			wage.setReimbursement(Float.parseFloat(reimbursement));
			wage.setAchievement(Float.parseFloat(achievement));
			wage.setCompany_id(company_id);
			wageList.add(wage);
			} catch (Exception e) {
				enterFailure.add("第" + (i+1) + "笔," + "有误");
				e.printStackTrace();
			}

		}
		if(enterFailure.size()>0){
			return enterFailure;
		}
		try {
			wageMapper.addWageList(wageList);//导入工资
			if (enterFailure.size() == 0) {
				enterFailure.add("录入成功");
				return enterFailure;
			} else {
				return enterFailure;
			}
		} catch (Exception e) {
			enterFailure.add("数据库出错!");
			e.printStackTrace();
		}
		
		return enterFailure;
	}
	/**
	 * 判断当前是否是integer型
	 * @param msg
	 * @return
	 */
	private boolean getInteger(String msg) {
		try {
			Integer xx = Integer.parseInt(msg);
			if(xx instanceof Integer ==false){
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 判断当前是否是float型
	 * @param msg
	 * @return
	 */
	private boolean getFloat(String msg){
		try {
			Float xx = Float.parseFloat(msg);
			if(xx instanceof Float ==false){
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 查询工资和邮件地址
	 */
	@Override
	public List<Wage> findWageMail(int company_id, int year, int month) {
		List<Wage> wageList = new ArrayList<Wage>();
		wageList = wageMapper.findWageMail(company_id,year,month);
		return wageList;
	}
	/**
	 * 查询选择时间段的工资信息
	 * @param bt_year
	 * @param bt_month
	 * @param et_year
	 * @param et_month
	 * @return
	 */
	@Override
	public List<Wage> findWage(String bt_year, String bt_month, String et_year, String et_month,String company_id) {
		List<Wage> wageList = new ArrayList<Wage>();
		try {
			wageList = wageMapper.findWage(bt_year,bt_month,et_year,et_month,company_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wageList;
	}
	/**
	 * 工资报表，导出全部的月工资
	 * @param month_year
	 * @param company_id
	 * @return
	 */
	@Override
	public List<Wage> findWageMonthAll(String month_year, String company_id) {
		List<Wage> wageList = new ArrayList<Wage>();
		try {
			wageList = wageMapper.findWageMonthAll(month_year,company_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wageList;
	}
	/**
	 * 工资报表，导出月工资
	 * @param month_year
	 * @param company_id
	 * @param month_month
	 * @return
	 */
	@Override
	public List<Wage> findWageMonth(String month_year, String company_id, String month_month) {
		List<Wage> wageList = new ArrayList<Wage>();
		try {
			wageList = wageMapper.findWageMonth(month_year,company_id,month_month);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wageList;
	}
	/**
	 * 工资报表，导出季度工资
	 * @param quarter_year
	 * @param company_id
	 * @param btMonth
	 * @param etMonth
	 * @return
	 */
	@Override
	public List<Wage> findWageQuarter(String quarter_year, String company_id, String btMonth, String etMonth) {
		List<Wage> wageList = new ArrayList<Wage>();
		try {
			wageList = wageMapper.findWageQuarter(quarter_year,company_id,btMonth,etMonth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wageList;
	}
	/**
	 * 导出工资报表
	 */
	@Override
	public void exportReportCustom(String[] titles, ServletOutputStream outputStream, List<Wage> wageList) {

		// 创建一个workbook 对应一个excel应用文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = workBook.createSheet("工资报表");
		ExcelUtil exportUtil = new ExcelUtil(workBook, sheet);
		HSSFCellStyle headStyle = exportUtil.getHeadStyle();
		HSSFCellStyle timeStyle = exportUtil.getTimeStyle();
		HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
		HSSFCellStyle titleStyle = exportUtil.getTitleStyle();
		HSSFCellStyle lastStyle = exportUtil.getLastStyle();
		HSSFCellStyle titleRightStyle = exportUtil.getTitleRightStyle();
		HSSFCellStyle bodyrightStyle = exportUtil.getBodyRightStyle();
		HSSFCellStyle LastBodyRightStyle = exportUtil.getLastBodyRightStyle();
		HSSFCellStyle titleLeftStyle = exportUtil.getTitleLeftStyle();
		HSSFCellStyle bodyLeftStyle = exportUtil.getBodyLeftStyle();
		HSSFCellStyle bodyLeftLastStyle = exportUtil.getBodyLeftLastStyle();
		for (int h = 0; h <= 32; h++) {
			sheet.setColumnWidth((short) h, (short) 3000);
		}
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) 500);
		HSSFCell titlecell = titleRow.createCell(1);
		titlecell.setCellValue("工资报表");
		HSSFRow timeRow = sheet.createRow(1);
		timeRow.setHeight((short) 450);
		HSSFCell timecell = timeRow.createCell(1);
		timecell.setCellValue("工资报表");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 10));
		titlecell.setCellStyle(headStyle);
		timecell.setCellStyle(timeStyle);
		// 构建表头
		HSSFRow headRow = sheet.createRow(2);
		headRow.setHeight((short) 450);
		HSSFCell cell = null;
		for (int i = 0; i < titles.length; i++) {
			if (i == 0) {
				cell = headRow.createCell(i);
				cell.setCellValue(titles[i]);
				cell.setCellStyle(titleLeftStyle);
			} else if (i == titles.length - 1) {
				cell = headRow.createCell(i);
				cell.setCellValue(titles[i]);
				cell.setCellStyle(titleRightStyle);
			} else {
				cell = headRow.createCell(i);
				cell.setCellValue(titles[i]);
				cell.setCellStyle(titleStyle);
			}
		}
		Calendar newdate = Calendar.getInstance();
		String year = String.valueOf(newdate.get(Calendar.YEAR));
		String month = String.valueOf(newdate.get(Calendar.MONTH));
		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		// 构建表体数据
				for (int j = 0; j < wageList.size(); j++) {
					Wage wage = wageList.get(j);
					if (j == 1) {
						HSSFRow bodyRow = sheet.createRow(j + 3);
						bodyRow.setHeight((short) 450);
						cell = bodyRow.createCell(0);
						cell.setCellValue(wage.getUser_id());
						cell.setCellStyle(bodyLeftLastStyle);

						cell = bodyRow.createCell(1);
						cell.setCellValue(wage.getUser_name());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(2);
						cell.setCellValue(wage.getYear());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(3);
						cell.setCellValue(wage.getMonth());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(4);
						cell.setCellValue(wage.getWage_basic());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(5);
						cell.setCellValue(decimalFormat.format(wage.getWorking()/8));
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(6);
						cell.setCellValue(wage.getSubsidy());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(7);
						cell.setCellValue(wage.getOvertime_hours());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(8);
						cell.setCellValue(wage.getSocial_security_fund_persional());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(9);
						cell.setCellValue(wage.getSocial_security_fund_company());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(10);
						cell.setCellValue(wage.getIndividual_income_tax());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(11);
						cell.setCellValue(wage.getWage_real());
						cell.setCellStyle(lastStyle);
						
						cell = bodyRow.createCell(12);
						cell.setCellValue(wage.getAgent_distribution_boc());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(13);
						cell.setCellValue(wage.getAgent_distribution_cmbc());
						cell.setCellStyle(lastStyle);
						
						cell = bodyRow.createCell(14);
						cell.setCellValue(wage.getReimbursement());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(15);
						cell.setCellValue(wage.getAchievement());
						cell.setCellStyle(LastBodyRightStyle);
					} else {
						HSSFRow bodyRow = sheet.createRow(j + 3);
						bodyRow.setHeight((short) 450);
						cell = bodyRow.createCell(0);
						cell.setCellValue(wage.getUser_id());
						cell.setCellStyle(bodyLeftStyle);

						cell = bodyRow.createCell(1);
						cell.setCellValue(wage.getUser_name());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(2);
						cell.setCellValue(wage.getYear());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(3);
						cell.setCellValue(wage.getMonth());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(4);
						cell.setCellValue(wage.getWage_basic());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(5);
						cell.setCellValue(decimalFormat.format(wage.getWorking()/8));
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(6);
						cell.setCellValue(wage.getSubsidy());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(7);
						cell.setCellValue(wage.getOvertime_hours());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(8);
						cell.setCellValue(wage.getSocial_security_fund_persional());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(9);
						cell.setCellValue(wage.getSocial_security_fund_company());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(10);
						cell.setCellValue(wage.getIndividual_income_tax());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(11);
						cell.setCellValue(wage.getWage_real());
						cell.setCellStyle(bodyStyle);
						
						cell = bodyRow.createCell(12);
						cell.setCellValue(wage.getAgent_distribution_boc());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(13);
						cell.setCellValue(wage.getAgent_distribution_cmbc());
						cell.setCellStyle(bodyStyle);
						
						cell = bodyRow.createCell(14);
						cell.setCellValue(wage.getReimbursement());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(15);
						cell.setCellValue(wage.getAchievement());
						cell.setCellStyle(bodyrightStyle);
					}

				}
		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * 导出全部月份 工资按一个月一个月的导出
	 */
	@Override
	public void exportReportMonth(String[] titles, ServletOutputStream outputStream, List<Wage> wageList) {
		List<Wage> wageList1 = new ArrayList<Wage>();
		List<Wage> wageList2 = new ArrayList<Wage>();
		List<Wage> wageList3 = new ArrayList<Wage>();
		List<Wage> wageList4 = new ArrayList<Wage>();
		List<Wage> wageList5 = new ArrayList<Wage>();
		List<Wage> wageList6 = new ArrayList<Wage>();
		List<Wage> wageList7 = new ArrayList<Wage>();
		List<Wage> wageList8 = new ArrayList<Wage>();
		List<Wage> wageList9 = new ArrayList<Wage>();
		List<Wage> wageList10 = new ArrayList<Wage>();
		List<Wage> wageList11 = new ArrayList<Wage>();
		List<Wage> wageList12 = new ArrayList<Wage>();
		wageList1 = wageList.stream().filter(x->x.getMonth()==1).collect(Collectors.toList());
		wageList2 = wageList.stream().filter(x->x.getMonth()==2).collect(Collectors.toList());
		wageList3 = wageList.stream().filter(x->x.getMonth()==3).collect(Collectors.toList());
		wageList4 = wageList.stream().filter(x->x.getMonth()==4).collect(Collectors.toList());
		wageList5 = wageList.stream().filter(x->x.getMonth()==5).collect(Collectors.toList());
		wageList6 = wageList.stream().filter(x->x.getMonth()==6).collect(Collectors.toList());
		wageList7 = wageList.stream().filter(x->x.getMonth()==7).collect(Collectors.toList());
		wageList8 = wageList.stream().filter(x->x.getMonth()==8).collect(Collectors.toList());
		wageList9 = wageList.stream().filter(x->x.getMonth()==9).collect(Collectors.toList());
		wageList10 = wageList.stream().filter(x->x.getMonth()==10).collect(Collectors.toList());
		wageList11 = wageList.stream().filter(x->x.getMonth()==11).collect(Collectors.toList());
		wageList12 = wageList.stream().filter(x->x.getMonth()==12).collect(Collectors.toList());
		// 创建一个workbook 对应一个excel应用文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		for(int ii = 1;ii<=12;ii++){
			switch (ii) {
			case 1:
				wageList = wageList1;
				break;
			case 2:
				wageList = wageList2;
				break;
			case 3:
				wageList = wageList3;
				break;
			case 4:
				wageList = wageList4;
				break;
			case 5:
				wageList = wageList5;
				break;
			case 6:
				wageList = wageList6;
				break;
			case 7:
				wageList = wageList7;
				break;
			case 8:
				wageList = wageList8;
				break;
			case 9:
				wageList = wageList9;
				break;
			case 10:
				wageList = wageList10;
				break;
			case 11:
				wageList = wageList11;
				break;
			case 12:
				wageList = wageList12;
				break;
			default:
				break;
			}
			HSSFSheet sheet = workBook.createSheet("工资报表"+ii+"月份");
			ExcelUtil exportUtil = new ExcelUtil(workBook, sheet);
			HSSFCellStyle headStyle = exportUtil.getHeadStyle();
			HSSFCellStyle timeStyle = exportUtil.getTimeStyle();
			HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			HSSFCellStyle titleStyle = exportUtil.getTitleStyle();
			HSSFCellStyle lastStyle = exportUtil.getLastStyle();
			HSSFCellStyle titleRightStyle = exportUtil.getTitleRightStyle();
			HSSFCellStyle bodyrightStyle = exportUtil.getBodyRightStyle();
			HSSFCellStyle LastBodyRightStyle = exportUtil.getLastBodyRightStyle();
			HSSFCellStyle titleLeftStyle = exportUtil.getTitleLeftStyle();
			HSSFCellStyle bodyLeftStyle = exportUtil.getBodyLeftStyle();
			HSSFCellStyle bodyLeftLastStyle = exportUtil.getBodyLeftLastStyle();
			for (int h = 0; h <= 32; h++) {
				sheet.setColumnWidth((short) h, (short) 5600);
			}
			HSSFRow titleRow = sheet.createRow(0);
			titleRow.setHeight((short) 500);
			HSSFCell titlecell = titleRow.createCell(1);
			titlecell.setCellValue("工资报表");
			HSSFRow timeRow = sheet.createRow(1);
			timeRow.setHeight((short) 450);
			HSSFCell timecell = timeRow.createCell(1);
			timecell.setCellValue("工资报表");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 10));
			titlecell.setCellStyle(headStyle);
			timecell.setCellStyle(timeStyle);
			// 构建表头
			HSSFRow headRow = sheet.createRow(2);
			headRow.setHeight((short) 450);
			HSSFCell cell = null;
			for (int i = 0; i < titles.length; i++) {
				if (i == 0) {
					cell = headRow.createCell(i);
					cell.setCellValue(titles[i]);
					cell.setCellStyle(titleLeftStyle);
				} else if (i == titles.length - 1) {
					cell = headRow.createCell(i);
					cell.setCellValue(titles[i]);
					cell.setCellStyle(titleRightStyle);
				} else {
					cell = headRow.createCell(i);
					cell.setCellValue(titles[i]);
					cell.setCellStyle(titleStyle);
				}
			}
			Calendar newdate = Calendar.getInstance();
			String year = String.valueOf(newdate.get(Calendar.YEAR));
			String month = String.valueOf(newdate.get(Calendar.MONTH));
			DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
			// 构建表体数据
					for (int j = 0; j < wageList.size(); j++) {
						Wage wage = wageList.get(j);
						if (j == 1) {
							HSSFRow bodyRow = sheet.createRow(j + 3);
							bodyRow.setHeight((short) 450);
							cell = bodyRow.createCell(0);
							cell.setCellValue(wage.getUser_id());
							cell.setCellStyle(bodyLeftLastStyle);

							cell = bodyRow.createCell(1);
							cell.setCellValue(wage.getUser_name());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(2);
							cell.setCellValue(wage.getYear());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(3);
							cell.setCellValue(wage.getMonth());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(4);
							cell.setCellValue(wage.getWage_basic());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(5);
							cell.setCellValue(decimalFormat.format(wage.getWorking()/8));
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(6);
							cell.setCellValue(wage.getSubsidy());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(7);
							cell.setCellValue(wage.getOvertime_hours());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(8);
							cell.setCellValue(wage.getSocial_security_fund_persional());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(9);
							cell.setCellValue(wage.getSocial_security_fund_company());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(10);
							cell.setCellValue(wage.getIndividual_income_tax());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(11);
							cell.setCellValue(wage.getWage_real());
							cell.setCellStyle(lastStyle);
							
							cell = bodyRow.createCell(12);
							cell.setCellValue(wage.getAgent_distribution_boc());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(13);
							cell.setCellValue(wage.getAgent_distribution_cmbc());
							cell.setCellStyle(lastStyle);
							
							cell = bodyRow.createCell(14);
							cell.setCellValue(wage.getReimbursement());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(15);
							cell.setCellValue(wage.getAchievement());
							cell.setCellStyle(LastBodyRightStyle);
						} else {
							HSSFRow bodyRow = sheet.createRow(j + 3);
							bodyRow.setHeight((short) 450);
							cell = bodyRow.createCell(0);
							cell.setCellValue(wage.getUser_id());
							cell.setCellStyle(bodyLeftStyle);

							cell = bodyRow.createCell(1);
							cell.setCellValue(wage.getUser_name());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(2);
							cell.setCellValue(wage.getYear());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(3);
							cell.setCellValue(wage.getMonth());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(4);
							cell.setCellValue(wage.getWage_basic());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(5);
							cell.setCellValue(decimalFormat.format(wage.getWorking()/8));
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(6);
							cell.setCellValue(wage.getSubsidy());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(7);
							cell.setCellValue(wage.getOvertime_hours());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(8);
							cell.setCellValue(wage.getSocial_security_fund_persional());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(9);
							cell.setCellValue(wage.getSocial_security_fund_company());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(10);
							cell.setCellValue(wage.getIndividual_income_tax());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(11);
							cell.setCellValue(wage.getWage_real());
							cell.setCellStyle(bodyStyle);
							
							cell = bodyRow.createCell(12);
							cell.setCellValue(wage.getAgent_distribution_boc());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(13);
							cell.setCellValue(wage.getAgent_distribution_cmbc());
							cell.setCellStyle(bodyStyle);
							
							cell = bodyRow.createCell(14);
							cell.setCellValue(wage.getReimbursement());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(15);
							cell.setCellValue(wage.getAchievement());
							cell.setCellStyle(bodyrightStyle);
						}

					}
		}
		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * 导出全部季度的工资
	 */
	@Override
	public void exportReportQuarter(String[] titles, ServletOutputStream outputStream, List<Wage> wageList) {
		List<Wage> wageList1 = new ArrayList<Wage>();
		List<Wage> wageList2 = new ArrayList<Wage>();
		List<Wage> wageList3 = new ArrayList<Wage>();
		List<Wage> wageList4 = new ArrayList<Wage>();
		
		wageList1 = wageList.stream().filter(x->x.getMonth()==1||x.getMonth()==2||x.getMonth()==3).collect(Collectors.toList());
		wageList2 = wageList.stream().filter(x->x.getMonth()==4||x.getMonth()==5||x.getMonth()==6).collect(Collectors.toList());
		wageList3 = wageList.stream().filter(x->x.getMonth()==7||x.getMonth()==8||x.getMonth()==9).collect(Collectors.toList());
		wageList4 = wageList.stream().filter(x->x.getMonth()==10||x.getMonth()==11||x.getMonth()==12).collect(Collectors.toList());
		// 创建一个workbook 对应一个excel应用文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		for(int ii = 1;ii<=4;ii++){
			switch (ii) {
			case 1:
				wageList = wageList1;
				break;
			case 2:
				wageList = wageList2;
				break;
			case 3:
				wageList = wageList3;
				break;
			case 4:
				wageList = wageList4;
				break;
			default:
				break;
			}
			HSSFSheet sheet = workBook.createSheet("工资报表第"+ii+"季度");
			ExcelUtil exportUtil = new ExcelUtil(workBook, sheet);
			HSSFCellStyle headStyle = exportUtil.getHeadStyle();
			HSSFCellStyle timeStyle = exportUtil.getTimeStyle();
			HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			HSSFCellStyle titleStyle = exportUtil.getTitleStyle();
			HSSFCellStyle lastStyle = exportUtil.getLastStyle();
			HSSFCellStyle titleRightStyle = exportUtil.getTitleRightStyle();
			HSSFCellStyle bodyrightStyle = exportUtil.getBodyRightStyle();
			HSSFCellStyle LastBodyRightStyle = exportUtil.getLastBodyRightStyle();
			HSSFCellStyle titleLeftStyle = exportUtil.getTitleLeftStyle();
			HSSFCellStyle bodyLeftStyle = exportUtil.getBodyLeftStyle();
			HSSFCellStyle bodyLeftLastStyle = exportUtil.getBodyLeftLastStyle();
			for (int h = 0; h <= 32; h++) {
				sheet.setColumnWidth((short) h, (short) 5600);
			}
			HSSFRow titleRow = sheet.createRow(0);
			titleRow.setHeight((short) 500);
			HSSFCell titlecell = titleRow.createCell(1);
			titlecell.setCellValue("工资报表");
			HSSFRow timeRow = sheet.createRow(1);
			timeRow.setHeight((short) 450);
			HSSFCell timecell = timeRow.createCell(1);
			timecell.setCellValue("工资报表");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 10));
			titlecell.setCellStyle(headStyle);
			timecell.setCellStyle(timeStyle);
			// 构建表头
			HSSFRow headRow = sheet.createRow(2);
			headRow.setHeight((short) 450);
			HSSFCell cell = null;
			for (int i = 0; i < titles.length; i++) {
				if (i == 0) {
					cell = headRow.createCell(i);
					cell.setCellValue(titles[i]);
					cell.setCellStyle(titleLeftStyle);
				} else if (i == titles.length - 1) {
					cell = headRow.createCell(i);
					cell.setCellValue(titles[i]);
					cell.setCellStyle(titleRightStyle);
				} else {
					cell = headRow.createCell(i);
					cell.setCellValue(titles[i]);
					cell.setCellStyle(titleStyle);
				}
			}
			Calendar newdate = Calendar.getInstance();
			String year = String.valueOf(newdate.get(Calendar.YEAR));
			String month = String.valueOf(newdate.get(Calendar.MONTH));
			DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
			// 构建表体数据
					for (int j = 0; j < wageList.size(); j++) {
						Wage wage = wageList.get(j);
						if (j == 1) {
							HSSFRow bodyRow = sheet.createRow(j + 3);
							bodyRow.setHeight((short) 450);
							cell = bodyRow.createCell(0);
							cell.setCellValue(wage.getUser_id());
							cell.setCellStyle(bodyLeftLastStyle);

							cell = bodyRow.createCell(1);
							cell.setCellValue(wage.getUser_name());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(2);
							cell.setCellValue(wage.getYear());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(3);
							cell.setCellValue(wage.getMonth());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(4);
							cell.setCellValue(wage.getWage_basic());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(5);
							cell.setCellValue(decimalFormat.format(wage.getWorking()/8));
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(6);
							cell.setCellValue(wage.getSubsidy());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(7);
							cell.setCellValue(wage.getOvertime_hours());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(8);
							cell.setCellValue(wage.getSocial_security_fund_persional());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(9);
							cell.setCellValue(wage.getSocial_security_fund_company());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(10);
							cell.setCellValue(wage.getIndividual_income_tax());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(11);
							cell.setCellValue(wage.getWage_real());
							cell.setCellStyle(lastStyle);
							
							cell = bodyRow.createCell(12);
							cell.setCellValue(wage.getAgent_distribution_boc());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(13);
							cell.setCellValue(wage.getAgent_distribution_cmbc());
							cell.setCellStyle(lastStyle);
							
							cell = bodyRow.createCell(14);
							cell.setCellValue(wage.getReimbursement());
							cell.setCellStyle(lastStyle);

							cell = bodyRow.createCell(15);
							cell.setCellValue(wage.getAchievement());
							cell.setCellStyle(LastBodyRightStyle);
						} else {
							HSSFRow bodyRow = sheet.createRow(j + 3);
							bodyRow.setHeight((short) 450);
							cell = bodyRow.createCell(0);
							cell.setCellValue(wage.getUser_id());
							cell.setCellStyle(bodyLeftStyle);

							cell = bodyRow.createCell(1);
							cell.setCellValue(wage.getUser_name());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(2);
							cell.setCellValue(wage.getYear());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(3);
							cell.setCellValue(wage.getMonth());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(4);
							cell.setCellValue(wage.getWage_basic());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(5);
							cell.setCellValue(decimalFormat.format(wage.getWorking()/8));
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(6);
							cell.setCellValue(wage.getSubsidy());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(7);
							cell.setCellValue(wage.getOvertime_hours());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(8);
							cell.setCellValue(wage.getSocial_security_fund_persional());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(9);
							cell.setCellValue(wage.getSocial_security_fund_company());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(10);
							cell.setCellValue(wage.getIndividual_income_tax());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(11);
							cell.setCellValue(wage.getWage_real());
							cell.setCellStyle(bodyStyle);
							
							cell = bodyRow.createCell(12);
							cell.setCellValue(wage.getAgent_distribution_boc());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(13);
							cell.setCellValue(wage.getAgent_distribution_cmbc());
							cell.setCellStyle(bodyStyle);
							
							cell = bodyRow.createCell(14);
							cell.setCellValue(wage.getReimbursement());
							cell.setCellStyle(bodyStyle);

							cell = bodyRow.createCell(15);
							cell.setCellValue(wage.getAchievement());
							cell.setCellStyle(bodyrightStyle);
						}

					}
		}
		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	

}
