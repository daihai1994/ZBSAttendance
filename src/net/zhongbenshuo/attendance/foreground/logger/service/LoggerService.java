package net.zhongbenshuo.attendance.foreground.logger.service;

import com.alibaba.fastjson.JSONObject;

public interface LoggerService {
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
	JSONObject findLoggerInfo(String bt, String et, int bNum, int rows, String user_id, String remarks,String company_id,int company_id_user);

}
