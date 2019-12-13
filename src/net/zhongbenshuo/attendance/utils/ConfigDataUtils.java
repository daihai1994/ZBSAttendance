package net.zhongbenshuo.attendance.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.zhongbenshuo.attendance.netty.model.ConfigData;

public class ConfigDataUtils {

	
	public ConfigDataUtils() {}

	public static ConfigData getConfigData() throws IOException{
		ConfigData data = new ConfigData();
		String contextPath = ConfigDataUtils.class.getClassLoader().getResource("configdata.properties").getPath();
		contextPath = URLDecoder.decode(contextPath,"utf-8"); 
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
			}
        }
        if(null != in) in.close();
		
		return data;
	}
	
	public static void main(String[] arg){
		try {
			ConfigData add = getConfigData();
			System.out.println(add);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static class TempData {

		private int totalCount;
		private List<TempData> rows = new ArrayList<TempData>();

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public List<TempData> getRows() {
			return rows;
		}

		public void setRows(List<TempData> rows) {
			this.rows = rows;
		}

		@Override
		public String toString() {
			return "TempData [totalCount=" + totalCount + ", rows=" + rows
					+ "]";
		}

	}

}
