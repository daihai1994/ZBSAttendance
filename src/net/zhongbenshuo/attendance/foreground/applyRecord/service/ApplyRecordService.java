package net.zhongbenshuo.attendance.foreground.applyRecord.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

public interface ApplyRecordService {
	/**
	 * 申请人，查询外勤打卡申请记录审批情况
	 * @param string
	 * @param string2
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findApply_Record(String bt, String et, String user_id, int bNum, int rows);
	/**
	 * 标记未已读
	 * @param jsonObj
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject markRead(String jsonObj, String userId,HttpSession session, HttpServletRequest request);
	/**
	 * 全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject allTagsRead(String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 查询最新消息
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject searchNews(String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 查询申请人补卡申请记录
	 * @param string
	 * @param string2
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findAppeal_Record(String string, String string2, String user_id, int bNum, int rows);
	/**
	 * 标记为无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject updateApplyEffective(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 补卡申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject updateAppealEffective(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 申请人查询加班申请记录
	 * @param string
	 * @param string2
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findOverTime_Record(String string, String string2, String user_id, int bNum, int rows);
	/**
	 * 加班申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject updateOverTimeEffective(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 加班标记已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject markReadoverTime(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 加班申请全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject allTagsReadoverTime(String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 申请人查询假期申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findvacation_Record(String bt, String et, String user_id, int bNum, int rows);
	/**
	 * 假期申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject updatevacationEffective(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 假期全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject allTagsReadvacation(String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 假期标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject markReadvacation(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 申请人查询外出申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findoutGoing_Record(String bt, String et, String user_id, int bNum, int rows);
	/**
	 * 外出申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject updateoutGoingEffective(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 外出标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject markReadoutGoing(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 外出全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject allTagsReadoutGoing(String userId, HttpSession session, HttpServletRequest request);
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
	JSONObject findbusinessTraveI_Record(String string, String string2, String user_id, int bNum, int rows);
	/**
	 * 出差申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject updatebusinessTraveIEffective(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request);
	/**
	 * 出差标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject markReadbusinessTraveI(String jsonObj, String userId, HttpSession session, HttpServletRequest request);
	/**
	 * 出差全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	JSONObject allTagsReadbusinessTraveI(String userId, HttpSession session, HttpServletRequest request);
	

}
