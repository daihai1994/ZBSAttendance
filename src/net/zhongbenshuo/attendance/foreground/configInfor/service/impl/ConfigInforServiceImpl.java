package net.zhongbenshuo.attendance.foreground.configInfor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.configInfor.bean.TimmerCron;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.foreground.configInfor.service.ConfigInforService;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.Futil;

@Service
public class ConfigInforServiceImpl implements ConfigInforService {
	
public static Logger logger = LogManager.getLogger(ConfigInforServiceImpl.class);
	
	@Autowired
	ConfigInforMapper configInforMapper;
	
	@Autowired
	LoggerMapper loggerMapper;
	/***
	 * 查询定时器任务
	 * @param company_id
	 * @return
	 */
	@Override
	public JSONObject findTimmerCron() {
		JSONObject jsonObject = new JSONObject();
		List<TimmerCron> timmerCrons= new ArrayList<TimmerCron>();
		try {
			timmerCrons = configInforMapper.findTimmerCron();
			for(TimmerCron table_timer : timmerCrons){
				if(table_timer.getMode().equals("每隔几秒钟")){
					String time = table_timer.getTtime().substring(17, 19);
					table_timer.setCronName("每隔"+time+"秒钟");
				}
				if(table_timer.getMode().equals("每隔几分钟")){
					String time = table_timer.getTtime().substring(14, 16);
					table_timer.setCronName("每隔"+time+"分钟");
				}
				if(table_timer.getMode().equals("每隔几小时")){
					String time = table_timer.getTtime().substring(11, 13);
					table_timer.setCronName("每隔"+time+"小时");
				}
				if(table_timer.getMode().equals("每天")){
					String time = table_timer.getTtime().substring(11, 19);
					table_timer.setCronName(time);
				}
				if(table_timer.getMode().equals("每周")){
					String cron  = table_timer.getCron();
					String time = "";
					String [] arr2 = cron.split(" ");
					switch (Integer.valueOf(arr2[5])) {
					case 1:
						time = "周日";
						break;
					case 2:
						time = "周一";			
						break;
					case 3:
						time = "周二";
						break;
					case 4:
						time = "周三";
						break;
					case 5:
						time = "周四";
						break;
					case 6:
						time = "周五";
						break;
					case 7:
						time = "周六";
						break;
					default:
						break;
					}
					 time = time+" "+table_timer.getTtime().substring(11, 19);
					table_timer.setCronName(time);
				}
				if(table_timer.getMode().equals("每月")){
					String cron  = table_timer.getCron();
					String time = "";
					String [] arr2 = cron.split(" ");
					time = "每月"+arr2[3]+"号"+arr2[2]+"时"+arr2[1]+"分";
					table_timer.setCronName(time);
				}
				if(table_timer.getMode().equals("每年")){
					String cron  = table_timer.getCron();
					String time = "";
					String [] arr2 = cron.split(" ");
					time = "每年"+arr2[4]+"月"+arr2[3]+"号"+arr2[2]+"时"+arr2[1]+"分";
					table_timer.setCronName(time);
				}
				
			}
		} catch (Exception e) {
			logger.info("查询定时任务报错!");
		}
		jsonObject.put("total",timmerCrons.size());
		jsonObject.put("rows",timmerCrons);
		return jsonObject;
	}
	
	/***
	 * 计算工时
	 * @param company_id
	 * @return
	 */
	@Override
	public JSONObject findTimmerCron_configure(int company_id) {
		JSONObject jsonObject = new JSONObject();
		List<TimmerCron> timmerCrons= new ArrayList<TimmerCron>();
		try {
			timmerCrons = configInforMapper.findTimmerCron_configure(company_id,1);
			for(TimmerCron table_timer : timmerCrons){
				if(table_timer.getMode().equals("每隔几秒钟")){
					String time = table_timer.getTtime().substring(17, 19);
					table_timer.setCronName("每隔"+time+"秒钟");
				}
				if(table_timer.getMode().equals("每隔几分钟")){
					String time = table_timer.getTtime().substring(14, 16);
					table_timer.setCronName("每隔"+time+"分钟");
				}
				if(table_timer.getMode().equals("每隔几小时")){
					String time = table_timer.getTtime().substring(11, 13);
					table_timer.setCronName("每隔"+time+"小时");
				}
				if(table_timer.getMode().equals("每天")){
					String time = table_timer.getTtime().substring(11, 19);
					table_timer.setCronName(time);
				}
				if(table_timer.getMode().equals("每周")){
					String cron  = table_timer.getCron();
					String time = "";
					String [] arr2 = cron.split(" ");
					switch (Integer.valueOf(arr2[5])) {
					case 1:
						time = "周日";
						break;
					case 2:
						time = "周一";			
						break;
					case 3:
						time = "周二";
						break;
					case 4:
						time = "周三";
						break;
					case 5:
						time = "周四";
						break;
					case 6:
						time = "周五";
						break;
					case 7:
						time = "周六";
						break;
					default:
						break;
					}
					 time = time+" "+table_timer.getTtime().substring(11, 19);
					table_timer.setCronName(time);
				}
				if(table_timer.getMode().equals("每月")){
					String cron  = table_timer.getCron();
					String time = "";
					String [] arr2 = cron.split(" ");
					time = "每月"+arr2[3]+"号"+arr2[2]+"时"+arr2[1]+"分";
					table_timer.setCronName(time);
				}
				if(table_timer.getMode().equals("每年")){
					String cron  = table_timer.getCron();
					String time = "";
					String [] arr2 = cron.split(" ");
					time = "每年"+arr2[4]+"月"+arr2[3]+"号"+arr2[2]+"时"+arr2[1]+"分";
					table_timer.setCronName(time);
				}
				
			}
		} catch (Exception e) {
			logger.info("查询定时任务报错!");
		}
		jsonObject.put("total",timmerCrons.size());
		jsonObject.put("rows",timmerCrons);
		return jsonObject;
	}
	/***
	 * 查询是否存在一样的定时任务
	 * @param cron
	 * @return
	 */
	@Override
	public int findCron(String cron) {
		int i = 0;
		try {
			i = configInforMapper.findCron(cron);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/***
	 * 保存定时任务
	 * @param mode
	 * @param ttime
	 * @param cron
	 * @param jobName
	 * @return
	 */
	@Override
	public int save_timmer(String mode, String ttime, String cron, String jobName,HttpSession session, HttpServletRequest request) {
		int i = 0;
		try {
			int user_ids = Futil.getUserId(session, request);
			i = configInforMapper.save_timmer(mode,ttime,cron,jobName,user_ids);
			if(i>0){
				String ip = Futil.getIpAddr(request);
				String remarks = user_ids+":新增定时任务："+jobName;
				loggerMapper.addLogger(user_ids,ip,remarks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/***
	 * 根据jobName查询定时任务
	 * @param jobName
	 * @return
	 */
	@Override
	public TimmerCron findByName(String jobName) {
		TimmerCron cron = new TimmerCron();
		try {
			cron = configInforMapper.findByName(jobName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cron;
	}
	/***
	 * 修改定时任务器
	 * @param mode
	 * @param ttime
	 * @param cron
	 * @param id
	 * @param jobName
	 * @return
	 */
	@Override
	public int update_timmer(String mode, String ttime, String cron, String id, String jobName, HttpSession session, HttpServletRequest request) {
		int i = 0;
		try {
			TimmerCron crons = new TimmerCron();
			TimmerCron cronsNew = new TimmerCron();
			crons = configInforMapper.findById(id);
			int user_ids = Futil.getUserId(session, request);
			i = configInforMapper.update_timmer(mode,ttime,cron,jobName,user_ids,id);
			cronsNew.setMode(mode);
			cronsNew.setJobName(jobName);
			cronsNew.setTtime(ttime);
			cronsNew.setCron(cron);
			cronsNew.setUser_id(user_ids);
			if(i>0){
				String ip = Futil.getIpAddr(request);
				String remarks = user_ids+":修改定时任务：原有定时任务"+JSONObject.toJSONString(crons)+";修改后定时任务:"+JSONObject.toJSONString(cronsNew);
				loggerMapper.addLogger(user_ids,ip,remarks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/***
	 * 查询是否存在使用
	 */
	@Override
	public int findtimmer(String id) {
		// TODO Auto-generated method stub
		return 0;
	}
	/***
	 * 物理删除定时任务器
	 */
	@Override
	public int deleteTimmerCron(String id,HttpSession session, HttpServletRequest request) {
		int i = 0;
		try {
			TimmerCron crons = new TimmerCron();
			crons = configInforMapper.findById(id);
			int user_ids = Futil.getUserId(session, request);
			i = configInforMapper.deleteTimmerCron(id);
			if(i>0){
				String ip = Futil.getIpAddr(request);
				String remarks = user_ids+":删除定时任务："+crons.getJobName();
				loggerMapper.addLogger(user_ids,ip,remarks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/***
	 * 下拉框查询定时任务器
	 */
	@Override
	public String findTimmer() {
		List<Combox> comboxList = new ArrayList<Combox>();
		comboxList = configInforMapper.findTimmer();
		return JSON.toJSONString(comboxList);
	}
	/**
	 * 新增定时任务
	 */
	@Override
	public JSONObject configureAdd_timmer(String company_id, String timmer_id, String timer_name, String group_id,
			HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","新增失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = configInforMapper.configureAdd_timmer(company_id,timmer_id,timer_name,group_id);
			 if(i>0){
					jsonObject.put("msg","新增成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "新增定时任务类型："+group_id+";任务名称："+timer_name+";公司ID："+company_id+";定时任务："+timmer_id;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("新增定时任务报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 修改定时任务
	 */
	@Override
	public JSONObject configureupdate_timmer(String id, String timmer_id, String timer_name, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","修改失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			TimmerCron crons = new TimmerCron();
			crons = configInforMapper.findTimmerByWorkId(id);
			 i = configInforMapper.configureupdate_timmer(id,timmer_id,timer_name);
			 if(i>0){
					jsonObject.put("msg","修改成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "原定时任务名称："+crons.getTimerName()+";定时任务器ID："+crons.getTimmer_id()+";修改后定时任务名称："+timer_name+";定时任务器ID："+timmer_id;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("修改定时任务报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 物理删除定时任务
	 */
	@Override
	public JSONObject deleteTimmerCron_configure(String id, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","删除失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			TimmerCron crons = new TimmerCron();
			crons = configInforMapper.findTimmerByWorkId(id);
			 i = configInforMapper.deleteTimmerCron_configure(id);
			 if(i>0){
					jsonObject.put("msg","删除成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "删除定时任务："+crons.getTimerName();
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("修改定时任务报错"+e);
		}
		return jsonObject;
	}
}
