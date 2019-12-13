package net.zhongbenshuo.attendance.foreground.attendanceRecord.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.ExportReportAttendanceRecord;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.WorkingTimeUser;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.mapper.AttendanceRecordMapper;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.service.AttendanceRecordService;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;
import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.ExcelUtil;
import net.zhongbenshuo.attendance.utils.TimeUtils;
import net.zhongbenshuo.attendance.utils.Utils;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {
	public static Logger logger = LogManager.getLogger(AttendanceRecordServiceImpl.class);
	
	@Autowired
	AttendanceRecordMapper attendanceRecordMapper;
	
	
	@Autowired
	LoggerMapper loggerMapper;

	
	@Override
	public JSONObject findAttendanceRecord(String user_id, String year, String month,String company_id) {
		JSONObject jsonObject = new  JSONObject();
		List<WorkingTime> workingTimes = new ArrayList<WorkingTime>();//这个月的应勤数据
		List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();//这个月的考勤记录
		 String et = TimeUtils.getLastDayOfMonth(Integer.valueOf(year),Integer.valueOf(month)); 
		 String bt = TimeUtils.getFirstDayOfMonth(Integer.valueOf(year),Integer.valueOf(month));  
		recordList = attendanceRecordMapper.findAttendanceRecord(user_id,et+" 23:59:59",bt+" 00:00:00");//查询这个月的考勤记录
		workingTimes = attendanceRecordMapper.findWorkingTimes(bt,et,Integer.valueOf(company_id));//查询这个月的应勤记录
		Map<String,Object> map = new HashMap<String,Object>();
		for(WorkingTime time : workingTimes){
			JSONObject object = new JSONObject();
			if(time.getStatus()==0){//0是工作日
				object.put("am",7);
				object.put("pm",7);
			}else{//其它的是休息日
				object.put("am",-1);
				object.put("pm",-1);
			}
			map.put(time.getDate(), object);
			for(AttendanceRecord record : recordList){
				String recordTime = record.getAttendance_time().substring(0, 10);
				if(time.getDate().equals(recordTime)){
					if(map.containsKey(time.getDate())){
						 object = (JSONObject) map.get(time.getDate());
						String key = "";
						int value = 7;//1是正常，2是迟到早退，3是外勤正常，4是外勤待审批，5是外勤审批中，6是审批失败，7是缺卡
						switch (record.getResult_id()) {
						case 1://签到成功
							key = "am";
							value = 1;
							break;
						case 2://签退成功
							key = "pm";
							value = 1;
							break;
						case 3://迟到打卡
							key = "am";
							if(time.getStatus()==0){//0是工作日
								value = 2;
							}else{//其它的是休息日
								value = 1;
							}
							break;
						case 4://早退打卡
							key = "pm";
							if(time.getStatus()==0){//0是工作日
								value = 2;
							}else{//其它的是休息日
								value = 1;
							}
							break;
						case 5://外勤签到成功
							key = "am";
							value = 3;
							break;
						case 6://外勤签退成功
							key = "pm";
							value = 3;
							break;
						case 7://外勤签到待审批
							key = "am";
							value = 4;
							break;
						case 8://外勤签退待审批
							key = "pm";
							value = 4;
							break;
						case 9://外勤签到审批中
							key = "am";
							value = 5;
							break;
						case 10://外勤签退审批中
							key = "pm";
							value = 5;
							break;
						case 11://外勤签到失败
							key = "am";
							value = 6;
							break;
						case 12://外勤签退失败
							key = "pm";
							value = 6;
							break;
						default:
							break;
						}
						if(key.equals("am")){
							if(object.optInt(key)==7){
								object.put(key,value);
							}
						}else{
							object.put(key,value);
						}
						
					}else{
						 object = (JSONObject) map.get(time.getDate());
						String key = "";
						int value = 7;//1是正常，2是迟到早退，3是外勤正常，4是外勤待审批，5是外勤审批中，6是审批失败，7是缺卡,-1是默认，跳过
						switch (record.getResult_id()) {
						case 1://签到成功
							key = "am";
							value = 1;
							break;
						case 2://签退成功
							key = "pm";
							value = 1;
							break;
						case 3://迟到打卡
							key = "am";
							if(time.getStatus()==0){//0是工作日
								value = 2;
							}else{//其它的是休息日
								value = 1;
							}
							break;
						case 4://早退打卡
							key = "pm";
							if(time.getStatus()==0){//0是工作日
								value = 2;
							}else{//其它的是休息日
								value = 1;
							}
							break;
						case 5://外勤签到成功
							key = "am";
							value = 3;
							break;
						case 6://外勤签退成功
							key = "pm";
							value = 3;
							break;
						case 7://外勤签到待审批
							key = "am";
							value = 4;
							break;
						case 8://外勤签退待审批
							key = "pm";
							value = 4;
							break;
						case 9://外勤签到审批中
							key = "am";
							value = 5;
							break;
						case 10://外勤签退审批中
							key = "pm";
							value = 5;
							break;
						case 11://外勤签到失败
							key = "am";
							value = 6;
							break;
						case 12://外勤签退失败
							key = "pm";
							value = 6;
							break;
						default:
							break;
						}
						 object = (JSONObject) map.get(time.getDate());
						object.put(key,value);
						map.put(time.getDate(), object);
					}
				}
			}
		}
		for(String key : map.keySet()){
			JSONObject object =  (JSONObject) map.get(key);
			int am = object.optInt("am");
			int pm = object.optInt("pm");
			System.out.println(key+":am"+am+",pm"+pm);
		}
		List<WorkingTimeUser>  workingTimeUser= new ArrayList<WorkingTimeUser>();
		month = Utils.addZeroForNum(month,2);
		String date = year+"-"+month;
		workingTimeUser = attendanceRecordMapper.findWorkingTimeUser(date,user_id,company_id);//查询这个人的一个月的工时，加班，请假等信息
		jsonObject.put("map", map);
		jsonObject.put("workingTime", workingTimeUser);
		return jsonObject;
	}

	/**
	 * 查询公司下的所有员工
	 * company_id 公司ID
	 */
	@Override
	public String findUser(String company_id) {
		List<Combox> comboxList = new ArrayList<Combox>();
		try {
			comboxList = attendanceRecordMapper.findUser(company_id);
			Combox combox = new Combox();
			combox.setId(00000000);
			combox.setText("全部");
			comboxList.add(0, combox);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询公司下的所有员工报错AttendanceRecordServiceImpl--findUser"+e);
		}
		return JSON.toJSONString(comboxList);
	}
	/**
	 * 
	 * @param titles 表格抬头
	 * @param outputStream
	 * @param user 员工
	 * @param year 年
	 * @param month 月
	 */
	@Override
	public void exportexportReportAttendanceRecord(String[] titles, ServletOutputStream outputStream, String user,
			String year, String month,String company_id) {
		List<ExportReportAttendanceRecord> allList = new ArrayList<ExportReportAttendanceRecord>();
		List<ExportReportAttendanceRecord> exportList = new ArrayList<ExportReportAttendanceRecord>();
		List<WorkingTimeUser> workingTimeUsersList = new ArrayList<WorkingTimeUser>();
		Map<String,WorkingTimeUser> workingTimeUsersMap = new HashMap<String, WorkingTimeUser>();
		if(month.length()<2){
			month = "0"+month;
		}
		int size = 31;
		size = TimeUtils.getDaysByYearMonth(Integer.valueOf(year),Integer.valueOf(month));
		String bt = year+"-"+month+"-01";
		String et = TimeUtils.getLastMonthDate(bt, 1);
		bt  = bt+" 04:00:00";
		et = et+" 03:59:59";
		exportList = attendanceRecordMapper.exportexportReportAttendanceRecord(user,bt,et,company_id);
		workingTimeUsersList = attendanceRecordMapper.findWorkingTimeUsersList(company_id,user,year+"-"+month);
		
		for(WorkingTimeUser timeUser : workingTimeUsersList){
			String key = timeUser.getUser_name()+"-"+timeUser.getDate();
			workingTimeUsersMap.put(key, timeUser);
		}
		Map<String,List<ExportReportAttendanceRecord>> exportMap = new HashMap<String, List<ExportReportAttendanceRecord>>();
		for(ExportReportAttendanceRecord attendanceRecord : exportList){
			if(exportMap.containsKey(attendanceRecord.getUserName())){
				List<ExportReportAttendanceRecord> exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
				exportUserNameList = exportMap.get(attendanceRecord.getUserName());
				exportUserNameList.add(attendanceRecord);
				exportMap.put(attendanceRecord.getUserName(), exportUserNameList);
			}else{
				List<ExportReportAttendanceRecord> exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
				exportUserNameList.add(attendanceRecord);
				exportMap.put(attendanceRecord.getUserName(), exportUserNameList);
			}
		}
		for(String key : exportMap.keySet()){
			List<ExportReportAttendanceRecord> exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
			exportUserNameList = exportMap.get(key);
			if(exportUserNameList==null){
				exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
			}
			String userName = "";
			if(exportUserNameList.size()>0){
				userName = exportUserNameList.get(0).getUserName();
			}
			for(int i = 1;i<=size;i++){
				String day = "";
				if(String.valueOf(i).length()<2){
					day = "0"+String.valueOf(i);
				}else{
					day = String.valueOf(i);
				}
				String time = year+"-"+month+"-"+day;
				WorkingTimeUser workingTimeUser = workingTimeUsersMap.get(userName+"-"+time);
				List<ExportReportAttendanceRecord> exportUserNameDayList = new ArrayList<ExportReportAttendanceRecord>();
				for(ExportReportAttendanceRecord attendanceRecord : exportUserNameList){
					if(StringUtils.isNotBlank(attendanceRecord.getAttendance_time())&&attendanceRecord.getAttendance_time().length()>=10){
						if(attendanceRecord.getAttendance_time().subSequence(0, 10).equals(time)){
							exportUserNameDayList.add(attendanceRecord);
						}
					}
				}
				List<ExportReportAttendanceRecord> signInList = new ArrayList<ExportReportAttendanceRecord>();
				List<ExportReportAttendanceRecord> signBackList = new ArrayList<ExportReportAttendanceRecord>();
				ExportReportAttendanceRecord signIn = new ExportReportAttendanceRecord();
				ExportReportAttendanceRecord signBack = new ExportReportAttendanceRecord();
				ExportReportAttendanceRecord all = new ExportReportAttendanceRecord();
				for(ExportReportAttendanceRecord attendanceRecord : exportUserNameDayList){
					if(StringUtils.isNotBlank(attendanceRecord.getAttendance_type())){
						if(attendanceRecord.getAttendance_type().equals("1")){
							signInList.add(attendanceRecord);
						}else if(attendanceRecord.getAttendance_type().equals("2")){
							signBackList.add(attendanceRecord);
						}
					}else{
						signIn.setUserName(attendanceRecord.getUserName());
					}
					
				}
				if(signInList.size()>0){
					signIn = signInList.get(0);
				}
				if(signBackList.size()>0){
					signBack = signBackList.get(signBackList.size()-1);
				}
				String status = "";
				switch (workingTimeUser.getStatus()) {
				case 0:
					status = "工作日";
					break;
				case 1:
					status = "休息日";
					break;
				case 2:
					status = "节假日";
					break;

				default:
					break;
				}
				all.setWorking(String.valueOf(Utils.roundValue(workingTimeUser.getWorking(), 2)));
				all.setWeek(workingTimeUser.getWeek());
				all.setStatus(status);
				all.setOvertimeTime(String.valueOf(Utils.roundValue(workingTimeUser.getOvertimeTime(), 2)));
				all.setVacationTime(String.valueOf(Utils.roundValue(workingTimeUser.getVacationTime(), 2)));
				all.setTime(time);
				all.setUserName(userName);
				all.setSignInTime(signIn.getAttendance_time());
				all.setSignInAddress(signIn.getAttendance_address());
				all.setSignInType(signIn.getResult());
				all.setSignBackAddress(signBack.getAttendance_address());
				all.setSignBackTime(signBack.getAttendance_time());
				all.setSignBackType(signBack.getResult());
				allList.add(all);
			}
		}
		// 创建一个workbook 对应一个excel应用文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = workBook.createSheet("考勤详情");
		sheet.createFreezePane( 0, 1, 0, 1 );  
		sheet.createFreezePane( 0, 2, 0, 2 ); 
		sheet.createFreezePane( 0, 3, 0, 3 ); 
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
		sheet.setColumnWidth((short) 0, (short) 5600);
		sheet.setColumnWidth((short) 1, (short) 5600);
		sheet.setColumnWidth((short) 2, (short) 5600);
		sheet.setColumnWidth((short) 3, (short) 5600);
		sheet.setColumnWidth((short) 4, (short) 7600);
		sheet.setColumnWidth((short) 5, (short) 7600);
		sheet.setColumnWidth((short) 6, (short) 7600);
		sheet.setColumnWidth((short) 7, (short) 7600);
		sheet.setColumnWidth((short) 8, (short) 7600);
		sheet.setColumnWidth((short) 9, (short) 15600);
		sheet.setColumnWidth((short) 10, (short) 15600);
		sheet.setColumnWidth((short) 11, (short) 5600);
		sheet.setColumnWidth((short) 12, (short) 5600);
		
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) 500);
		HSSFCell titlecell = titleRow.createCell(1);
		titlecell.setCellValue("考勤详情");
		HSSFRow timeRow = sheet.createRow(1);
		timeRow.setHeight((short) 450);
		HSSFCell timecell = timeRow.createCell(1);
		timecell.setCellValue("考勤详情");
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
		// 构建表体数据
				for (int j = 0; j < allList.size(); j++) {
					ExportReportAttendanceRecord export = allList.get(j);
					if (j == 1) {
						HSSFRow bodyRow = sheet.createRow(j + 3);
						bodyRow.setHeight((short) 450);
						cell = bodyRow.createCell(0);
						cell.setCellValue(export.getUserName());
						cell.setCellStyle(bodyLeftLastStyle);

						cell = bodyRow.createCell(1);
						cell.setCellValue(export.getTime());
						cell.setCellStyle(lastStyle);
						
						cell = bodyRow.createCell(2);
						cell.setCellValue(export.getWeek());
						cell.setCellStyle(lastStyle);

						
						cell = bodyRow.createCell(3);
						cell.setCellValue(export.getStatus());
						cell.setCellStyle(lastStyle);

						
						cell = bodyRow.createCell(4);
						cell.setCellValue(export.getWorking());
						cell.setCellStyle(lastStyle);
						
						cell = bodyRow.createCell(5);
						cell.setCellValue(export.getOvertimeTime());
						cell.setCellStyle(lastStyle);
						
						cell = bodyRow.createCell(6);
						cell.setCellValue(export.getVacationTime());
						cell.setCellStyle(lastStyle);


						cell = bodyRow.createCell(7);
						cell.setCellValue(export.getSignInTime());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(8);
						cell.setCellValue(export.getSignBackTime());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(9);
						cell.setCellValue(export.getSignInAddress());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(10);
						cell.setCellValue(export.getSignBackAddress());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(11);
						cell.setCellValue(export.getSignInType());
						cell.setCellStyle(lastStyle);

						cell = bodyRow.createCell(12);
						cell.setCellValue(export.getSignBackType());
						cell.setCellStyle(LastBodyRightStyle);
					} else {
						HSSFRow bodyRow = sheet.createRow(j + 3);
						bodyRow.setHeight((short) 450);
						cell = bodyRow.createCell(0);
						cell.setCellValue(export.getUserName());
						cell.setCellStyle(bodyLeftStyle);

						cell = bodyRow.createCell(1);
						cell.setCellValue(export.getTime());
						cell.setCellStyle(bodyStyle);
						
						cell = bodyRow.createCell(2);
						cell.setCellValue(export.getWeek());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(3);
						cell.setCellValue(export.getStatus());
						cell.setCellStyle(bodyStyle);
						
						cell = bodyRow.createCell(4);
						cell.setCellValue(export.getWorking());
						cell.setCellStyle(bodyStyle);
						
						cell = bodyRow.createCell(5);
						cell.setCellValue(export.getOvertimeTime());
						cell.setCellStyle(bodyStyle);
						
						cell = bodyRow.createCell(6);
						cell.setCellValue(export.getVacationTime());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(7);
						cell.setCellValue(export.getSignInTime());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(8);
						cell.setCellValue(export.getSignBackTime());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(9);
						cell.setCellValue(export.getSignInAddress());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(10);
						cell.setCellValue(export.getSignBackAddress());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(11);
						cell.setCellValue(export.getSignInType());
						cell.setCellStyle(bodyStyle);

						cell = bodyRow.createCell(12);
						cell.setCellValue(export.getSignBackType());
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
	 * 
	 * @param titles 表格抬头
	 * @param outputStream
	 * @param user 员工
	 * @param year 年
	 * @param month 月
	 */
	@Override
	public void exportexportReportAttendanceAll(String[] titless, ServletOutputStream outputStream, String user,
			String year, String month,String company_id) {
		List<ExportReportAttendanceRecord> allLists = new ArrayList<ExportReportAttendanceRecord>();
		List<ExportReportAttendanceRecord> exportList = new ArrayList<ExportReportAttendanceRecord>();
		List<WorkingTimeUser> workingTimeUsersList = new ArrayList<WorkingTimeUser>();
		Map<String,WorkingTimeUser> workingTimeUsersMap = new HashMap<String, WorkingTimeUser>();
		
		Map<String,List<ExportReportAttendanceRecord>> allMap = new HashMap<String, List<ExportReportAttendanceRecord>>();
		List<String> titles = new ArrayList<String>();
		if(month.length()<2){
			month = "0"+month;
		}
		int sizeMonthDays = 31;
		sizeMonthDays = TimeUtils.getDaysByYearMonth(Integer.valueOf(year),Integer.valueOf(month));
		String bt = year+"-"+month+"-01";
		String et = TimeUtils.getLastMonthDate(bt, 1);
		bt  = bt+" 04:00:00";
		et = et+" 03:59:59";
		exportList = attendanceRecordMapper.exportexportReportAttendanceRecord(user,bt,et,company_id);
		workingTimeUsersList = attendanceRecordMapper.findWorkingTimeUsersList(company_id,user,year+"-"+month);
		
		for(WorkingTimeUser timeUser : workingTimeUsersList){
			String key = timeUser.getUser_name()+"-"+timeUser.getDate();
			workingTimeUsersMap.put(key, timeUser);
		}
		Map<String,List<ExportReportAttendanceRecord>> exportMap = new HashMap<String, List<ExportReportAttendanceRecord>>();
		for(ExportReportAttendanceRecord attendanceRecord : exportList){
			if(exportMap.containsKey(attendanceRecord.getUserName())){
				List<ExportReportAttendanceRecord> exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
				exportUserNameList = exportMap.get(attendanceRecord.getUserName());
				exportUserNameList.add(attendanceRecord);
				exportMap.put(attendanceRecord.getUserName(), exportUserNameList);
			}else{
				List<ExportReportAttendanceRecord> exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
				exportUserNameList.add(attendanceRecord);
				exportMap.put(attendanceRecord.getUserName(), exportUserNameList);
			}
		}
		for(String key : exportMap.keySet()){
			List<ExportReportAttendanceRecord> allList = new ArrayList<ExportReportAttendanceRecord>();
			List<ExportReportAttendanceRecord> exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
			exportUserNameList = exportMap.get(key);
			if(exportUserNameList==null){
				exportUserNameList = new ArrayList<ExportReportAttendanceRecord>();
			}
			String userName = "";
			if(exportUserNameList.size()>0){
				userName = exportUserNameList.get(0).getUserName();
			}
			int workingSize = 0;//工作的个数
			int mealAllowanceSize = 0;//餐补个数
			int laterSize = 0;//迟到个数
			int leaveEarlySize = 0;//早退个数
			int lackSignInSize = 0;//缺少签到卡
			int lackSignBackSize = 0;//缺少签退卡
			float overtimeTimeAll = 0;//加班小时
			for(int i = 1;i<=sizeMonthDays;i++){
				String day = "";
				if(String.valueOf(i).length()<2){
					day = "0"+String.valueOf(i);
				}else{
					day = String.valueOf(i);
				}
				String time = year+"-"+month+"-"+day;
				WorkingTimeUser workingTimeUser = workingTimeUsersMap.get(userName+"-"+time);
				List<ExportReportAttendanceRecord> exportUserNameDayList = new ArrayList<ExportReportAttendanceRecord>();
				for(ExportReportAttendanceRecord attendanceRecord : exportUserNameList){
					if(StringUtils.isNotBlank(attendanceRecord.getAttendance_time())&&attendanceRecord.getAttendance_time().length()>=10){
						if(attendanceRecord.getAttendance_time().subSequence(0, 10).equals(time)){
							exportUserNameDayList.add(attendanceRecord);
						}
					}
				}
				List<ExportReportAttendanceRecord> signInList = new ArrayList<ExportReportAttendanceRecord>();
				List<ExportReportAttendanceRecord> signBackList = new ArrayList<ExportReportAttendanceRecord>();
				ExportReportAttendanceRecord signIn = new ExportReportAttendanceRecord();
				ExportReportAttendanceRecord signBack = new ExportReportAttendanceRecord();
				ExportReportAttendanceRecord all = new ExportReportAttendanceRecord();
				for(ExportReportAttendanceRecord attendanceRecord : exportUserNameDayList){
					if(StringUtils.isNotBlank(attendanceRecord.getAttendance_type())){
						if(attendanceRecord.getAttendance_type().equals("1")){
							signInList.add(attendanceRecord);
						}else if(attendanceRecord.getAttendance_type().equals("2")){
							signBackList.add(attendanceRecord);
						}
					}else{
						signIn.setUserName(attendanceRecord.getUserName());
					}
					
				}
				if(signInList.size()>0){
					signIn = signInList.get(0);
				}
				if(signBackList.size()>0){
					signBack = signBackList.get(signBackList.size()-1);
				}
				String status = "";
				switch (workingTimeUser.getStatus()) {
				case 0:
					status = "工作日";
					break;
				case 1:
					status = "休息日";
					break;
				case 2:
					status = "节假日";
					break;

				default:
					break;
				}
				overtimeTimeAll+=workingTimeUser.getOvertimeTime();//加班时间相加
				workingSize+=workingTimeUser.getWhetherWorking();//是否有工时
				mealAllowanceSize+=workingTimeUser.getWhetherMealAllowance();//是否有餐补
				laterSize+=workingTimeUser.getWhetherLater();//是否迟到
				leaveEarlySize+=workingTimeUser.getWhetherLeaveEarly();//是否早退
				if(workingTimeUser.getWhetherWorking()==1){
					lackSignInSize+=workingTimeUser.getLackSignIn();//是否缺少签到卡
					lackSignBackSize+=workingTimeUser.getLackSignBack();//是否缺少签退卡
				}
				all.setWorking(String.valueOf(Utils.roundValue(workingTimeUser.getWorking(), 2)));
				all.setWeek(workingTimeUser.getWeek());
				all.setStatus(status);
				all.setOvertimeTime(String.valueOf(Utils.roundValue(workingTimeUser.getOvertimeTime(), 2)));
				all.setVacationTime(String.valueOf(Utils.roundValue(workingTimeUser.getVacationTime(), 2)));
				all.setTime(time);
				all.setUserName(userName);
				all.setSignInTime(signIn.getAttendance_time());
				all.setSignInAddress(signIn.getAttendance_address());
				all.setSignInType(signIn.getResult());
				all.setSignBackAddress(signBack.getAttendance_address());
				all.setSignBackTime(signBack.getAttendance_time());
				all.setSignBackType(signBack.getResult());
				all.setWhetherWorking(workingTimeUser.getWhetherWorking());
				all.setWhetherMealAllowance(workingTimeUser.getWhetherMealAllowance());
				all.setWhetherLater(workingTimeUser.getWhetherLater());
				all.setWhetherLeaveEarly(workingTimeUser.getWhetherLeaveEarly());
				all.setLaterTime(workingTimeUser.getLaterTime());
				all.setLeaveEarly(workingTimeUser.getLeaveEarly());
				all.setWhetherOverTime(workingTimeUser.getWhetherOverTime());
				all.setLackSignIn(workingTimeUser.getLackSignIn());
				all.setLackSignBack(workingTimeUser.getLackSignBack());
				allList.add(all);
				allLists.add(all);
			}
			ExportReportAttendanceRecord all = new ExportReportAttendanceRecord();
			//添加工作日
			all = new ExportReportAttendanceRecord();
			all.setUserName(userName);
			all.setWhetherWorking(workingSize);
			all.setOther(workingSize);
			allList.add(all);
			//添加餐补
			all = new ExportReportAttendanceRecord();
			all.setUserName(userName);
			all.setWhetherMealAllowance(mealAllowanceSize);
			all.setOther(mealAllowanceSize);
			allList.add(all);
			//添加加班小时
			all = new ExportReportAttendanceRecord();
			all.setUserName(userName);
			all.setOvertimeTimeAll(Utils.roundValue(overtimeTimeAll,2));
			all.setOther(Utils.roundValue(overtimeTimeAll,2));
			allList.add(all);
			//添加迟到
			all = new ExportReportAttendanceRecord();
			all.setUserName(userName);
			all.setWhetherLater(laterSize);
			all.setOther(laterSize);
			allList.add(all);
			//添加早退
			all = new ExportReportAttendanceRecord();
			all.setUserName(userName);
			all.setWhetherLeaveEarly(leaveEarlySize);
			all.setOther(leaveEarlySize);
			allList.add(all);
			//添加缺签到卡
			all = new ExportReportAttendanceRecord();
			all.setUserName(userName);
			all.setLackSignIn(lackSignInSize);
			all.setOther(lackSignInSize);
			allList.add(all);
			//添加缺签退卡
			all = new ExportReportAttendanceRecord();
			all.setUserName(userName);
			all.setLackSignBack(lackSignBackSize);
			all.setOther(lackSignBackSize);
			allList.add(all);
			allMap.put(userName, allList);
		}
		// 创建一个workbook 对应一个excel应用文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = workBook.createSheet("考勤总览");
		HSSFSheet sheet1 = workBook.createSheet("考勤详情");
		sheet.createFreezePane( 0, 1, 0, 1 );  
		sheet.createFreezePane( 0, 2, 0, 2 ); 
		sheet.createFreezePane( 0, 3, 0, 3 ); 
		sheet1.createFreezePane( 0, 1, 0, 1 );  
		sheet1.createFreezePane( 0, 2, 0, 2 ); 
		sheet1.createFreezePane( 0, 3, 0, 3 );
		ExcelUtil exportUtil = new ExcelUtil(workBook, sheet);
		ExcelUtil exportUtil1 = new ExcelUtil(workBook, sheet1);
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
		
		 headStyle = exportUtil1.getHeadStyle();
		 timeStyle = exportUtil1.getTimeStyle();
		 bodyStyle = exportUtil1.getBodyStyle();
		 titleStyle = exportUtil1.getTitleStyle();
		 lastStyle = exportUtil1.getLastStyle();
		 titleRightStyle = exportUtil1.getTitleRightStyle();
		 bodyrightStyle = exportUtil1.getBodyRightStyle();
		 LastBodyRightStyle = exportUtil1.getLastBodyRightStyle();
		 titleLeftStyle = exportUtil1.getTitleLeftStyle();
		 bodyLeftStyle = exportUtil1.getBodyLeftStyle();
		 bodyLeftLastStyle = exportUtil1.getBodyLeftLastStyle();
		for(int i = 0;i<=sizeMonthDays;i++){
			if(i==0){
				sheet.setColumnWidth((short) i, (short) 3000);
				titles.add("姓名");
			}else{
				sheet.setColumnWidth((short) i, (short) 1500);
				titles.add(String.valueOf(i));
			}
		}
		sheet.setColumnWidth((short) sizeMonthDays+1, (short) 2000);
		titles.add("工作");
		sheet.setColumnWidth((short) sizeMonthDays+2, (short) 2000);
		titles.add("餐补");
		sheet.setColumnWidth((short) sizeMonthDays+3, (short) 2000);
		titles.add("加班");
		sheet.setColumnWidth((short) sizeMonthDays+4, (short) 2000);
		titles.add("迟到");
		sheet.setColumnWidth((short) sizeMonthDays+5, (short) 2000);
		titles.add("早退");
		sheet.setColumnWidth((short) sizeMonthDays+6, (short) 2000);
		titles.add("缺签到");
		sheet.setColumnWidth((short) sizeMonthDays+7, (short) 2000);
		titles.add("缺签退");
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) 500);
		HSSFCell titlecell = titleRow.createCell(1);
		titlecell.setCellValue("考勤总览");
		HSSFRow timeRow = sheet.createRow(1);
		timeRow.setHeight((short) 450);
		HSSFCell timecell = timeRow.createCell(1);
		timecell.setCellValue(TimeUtils.getCurrentDateTime());
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, sizeMonthDays+7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, sizeMonthDays+7));
		titlecell.setCellStyle(headStyle);
		timecell.setCellStyle(timeStyle);
		
		// 构建表头
		HSSFRow headRow = sheet.createRow(2);
		headRow.setHeight((short) 450);
		HSSFCell cell = null;
		for (int i = 0; i < titles.size(); i++) {
			if (i == 0) {
				cell = headRow.createCell(i);
				cell.setCellValue(titles.get(i));
				cell.setCellStyle(titleLeftStyle);
			} else if (i == titles.size() - 1) {
				cell = headRow.createCell(i);
				cell.setCellValue(titles.get(i));
				cell.setCellStyle(titleRightStyle);
			} else {
				cell = headRow.createCell(i);
				cell.setCellValue(titles.get(i));
				cell.setCellStyle(titleStyle);
			}
		}
		// 构建表体数据
		int j = 0;
		for(String key : allMap.keySet()){
			HSSFRow bodyRow = sheet.createRow(j + 3);
			bodyRow.setHeight((short) 800);
			List<ExportReportAttendanceRecord> allList = allMap.get(key);
			if((j+1)==allMap.size()){
				for(int xx = -1;xx<allList.size();xx++){
					if(xx==-1){
						cell = bodyRow.createCell(0);
						cell.setCellValue(allList.get(0).getUserName());
						cell.setCellStyle(lastStyle);
					} else if (xx<sizeMonthDays){
						String signInTime = "";
						String signBackTime = "";
						if(StringUtils.isNotBlank(allList.get(xx).getSignInTime())){
							signInTime = allList.get(xx).getSignInTime().substring(11, 16);
						}
						if(StringUtils.isNotBlank(allList.get(xx).getSignBackTime())){
							signBackTime = allList.get(xx).getSignBackTime().substring(11, 16);
						}
						cell = bodyRow.createCell(xx+1);
						cell.setCellValue(signInTime+"\n"+signBackTime);
						cell.setCellStyle(lastStyle);
					}else{
						cell = bodyRow.createCell(xx+1);
						cell.setCellValue(allList.get(xx).getOther().toString());
						if(xx+1==allList.size()){
							cell.setCellStyle(LastBodyRightStyle);
						}else{
							cell.setCellStyle(lastStyle);
						}
						
					}
				}
			}else{
				for(int xx = -1;xx<allList.size();xx++){
					if(xx==-1){
						cell = bodyRow.createCell(0);
						cell.setCellValue(allList.get(0).getUserName());
						cell.setCellStyle(bodyStyle);
					} else if (xx<sizeMonthDays){
						String signInTime = "";
						String signBackTime = "";
						if(StringUtils.isNotBlank(allList.get(xx).getSignInTime())){
							signInTime = allList.get(xx).getSignInTime().substring(11, 16);
						}
						if(StringUtils.isNotBlank(allList.get(xx).getSignBackTime())){
							signBackTime = allList.get(xx).getSignBackTime().substring(11, 16);
						}
						cell = bodyRow.createCell(xx+1);
						cell.setCellValue(signInTime+"\n"+signBackTime);
						cell.setCellStyle(bodyStyle);
					}else{
						cell = bodyRow.createCell(xx+1);
						cell.setCellValue(allList.get(xx).getOther().toString());
						if(xx+1==allList.size()){
							cell.setCellStyle(bodyrightStyle);
						}else{
							cell.setCellStyle(bodyStyle);
						}
						
					}
				}
			}
			j++;
		}
		sheet1.setColumnWidth((short) 0, (short) 3000);//员工
		sheet1.setColumnWidth((short) 1, (short) 3500);//日期
		sheet1.setColumnWidth((short) 2, (short) 2500);//星期几
		sheet1.setColumnWidth((short) 3, (short) 2500);//状态
		sheet1.setColumnWidth((short) 4, (short) 2500);//工作时间
		sheet1.setColumnWidth((short) 5, (short) 2500);//加班时间
		sheet1.setColumnWidth((short) 6, (short) 2500);//请假时间

		sheet1.setColumnWidth((short) 7, (short) 2500);//是否迟到
		sheet1.setColumnWidth((short) 8, (short) 2500);//是否早退
		sheet1.setColumnWidth((short) 9, (short) 2500);//迟到时间
		sheet1.setColumnWidth((short) 10, (short) 2500);//早退时间
		sheet1.setColumnWidth((short) 11, (short) 2500);//缺少签到
		sheet1.setColumnWidth((short) 12, (short) 2500);//缺少签退
		
		sheet1.setColumnWidth((short) 13, (short) 7600);//签到时间
		sheet1.setColumnWidth((short) 14, (short) 7600);//签退时间
		sheet1.setColumnWidth((short) 15, (short) 15600);//签到地址
		sheet1.setColumnWidth((short) 16, (short) 15600);//签退地址
		sheet1.setColumnWidth((short) 17, (short) 5600);//签到类型
		sheet1.setColumnWidth((short) 18, (short) 5600);//签退类型
		
		 titleRow = sheet1.createRow(0);
		titleRow.setHeight((short) 500);
		 titlecell = titleRow.createCell(1);
		titlecell.setCellValue("考勤详情");
		 timeRow = sheet1.createRow(1);
		timeRow.setHeight((short) 450);
		 timecell = timeRow.createCell(1);
		timecell.setCellValue(TimeUtils.getCurrentDateTime());
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 1, 18));
		sheet1.addMergedRegion(new CellRangeAddress(1, 1, 1, 18));
		titlecell.setCellStyle(headStyle);
		timecell.setCellStyle(timeStyle);
		// 构建表头
		 headRow = sheet1.createRow(2);
		headRow.setHeight((short) 450);
		HSSFCell cell1 = null;
		for (int i = 0; i < titless.length; i++) {
			if (i == 0) {
				cell1 = headRow.createCell(i);
				cell1.setCellValue(titless[i]);
				cell1.setCellStyle(titleLeftStyle);
			} else if (i == titless.length - 1) {
				cell1 = headRow.createCell(i);
				cell1.setCellValue(titless[i]);
				cell1.setCellStyle(titleRightStyle);
			} else {
				cell1 = headRow.createCell(i);
				cell1.setCellValue(titless[i]);
				cell1.setCellStyle(titleStyle);
			}
		}
		// 构建表体数据
				for (int jj = 0; jj < allLists.size(); jj++) {
					ExportReportAttendanceRecord export = allLists.get(jj);
					if (jj == allLists.size()-1) {
						HSSFRow bodyRow = sheet1.createRow(jj + 3);
						bodyRow.setHeight((short) 450);
						cell1 = bodyRow.createCell(0);
						cell1.setCellValue(export.getUserName());
						cell1.setCellStyle(bodyLeftLastStyle);

						cell1 = bodyRow.createCell(1);
						cell1.setCellValue(export.getTime());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(2);
						cell1.setCellValue(export.getWeek());
						cell1.setCellStyle(lastStyle);

						
						cell1 = bodyRow.createCell(3);
						cell1.setCellValue(export.getStatus());
						cell1.setCellStyle(lastStyle);

						
						cell1 = bodyRow.createCell(4);
						cell1.setCellValue(export.getWorking());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(5);
						cell1.setCellValue(export.getOvertimeTime());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(6);
						cell1.setCellValue(export.getVacationTime());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(7);
						cell1.setCellValue(export.getWhetherLater());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(8);
						cell1.setCellValue(export.getWhetherLeaveEarly());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(9);
						cell1.setCellValue(export.getLaterTime());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(10);
						cell1.setCellValue(export.getLeaveEarly());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(11);
						cell1.setCellValue(export.getLackSignIn());
						cell1.setCellStyle(lastStyle);
						
						cell1 = bodyRow.createCell(12);
						cell1.setCellValue(export.getLackSignBack());
						cell1.setCellStyle(lastStyle);


						cell1 = bodyRow.createCell(13);
						cell1.setCellValue(export.getSignInTime());
						cell1.setCellStyle(lastStyle);

						cell1 = bodyRow.createCell(14);
						cell1.setCellValue(export.getSignBackTime());
						cell1.setCellStyle(lastStyle);

						cell1 = bodyRow.createCell(15);
						cell1.setCellValue(export.getSignInAddress());
						cell1.setCellStyle(lastStyle);

						cell1 = bodyRow.createCell(16);
						cell1.setCellValue(export.getSignBackAddress());
						cell1.setCellStyle(lastStyle);

						cell1 = bodyRow.createCell(17);
						cell1.setCellValue(export.getSignInType());
						cell1.setCellStyle(lastStyle);

						cell1 = bodyRow.createCell(18);
						cell1.setCellValue(export.getSignBackType());
						cell1.setCellStyle(LastBodyRightStyle);
					} else {
						HSSFRow bodyRow = sheet1.createRow(jj + 3);
						bodyRow.setHeight((short) 450);
						cell1 = bodyRow.createCell(0);
						cell1.setCellValue(export.getUserName());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(1);
						cell1.setCellValue(export.getTime());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(2);
						cell1.setCellValue(export.getWeek());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(3);
						cell1.setCellValue(export.getStatus());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(4);
						cell1.setCellValue(export.getWorking());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(5);
						cell1.setCellValue(export.getOvertimeTime());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(6);
						cell1.setCellValue(export.getVacationTime());
						cell1.setCellStyle(bodyStyle);
						
						
						cell1 = bodyRow.createCell(7);
						cell1.setCellValue(export.getWhetherLater());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(8);
						cell1.setCellValue(export.getWhetherLeaveEarly());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(9);
						cell1.setCellValue(export.getLaterTime());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(10);
						cell1.setCellValue(export.getLeaveEarly());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(11);
						cell1.setCellValue(export.getLackSignIn());
						cell1.setCellStyle(bodyStyle);
						
						cell1 = bodyRow.createCell(12);
						cell1.setCellValue(export.getLackSignBack());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(13);
						cell1.setCellValue(export.getSignInTime());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(14);
						cell1.setCellValue(export.getSignBackTime());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(15);
						cell1.setCellValue(export.getSignInAddress());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(16);
						cell1.setCellValue(export.getSignBackAddress());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(17);
						cell1.setCellValue(export.getSignInType());
						cell1.setCellStyle(bodyStyle);

						cell1 = bodyRow.createCell(18);
						cell1.setCellValue(export.getSignBackType());
						cell1.setCellStyle(bodyrightStyle);
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
