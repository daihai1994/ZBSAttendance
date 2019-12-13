/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.zhongbenshuo.attendance.netty;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

	public static Logger logger = LogManager.getLogger(NettyServerHandler.class);
	/*
     * 覆盖了 channelRead0() 事件处理方法。
     * 每当从服务端读到客户端写入信息时，
     * 其中如果你使用的是 Netty 5.x 版本时，
     * 需要把 channelRead0() 重命名为messageReceived()
     */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, String  msg) throws Exception {
		try {
			System.out.println("服务端传递");
			System.out.println("服务端传递"+msg);
			ctx.writeAndFlush(msg);
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 断开进入
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		try {
			System.out.println("channel 不活跃，断开连接");
			super.channelInactive(ctx);
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		try {
			System.out.println("channel注册了");
			super.channelRegistered(ctx);
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel 读取完毕");
		super.channelReadComplete(ctx);
	}
	/**
	 * 错误进入
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info("netty 发生巨大异常！！！.", cause);
		ctx.close();
	}
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel 可写更改");
		super.channelWritabilityChanged(ctx);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime=df.format(new Date());
			System.out.println("客户端开始读取服务端过来的信息");
			System.out.println(message.toString());
			try {
				AnalysisThread thread = new AnalysisThread(message.toString(),createTime);
				new Thread(thread).start();
			} catch (Exception e) {
				System.out.println(e);
				logger.info("环境检测仪发生数据，开启线程处理报错");
			}
			
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}
	/**
	 * 通讯上进入
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端传给服务端");
		 super.channelActive(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			// IdleStateEvent event = (IdleStateEvent) evt;
			logger.info("远程IP:" + ctx.channel().remoteAddress() + "空闲超时，即将关闭！");
			ctx.close();
		}
	}

	
}
