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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MeterDataDecoder extends ByteToMessageDecoder {
	public static Logger logger = LogManager.getLogger(MeterDataDecoder.class);


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		System.out.println(in);
		int remainLen = in.readableBytes();
		in.markReaderIndex();
		byte[] orgdata = new byte[remainLen];
		in.readBytes(orgdata);
		in.resetReaderIndex();
		String data = "";
		while (in.readableBytes() > 0) {
			byte _dat = in.readByte();
			String dat = byteToHex(_dat).toUpperCase();
			data+=dat;
		}
		out.add(data);
		//logger.info(data);
	}
	/** 
	 * 字节转十六进制 
	 * @param b 需要进行转换的byte字节 
	 * @return  转换后的Hex字符串 
	 */  
	public static String byteToHex(byte b){  
	    String hex = Integer.toHexString(b & 0xFF);  
	    if(hex.length() < 2){  
	        hex = "0" + hex;  
	    }  
	    return hex;  
	}
}
