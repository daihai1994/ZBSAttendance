package net.zhongbenshuo.attendance.foreground.wage.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;
import net.zhongbenshuo.attendance.foreground.wage.service.WageService;
import net.zhongbenshuo.attendance.mail.MailModel;
import net.zhongbenshuo.attendance.mail.MailService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.Futil;
import net.zhongbenshuo.attendance.utils.TimeUtils;

@Controller
@RequestMapping(value = "/wageController", produces = "text/html;charset=UTF-8")
public class WageController {
	@Autowired
	WageService wageService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	LoggerMapper loggerMapper;
	
	@RequestMapping(value="/findwage.do")
	@ResponseBody
	private String findwage(int bNum,int rows,int year,int month,String userName,int company_id,HttpSession session,HttpServletRequest httpServletRequest){
		JSONObject jsonObject = new JSONObject();
		jsonObject = wageService.findwage(bNum,rows,year,month,userName,company_id);
		return jsonObject.toString();
	}
	/**
	 * 下载导入模板
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/uploadTemplate.do")
	public @ResponseBody void importUserExcelTemplate(HttpSession session,HttpServletRequest request, HttpServletResponse response) throws IOException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=df.format(new Date());
		String [] titles={"用户ID","用户姓名","年份","月份","基本工资","出勤天数","补助(餐补)","加班时间","个人承担社保","公司承担社保","个人所得税","实发工资","中国银行代发"
				,"民生银行代发","报销","业绩"};
		ServletOutputStream outputStream = response.getOutputStream();
		String fileName = new String(("工资导入模板").getBytes(), "ISO8859_1");
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName + time+ ".xls");// 组装附件名称和格式
		User user = new User();
		user  = (User) session.getAttribute("user");
		int company_id  = 0;
		String department = "";
		String company = "";
		if(user!=null){
			 company_id = user.getCompany_id();
			 department = user.getDepartment();
			 company = user.getCompany_name();
		}
		
		wageService.uploadTemplate(titles, outputStream,company_id,department,company);
	}
	/**
	 * 导出报表
	 * @param session
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/exportReport.do")
	public @ResponseBody void exportReport(HttpSession session,HttpServletRequest request, HttpServletResponse response) throws IOException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=df.format(new Date());
		String [] titles={"用户ID","用户姓名","年份","月份","基本工资","出勤天数","补助(餐补)","加班时间","个人承担社保","公司承担社保","个人所得税","实发工资","中国银行代发"
				,"民生银行代发","报销","业绩"};
		String state_reportForm = request.getParameter("state_reportForm");//报表类型//custom 自定义 month月 quarter季度 year年
		String custom_bt_year = request.getParameter("wage_reportForm_bt_year_custom");//自定义开始时间年
		String custom_bt_month = request.getParameter("wage_reportForm_bt_month_custom");//自定义开始时间月
		String custom_et_year = request.getParameter("wage_reportForm_et_year_custom");//自定义结束时间年
		String custom_et_month = request.getParameter("wage_reportForm_et_month_custom");//自定义结束时间月
		
		String month_year = request.getParameter("wage_reportForm_year_month");//月的年份
		String month_month = request.getParameter("wage_reportForm_month_month");//月的月份
		
		String quarter_year = request.getParameter("wage_reportForm_year_quarter");//季度的年份
		String quarter_month = request.getParameter("wage_reportForm_month_quarter");//季度的月份
		
		String year_year = request.getParameter("wage_reportForm_year_year");//年的年份
		
		String company_id = request.getParameter("wage_reportForm_company_id");//公司ID 
		List<Wage> wageList = new ArrayList<Wage>();
		ServletOutputStream outputStream = response.getOutputStream();
		String fileName = new String(("工资报表").getBytes(), "ISO8859_1");
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName + time+ ".xls");// 组装附件名称和格式
		switch (state_reportForm) {
		case "custom"://自定义
			String bt_year = custom_bt_year;
			String bt_month = custom_bt_month;
			String et_year = custom_et_year;
			String et_month = custom_et_month;
			wageList = wageService.findWage(bt_year,bt_month,et_year,et_month,company_id);
			wageService.exportReportCustom(titles, outputStream,wageList);
			break;
		case "month"://月
			if(month_month.equals("all")){
				wageList = wageService.findWageMonthAll(month_year,company_id);
				wageService.exportReportMonth(titles, outputStream,wageList);
			}else{
				wageList = wageService.findWageMonth(month_year,company_id,month_month);
				wageService.exportReportCustom(titles, outputStream,wageList);
			}
			break;
		case "quarter"://季度
			if(quarter_month.equals("all")){
				wageList = wageService.findWageMonthAll(quarter_year,company_id);
				wageService.exportReportQuarter(titles, outputStream,wageList);
			}else{
				String btMonth = "";
				String etMonth = "";
				switch (quarter_month) {
				case "1":
					btMonth = "1";
					etMonth = "3";
					break;
				case "2":
					btMonth = "4";
					etMonth = "6";
					break;
				case "3":
					btMonth = "7";
					etMonth = "9";
					break;
				case "4":
					btMonth = "10";
					etMonth = "12";
					break;
				default:
					break;
				}
				wageList = wageService.findWageQuarter(quarter_year,company_id,btMonth,etMonth);
				wageService.exportReportCustom(titles, outputStream,wageList);
			}
			break;
		case "year"://年
			wageList = wageService.findWageMonthAll(year_year,company_id);
			wageService.exportReportCustom(titles, outputStream,wageList);
			break;

		default:
			break;
		}
	}
	@RequestMapping(value="/uploadWage.do") 
	@ResponseBody
	public String uploadWage(MultipartFile excelFileWage,HttpSession session,HttpServletRequest request, HttpServletResponse response) throws   Exception{
		User user = new User();
		user  = (User) session.getAttribute("user");
		int company_id  = 0;
		String department_id = "";
		if(user!=null){
			 company_id = user.getCompany_id();
			 department_id = user.getDepartment_id();
		}
		List<String> list = wageService.uploadWage(session,excelFileWage,company_id,department_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", list);
		return jsonObject.toString();
	}
	/**
	 * 发送邮件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sendEmail.do")
	@ResponseBody
	public String sendEmail(HttpServletRequest request,int company_id,int year,int month,HttpSession session) {
		String title = year+"年"+month+"月工资明细";
		List<Wage> wageList = new ArrayList<Wage>();
		String msg = "发送成功";
		wageList = wageService.findWageMail(company_id,year,month);
		try {
			wageList.forEach(x->setAttachMail(x,title));
			String ip = Futil.getIpAddr(request);
			int user_id = Futil.getUserId(session, request);
			String remarks = user_id+"在"+TimeUtils.getCurrentDateTime()+",发送"+year+"年"+month+"月的工资";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			 msg = "发送失败";
			e.printStackTrace();
			System.out.println("发送邮件报错");
		}
		return msg;
	}
	
	private Object setAttachMail(Wage x, String title) {
		String userName= x.getUser_name();
		String email = x.getMail_address();
		//String email = "937138144@qq.com";
		MailModel mail = new MailModel();//创建邮件
		mail.setFromAddress("postmaster@zhongbenshuo.com");//发送人
		mail.setToAddresses(email);//收件人
		mail.setSubject(title);//主题
		StringBuffer sb = new StringBuffer();
		sb.append(title+"<br/><br/><br/>");
		sb.append("&#160;&#160;&#160;&#160;"+userName+",您好!<br/><br/>");
		sb.append("&#160;&#160;&#160;&#160;以下是您"+title+",请注意查收<br/><br/>");
		//StringBuilder content = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<table border=\"1\"   style=\"border-collapse: collapse;Helvetica,Arial,sans-serif;font-size:1rem;width:100%\" >");
		sb.append("<tr style=\"font-weight: bolder;\"><th>姓名</th><th>月份</th><th>基本工资</th>"
				+ "<th>出勤</th>"+ "<th>午餐补贴</th><th>加班时间</th>"
				+ "<th>个人承担社保公积金</th><th>公司承担社保公积金</th><th>个税</th>"
				+ "<th>实发工资</th><th>中行代发</th><th>民生代发</th></tr>");
		sb.append("<tr style=\"text-align: center;\">");
		sb.append("<td>" + x.getUser_name() + "</td>"); //第一列
		sb.append("<td>" + title.substring(0, title.length()-4) + "</td>"); //第二列
		sb.append("<td>" + x.getWage_basic() + "</td>"); //第三列
		sb.append("<td>" + x.getAttendance_days() + "</td>"); //第4列
		sb.append("<td>" + x.getSubsidy() + "</td>"); //第5列
		sb.append("<td>" + x.getOvertime_hours() + "</td>"); //第6列
		sb.append("<td>" + x.getSocial_security_fund_persional() + "</td>"); //第7列
		sb.append("<td>" + x.getSocial_security_fund_company() + "</td>"); //第8列
		sb.append("<td>" + x.getIndividual_income_tax() + "</td>"); //第9列
		sb.append("<td>" + x.getWage_real() + "</td>"); //第10列
		sb.append("<td>" + x.getAgent_distribution_boc() + "</td>"); //第11列
		sb.append("<td>" + x.getAgent_distribution_cmbc() + "</td>"); //第12列
		sb.append("</tr>");
		sb.append("</table><br/><br/><br/>");
		sb.append("<span style=\"float: right;\">人力资源部</span></br>");
		sb.append("<span style=\"float: right;\">"+TimeUtils.getCurrentDateTime()+"</span>");
		//sb.append("<h3>description</h3>");
		sb.append("</body></html>");
		System.out.println(sb.toString());
		mail.setContent(sb.toString());
		String msg ="发送成功";
		try{
			mailService.sendAttachMail(mail);
		}catch(Exception e){
			msg = e.getMessage();
		}
		return msg;
	}
}
