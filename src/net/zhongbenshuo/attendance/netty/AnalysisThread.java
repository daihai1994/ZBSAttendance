package net.zhongbenshuo.attendance.netty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.foreground.Condition.Environment;
import net.zhongbenshuo.attendance.foreground.Condition.ParseUtil;
import net.zhongbenshuo.attendance.foreground.Condition.StationInfo;
import net.zhongbenshuo.attendance.foreground.Condition.StringUtil;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.netty.mapper.AnalysisMapper;
import net.zhongbenshuo.attendance.utils.PushType;
import net.zhongbenshuo.attendance.utils.SpringBeanUtils;
import net.zhongbenshuo.attendance.utils.TimeUtils;

public class AnalysisThread implements Runnable{
	public static Logger logger = LogManager.getLogger(AnalysisThread.class);
	public String message;//数据
	public String date;//时间
	public AnalysisThread(String message,String date){
		this.message = message;
		this.date = date;
	}
	/**
	 * 解析数据
	 */
	@Override
	public void run() {
		try {
			AnalysisMapper analysisMapper = (AnalysisMapper) SpringBeanUtils.getBean("analysisMapper");
			Environment condition = new Environment();
	        ParseUtil.parseData(message.replaceAll(" ",""), condition);
//	        System.out.println("站号：" + StringUtil.removeZero(String.valueOf(condition.getStation())));
//	        System.out.println("温度：" + StringUtil.removeZero(String.valueOf(condition.getTemperature())) + "℃");
//	        System.out.println("湿度：" + StringUtil.removeZero(String.valueOf(condition.getHumidity())) + "%");
//	        System.out.println("PM2.5：" + StringUtil.removeZero(String.valueOf(condition.getPm25())) + "μg/m³");
//	        System.out.println("PM10：" + StringUtil.removeZero(String.valueOf(condition.getPm10())) + "μg/m³");
//	        System.out.println("CO2：" + StringUtil.removeZero(String.valueOf(condition.getCarbonDioxide())) + "ppm");
//	        System.out.println("HCHO：" + StringUtil.removeZero(String.valueOf(condition.getFormaldehyde())) + "mg/m³");
//	        System.out.println("光照度：" + StringUtil.removeZero(String.valueOf(condition.getIlluminance())) + "lux");
	        condition.setCreateTime(date);
	        if(OnLineInitial.stationInfoList!=null){
	        	for(StationInfo stationInfo : OnLineInitial.stationInfoList){
	        		if(stationInfo.getStation_id()==condition.getStation()){
	        			condition.setStation_name(stationInfo.getStation_name());
	        			condition.setStation_remarks(stationInfo.getStation_remarks());
	        			break;
	        		}
	        	}
	        }
	        //analysisMapper.addCondition(condition);
	       //analysisMapper.addMessage(condition.getStation(),date,message);
	       JSONObject websocket = new JSONObject();
			websocket.put("key",PushType.CONDITION_MESSAGE);
			websocket.put("message", condition);
			WebSocketFrameHandler.sendData(websocket);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
		}
		
	}

}
