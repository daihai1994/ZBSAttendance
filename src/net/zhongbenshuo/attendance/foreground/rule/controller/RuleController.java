package net.zhongbenshuo.attendance.foreground.rule.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.rule.service.RuleService;

@Controller
@RequestMapping(value = "/RuleController", produces = "text/html;charset=UTF-8")
public class RuleController {
	
	@Autowired
	RuleService ruleService;
	/***
	 * 查询考勤规则
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findRuleInfo.do")
	@ResponseBody
	public String findRuleInfo(String company_id,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = ruleService.findRuleInfo(company_id);
		return jsonObject.toString();
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
	@RequestMapping(value = "/submitRuleDetailsDialog.do")
	@ResponseBody
	public String submitRuleDetailsDialog(String rule_id,String company_id,String user_id,String rule_name,
			String rule_address,String rule_radius,String rule_time_work,String rule_time_off_work,
			String rule_rest_start,String rule_rest_end,String effective,String lat,String lng,String rule_type,String rule_unique_address,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = ruleService.submitRuleDetailsDialog(rule_id,company_id,user_id,rule_name,rule_address,rule_radius,rule_time_work,rule_time_off_work,
				rule_rest_start,rule_rest_end,effective,lat,lng,rule_type,rule_unique_address,session,request);
		return jsonObject.toString();
	}
	
}
