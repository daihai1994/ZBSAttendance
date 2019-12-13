package cn.metter.background.netty.websocket;




import java.io.File;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;
import net.zhongbenshuo.attendance.netty.WebSocketFrameHandler;

public class WebsocketServerInitializer extends
		ChannelInitializer<SocketChannel> {	//1
	public static Logger logger = LogManager.getLogger(WebsocketServerInitializer.class);
	@Override
    public void initChannel(SocketChannel ch) throws Exception {//2
		logger.info("进入WebsocketServerInitializer initChannel");
		 ChannelPipeline pipeline = ch.pipeline();
//		 String certPath = "/SSLcertificate";
//			try {
//				 if (new File(certPath).exists()) {
//			            final File certFile = new File(certPath + "/zbs.pem");
//			            final File keyFile = new File(certPath + "/zbs.key");
//			            SslContext sslContext = SslContextBuilder.forServer(certFile, keyFile).build();
//			            pipeline.addLast(sslContext.newHandler(ch.alloc()));
//			        }
//			} catch (Exception e) {
//				logger.info("WebsocketServerInitializer 生成sslContext报错"+e.getMessage());
//			}
		 
        pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64*1024));
		pipeline.addLast(new ChunkedWriteHandler());
		//pipeline.addLast(new HttpRequestHandler("/energymanagerws"));
		pipeline.addLast(new WebSocketServerProtocolHandler("/environment"));
		pipeline.addLast(new WebSocketFrameHandler());
		try {
		      SSLContext sslContext = null;
		      // TODO 这里面的几项需要换成ServerConfig中的参数
		      sslContext = SslUtil.createSSLContext("jks", "D:/SSLJKS/zbs.jks", "udbP7UxW");
		      logger.info("创建sslContext是否是null"+sslContext);
		      // SSLEngine 此类允许使用ssl安全套接层协议进行安全通信
		      SSLEngine engine = sslContext.createSSLEngine();
		      // 服务器模式
		      engine.setUseClientMode(false);
		      //ssl双向认证
		      engine.setNeedClientAuth(false);
		      engine.setWantClientAuth(false);
		      // engine.setEnabledProtocols(new String[] { "SSLv3", "TLSv1" })
		      // TLSv1.2包括了SSLv3
		      //engine.setEnabledProtocols(new String[] { "TLSv1.2" });
		      pipeline.addFirst(new SslHandler(engine));

		    } catch (SSLException e) {
		      logger.debug("创建ssl出现错误", e);
		    }
    }
}
