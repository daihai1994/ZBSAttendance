package net.zhongbenshuo.attendance.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.ExportReportAttendanceRecord;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.WorkingTimeUser;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.mapper.AttendanceRecordMapper;
import net.zhongbenshuo.attendance.mapper.LoginMapper;
import net.zhongbenshuo.attendance.utils.TimeUtils;
import net.zhongbenshuo.attendance.utils.Utils;

@Component
@EnableScheduling
public class QuartzMonthWorking {
	@Autowired
	LoginMapper  loginMapper;
	
	@Autowired
	AttendanceRecordMapper attendanceRecordMapper;
	@Scheduled(cron = "0 0 1 1 * ?")//计算每人每月的出勤情况
	//@Scheduled(cron = "0 53 13 * * ?")//计算每人每月的出勤情况
	public void run() {
		loginMapper.updateUserBase();
		Calendar newdate = Calendar.getInstance();
		String year = String.valueOf(newdate.get(Calendar.YEAR));
		//String month = "12";
		String month = String.valueOf(newdate.get(Calendar.MONTH));
		if(month.length()<2){
			month= "0"+month;
		}
		String yearMonth = year+"-"+month;
		List<WorkingTimeUser> workingTimeUsersList = new ArrayList<WorkingTimeUser>();
		Map<String,List<WorkingTimeUser>> workingTimeUsersMap = new HashMap<String, List<WorkingTimeUser>>();
		
		if(month.length()<2){
			month = "0"+month;
		}
		List<ExportReportAttendanceRecord> recordList = new ArrayList<ExportReportAttendanceRecord>();
		String bt = year+"-"+month+"-01";
		String et = TimeUtils.getLastMonthDate(bt, 1);
		bt  = bt+" 04:00:00";
		et = et+" 03:59:59";
		workingTimeUsersList = attendanceRecordMapper.findWorkingTimeUsersList("1","",yearMonth);//一个月的考勤总览
		for(WorkingTimeUser timeUser : workingTimeUsersList){//遍历list把相同的人，分到map
			String key = timeUser.getUser_name();
			if(workingTimeUsersMap.containsKey(key)){
				List<WorkingTimeUser> list = workingTimeUsersMap.get(key);
				list.add(timeUser);
			}else{
				List<WorkingTimeUser> list = new ArrayList<WorkingTimeUser>();
				list.add(timeUser);
				workingTimeUsersMap.put(key, list);
			}
			
		}
		
		for(String key : workingTimeUsersMap.keySet()){
			List<WorkingTimeUser> list = new ArrayList<WorkingTimeUser>();
			list = workingTimeUsersMap.get(key);
			int workingSize = 0;//工作的个数
			int mealAllowanceSize = 0;//餐补个数
			int laterSize = 0;//迟到个数
			int leaveEarlySize = 0;//早退个数
			int lackSignInSize = 0;//缺少签到卡
			int lackSignBackSize = 0;//缺少签退卡
			float overtimeTimeAll = 0;//加班小时
			int user_id  = 0;
			int company_id = 0;
			for(WorkingTimeUser workingTimeUser:list){
				if(user_id==0){
					user_id = workingTimeUser.getUser_id();
				}
				if(company_id==0){
					company_id = workingTimeUser.getCompany_id();
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
			}
			ExportReportAttendanceRecord all = new ExportReportAttendanceRecord();
			//添加工作日
			all = new ExportReportAttendanceRecord();
			all.setUser_id(user_id);
			all.setCompany_id(company_id);
			all.setWhetherWorking(workingSize);
			
			//添加餐补
			all.setWhetherMealAllowance(mealAllowanceSize);
		
			//添加加班小时
			all.setOvertimeTimeAll(Utils.roundValue(overtimeTimeAll,2));
			
			//添加迟到
			all.setWhetherLater(laterSize);
			
			//添加早退
			all.setWhetherLeaveEarly(leaveEarlySize);
			
			//添加缺签到卡
			all.setLackSignIn(lackSignInSize);
			
			//添加缺签退卡
			all.setLackSignBack(lackSignBackSize);
			recordList.add(all);
		}
		
		
		
		
//		List<WorkingTimeUser> workingTimeUserList = new ArrayList<WorkingTimeUser>();
//		workingTimeUserList = loginMapper.findWorkingTimeUser(yearMonth);
//		List<WorkingTimeUser> workingTimeUserAll = new ArrayList<WorkingTimeUser>();
//		workingTimeUserAll = workingTimeUserList.stream().filter(x->x.getUser_id()!=0).collect(Collectors.toList());
//		workingTimeUserAll.forEach(x->x.setRealityOvertimeTime(x.getOvertimeTime()-x.getVacationTime()>0?x.getOvertimeTime()-x.getVacationTime():0));
//		workingTimeUserAll.forEach(x->System.out.println(x.getRealityOvertimeTime()));
		loginMapper.addUserBaseMonth(recordList,year,month);
	}
}
