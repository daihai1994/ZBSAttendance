package net.zhongbenshuo.attendance.foreground.attendanceRecord.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.media.jfxmedia.logging.Logger;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.service.AttendanceRecordService;

@Controller
@RequestMapping(value = "/AttendanceRecordController", produces = "text/html;charset=UTF-8")
public class AttendanceRecordController {
	@Autowired
	AttendanceRecordService attendanceRecordService;
	
	/**
	 * 查询某一个人的考勤记录以及考勤状态
	 * @param user_id
	 * @param year
	 * @param month
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findAttendanceRecord.do")
	@ResponseBody
	public String findAttendanceRecord(String user_id,String year,String month,String company_id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = attendanceRecordService.findAttendanceRecord(user_id,year,month,company_id);
		return jsonObject.toString();
	}
	/**
	 * 
	 * @param company_id 公司ID
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findUser.do")
	@ResponseBody
	public String findUser(String company_id,HttpSession session,HttpServletRequest request) {
		String str = "";
		str = attendanceRecordService.findUser(company_id);
		return str;
	}
	/**
	 * 导出员工考勤详情
	 * @param session
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportexportReportAttendanceRecord.do")
	@ResponseBody
	public void exportexportReportAttendanceRecord(HttpSession session,HttpServletRequest request,HttpServletResponse response) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=df.format(new Date());
			String [] titles={"员工","日期","星期几","状态","工作时间","加班时间","请假时间",
					"是否迟到","是否早退","迟到时间","早退时间","缺少签到","缺少签退",
					"签到时间","签退时间","签到地址","签退地址","签到类型","签退类型"};
			String user = request.getParameter("user_reportForm");//员工
			String year = request.getParameter("repoetAttendanceRecord_year");//年
			String month = request.getParameter("repoetAttendanceRecord_month");//月
			String company_id = request.getParameter("attendanceRecort_reportForm_company_id");//公司ID
			ServletOutputStream outputStream = response.getOutputStream();
			String fileName = new String((year+"-"+month+" 考勤记录").getBytes(), "ISO8859_1");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName + time+ ".xls");// 组装附件名称和格式
			//attendanceRecordService.exportexportReportAttendanceRecord(titles, outputStream,user,year,month,company_id);
			attendanceRecordService.exportexportReportAttendanceAll(titles, outputStream,user,year,month,company_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
