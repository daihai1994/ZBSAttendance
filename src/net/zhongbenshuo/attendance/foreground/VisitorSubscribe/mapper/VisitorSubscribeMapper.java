package net.zhongbenshuo.attendance.foreground.VisitorSubscribe.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.Face;
import net.zhongbenshuo.attendance.bean.VisitorSubscribe;

public interface VisitorSubscribeMapper {
	/**
	 * 查询访客预约审批
	 * @param bt
	 * @param et
	 * @param user_id
	 * @param bNum
	 * @param rows
	 * @return
	 */
	List<VisitorSubscribe> findvisitorSubscribe(@Param("bt")String bt, @Param("et")String et, @Param("user_id")String user_id, @Param("bNum")int bNum, @Param("rows")int rows);
	/**
	 * 提交访客预约审批信息
	 * @param id
	 * @param audit_status
	 * @param audit_resmarks
	 * @return
	 */
	int submitAuditvisitorSubscribe(@Param("id")String id, @Param("audit_status")String audit_status, @Param("audit_resmarks")String audit_resmarks);
	/**
	 * 查询访客预约信息
	 * @param id
	 * @return
	 */
	VisitorSubscribe findVistorSubscribeById(@Param("id")String id);
	/**
	 * 访客预约审核通过，存入到face表中
	 * @param visitorSubscribe
	 */
	void insertFace(Face face);
}
