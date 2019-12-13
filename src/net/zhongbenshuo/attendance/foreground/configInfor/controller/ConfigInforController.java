package net.zhongbenshuo.attendance.foreground.configInfor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.configInfor.bean.TimmerCron;
import net.zhongbenshuo.attendance.foreground.configInfor.service.ConfigInforService;
import net.zhongbenshuo.attendance.quartz.MyJob;
import net.zhongbenshuo.attendance.quartz.QuartzManager;

@Controller
@RequestMapping(value = "/ConfigInforController", produces = "text/html;charset=UTF-8")
public class ConfigInforController {
	@Autowired
	ConfigInforService configInforService;
	/***
	 * 查询定时器任务
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findTimmerCron.do")
	@ResponseBody
	public String findTimmerCron(HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = configInforService.findTimmerCron();
		return jsonObject.toString();
	}
	/***
	 * 查询公司下的计算工时
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findTimmerCron_configure.do")
	@ResponseBody
	public String findTimmerCron_configure(int company_id , HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = configInforService.findTimmerCron_configure(company_id);
		return jsonObject.toString();
	}
	
	/***
	 * 保存定时任务
	 * @return
	 */
	@RequestMapping(value="/save_timmer")
	@ResponseBody
	public String save_timmer(String mode,String ttime,String cron,String jobName,HttpSession session, HttpServletRequest request){
		int i = configInforService.findCron(cron);
		if(i>0){
			i = -1;
			return String.valueOf(i);
		}
		 i = configInforService.save_timmer(mode,ttime,cron,jobName,session,request);
		 if(i==1){
			 TimmerCron table_timer = configInforService.findByName(jobName);
			 QuartzManager.addJob(table_timer.getId(), table_timer.getId(), table_timer.getId(), table_timer.getId(), MyJob.class, cron);
		 }
		 return String.valueOf(i);
	}
	
	/***
	 *  修改定时任务
	 * @return
	 */
	@RequestMapping(value="/update_timmer")
	@ResponseBody
	public String update_timmer(String mode,String ttime,String cron,String id,String jobName,HttpSession session, HttpServletRequest request){
		int i = configInforService.findCron(cron);
		if(i>0){
			i = -1;
			return String.valueOf(i);
		}
		 i = configInforService.update_timmer(mode,ttime,cron,id,jobName,session,request);
		if(i==1){
			QuartzManager.modifyJobTime(id, id, id, id, cron); 
		}
		 return String.valueOf(i);
	}
	

	/***
	 * 删除定时任务
	 * @param id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteTimmerCron")
	@ResponseBody
	public String deleteTimmerCron(String id,HttpSession session, HttpServletRequest request){
		int i = configInforService.findtimmer(id);
		if(i>0){
			i = -1;
			return String.valueOf(i);
		}
		 i = configInforService.deleteTimmerCron(id,session,request);
		 if(i==1){
			 QuartzManager.removeJob(id, id, id, id);
		 }
		return String.valueOf(i);
	}
	
	/***
	 * 下拉框查询定时任务器
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findTimmer.do")
	@ResponseBody
	public String findTimmer(HttpSession session,HttpServletRequest request) {
		String res = "";
		res = configInforService.findTimmer();
		return res;
	}
	
	/***
	 * 新增计算工时定时任务
	 * @param company_id
	 * @param timmer_id
	 * @param timer_name
	 * @param group_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/configureAdd_timmer.do")
	@ResponseBody
	public String configureAdd_timmer(String company_id,String timmer_id,String timer_name,String group_id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = configInforService.configureAdd_timmer(company_id,timmer_id,timer_name,group_id,session,request);
		return jsonObject.toString();
	}
	/***
	 * 修改定时任务
	 * @param id
	 * @param timmer_id
	 * @param timer_name
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/configureupdate_timmer.do")
	@ResponseBody
	public String configureupdate_timmer(String id,String timmer_id,String timer_name,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = configInforService.configureupdate_timmer(id,timmer_id,timer_name,session,request);
		return jsonObject.toString();
	}
	/***
	 * 物理删除定时任务
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteTimmerCron_configure.do")
	@ResponseBody
	public String deleteTimmerCron_configure(String id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = configInforService.deleteTimmerCron_configure(id,session,request);
		return jsonObject.toString();
	}
	
}
