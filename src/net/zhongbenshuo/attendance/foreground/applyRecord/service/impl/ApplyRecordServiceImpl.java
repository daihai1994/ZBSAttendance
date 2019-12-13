package net.zhongbenshuo.attendance.foreground.applyRecord.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.applyRecord.mapper.ApplyRecordMapper;
import net.zhongbenshuo.attendance.foreground.applyRecord.service.ApplyRecordService;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfoAuditRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.mapper.OutAttendanceMapper;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.Futil;

@Service
public class ApplyRecordServiceImpl implements ApplyRecordService {
	public static Logger logger = LogManager.getLogger(ApplyRecordServiceImpl.class);
	
	@Autowired
	ApplyRecordMapper applyRecordMapper;
	
	@Autowired
	OutAttendanceMapper outAttendanceMapper;
	
	@Autowired
	LoggerMapper loggerMapper;
	/**
	 * 申请人，查询外勤打卡申请记录审批情况
	 */
	@Override
	public JSONObject findApply_Record(String bt, String et, String user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outAttendanceInfoList = applyRecordMapper.findApply_Records(bt,et,user_id,bNum,rows);
			if(outAttendanceInfoList!=null&&outAttendanceInfoList.size()>0){
				size = outAttendanceInfoList.get(0).getSize();
				for(OutAttendanceInfo attendanceInfo : outAttendanceInfoList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findOutAttendanceInfoAuditRecord(attendanceInfo.getOut_attendance_id());//查询外勤打卡审批的相关记录
					attendanceInfo.setOutAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤打卡需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", outAttendanceInfoList);
		return jsonObject;
	}
	/**
	 * 申请人，查询补卡申请记录
	 */
	@Override
	public JSONObject findAppeal_Record(String bt, String et, String user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			appealAttendanceInfoList = applyRecordMapper.findAppeal_Records(bt,et,user_id,bNum,rows);
			if(appealAttendanceInfoList!=null&&appealAttendanceInfoList.size()>0){
				size = appealAttendanceInfoList.get(0).getSize();
				for(AppealAttendanceInfo attendanceInfo : appealAttendanceInfoList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findAppealRecordInfoAuditRecord(attendanceInfo.getAppeal_attendance_id());//查询补卡审批的相关记录
					attendanceInfo.setAppealAttendanceInfoAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询外勤打卡需要审批信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", appealAttendanceInfoList);
		return jsonObject;
	}
	/**
	 * 标记为已读
	 */
	@Override
	public JSONObject markRead(String jsonObj,String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.markRead(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+jsonObj+"标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("标记为已读"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 全部标记已读
	 */
	@Override
	public JSONObject allTagsRead(String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		try {
			applyRecordMapper.allTagsRead(userId);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+userId+"用户全部标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("标记为已读"+e);
		}
		return jsonObject;
	}
	/**
	 * 加班申请全部标记已读
	 */
	@Override
	public JSONObject allTagsReadoverTime(String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		try {
			applyRecordMapper.allTagsReadoverTime(userId);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+userId+"加班申请用户全部标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("加班申请标记为已读"+e);
		}
		return jsonObject;
	}
	/**
	 * 查询最新消息
	 */
	@Override
	public JSONObject searchNews(String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		int outAttendanceApproval = 0;//外勤打卡审批
		int outAttendanceApply = 0;//外勤打卡申请
		int appealAttendanceApproval = 0;//补卡审批
		int appealAttendanceApply = 0;//补卡申请
		int overTimeApproval = 0;//加班审批
		int overTimeApply = 0;//加班申请
		int vacationApproval = 0;//休假审批
		int vacationApply = 0;//休假申请
		int outGoingApproval = 0;//外出审批
		int outGoingApply = 0;//外出申请
		int businessTraveIApproval = 0;//出差审批
		int businessTraveIApply = 0;//出差申请
		try {
			outAttendanceApproval = applyRecordMapper.findOutAttendanceApproval(userId);
			outAttendanceApply = applyRecordMapper.findOutAttendanceApply(userId);
			
			appealAttendanceApproval = applyRecordMapper.findAppealAttendanceApproval(userId);
			appealAttendanceApply = applyRecordMapper.findAppealAttendanceApply(userId);
			
			overTimeApproval = applyRecordMapper.findOverTimeApproval(userId);
			overTimeApply = applyRecordMapper.findOverTimeApply(userId);
			
			vacationApproval = applyRecordMapper.findVacationApproval(userId);
			vacationApply = applyRecordMapper.findVacationApply(userId);
			
			outGoingApproval = applyRecordMapper.findOutGoingApproval(userId);
			outGoingApply = applyRecordMapper.findOutGoingApply(userId);
			
			businessTraveIApproval = applyRecordMapper.findBusinessTraveIApproval(userId);
			businessTraveIApply = applyRecordMapper.findBusinessTraveIApply(userId);
			
			jsonObject.put("outAttendanceApproval", outAttendanceApproval);
			jsonObject.put("outAttendanceApply", outAttendanceApply);
			jsonObject.put("appealAttendanceApproval", appealAttendanceApproval);
			jsonObject.put("appealAttendanceApply", appealAttendanceApply);
			jsonObject.put("overTimeApproval", overTimeApproval);
			jsonObject.put("overTimeApply", overTimeApply);
			jsonObject.put("vacationApproval", vacationApproval);
			jsonObject.put("vacationApply", vacationApply);
			jsonObject.put("outGoingApproval", outGoingApproval);
			jsonObject.put("outGoingApply", outGoingApply);
			jsonObject.put("businessTraveIApproval", businessTraveIApproval);
			jsonObject.put("businessTraveIApply", businessTraveIApply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	/**
	 * 标记为无效
	 */
	@Override
	public JSONObject updateApplyEffective(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.updateApplyEffective(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把外勤打卡申请"+jsonObj+"标记无效";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("标记为已读"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 补卡申请标记无效
	 */
	@Override
	public JSONObject updateAppealEffective(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.updateAppealEffective(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把补卡申请"+jsonObj+"标记无效";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("标记为已读"+e);
		}
		return jsonObject;
	}
	/**
	 * 申请人查询加班申请记录
	 */
	@Override
	public JSONObject findOverTime_Record(String bt, String et, String user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			overTimeRecordList = applyRecordMapper.findOverTime_Record(bt,et,user_id,bNum,rows);
			if(overTimeRecordList!=null&&overTimeRecordList.size()>0){
				size = overTimeRecordList.get(0).getSize();
				for(OverTimeRecord overTimeRecord : overTimeRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoverTimeauditRecord(overTimeRecord.getId());//查询加班审批的相关记录
					overTimeRecord.setOverTimeAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询申请人查询加班申请记录信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", overTimeRecordList);
		return jsonObject;
	
	}
	/**
	 * 加班申请标记无效
	 */
	@Override
	public JSONObject updateOverTimeEffective(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.updateOverTimeEffective(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把加班申请"+jsonObj+"标记无效";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("标记为已读"+e);
		}
		return jsonObject;
	}
	/**
	 * 加班标记已读
	 */
	@Override
	public JSONObject markReadoverTime(String jsonObj, String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.markReadoverTime(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+jsonObj+"加班标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("加班标记为已读"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 申请人查询假期申请记录
	 */
	@Override
	public JSONObject findvacation_Record(String bt, String et, String user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			vacationRecordList = applyRecordMapper.findVacation_Record(bt,et,user_id,bNum,rows);
			if(vacationRecordList!=null&&vacationRecordList.size()>0){
				size = vacationRecordList.get(0).getSize();
				for(VacationRecord vacationRecord : vacationRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findvacationauditRecord(vacationRecord.getId());//查询加班审批的相关记录
					vacationRecord.setVacationAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询申请人查询假期申请记录信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", vacationRecordList);
		return jsonObject;
	
	}
	/**
	 * 假期申请标记无效
	 */
	@Override
	public JSONObject updatevacationEffective(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.updatevacationEffective(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把假期申请"+jsonObj+"标记无效";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("假期申请标记无效"+e);
		}
		return jsonObject;
	}
	/**
	 * 假期全部标记已读
	 */
	@Override
	public JSONObject allTagsReadvacation(String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		try {
			applyRecordMapper.allTagsReadvacation(userId);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+userId+"假期申请用户全部标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("假期申请标记为已读"+e);
		}
		return jsonObject;
	}
	/**
	 * 假期标记为已读
	 */
	@Override
	public JSONObject markReadvacation(String jsonObj, String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.markReadvacation(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+jsonObj+"假期标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("假期标记为已读"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 申请人查询外出申请记录
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	@Override
	public JSONObject findoutGoing_Record(String bt, String et, String user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			outGoingRecordList = applyRecordMapper.findOutGoing_Record(bt,et,user_id,bNum,rows);
			if(outGoingRecordList!=null&&outGoingRecordList.size()>0){
				size = outGoingRecordList.get(0).getSize();
				for(OutGoingRecord outGoingRecord : outGoingRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findoutGoingauditRecord(outGoingRecord.getId());//查询加班审批的相关记录
					outGoingRecord.setOutGoingAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询申请人查询外出申请记录信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", outGoingRecordList);
		return jsonObject;
	
	}
	/**
	 * 外出申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject updateoutGoingEffective(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.updateoutGoingEffective(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把外出申请"+jsonObj+"标记无效";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("标记为已读"+e);
		}
		return jsonObject;
	}
	/**
	 * 外出标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject markReadoutGoing(String jsonObj, String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.markReadoutGoing(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+jsonObj+"外出标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("外出标记为已读"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 外出全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject allTagsReadoutGoing(String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		try {
			applyRecordMapper.allTagsReadoutGoing(userId);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+userId+"外出申请用户全部标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("外出申请标记为已读"+e);
		}
		return jsonObject;
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
	@Override
	public JSONObject findbusinessTraveI_Record(String bt, String et, String user_id, int bNum, int rows) {
		JSONObject jsonObject = new JSONObject();
		int size = 0;
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		List<OutAttendanceInfoAuditRecord> outAttendanceInfoAuditRecord = new ArrayList<OutAttendanceInfoAuditRecord>();//审批过的历史记录
		try {
			businessTraveIRecordList = applyRecordMapper.findbusinessTraveI_Record(bt,et,user_id,bNum,rows);
			if(businessTraveIRecordList!=null&&businessTraveIRecordList.size()>0){
				size = businessTraveIRecordList.get(0).getSize();
				for(BusinessTraveIRecord businessTraveIRecord : businessTraveIRecordList){
					outAttendanceInfoAuditRecord = outAttendanceMapper.findbusinessTraveIauditRecord(businessTraveIRecord.getId());//查询加班审批的相关记录
					businessTraveIRecord.setBusinessTraveIAuditRecord(outAttendanceInfoAuditRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询申请人查询出差申请记录信息报错");
		}
		jsonObject.put("total", size);
		jsonObject.put("rows", businessTraveIRecordList);
		return jsonObject;
	
	}
	/**
	 * 出差申请标记无效
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject updatebusinessTraveIEffective(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.updatebusinessTraveIEffective(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把出差申请"+jsonObj+"标记无效";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("标记为已读"+e);
		}
		return jsonObject;
	}
	/**
	 * 出差标记为已读
	 * @param jsonObj
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject markReadbusinessTraveI(String jsonObj, String userId, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		JSONArray  object = new JSONArray ();
		object = JSONArray.fromObject(jsonObj);
		 List<String> list = JSONArray.toList(object, String.class);// 过时方法
		try {
			applyRecordMapper.markReadbusinessTraveI(list);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+jsonObj+"外出标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("外出标记为已读"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 出差全部标记已读
	 * @param userId
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject allTagsReadbusinessTraveI(String userId, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","标记失败");
		jsonObject.put("result","fail");
		try {
			applyRecordMapper.allTagsReadbusinessTraveI(userId);
			jsonObject.put("msg","标记成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "把"+userId+"出差申请用户全部标记已读";
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("出差申请标记为已读"+e);
		}
		return jsonObject;
	}
}
