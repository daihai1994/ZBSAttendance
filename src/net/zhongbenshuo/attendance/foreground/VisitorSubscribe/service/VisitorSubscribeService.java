package net.zhongbenshuo.attendance.foreground.VisitorSubscribe.service;

import net.sf.json.JSONObject;

public interface VisitorSubscribeService {
	/**
	 * 查询访客预约审批
	 * @param string
	 * @param string2
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	JSONObject findvisitorSubscribe(String string, String string2, String user_id, int bNum, int rows);
	/**
	 * 提交访客预约审批信息
	 * @param id
	 * @param audit_status
	 * @param audit_resmarks
	 * @return
	 */
	JSONObject submitAuditvisitorSubscribe(String id, String audit_status, String audit_resmarks);
	

}
