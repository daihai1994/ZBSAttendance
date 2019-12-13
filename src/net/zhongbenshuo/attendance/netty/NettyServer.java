package net.zhongbenshuo.attendance.netty;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.netty.model.ServerInfor;

public class NettyServer implements Runnable {
	public static Logger logger = LogManager.getLogger(NettyServer.class);
	

	

	public static ServerBootstrap serverBootstrap;
	public static ChannelFuture serverChannelFuture;
	public static ServerInfor serverinfor = new ServerInfor();// TCP UDP服务器状态
	public static ThreadPoolExecutor sendtogprsthreadPool = null;
	public static ThreadPoolExecutor saveDataToDBThreadPool = null;
	public static ThreadPoolExecutor responseAndroidThreadPool = null;
	public static ThreadPoolExecutor abnormalProcessThreadPool = null;
	public static ThreadPoolExecutor pushOtherDatabasesThreadPool = null;
	public static ThreadPoolExecutor pushTaiGuDatabasesThreadPool = null;
	
	public static ThreadPoolExecutor readMeterDataByImeiThreadPool = null;
	

	
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String unInsertedMeterID = "";
	public static String URLInsert = "";
	public static boolean insertFlag = false;// 载入数据--添加表号
	public static List<String> meterIdList = new ArrayList<String>();
	public static int port;

	private static EventLoopGroup bossGroup = null;
	private static EventLoopGroup workerGroup = null;
	public static  EventExecutorGroup HANDER_GROUP = null;  
	public NettyServer(int port) {
		NettyServer.port = port;
	}

	public static void startServer() throws Exception {
		// this.port = port;
		if(bossGroup!=null){
			try{
				bossGroup.shutdownGracefully();
			}catch(Exception ex){
			}
		}
		if(workerGroup!=null){
			try{
				workerGroup.shutdownGracefully();
			}catch(Exception ex){
			}
		}
		if(HANDER_GROUP!=null){
			try{
				HANDER_GROUP.shutdownGracefully();
			}catch(Exception ex){
			}
		}
		bossGroup = new NioEventLoopGroup(2, new DefaultThreadFactory("bossGroupServer1", true));
		workerGroup = new NioEventLoopGroup(4,new DefaultThreadFactory("workerGroupServer1", true));
		HANDER_GROUP = new DefaultEventExecutorGroup(3); 
		try {
			serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new NettyServerInitializer());
			serverBootstrap.option(ChannelOption.TCP_NODELAY, true);  
			serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);  
			serverChannelFuture = serverBootstrap.bind(OnLineInitial.configData.getTcpPort()).sync();
			
			serverinfor.setServerIP(InetAddress.getLocalHost().getHostAddress());
			serverinfor.setServerPort(OnLineInitial.configData.getTcpPort());
			serverinfor.setExecflage(true);
			
			
			
			// 创建一个池子 最少数量20，最大数量300， 空闲时间3，单位S，缓冲队列， 对拒绝的任务处理
			sendtogprsthreadPool = new ThreadPoolExecutor(50, 500, 15,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			saveDataToDBThreadPool = new ThreadPoolExecutor(100, 1000, 10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			responseAndroidThreadPool = new ThreadPoolExecutor(100, 1000, 10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			abnormalProcessThreadPool = new ThreadPoolExecutor(100, 1000, 10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			pushOtherDatabasesThreadPool= new ThreadPoolExecutor(100, 1000, 10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			pushTaiGuDatabasesThreadPool= new ThreadPoolExecutor(100, 1000, 10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			
			readMeterDataByImeiThreadPool = new ThreadPoolExecutor(50, 50, 10, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>());
			//refreashgprslisttimer = new Timer();
	    	//设置成上线定时器 ,延时10秒后开始执行，3分钟执行一次 
			//refreashgprslisttimer.schedule(new TimerRefreashGPRS.TimerRefreashGPRSTimerTask(), 60000,TimerRefreashGPRS.timeInterval);
			//删除Table_rx的全部内容
//			DataFromGPRSMapper dataFromGPRSMapper = (DataFromGPRSMapper) SpringBeanUtils.getBean("dataFromGPRSMapper");
//			dataFromGPRSMapper.truncatesqlreadmeterrx();
			
			serverChannelFuture.channel().closeFuture().sync();
			
		} finally {
			serverinfor.setExecflage(false);
			
			if(bossGroup!=null){
				try{
					bossGroup.shutdownGracefully();
				}catch(Exception ex){
				}
			}
			if(workerGroup!=null){
				try{
					workerGroup.shutdownGracefully();
				}catch(Exception ex){
				}
			}
			if(HANDER_GROUP!=null){
				try{
					HANDER_GROUP.shutdownGracefully();
				}catch(Exception ex){
				}
			}
			
//			if (refreashgprslisttimer != null){
//				refreashgprslisttimer.cancel();
//				refreashgprslisttimer = null;
//			}
			if (sendtogprsthreadPool != null){
				sendtogprsthreadPool.shutdown();
				sendtogprsthreadPool = null;
			}
			if (saveDataToDBThreadPool != null){
				saveDataToDBThreadPool.shutdown();
				saveDataToDBThreadPool = null;
			}
		}
	}


	public static final Map<String, Object> userexecsendtogprsthread_hashmap =Collections.synchronizedMap( new HashMap<String, Object>());
	public static final Map<String, Object> usersendtogprsthread_hashmap = Collections.synchronizedMap( new HashMap<String, Object>());
	public static final Map<String, Object> usertxinfostatus = Collections.synchronizedMap( new HashMap<String, Object>());
	public static Map<String, Object> txInfoList =  Collections.synchronizedMap(new HashMap<String, Object>());

	

	
	
	@Override
	public void run() {
		try {
			Thread.sleep(5000);
			startServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
