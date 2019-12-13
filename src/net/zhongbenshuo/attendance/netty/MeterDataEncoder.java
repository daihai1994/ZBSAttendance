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

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Bytes;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.zhongbenshuo.attendance.netty.model.Frequent;
import net.zhongbenshuo.attendance.netty.model.GetWayProtocol;


public class MeterDataEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) throws Exception {
        System.out.println(message);
    	if (message instanceof GetWayProtocol) {
    		GetWayProtocol getwayprotocol = (GetWayProtocol) message; 
    		ArrayList<Byte> listByte = new ArrayList<Byte>();
	    	int getwayASCIIlen = 0;
	    	int datalen=0;
	    	if (getwayprotocol.getGetWayASCII()!=null) getwayASCIIlen=getwayprotocol.getGetWayASCII().length;
	    	if (getwayprotocol.getData()!=null) datalen=getwayprotocol.getData().length;
	    	int len=1+1+1+1+getwayASCIIlen+datalen+1;
	    	
	    	listByte.add((byte) 0x7B);
	    	listByte.add(getwayprotocol.getCommd());
	    	listByte.add((byte) 0x00);
	    	listByte.add((byte) len);
	    	if (getwayprotocol.getGetWayASCII()!=null){
	    		for(int i=0;i<getwayprotocol.getGetWayASCII().length;i++)
	    		listByte.add(getwayprotocol.getGetWayASCII()[i]);
	    	}
	    	if (getwayprotocol.getData()!=null){
	    		for(int i=0;i<getwayprotocol.getData().length;i++)
	    			listByte.add(getwayprotocol.getData()[i]);
	    	}
	    	listByte.add((byte) 0x7B); 
	    	
	    	byte[] bytes = new byte[listByte.size()];
	    	for(int i=0;i<listByte.size();i++)
	    		bytes[i]=listByte.get(i);
	    	
	    	System.out.println(Frequent.BytesToHexs(bytes));
	    	out.writeBytes(bytes);
    	} else if (message instanceof String){
    		String strmsg =(String)message;
    		out.writeBytes(strmsg.getBytes("UTF-8"));
    	} else if (message instanceof List){
    		List<Byte> list = (List<Byte>)message;
    		out.writeBytes(Bytes.toArray(list));
    	}
        	
    }
}
