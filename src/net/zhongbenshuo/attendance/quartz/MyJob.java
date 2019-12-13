package net.zhongbenshuo.attendance.quartz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.bean.OvertimeType;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.configInfor.bean.TimmerCron;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.utils.SpringBeanUtils;
import net.zhongbenshuo.attendance.utils.TimeBucket;
import net.zhongbenshuo.attendance.utils.TimeUtils;
import net.zhongbenshuo.attendance.utils.Utils;

public class MyJob implements Job {
	private static int QIANDAO = 1;//签到
	private static int QIANTUI = 2;//签退
	private static int QIANDAOCHENGGONG = 1;//签到成功
	private static int QIANTUICHENGGONG = 2;//签退成功
	private static int QIANDAOCHIDAO = 3;//迟到打卡
	private static int QIANTUICHIDAO = 4;//早退打卡
	private static int DAISHENPI = 5;//待审批
	private static int SHENPIZHONG = 6;//审批中
	private static int SHENPICHENGGONG = 7;//审批成功
	private static int SGENPISHIBAI = 8;//审批失败
	private static  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	ConfigInforMapper configInforMapper = (ConfigInforMapper) SpringBeanUtils.getBean("configInforMapper");
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.println("进入myjob");
		List<TimmerCron> table_timer = new ArrayList<TimmerCron>();
		TriggerKey map = jobExecutionContext.getTrigger().getKey();
		String c = map.getName();
		table_timer = configInforMapper.findTimer(c);
		for(TimmerCron cron : table_timer){
			switch (cron.getGroup_id()) {
			case "1"://1是计算每日工作小时
				JSONObject object  = new JSONObject();
				object = TimeUtils.getUpdateDate(-1);//当前计算工时的开始时间，结束时间
				String company_id = cron.getCompany_id();//公司ID
				List<User> userList = new ArrayList<User>();//用户列表
				userList = configInforMapper.findUserList(company_id);
				String bt = object.optString("bt");//开始时间
				String et = object.optString("et");//结束时间
				//String bt = "2019-12-10 04:00:00";
				//String et = "2019-12-11 04:00:00";
				String date = TimeUtils.getCurrentDate();
				try {
					date = TimeUtils.delayDates(date, -1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//date = bt.substring(0, 10);
				WorkingTime workingTime = new WorkingTime();
				workingTime = configInforMapper.findworkingTime(date);
				List<OvertimeType> overtimeType = new ArrayList<OvertimeType>();
				overtimeType = configInforMapper.findOvertimeType();//加班的类型
				for(User user : userList){
					/*************************************修改人员的工作小时***************************************************************/
					int user_id = user.getUser_id();
					boolean qiandaostatus = false;//默认签到状态是false
					boolean qiantuistatus = false;//默认签退状态是false
					int qiandaoMinute = 0;//签到迟到多少分钟
					int qiantuiMinute = 0;//签退早多少分钟
					float hours = 0;//一天工作了几个小时
					boolean qiandaoWaiQin = false;//外勤签到
					boolean qiantuiWaiQin = false;//外勤签退
					List<AttendanceRecord> attendanceRecords = new ArrayList<AttendanceRecord>();//每人每天的考勤详情（包括正常打卡，补卡，外勤等）
					attendanceRecords = configInforMapper.findAttendanceRecordByUserId(user_id,bt,et);
					List<AttendanceRecord> attendanceRecordQIANDAO = new ArrayList<AttendanceRecord>();//每人每天的签到考勤详情
					List<AttendanceRecord> attendanceRecordQIANTUI = new ArrayList<AttendanceRecord>();//每人每天的签退考勤详情
					AttendanceRecord attendanceQianDao = new AttendanceRecord();//签到的第一条
					AttendanceRecord attendanceQianTui = new AttendanceRecord();//签退的第一条
					for(AttendanceRecord attendanceRecord : attendanceRecords){//遍历全部的，分为签到和签退两个list
						if(attendanceRecord.getAttendance_type()==QIANDAO){
							attendanceRecordQIANDAO.add(attendanceRecord);
							if(!qiandaoWaiQin){
								if(attendanceRecord.getOut_attendance()==1&&attendanceRecord.getResult_id()==SHENPICHENGGONG){//外勤签到是否审批成功
									qiandaoWaiQin = true;
								}
							}
						}else if(attendanceRecord.getAttendance_type()==QIANTUI){
							attendanceRecordQIANTUI.add(attendanceRecord);
							if(!qiantuiWaiQin){
								if(attendanceRecord.getOut_attendance()==1&&attendanceRecord.getResult_id()==SHENPICHENGGONG){//外勤签退是否审批成功
									qiantuiWaiQin = true;
								}
							}
						}
					}
					int whetherWorking = 0;//是否工作，1是，0不是，只要有一个签到或者签退，就算是工作
					int whetherMealAllowance = 0;//是否餐补 1是，0不是，只要迟到早退，或者缺少签到或者签退，就没有餐补
					int whetherLater = 0;//是否迟到，1是，0不是
					int whetherLeaveEarly = 0;//是否早退，1是，0不是
					int whetherOverTimes = 0;//是否加班，1是，0不是
					int lackSignIn = 0;//是否缺少签到卡 1是，0不是
					int lackSignBack = 0;//是否缺少签退卡，1是，0不是
					boolean whetherOverTime = false;//是否计算加班，当迟到超过30分钟，就不算加班
					if(attendanceRecordQIANDAO.size()>0){//当签到存在的时候，今天就算工作了
						whetherWorking = 1;
						hours = 8;
						attendanceQianDao = attendanceRecordQIANDAO.get(0);
					}else{//缺少签到卡
						lackSignIn = 1;
					}
					if(attendanceRecordQIANTUI.size()>0){//当签退存在的时候，今天就算工作了
						whetherWorking = 1;
						hours = 8;
						attendanceQianTui = attendanceRecordQIANTUI.get(attendanceRecordQIANTUI.size()-1);
					}else{//缺少签退卡
						lackSignBack = 1;
					}
					if(user.getNationalRestSystem()==1){//国家制度休息
						if(workingTime.getStatus()==0){//0的时候是工作日
							if(qiandaoWaiQin){
								qiandaostatus = true;
								qiandaoMinute = 0;
							}else{
								if(attendanceQianDao.getAppeal_attendance()==1&&attendanceQianDao.getResult_id()==SHENPICHENGGONG){//补卡签到是否审批成功
									if (TimeUtils.getTimeCompareSize(attendanceQianDao.getAttendance_time(), attendanceQianDao.getRule_time_work()) > 1) {//正常补卡签到
										qiandaostatus = true;
										qiantuiMinute = 0;
									}else{
										qiandaostatus = false;
										int minute = TimeUtils.getMinute( attendanceQianDao.getRule_time_work(),attendanceQianDao.getAttendance_time().substring(11, 16));
										qiandaoMinute = minute;
										whetherLater = 1;
									}
									
								}
								if(attendanceQianDao.getResult_id()==QIANDAOCHENGGONG){//签到成功
									qiandaostatus = true;
									qiantuiMinute = 0;
								}
								if(attendanceQianDao.getResult_id()==QIANDAOCHIDAO){//签到迟到
									qiandaostatus = false;
									int minute = TimeUtils.getMinute( attendanceQianDao.getRule_time_work(),attendanceQianDao.getAttendance_time().substring(11, 16));
									qiandaoMinute = minute;
									whetherLater = 1;
								}
							}
							if(qiandaoMinute>user.getLateLimit()){
								whetherOverTime = false;
							}
							if(qiantuiWaiQin){
								qiantuistatus = true;
								qiantuiMinute = 0;
							}else{
								if(attendanceQianTui.getAppeal_attendance()==1&&attendanceQianTui.getResult_id()==SHENPICHENGGONG){//补卡签退是否审批成功
									if (TimeUtils.getTimeCompareSize(attendanceQianTui.getAttendance_time(), attendanceQianTui.getRule_time_off_work()) < 3) {
										// 正常签退
										qiantuistatus = true;
										qiantuiMinute = 0;
									} else {
										// 早退打卡
										qiantuistatus = false;
										String rest_start = attendanceQianTui.getRule_rest_start();//休息开始时间
										String rest_end  = attendanceQianTui.getRule_rest_end();//休息结束时间
										String attendanceRecord_time = attendanceQianTui.getAttendance_time().substring(11, 16);//打卡时间（时分）
										if(attendanceRecord_time.compareTo(rest_end)>=1){//如果打卡时间大于结束休息时间，用打卡时间计算缺勤分钟
											qiantuiMinute = TimeUtils.getMinute(attendanceRecord_time,attendanceQianTui.getRule_time_off_work());
										}else{//其它的都用休息结束时间计算缺勤分钟
											qiantuiMinute = TimeUtils.getMinute(rest_end,attendanceQianTui.getRule_time_off_work());
										}
										whetherLeaveEarly = 1;
									}
								}
								if(attendanceQianTui.getResult_id()==QIANTUICHENGGONG){//签退成功
									qiantuistatus = true;
									qiantuiMinute = 0;
								}
								if(attendanceQianTui.getResult_id()==QIANTUICHIDAO){//早退
									qiantuistatus = false;
									String rest_start = attendanceQianTui.getRule_rest_start();//休息开始时间
									String rest_end  = attendanceQianTui.getRule_rest_end();//休息结束时间
									String attendanceRecord_time = attendanceQianTui.getAttendance_time().substring(11, 16);//打卡时间（时分）
									if(attendanceRecord_time.compareTo(rest_end)>=1){//如果打卡时间大于结束休息时间，用打卡时间计算缺勤分钟
										qiantuiMinute = TimeUtils.getMinute(attendanceRecord_time,attendanceQianTui.getRule_time_off_work());
									}else{//其它的都用休息结束时间计算缺勤分钟
										qiantuiMinute = TimeUtils.getMinute(rest_end,attendanceQianTui.getRule_time_off_work());
									}
									whetherLeaveEarly = 1;
								}
							}
							if(qiantuiMinute>user.getLateLimit()){
								whetherOverTime = false;
							}
							if(qiandaostatus&&qiantuistatus){//当签到签退都是正常的时候，默认是正常的
								hours = 8;
								whetherOverTime = true;
								if(user.getMealAllowance()==1){
									whetherMealAllowance = 1;
								}
							}
						}else{//非工作日
							if(lackSignIn==0&&lackSignBack==0){//到签到卡和签退卡都存在的时候（休息日有加班的情况）
								whetherWorking = 0;//休息日加班 是工作
								whetherOverTime = true;
								if(qiandaoWaiQin){
									qiandaostatus = true;
								}else{
									if(attendanceQianDao.getAppeal_attendance()==1&&attendanceQianDao.getResult_id()==SHENPICHENGGONG){//补卡签到是否审批成功
										if (TimeUtils.getTimeCompareSize(attendanceQianDao.getAttendance_time(), attendanceQianDao.getRule_time_work()) > 1) {//正常补卡签到
											qiandaostatus = true;
										}else{
											qiandaostatus = false;
										}
										
									}
									if(attendanceQianDao.getResult_id()==QIANDAOCHENGGONG){//签到成功
										qiandaostatus = true;
									}
									if(attendanceQianDao.getResult_id()==QIANDAOCHIDAO){//签到迟到
										qiandaostatus = false;
									}
								}
								if(qiantuiWaiQin){
									qiantuistatus = true;
								}else{
									if(attendanceQianTui.getAppeal_attendance()==1&&attendanceQianTui.getResult_id()==SHENPICHENGGONG){//补卡签退是否审批成功
										if (TimeUtils.getTimeCompareSize(attendanceQianTui.getAttendance_time(), attendanceQianTui.getRule_time_off_work()) < 3) {
											// 正常签退
											qiantuistatus = true;
										} else {
											// 早退打卡
											qiantuistatus = false;
										}
									}
									if(attendanceQianTui.getResult_id()==QIANTUICHENGGONG){//签退成功
										qiantuistatus = true;
									}
									if(attendanceQianTui.getResult_id()==QIANTUICHIDAO){//早退
										qiantuistatus = false;
									}
								}
								if(qiandaostatus&&qiantuistatus){//当签到签退都是正常的时候，默认是正常的
									if(user.getMealAllowance()==1){
										whetherMealAllowance = 1;
									}
								}
								whetherLater = 0;//休息日加班 不算迟到
								whetherLeaveEarly = 0;//休息日加班 不算早退
								qiandaoMinute = 0;//休息日加班 不算迟到多少时间
								qiantuiMinute = 0;//休息日加班 不算早退多少时间
								lackSignIn = 0;//休息日加班 不缺少签到卡
								lackSignBack = 0;//休息日加班 不缺少签退卡
								hours = 0;//休息日加班 不算工作时间，全部算成加班时间 （后面计算餐补的时候，直接拿是否餐补计算）
							}
						}
						configInforMapper.updateWorkingUser(user_id,hours,bt.substring(0, 10),whetherWorking,whetherMealAllowance,whetherLater,whetherLeaveEarly,
								qiandaoMinute,qiantuiMinute,lackSignIn,lackSignBack);//修改人员的每天工作时间
					}else if(user.getNationalRestSystem()==0){//非国家制度休息  不计算休息日，每天都是当正常上班计算（最后计算加班时间的时候，把月应该休息日，减去实际休息日，乘以8）
						if(qiandaoWaiQin){
							qiandaostatus = true;
							qiandaoMinute = 0;
						}else{
							if(attendanceQianDao.getAppeal_attendance()==1&&attendanceQianDao.getResult_id()==SHENPICHENGGONG){//补卡签到是否审批成功
								if (TimeUtils.getTimeCompareSize(attendanceQianDao.getAttendance_time(), attendanceQianDao.getRule_time_work()) > 1) {//正常补卡签到
									qiandaostatus = true;
									qiantuiMinute = 0;
								}else{
									qiandaostatus = false;
									int minute = TimeUtils.getMinute( attendanceQianDao.getRule_time_work(),attendanceQianDao.getAttendance_time().substring(11, 16));
									qiandaoMinute = minute;
									whetherLater = 1;
								}
								
							}
							if(attendanceQianDao.getResult_id()==QIANDAOCHENGGONG){//签到成功
								qiandaostatus = true;
								qiantuiMinute = 0;
							}
							if(attendanceQianDao.getResult_id()==QIANDAOCHIDAO){//签到迟到
								qiandaostatus = false;
								int minute = TimeUtils.getMinute( attendanceQianDao.getRule_time_work(),attendanceQianDao.getAttendance_time().substring(11, 16));
								qiandaoMinute = minute;
								whetherLater = 1;
							}
						}
						if(qiandaoMinute>user.getLateLimit()){
							whetherOverTime = false;
						}
						if(qiantuiWaiQin){
							qiantuistatus = true;
							qiantuiMinute = 0;
						}else{
							if(attendanceQianTui.getAppeal_attendance()==1&&attendanceQianTui.getResult_id()==SHENPICHENGGONG){//补卡签退是否审批成功
								if (TimeUtils.getTimeCompareSize(attendanceQianTui.getAttendance_time(), attendanceQianTui.getRule_time_off_work()) < 3) {
									// 正常签退
									qiantuistatus = true;
									qiantuiMinute = 0;
								} else {
									// 早退打卡
									qiantuistatus = false;
									String rest_start = attendanceQianTui.getRule_rest_start();//休息开始时间
									String rest_end  = attendanceQianTui.getRule_rest_end();//休息结束时间
									String attendanceRecord_time = attendanceQianTui.getAttendance_time().substring(11, 16);//打卡时间（时分）
									if(attendanceRecord_time.compareTo(rest_end)>=1){//如果打卡时间大于结束休息时间，用打卡时间计算缺勤分钟
										qiantuiMinute = TimeUtils.getMinute(attendanceRecord_time,attendanceQianTui.getRule_time_off_work());
									}else{//其它的都用休息结束时间计算缺勤分钟
										qiantuiMinute = TimeUtils.getMinute(rest_end,attendanceQianTui.getRule_time_off_work());
									}
									whetherLeaveEarly = 1;
								}
							}
							if(attendanceQianTui.getResult_id()==QIANTUICHENGGONG){//签退成功
								qiantuistatus = true;
								qiantuiMinute = 0;
							}
							if(attendanceQianTui.getResult_id()==QIANTUICHIDAO){//早退
								qiantuistatus = false;
								String rest_start = attendanceQianTui.getRule_rest_start();//休息开始时间
								String rest_end  = attendanceQianTui.getRule_rest_end();//休息结束时间
								String attendanceRecord_time = attendanceQianTui.getAttendance_time().substring(11, 16);//打卡时间（时分）
								if(attendanceRecord_time.compareTo(rest_end)>=1){//如果打卡时间大于结束休息时间，用打卡时间计算缺勤分钟
									qiantuiMinute = TimeUtils.getMinute(attendanceRecord_time,attendanceQianTui.getRule_time_off_work());
								}else{//其它的都用休息结束时间计算缺勤分钟
									qiantuiMinute = TimeUtils.getMinute(rest_end,attendanceQianTui.getRule_time_off_work());
								}
								whetherLeaveEarly = 1;
							}
						}
						if(qiantuiMinute>user.getLateLimit()){
							whetherOverTime = false;
						}
						if(qiandaostatus&&qiantuistatus){//当签到签退都是正常的时候，默认是正常的
							hours = 8;
							whetherOverTime = true;
							if(user.getMealAllowance()==1){
								whetherMealAllowance = 1;
							}
						}
						configInforMapper.updateWorkingUser(user_id,hours,bt.substring(0, 10),whetherWorking,whetherMealAllowance,whetherLater,whetherLeaveEarly,
								qiandaoMinute,qiantuiMinute,lackSignIn,lackSignBack);//修改人员的每天工作时间
					}
					/************************************计算人员的加班时间******************************************************/
					if(user.getEnable_application()==1){//启用加班申请
						List<OverTimeRecord> overTimeRecords = new ArrayList<OverTimeRecord>();//当前人员还没有处理已经审核过的加班申请
						overTimeRecords = configInforMapper.findOverTimeRecords(user_id);
						for(OverTimeRecord overTimeRecord : overTimeRecords){//遍历还没有处理过的加班申请
							float coefficient = overTimeRecord.getCoefficient();//加班系数
							String now = et;
							String overTimeStart = overTimeRecord.getStart_time().substring(0, 19);//加班开始时间
							String overTimeStop = overTimeRecord.getStop_time().substring(0, 19);//加班结束时间
							String start = "";//计算加班开始时间
							String stop = "";//计算加班结束时间
							JSONArray array = new JSONArray();//当前所有的加班时间段（分为一天一天的）
							boolean status = false;//当为false的时候不修改当前加班申请的处理状态
							if(now.compareTo(overTimeStart)<0){//当当前时间小于加班的开始时间，不做处理
								
							}else if(now.compareTo(overTimeStart)>0&&now.compareTo(overTimeStop)<0){//当当前时间大于开始时间，小于结束时间  取开始时间到现在的时间
								 start = overTimeStart;
								 stop = now;
								 status = false;
							}else if(now.compareTo(overTimeStop)>0){//当当前时间大于结束时间  取开始时间到结束时间
								 start = overTimeStart;
								 stop = overTimeStop;
								 status = true;
							}
							if(StringUtils.isNotBlank(start)&&StringUtils.isNotBlank(stop)){//当加班开始时间，结束时间都不为空的时候计算加班时间
								int daysbetween = TimeUtils.getDaysBetween(start, stop);
								String etdays = "";//前一天结束的时候
								if(daysbetween==0){
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("start", start);
									jsonObject.put("stop", stop);
									array.add(jsonObject);
								}else{
									for(int i = 0;i<daysbetween;i++){
										JSONObject jsonObject = new JSONObject();
										if(i==0){
											try {
												jsonObject = TimeUtils.getUpdateDates(start,1);
												etdays = jsonObject.optString("stop").substring(0,10)+bt.substring(10, 19);
												jsonObject.put("stop", etdays);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}else if(i!=daysbetween){
											try {
												jsonObject = TimeUtils.getUpdateDates(etdays,1);
												etdays = jsonObject.optString("stop");
											} catch (Exception e) {
												e.printStackTrace();
											}
										}else if(i==daysbetween){
											try {
												jsonObject.put("start", etdays);
												jsonObject.put("stop", stop);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										array.add(jsonObject);
									}
								}
								for(int i = 0;i<array.size();i++){//遍历每条加班时间段
									JSONObject jsonObject = new JSONObject();
									jsonObject = (JSONObject) array.get(i);
									String overTimeTimeStart = jsonObject.optString("start");//加班开始时间
									String overTimeTimeStop = jsonObject.optString("stop");//加班结束时间
									String startTime = "";
									String stopTime = "";
									startTime = overTimeTimeStart.substring(0, 10)+bt.substring(10, 19);
									 try {
										stopTime = TimeUtils.getUpdateDates(startTime,1).optString("stop");
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									attendanceRecords = configInforMapper.findAttendanceRecordByUserId(user_id,startTime,stopTime);
									String attendanceQianDaoTime = "";//实际最早的签到时间
									String attendanceQianTuiTime = "";//实际最迟的签退时间
									String actualOverTimeTimeStart = "";//实际加班开始时间
									String actualOverTimeTimeStop = "";//实际加班结束时间
									for(AttendanceRecord attendanceRecord : attendanceRecords){//遍历考勤记录，查看签到第一条，和签退最后一条
										if(attendanceRecord.getAttendance_type()==QIANDAO){//当是签到的时候
											if(attendanceQianDaoTime.equals("")){
												attendanceQianDaoTime = attendanceRecord.getAttendance_time().substring(0, 19);
											}else if(attendanceRecord.getAttendance_time().compareTo(attendanceQianDaoTime)<0){//获取更早的签到时间
												attendanceQianDaoTime = attendanceRecord.getAttendance_time().substring(0, 19);
											}
										}else if(attendanceRecord.getAttendance_type()==QIANTUI){//当是签退的时候
											if(attendanceQianTuiTime.equals("")){
												attendanceQianTuiTime = attendanceRecord.getAttendance_time().substring(0, 19);
											}else if(attendanceRecord.getAttendance_time().compareTo(attendanceQianTuiTime)>0){//获取更迟的签退时间
												attendanceQianTuiTime = attendanceRecord.getAttendance_time().substring(0, 19);
											}
										}
									}
									if(StringUtils.isBlank(attendanceQianDaoTime)||StringUtils.isBlank(attendanceQianTuiTime)){//实际的签到时间或者签退时间有一个是空的就结束
										
									}else {//当都不是空的时候  (加班的开始时间和实际的签到签退时间做对比)
										try {
											TimeBucket[] buckets = {
									                new TimeBucket(overTimeTimeStart, overTimeTimeStop),
									                new TimeBucket(attendanceQianDaoTime, attendanceQianTuiTime)
									        };
											 TimeBucket union = TimeBucket.union(buckets);
										        if (null != union) {
										            System.out.println("存在重叠区域,重叠时间段:" + union.toString());
										          //当实际加班开始时间和实际加班结束时间都不为空的时候计算实际加班了多少小时
													int minute = TimeUtils.getDaysMinute(sdf.format(union.getStart()), sdf.format(union.getEnd()));
													float overtimeHours = Float.valueOf(Float.valueOf(minute)/Float.valueOf(60))*coefficient;
													overtimeHours = Utils.roundValue(overtimeHours,2);
													if(overtimeHours<0){
														overtimeHours = 0;
													}
													//configInforMapper.updateUserOvertime(user_id,overtimeHours,overTimeTimeStart.substring(0, 10));//修改人员的加班时间
													float allovertimeHour = 0;
													String allovertimeHourstring = "0";
													allovertimeHourstring = configInforMapper.findOvertimeHour(user_id);//查询人员的所有加班时间
													if(StringUtils.isNotBlank(allovertimeHourstring)){
														allovertimeHour = Float.valueOf(allovertimeHourstring);
													}
													allovertimeHour+=overtimeHours;
													configInforMapper.updateOvertimeUser(user_id,allovertimeHour);//修改人员的加班时间
													if(status){
														configInforMapper.updateOvertimeStatus(overTimeRecord.getId());//修改加班申请的状态
													}
										        }
										} catch (Exception e) {
											e.printStackTrace();
										}
										
									}
									
								}
							}
							
						}
					}else{//不启用申请加班
						if(whetherOverTime){//当计算加班的时候才计算加班
							int status = workingTime.getStatus();//当天是否是工作日，休息日，节假日
							float coefficient = 0;//加班系数
							String overTimestart = "";//加班开始时间
							String overTimeend = "";//加班结束时间
							if(user.getNationalRestSystem()==0){
								status = 0;
							}
							switch (status) {
							case 0://工作日
								for(OvertimeType type : overtimeType){
									if(type.getOvertime_type_id()==1){
										coefficient = type.getCoefficient();
										break;
									}
								}
								break;
							case 1://休息日
								for(OvertimeType type : overtimeType){
									if(type.getOvertime_type_id()==2){
										coefficient = type.getCoefficient();
										break;
									}
								}
								break;
							case 2://节假日
								for(OvertimeType type : overtimeType){
									if(type.getOvertime_type_id()==3){
										coefficient = type.getCoefficient();
										break;
									}
								}
								break;
							default:
								break;
							}
							float overtimeHours = 0;
							if(status==0){
								float overtimeHoursQianTui = 0;
								float overtimeHoursQianDao = 0;
								if(StringUtils.isNotBlank(attendanceQianTui.getAttendance_time())){
									String end = bt.substring(0, 10)+" "+attendanceQianTui.getRule_time_off_work()+":00";
									if(attendanceQianTui.getAttendance_time().compareTo(end)>0){
										overTimestart = end;
										overTimeend = attendanceQianTui.getAttendance_time();
									}
								}
								int minute = 0;
								if(StringUtils.isBlank(overTimestart)||StringUtils.isBlank(overTimeend)){
									minute = 0;
								}else{
									minute = TimeUtils.getDaysMinute(overTimestart, overTimeend);
								}
								if(minute<0){
									minute = 0;
								}
								overtimeHoursQianTui = Float.valueOf(Float.valueOf(minute)/Float.valueOf(60))*coefficient;
								overtimeHoursQianTui = Utils.roundValue(overtimeHoursQianTui,2);
								if(overtimeHoursQianTui<0.5){
									overtimeHoursQianTui = 0;
								}else{
									whetherOverTimes = 1;
								}
								overTimestart = "";
								overTimeend = "";
								if(StringUtils.isNotBlank(attendanceQianDao.getAttendance_time())){
									String start = bt.substring(0, 10)+" "+attendanceQianDao.getRule_time_work()+":00";
									if(attendanceQianDao.getAttendance_time().compareTo(start)>0){
										overTimestart = attendanceQianDao.getAttendance_time();
										overTimeend = start;
									}
								}else
								 minute = 0;
								if(StringUtils.isBlank(overTimestart)||StringUtils.isBlank(overTimeend)){
									minute = 0;
								}else{
									minute = TimeUtils.getDaysMinute(overTimestart, overTimeend);
								}
								if(minute<0){
									minute = 0;
								}
								overtimeHoursQianDao = Float.valueOf(Float.valueOf(minute)/Float.valueOf(60))*coefficient;
								overtimeHoursQianDao = Utils.roundValue(overtimeHoursQianDao,2);
								if(overtimeHoursQianDao<0.5){
									overtimeHoursQianDao = 0;
								}else{
									whetherOverTimes = 1;
								}
								overtimeHours = overtimeHoursQianDao+overtimeHoursQianTui;
							}else{
								if(StringUtils.isNotBlank(attendanceQianTui.getAttendance_time())&&StringUtils.isNotBlank(attendanceQianDao.getAttendance_time())){
									overTimestart = attendanceQianDao.getAttendance_time();
									overTimeend = attendanceQianTui.getAttendance_time();
								}
								int minute = 0;
								if(StringUtils.isBlank(overTimestart)||StringUtils.isBlank(overTimeend)){
									minute = 0;
								}else{
									minute = TimeUtils.getDaysMinute(overTimestart, overTimeend);
								}
								if(minute<0){
									minute = 0;
								}
								 overtimeHours = Float.valueOf(Float.valueOf(minute)/Float.valueOf(60))*coefficient;
								overtimeHours = Utils.roundValue(overtimeHours,2);
								if(overtimeHours<0.5){
									overtimeHours = 0;
								}else{
									whetherOverTimes = 1;
								}
							}
							configInforMapper.updateUserOvertime(user_id,overtimeHours,bt.substring(0, 10),whetherOverTimes);//修改人员的加班时间
							float allovertimeHour = 0;
							String allovertimeHourstring = "0";
							allovertimeHourstring = configInforMapper.findOvertimeHour(user_id);//查询人员的所有加班时间
							if(StringUtils.isNotBlank(allovertimeHourstring)){
								allovertimeHour = Float.valueOf(allovertimeHourstring);
							}
							allovertimeHour+=overtimeHours;
							configInforMapper.updateOvertimeUser(user_id,allovertimeHour);//修改人员的加班时间
						}
					}
					
					/************************************计算人员的假期时间******************************************************/
					List<VacationRecord> vacationRecords = new ArrayList<VacationRecord>();//当前人员还没有处理已经审核过的假期申请
					vacationRecords = configInforMapper.findVacationRecords(user_id);
					for(VacationRecord vacationRecord : vacationRecords){//遍历还没有处理过的加班申请
						float coefficient = vacationRecord.getCoefficient();//假期系数
						String now = et;
						String vacationStart = vacationRecord.getStart_time().substring(0, 19);//假期开始时间
						String vacationStop = vacationRecord.getStop_time().substring(0, 19);//假期结束时间
						String start = "";//计算假期开始时间
						String stop = "";//计算假期结束时间
						JSONArray array = new JSONArray();//当前所有的假期时间段（分为一天一天的）
						boolean status = false;//当为false的时候不修改当前假期申请的处理状态
						if(now.compareTo(vacationStart)<0){//当当前时间小于假期的开始时间，不做处理
							
						}else if(now.compareTo(vacationStart)>0&&now.compareTo(vacationStop)<0){//当当前时间大于开始时间，小于结束时间  取开始时间到现在的时间
							 start = vacationStart;
							 stop = now;
							 status = false;
						}else if(now.compareTo(vacationStop)>0){//当当前时间大于结束时间  取开始时间到结束时间
							 start = vacationStart;
							 stop = vacationStop;
							 status = true;
						}
						if(StringUtils.isNotBlank(start)&&StringUtils.isNotBlank(stop)){//当假期开始时间，结束时间都不为空的时候计算加班时间
							int daysbetween = TimeUtils.getDaysBetween(start, stop);
							String etdays = "";//前一天结束的时候
							if(daysbetween==0){
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("start", start);
								jsonObject.put("stop", stop);
								array.add(jsonObject);
							}else{
								for(int i = 0;i<daysbetween;i++){
									JSONObject jsonObject = new JSONObject();
									if(i==0){
										try {
											jsonObject = TimeUtils.getUpdateDates(start,1);
											etdays = jsonObject.optString("stop").substring(0,10)+bt.substring(10, 19);
											jsonObject.put("stop", etdays);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}else if(i!=daysbetween){
										try {
											jsonObject = TimeUtils.getUpdateDates(etdays,1);
											etdays = jsonObject.optString("stop");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}else if(i==daysbetween){
										try {
											jsonObject.put("start", etdays);
											jsonObject.put("stop", stop);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									array.add(jsonObject);
								}
							}
							for(int i = 0;i<array.size();i++){//遍历每条假期时间段
								JSONObject jsonObject = new JSONObject();
								jsonObject = (JSONObject) array.get(i);
								String vacationTimeStart = jsonObject.optString("start");//假期开始时间
								String vacationTimeStop = jsonObject.optString("stop");//假期结束时间
								String startTime = "";
								String stopTime = "";
								startTime = vacationTimeStart.substring(0, 10)+bt.substring(10, 19);
								 try {
									stopTime = TimeUtils.getUpdateDates(startTime,1).optString("stop");
								} catch (Exception e) {
									e.printStackTrace();
								}
								attendanceRecords = configInforMapper.findAttendanceRecordByUserId(user_id,startTime,stopTime);
								String attendanceQianDaoTime = "";//实际最早的签到时间
								String attendanceQianTuiTime = "";//实际最迟的签退时间
								String actualvacationTimeStart = "";//实际假期开始时间
								String actualvacationTimeStop = "";//实际假期结束时间
								String actualvacationTimeStart1 = "";//实际假期开始时间
								String actualvacationTimeStop1 = "";//实际假期结束时间
								for(AttendanceRecord attendanceRecord : attendanceRecords){//遍历考勤记录，查看签到第一条，和签退最后一条
									if(attendanceRecord.getAttendance_type()==QIANDAO){//当是签到的时候
										if(attendanceQianDaoTime.equals("")){
											attendanceQianDaoTime = attendanceRecord.getAttendance_time().substring(0, 19);
										}else if(attendanceRecord.getAttendance_time().compareTo(attendanceQianDaoTime)<0){//获取更早的签到时间
											attendanceQianDaoTime = attendanceRecord.getAttendance_time().substring(0, 19);
										}
									}else if(attendanceRecord.getAttendance_type()==QIANTUI){//当是签退的时候
										if(attendanceQianTuiTime.equals("")){
											attendanceQianTuiTime = attendanceRecord.getAttendance_time().substring(0, 19);
										}else if(attendanceRecord.getAttendance_time().compareTo(attendanceQianTuiTime)>0){//获取更迟的签退时间
											attendanceQianTuiTime = attendanceRecord.getAttendance_time().substring(0, 19);
										}
									}
								}
//								if(attendanceRecordQIANDAO.get(0)!=null&&StringUtils.isNotBlank(attendanceRecordQIANDAO.get(0).getAttendance_time())){
//									attendanceQianDaoTime = attendanceRecordQIANDAO.get(0).getAttendance_time().substring(0, 19);
//								}
//								if(attendanceRecordQIANTUI.get(0)!=null&&StringUtils.isNotBlank(attendanceRecordQIANTUI.get(0).getAttendance_time())){
//									attendanceQianTuiTime = attendanceRecordQIANTUI.get(0).getAttendance_time().substring(0, 19);
//								}
								if(StringUtils.isBlank(attendanceQianDaoTime)||StringUtils.isBlank(attendanceQianTuiTime)){//实际的签到时间或者签退时间有一个是空的就结束
									
								}else {//当都不是空的时候  (假期的开始时间和实际的签到签退时间做对比)
									if(vacationTimeStart.compareTo(attendanceQianDaoTime)<0&&vacationTimeStop.compareTo(attendanceQianDaoTime)>0
											&&vacationTimeStop.compareTo(attendanceQianTuiTime)<0){
										//当休假时间小于签到时间且休假结束时间大于签到时间且休假结束时间小于签退时间
										actualvacationTimeStart = vacationTimeStart;
										actualvacationTimeStop = attendanceQianDaoTime;
									}else if(vacationTimeStart.compareTo(attendanceQianTuiTime)>0){
										//当休假时间大于签退时间的时候
										actualvacationTimeStart = vacationTimeStart;
										actualvacationTimeStop = vacationTimeStop;
									}else if(vacationTimeStop.compareTo(attendanceQianDaoTime)<0){
										//当休假开始时间小于签到时间且休假结束时间小于签到时间
										actualvacationTimeStart = vacationTimeStart;
										actualvacationTimeStop = vacationTimeStop;
									}else if(vacationTimeStart.compareTo(attendanceQianDaoTime)<0
											&&vacationTimeStop.compareTo(attendanceQianTuiTime)>0){
										//当休假开始时间小于签到时间且休假结束时间大于签退时间
										actualvacationTimeStart = vacationTimeStart;
										actualvacationTimeStop = attendanceQianDaoTime;
										actualvacationTimeStart1 = attendanceQianTuiTime;
										actualvacationTimeStop1 = vacationTimeStop;
									}else if(vacationTimeStart.compareTo(attendanceQianDaoTime)>0&&vacationTimeStart.compareTo(attendanceQianTuiTime)<0
											&&vacationTimeStop.compareTo(attendanceQianTuiTime)>0){
										//当休假开始时间小于签退时间，大于签到时间且休假结束时间大于签退时间
										actualvacationTimeStart = attendanceQianTuiTime;
										actualvacationTimeStop = vacationTimeStop;
									}
									float allVacationHours = 0;//总的请假时间
									if(StringUtils.isBlank(actualvacationTimeStart)&&StringUtils.isBlank(actualvacationTimeStop)){
										//当实际假期开始时间和实际及爱妻结束时间都不为空的时候计算实际加班了多少小时
										int minute = TimeUtils.getDaysMinute(actualvacationTimeStart, actualvacationTimeStop);
										float vacationHours = Float.valueOf(Float.valueOf(minute)/Float.valueOf(60))*coefficient;
										vacationHours = Utils.roundValue(vacationHours,2);
										if(vacationHours<0){
											vacationHours = 0;
										}
										allVacationHours +=vacationHours;
									}
									if(StringUtils.isBlank(actualvacationTimeStart1)&&StringUtils.isBlank(actualvacationTimeStop1)){
										//当实际假期开始时间和实际及爱妻结束时间都不为空的时候计算实际加班了多少小时
										int minute = TimeUtils.getDaysMinute(actualvacationTimeStart1, actualvacationTimeStop1);
										float vacationHours = Float.valueOf(Float.valueOf(minute)/Float.valueOf(60))*coefficient;
										vacationHours = Utils.roundValue(vacationHours,2);
										if(vacationHours<0){
											vacationHours = 0;
										}
										allVacationHours +=vacationHours;
									}
									if(allVacationHours!=0){
										configInforMapper.updateUservacation(user_id,allVacationHours,vacationTimeStart.substring(0, 10));//修改人员的休假时间
										float allovertimeHour = 0;
										String allovertimeHourstring = "0";
										allovertimeHourstring = configInforMapper.findOvertimeHour(user_id);//查询人员的所有加班时间
										if(StringUtils.isNotBlank(allovertimeHourstring)){
											allovertimeHour = Float.valueOf(allovertimeHourstring);
										}
										if(allovertimeHour-allVacationHours>=0){
											configInforMapper.updateOvertimeUser(user_id,allovertimeHour-allVacationHours);//修改人员的加班时间
										}else{
											configInforMapper.updateOvertimeUser(user_id,0);//修改人员的加班时间
											float alluserBasevacationHour = 0;
											alluserBasevacationHour = configInforMapper.findAllUserBaseVacationHours(user_id);//查询人员的所有请假时间
											alluserBasevacationHour+=(allVacationHours-allovertimeHour);
											configInforMapper.updateAllUserBaseVacationHours(user_id,alluserBasevacationHour);//修改人员的所有请假时间
										}
										
										if(status){
											configInforMapper.updateVacationStatus(vacationRecord.getId());//修改休假申请的状态
										}
									}
								}
								
							}
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}
}
