package net.zhongbenshuo.attendance.netty;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.metter.background.netty.websocket.WebsocketServer;
import cn.metter.background.netty.websocket.WebsocketServerInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.bean.FaceWebsocket;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.utils.SpringBeanUtils;


public class WebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {
	public static Logger logger = LogManager.getLogger(WebSocketFrameHandler.class);
//	@Autowired
//	private  IUserService iuserService;
	ConfigInforMapper configInforMapper = (ConfigInforMapper) SpringBeanUtils.getBean("configInforMapper");
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static void sendData(Object data){
		//System.out.println(WebsocketServer.startFlag);
		 if(WebsocketServer.startFlag){
//			 for (Channel channel : channels) {
//		            System.out.println(channel.remoteAddress());
//		        }
			 channels.writeAndFlush(new TextWebSocketFrame(JSONObject.fromObject(data).toString()));
		 }
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception { // (1)
		String message = msg.text();//收到的数据
		System.out.println("websocket的信息------"+message);
		JSONObject jsonObj = JSONObject.fromObject(message);
		FaceWebsocket faceWebsocket = (FaceWebsocket) JSONObject.toBean(jsonObj, FaceWebsocket.class);
		if(faceWebsocket!=null){
			if(faceWebsocket.getType().equals("ClientResponse")){//单个处理
				int id =  faceWebsocket.getId();
				configInforMapper.updateFaceStatus(id);
			}
			if(faceWebsocket.getType().equals("ClientResponseList")){//批量处理
				List<Integer> updateList=  faceWebsocket.getUpdateList();
				//iuserService.updateFaceStatus(id);
				configInforMapper.updateFaceStatusList(updateList);
			}
			if(faceWebsocket.getType().equals("ClientResponseDelete")){//删除人脸信息
				int id =  faceWebsocket.getId();
				configInforMapper.deleteFace(id);
			}
			if(faceWebsocket.getType().equals("ClientResponseDeleteList")){//批量删除人脸信息
				List<Integer> updateList=  faceWebsocket.getUpdateList();
				//iuserService.updateFaceStatus(id);
				configInforMapper.deleteFaceList(updateList);
			}
		}
//		Channel incoming = ctx.channel();
//		for (Channel channel : channels) {
//            if (channel != incoming){
//                channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
//            } else {
//            	channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
//            }
//        }
	}
	
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
        
        // Broadcast a message to multiple Channels
       // channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
        
        channels.add(incoming);
		System.out.println("Client:"+incoming.remoteAddress() +"加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        
        // Broadcast a message to multiple Channels
        //channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
        
		System.out.println("Client:"+incoming.remoteAddress() +"离开");

        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }
	    
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
		try {
			 Channel incoming = ctx.channel();
				System.out.println("Client:"+incoming.remoteAddress()+"在线");
		} catch (Exception e) {
			logger.info("WebSocketFrameHandler channelActive异常"+e);
		}
       
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
		System.out.println("Client:"+incoming.remoteAddress()+"掉线");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)	// (7)
			 {
		try {
			Channel incoming = ctx.channel();
			System.out.println("Client:"+incoming.remoteAddress()+"异常");
	        // 当出现异常就关闭连接
	        cause.printStackTrace();
	        ctx.close();
		} catch (Exception e) {
			logger.info("WebSocketFrameHandler exceptionCaught异常"+e);
		}
    	
	}
	
	
}
