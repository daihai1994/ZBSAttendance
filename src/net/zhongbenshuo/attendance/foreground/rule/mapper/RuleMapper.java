package net.zhongbenshuo.attendance.foreground.rule.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.AttendanceRule;

public interface RuleMapper {
	/***
	 * 查询考勤规则
	 * @param company_id
	 * @return
	 */
	List<AttendanceRule> findRuleInfo(@Param("company_id")String company_id);
	/***
	 * 查询规则
	 * @param rule_id
	 * @return
	 */
	AttendanceRule findRule(@Param("rule_id")String rule_id);
	/***
	 * 修改考勤规则
	 * @param user_id
	 * @param rule_name
	 * @param rule_address
	 * @param rule_radius
	 * @param rule_time_workm
	 * @param rule_time_off_work
	 * @param rule_rest_start
	 * @param rule_rest_end
	 * @param effective
	 * @param rule_id
	 * @param lat 
	 * @param lng 
	 * @param rule_unique_address 
	 * @param rule_type 
	 * @return
	 */
	int updateRule(@Param("user_id")String user_id, @Param("rule_name")String rule_name, @Param("rule_address")String rule_address, 
			@Param("rule_radius")String rule_radius, @Param("rule_time_work")String rule_time_workm,
			@Param("rule_time_off_work")String rule_time_off_work, @Param("rule_rest_start")String rule_rest_start, 
			@Param("rule_rest_end")String rule_rest_end, @Param("effective")String effective, @Param("rule_id")String rule_id, 
			@Param("lat")String lat, @Param("lng")String lng, @Param("rule_type")String rule_type, @Param("rule_unique_address")String rule_unique_address);
	/***
	 * 新增考勤规则
	 * @param user_id
	 * @param rule_name
	 * @param rule_address
	 * @param rule_radius
	 * @param rule_time_workm
	 * @param rule_time_off_work
	 * @param rule_rest_start
	 * @param rule_rest_end
	 * @param effective
	 * @param company_id
	 * @param lat
	 * @param lng
	 * @param rule_unique_address 
	 * @param rule_type 
	 * @return
	 */
	int addRule(@Param("user_id")String user_id, @Param("rule_name")String rule_name, @Param("rule_address")String rule_address, @Param("rule_radius")String rule_radius, 
			@Param("rule_time_work")String rule_time_workm,
			@Param("rule_time_off_work")String rule_time_off_work, @Param("rule_rest_start")String rule_rest_start, @Param("rule_rest_end")String rule_rest_end, 
			@Param("effective")String effective,
			@Param("company_id")String company_id, @Param("lat")String lat, @Param("lng")String lng, @Param("rule_type")String rule_type, @Param("rule_unique_address")String rule_unique_address);

}
