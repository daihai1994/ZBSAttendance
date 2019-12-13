package net.zhongbenshuo.attendance.foreground.holidaySetting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.WorkingTime;

public interface HolidaySettingMapper {
	/**
	 * 查询假期设置
	 * @param year
	 * @param month
	 * @return
	 */
	List<WorkingTime> findWorkingTime(@Param("year")String year, @Param("month")String month,@Param("company_id")int company_id);
	/**
	 * 修改工作状态
	 * @param id
	 * @param status
	 * @return
	 */
	int updateWorkingInfo(@Param("id")String id, @Param("status")String status);
}
