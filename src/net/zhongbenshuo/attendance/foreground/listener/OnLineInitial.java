package net.zhongbenshuo.attendance.foreground.listener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.metter.background.netty.websocket.WebsocketServer;
import net.zhongbenshuo.attendance.FaceEngine.FaceEngineJava;
import net.zhongbenshuo.attendance.bean.WeChatInfo;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.Condition.StationInfo;
import net.zhongbenshuo.attendance.foreground.WeChat.controller.WeChatSend;
import net.zhongbenshuo.attendance.foreground.configInfor.bean.TimmerCron;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;
import net.zhongbenshuo.attendance.foreground.version.mapper.VersionMapper;
import net.zhongbenshuo.attendance.netty.NettyServer;
import net.zhongbenshuo.attendance.netty.model.ConfigData;
import net.zhongbenshuo.attendance.quartz.MyJob;
import net.zhongbenshuo.attendance.quartz.QuartzManager;
import net.zhongbenshuo.attendance.utils.ConfigDataUtils;
import net.zhongbenshuo.attendance.utils.TimeUtils;

@Service
public class OnLineInitial implements InitializingBean {
	
	public static Logger logger = LogManager.getLogger(OnLineInitial.class);
	public static ConfigData configData = new ConfigData();
	public static List<VersionInfo> versionInfoList = new ArrayList<VersionInfo>();// 版本号信息
	public static List<StationInfo> stationInfoList = new ArrayList<StationInfo>();//环境检测仪基本信息
	public static WorkingTime workingTime = new WorkingTime();//当日休息状态
	public static ThreadPoolExecutor PUSHAUDIT = null;
	public static WeChatInfo weChatInfo = new WeChatInfo();//微信公众号基础信息
	
	@Autowired
	private VersionMapper versionMapper;
	
	@Autowired
	ConfigInforMapper configInforMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("开始初始化:OnLineInitial");
		//初始化人脸引擎报错
		try {
			FaceEngineJava.init();
		} catch (Exception e) {
			System.out.println("初始化人脸引擎报错");
		}
		try {
			SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat simtime=new SimpleDateFormat("HH:mm:ss");
			Date date=new Date();
			String sdate=simdate.format(date);
			workingTime = configInforMapper.findworkingTime(sdate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			weChatInfo = configInforMapper.findWeChatInfo();
			if(weChatInfo!=null){
				/**
				 * 计算accesstoken
				 */
				if(StringUtils.isNotBlank(weChatInfo.getAccess_token())){
					Date date=new Date();
					String sdate=simdate.format(date);
					String expires_in = weChatInfo.getAccess_token_time();
					if(sdate.compareTo(expires_in)>0){
						String expires = TimeUtils.addTime(7200);
						String access_token = WeChatSend.getWeiXinAccessToken();
						weChatInfo.setAccess_token(access_token);
						weChatInfo.setAccess_token_time(expires);
						configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
					}
				}else{
					String expires = TimeUtils.addTime(7200);
					String access_token = WeChatSend.getWeiXinAccessToken();
					weChatInfo.setAccess_token(access_token);
					weChatInfo.setAccess_token_time(expires);
					configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
				}
				/**
				 * 计算jsapi_ticket
				 */
				if(StringUtils.isNotBlank(weChatInfo.getTicket())){
					Date date=new Date();
					String sdate=simdate.format(date);
					String expires_in = weChatInfo.getTicket_time();
					if(sdate.compareTo(expires_in)>0){
						String expires = TimeUtils.addTime(7200);
						String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
						weChatInfo.setTicket(ticket);
						weChatInfo.setTicket_time(expires);
						configInforMapper.updateWeChatInfoTicket(expires,ticket);
					}
				}else{
					String expires = TimeUtils.addTime(7200);
					String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
					weChatInfo.setTicket(ticket);
					weChatInfo.setTicket_time(expires);
					configInforMapper.updateWeChatInfoTicket(expires,ticket);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取微信基本信息报错!");
		}
		try {
			// 查询最新发布稳定版APP信息
			versionInfoList = versionMapper.findVersionInfoList();
			logger.info("成功初始化:OnLineInitial,versionInfoList:" + JSON.toJSONString(versionInfoList));
			
			//start
			stationInfoList = versionMapper.findStationInfoList();
			//end 查询环境检测仪的基本信息
			List<TimmerCron> table_timer = new ArrayList<TimmerCron>();
			table_timer = configInforMapper.findTimmerCron();
			for (TimmerCron timer : table_timer) {
				QuartzManager.addJob(timer.getId(), timer.getId(), timer.getId(), timer.getId(), MyJob.class,
						timer.getCron());
			}
			PUSHAUDIT= new ThreadPoolExecutor(50, 1000, 10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			if (configData.getTcpPort() == 0) {
				configData = getConfigData();

//				Thread websocketThreadEnvironmentalDetector = new Thread( new WebsocketServer(configData.getWebsocket_portEnvironmentalDetector()));
//				websocketThreadEnvironmentalDetector.setDaemon(true);
//				websocketThreadEnvironmentalDetector.start();
				
				Thread websocketThread = new Thread( new WebsocketServer(configData.getWebsocketPort()));
				websocketThread.setDaemon(true);
				websocketThread.start();
				
				if (!NettyServer.serverinfor.getExecflage()) {
					NettyServer.serverinfor.setExecflage(true);
					NettyServer server = new NettyServer(configData.getTcpPort());
					Thread tcpserverThread = new Thread(server);
					tcpserverThread.setDaemon(true);
					tcpserverThread.start();
				}
			}
		} catch (Exception e) {
			logger.info("错误初始化:OnLineInitial:" + e.getMessage());
		}
	}

	private ConfigData getConfigData() throws Exception {

		ConfigData data = new ConfigData();

		String contextPath = ConfigDataUtils.class.getClassLoader().getResource("configdata.properties").getPath();
		contextPath = URLDecoder.decode(contextPath, "utf-8");
		contextPath = contextPath.substring(1, contextPath.length());
		InputStream in = new BufferedInputStream(new FileInputStream(contextPath));
		Properties prop = new Properties();
		prop.load(in);
		Iterator<String> it = prop.stringPropertyNames().iterator();
		while (it.hasNext()) {
			String key = it.next();
			switch (key) {
			case "version":
				data.setVersion(Integer.parseInt(prop.getProperty(key)));
				break;
			case "tcp_port":
				data.setTcpPort(Integer.parseInt(prop.getProperty(key)));
				break;
			case "tcp_idle":
				data.setTcpidel(Integer.parseInt(prop.getProperty(key)));
				break;
			case "websocket_port":
				data.setWebsocketPort(Integer.parseInt(prop.getProperty(key)));
				break;
			case "websocket_portEnvironmentalDetector":
				data.setWebsocket_portEnvironmentalDetector(Integer.parseInt(prop.getProperty(key)));
				break;
			}
		}
		if (null != in)
			in.close();

		return data;
	
	}
}
