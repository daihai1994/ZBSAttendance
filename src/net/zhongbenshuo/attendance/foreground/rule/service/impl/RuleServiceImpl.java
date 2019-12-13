package net.zhongbenshuo.attendance.foreground.rule.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.AttendanceRule;
import net.zhongbenshuo.attendance.foreground.rule.mapper.RuleMapper;
import net.zhongbenshuo.attendance.foreground.rule.service.RuleService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.Futil;

@Service
public class RuleServiceImpl implements RuleService {
	public static Logger logger = LogManager.getLogger(RuleServiceImpl.class);
	
	@Autowired
	RuleMapper ruleMapper;
	
	@Autowired
	LoggerMapper loggerMapper;
	/***
	 * 查询考勤规则
	 * @param company_id
	 * @return
	 */
	@Override
	public JSONObject findRuleInfo(String company_id) {
		JSONObject jsonObject = new JSONObject();
		List<AttendanceRule> attendanceRuleInfos= new ArrayList<AttendanceRule>();
		try {
			attendanceRuleInfos = ruleMapper.findRuleInfo(company_id);
		} catch (Exception e) {
			logger.info("查询考勤规则报错!");
		}
		jsonObject.put("total",attendanceRuleInfos.size());
		jsonObject.put("rows",attendanceRuleInfos);
		return jsonObject;
	}
	/***
	 * 新增/修改---考勤规则
	 * @param rule_id
	 * @param company_id
	 * @param user_id
	 * @param rule_name
	 * @param rule_address
	 * @param rule_radius
	 * @param rule_time_workm
	 * @param rule_time_off_work
	 * @param rule_rest_start
	 * @param rule_rest_end
	 * @param effective
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitRuleDetailsDialog(String rule_id, String company_id, String user_id, String rule_name,
			String rule_address, String rule_radius, String rule_time_workm, String rule_time_off_work,
			String rule_rest_start, String rule_rest_end, String effective,String lat, String lng,String rule_type,String rule_unique_address,HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isNotBlank(rule_id)){
			jsonObject.put("msg","修改失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				AttendanceRule rule = new AttendanceRule();
				rule = ruleMapper.findRule(rule_id);
				 i = ruleMapper.updateRule(user_id,rule_name,rule_address,rule_radius,rule_time_workm,rule_time_off_work,rule_rest_start,rule_rest_end,effective,rule_id,lat,lng,rule_type,rule_unique_address);
				 if(i>0){
						jsonObject.put("msg","修改成功");
						jsonObject.put("result","success");
						int user_ids = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "原有考勤规则||名称："+rule.getRule_name()+",地址："+rule.getRule_address()+",半径:"+
								rule.getRule_radius()+",经度："+rule.getRule_longitude()+",纬度："+rule.getRule_latitude()+
								",上班时间："+rule.getRule_time_work()+",下班时间："+
								rule.getRule_time_off_work()+",是否有效："+rule.getEffective()+",休息开始时间："+rule.getRule_rest_start()+",休息结束时间："+
								rule.getRule_rest_end()+";现有考勤规则||名称："+rule_name+",地址："+rule_address+",半径:"+
								rule_radius+",经度："+lng+",纬度："+lat+
								",上班时间："+rule_time_workm+",下班时间："+
								rule_time_off_work+",是否有效："+effective+",休息开始时间："+rule_rest_start+",休息结束时间："+
								rule_rest_end;
						loggerMapper.addLogger(user_ids,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("修改考勤信息报错"+e);
			}
			
		}else{
			jsonObject.put("msg","新增失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = ruleMapper.addRule(user_id,rule_name,rule_address,rule_radius,rule_time_workm,rule_time_off_work,rule_rest_start,rule_rest_end,effective,company_id,lat,lng,rule_type,rule_unique_address);
				 if(i>0){
						jsonObject.put("msg","新增成功");
						jsonObject.put("result","success");
						int user_ids = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "新增考勤规则||名称："+rule_name+",地址："+rule_address+",半径:"+
								rule_radius+",经度："+lng+",纬度："+lat+
								",上班时间："+rule_time_workm+",下班时间："+
								rule_time_off_work+",是否有效："+effective+",休息开始时间："+rule_rest_start+",休息结束时间："+
								rule_rest_end;
						loggerMapper.addLogger(user_ids,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("新增考勤信息报错"+e);
			}
		}
		return jsonObject;
	
	
	}

}
