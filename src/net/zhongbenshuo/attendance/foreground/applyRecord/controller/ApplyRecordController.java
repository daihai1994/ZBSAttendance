package net.zhongbenshuo.attendance.foreground.applyRecord.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.foreground.applyRecord.service.ApplyRecordService;

@Controller
@RequestMapping(value = "/ApplyRecordController", produces = "text/html;charset=UTF-8")
public class ApplyRecordController {
	@Autowired
	ApplyRecordService applyRecordService;
	/**
	 * 申请人查询自己的外勤打卡审批信息
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findApply_Record.do")
	@ResponseBody
	public String findApply_Record(String bt,String et,String user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.findApply_Record(bt+" 00:00:00",et+" 23:59:59",user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 查询申请人申请补卡记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findAppeal_Record.do")
	@ResponseBody
	public String findAppeal_Record(String bt,String et,String user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.findAppeal_Record(bt+" 00:00:00",et+" 23:59:59",user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 申请人查询加班申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findOverTime_Record.do")
	@ResponseBody
	public String findOverTime_Record(String bt,String et,String user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.findOverTime_Record(bt+" 00:00:00",et+" 23:59:59",user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 申请人查询假期申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findvacation_Record.do")
	@ResponseBody
	public String findvacation_Record(String bt,String et,String user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.findvacation_Record(bt+" 00:00:00",et+" 23:59:59",user_id,bNum,rows);
		return jsonObject.toString();
	}
	
	/**
	 * 申请人查询外出申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findoutGoing_Record.do")
	@ResponseBody
	public String findoutGoing_Record(String bt,String et,String user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.findoutGoing_Record(bt+" 00:00:00",et+" 23:59:59",user_id,bNum,rows);
		return jsonObject.toString();
	}
	/**
	 * 申请人查询出差申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findbusinessTraveI_Record.do")
	@ResponseBody
	public String findbusinessTraveI_Record(String bt,String et,String user_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.findbusinessTraveI_Record(bt+" 00:00:00",et+" 23:59:59",user_id,bNum,rows);
		return jsonObject.toString();
	}
	
	/**
	 * 标记已读
	 * @param jsonObj
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/markRead.do")
	@ResponseBody
	public String markRead(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.markRead(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 加班标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/markReadoverTime.do")
	@ResponseBody
	public String markReadoverTime(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.markReadoverTime(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 外出标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/markReadoutGoing.do")
	@ResponseBody
	public String markReadoutGoing(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.markReadoutGoing(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	
	/**
	 * 出差标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/markReadbusinessTraveI.do")
	@ResponseBody
	public String markReadbusinessTraveI(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.markReadbusinessTraveI(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 假期标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/markReadvacation.do")
	@ResponseBody
	public String markReadvacation(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.markReadvacation(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	
	
	/**
	 * 标记为无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateApplyEffective.do")
	@ResponseBody
	public String updateApplyEffective(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.updateApplyEffective(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 补卡申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateAppealEffective.do")
	@ResponseBody
	public String updateAppealEffective(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.updateAppealEffective(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 加班申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateOverTimeEffective.do")
	@ResponseBody
	public String updateOverTimeEffective(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.updateOverTimeEffective(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 外出申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateoutGoingEffective.do")
	@ResponseBody
	public String updateoutGoingEffective(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.updateoutGoingEffective(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 出差申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatebusinessTraveIEffective.do")
	@ResponseBody
	public String updatebusinessTraveIEffective(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.updatebusinessTraveIEffective(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	
	/**
	 * 假期申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatevacationEffective.do")
	@ResponseBody
	public String updatevacationEffective(String jsonObj,String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.updatevacationEffective(jsonObj,userId,session,request);
		return jsonObject.toString();
	}
	
	/**
	 * 全部标记已读
	 * @param jsonObj
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/allTagsRead.do")
	@ResponseBody
	public String allTagsRead(String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.allTagsRead(userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 加班全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/allTagsReadoverTime.do")
	@ResponseBody
	public String allTagsReadoverTime(String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.allTagsReadoverTime(userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 外出全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/allTagsReadoutGoing.do")
	@ResponseBody
	public String allTagsReadoutGoing(String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.allTagsReadoutGoing(userId,session,request);
		return jsonObject.toString();
	}
	/**
	 * 出差全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/allTagsReadbusinessTraveI.do")
	@ResponseBody
	public String allTagsReadbusinessTraveI(String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.allTagsReadbusinessTraveI(userId,session,request);
		return jsonObject.toString();
	}
	
	/**
	 * 假期全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/allTagsReadvacation.do")
	@ResponseBody
	public String allTagsReadvacation(String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.allTagsReadvacation(userId,session,request);
		return jsonObject.toString();
	}
	
	/**
	 * 查询最新消息
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchNews.do")
	@ResponseBody
	public String searchNews(String userId,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = applyRecordService.searchNews(userId,session,request);
		return jsonObject.toString();
	}
	
}
