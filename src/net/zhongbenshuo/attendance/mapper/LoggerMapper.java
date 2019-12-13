package net.zhongbenshuo.attendance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.foreground.logger.bean.LoggerInfo;

public interface LoggerMapper {
	/***
	 * 日志的新增
	 * @param user_id
	 * @param ip
	 * @param remarks
	 */
	void addLogger(@Param("user_id")int user_id, @Param("ip")String ip, @Param("remarks")String remarks);
	/***
	 * 日志查询
	 * @param bt
	 * @param et
	 * @param bNum
	 * @param rows
	 * @param user_id
	 * @param remarks
	 * @param company_id
	 * @return
	 */
	List<LoggerInfo> findLoggerInfo(@Param("bt")String bt, @Param("et")String et, @Param("bNum")int bNum, @Param("rows")int rows, @Param("user_id")String user_id, 
			@Param("remarks")String remarks,
			@Param("company_id")String company_id);
	/***
	 * 日志查询当是admin登入的时候
	 * @param bt
	 * @param et
	 * @param bNum
	 * @param rows
	 * @param user_id
	 * @param remarks
	 * @param company_id
	 * @return
	 */
	List<LoggerInfo> findLoggerInfoByAdmin(@Param("bt")String bt, @Param("et")String et, @Param("bNum")int bNum, @Param("rows")int rows, @Param("user_id")String user_id, 
			@Param("remarks")String remarks,
			@Param("company_id")String company_id);

}
