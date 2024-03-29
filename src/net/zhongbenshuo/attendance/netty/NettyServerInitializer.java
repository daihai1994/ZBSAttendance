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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;

/**
 * Creates a newly configured {@link ChannelPipeline} for a server-side channel.
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("decoder", new MeterDataDecoder());
        pipeline.addLast("encoder", new MeterDataEncoder());

        pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, OnLineInitial.configData.getTcpidel()));
        pipeline.addLast(NettyServer.HANDER_GROUP,"handler", new NettyServerHandler());
    }
}
