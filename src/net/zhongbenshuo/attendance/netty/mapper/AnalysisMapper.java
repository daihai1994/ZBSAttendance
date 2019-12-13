package net.zhongbenshuo.attendance.netty.mapper;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.foreground.Condition.Environment;

public interface AnalysisMapper {

	void addCondition(Environment condition);

	void addMessage(@Param("station")int station,@Param("date")String date, @Param("message")String message);

}
