package net.zhongbenshuo.attendance.foreground.configInfor.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.configInfor.bean.TimmerCron;

public interface ConfigInforService {
	/***
	 * 查询定时器任务
	 * @param company_id
	 * @return
	 */
	JSONObject findTimmerCron();

	/***
	 * 查询是否存在一样的定时任务
	 * @param cron
	 * @return
	 */
	int findCron(String cron);
	/***
	 * 保存定时任务
	 * @param mode
	 * @param ttime
	 * @param cron
	 * @param jobName
	 * @return
	 */
	int save_timmer(String mode, String ttime, String cron, String jobName,HttpSession session, HttpServletRequest request);
	/***
	 * 根据jobName查询定时任务
	 * @param jobName
	 * @return
	 */
	TimmerCron findByName(String jobName);

	/***
	 * 修改定时任务器
	 * @param mode
	 * @param ttime
	 * @param cron
	 * @param id
	 * @param jobName
	 * @param request 
	 * @param session 
	 * @return
	 */
	int update_timmer(String mode, String ttime, String cron, String id, String jobName, HttpSession session, HttpServletRequest request);

	/***
	 * 查询定时任务根据ID
	 * @param id
	 * @return
	 */
	int findtimmer(String id);
	/***
	 * 物理删除定时任务
	 * @param id
	 * @return
	 */
	int deleteTimmerCron(String id,HttpSession session, HttpServletRequest request);
	/***
	 * 计算工时
	 * @param company_id
	 * @return
	 */
	JSONObject findTimmerCron_configure(int company_id);
	/***
	 * 下拉框查询定时任务器
	 * @return
	 */
	String findTimmer();
	/**
	 * 新增定时任务
	 * @param company_id
	 * @param timmer_id
	 * @param timer_name
	 * @param group_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject configureAdd_timmer(String company_id, String timmer_id, String timer_name, String group_id,
			HttpSession session, HttpServletRequest request);
	/***
	 * 修改定时任务
	 * @param id
	 * @param timmer_id
	 * @param timer_name
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject configureupdate_timmer(String id, String timmer_id, String timer_name, HttpSession session,
			HttpServletRequest request);
	/***
	 * 物理删除定时任务
	 * @param id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject deleteTimmerCron_configure(String id, HttpSession session, HttpServletRequest request);

}
