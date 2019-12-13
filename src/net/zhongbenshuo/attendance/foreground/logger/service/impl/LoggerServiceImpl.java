package net.zhongbenshuo.attendance.foreground.logger.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.logger.bean.LoggerInfo;
import net.zhongbenshuo.attendance.foreground.logger.service.LoggerService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;

@Service
public class LoggerServiceImpl implements LoggerService {
	public static Logger logger = LogManager.getLogger(LoggerServiceImpl.class);
	
	@Autowired
	LoggerMapper loggerMapper;
	/***
	 * 查询日志信息
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param remarks
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject findLoggerInfo(String bt, String et, int bNum, int rows, String user_id, String remarks,String company_id,int company_id_user) {
		JSONObject jsonObject = new JSONObject();
		List<LoggerInfo> loggerList = new ArrayList<LoggerInfo>();
		bt = bt+" 00:00:00";
		et = et+" 23:59:59";
		try {
			if(company_id_user==0){
				loggerList = loggerMapper.findLoggerInfoByAdmin(bt,et,bNum,rows,user_id,remarks,company_id);
			}else{
				loggerList = loggerMapper.findLoggerInfo(bt,et,bNum,rows,user_id,remarks,company_id);
			}
			
			int size = 0;
			if(loggerList!=null&&loggerList.size()>0){
				size = loggerList.get(0).getSize();
			}
			jsonObject.put("rows", loggerList);
			jsonObject.put("total", size);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("查询日志信息报错"+e);
		}
		return jsonObject;
	
	}
	
	
}
