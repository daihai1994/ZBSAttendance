package net.zhongbenshuo.attendance.foreground.rule.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

public interface RuleService {
	/***
	 * 查询考勤规则
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject findRuleInfo(String company_id);
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
	 * @param lng 
	 * @param lat 
	 * @param request 
	 * @param session 
	 * @return
	 */
	JSONObject submitRuleDetailsDialog(String rule_id, String company_id, String user_id, String rule_name,
			String rule_address, String rule_radius, String rule_time_workm, String rule_time_off_work,
			String rule_rest_start, String rule_rest_end, String effective, String lat, String lng,String rule_type,String rule_unique_address, HttpSession session, HttpServletRequest request);

}
