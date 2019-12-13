package net.zhongbenshuo.attendance.netty.model;

public class ConfigData {
	private int version;
	private int tcpPort;
	private int websocketPort;
	private int tcpidel;
	private int websocket_portEnvironmentalDetector;
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}
	public int getWebsocketPort() {
		return websocketPort;
	}
	public void setWebsocketPort(int websocketPort) {
		this.websocketPort = websocketPort;
	}
	public int getTcpidel() {
		return tcpidel;
	}
	public void setTcpidel(int tcpidel) {
		this.tcpidel = tcpidel;
	}
	public int getWebsocket_portEnvironmentalDetector() {
		return websocket_portEnvironmentalDetector;
	}
	public void setWebsocket_portEnvironmentalDetector(int websocket_portEnvironmentalDetector) {
		this.websocket_portEnvironmentalDetector = websocket_portEnvironmentalDetector;
	}
	@Override
	public String toString() {
		return "ConfigData [version=" + version + ", tcpPort=" + tcpPort + ", websocketPort=" + websocketPort
				+ ", tcpidel=" + tcpidel + ", websocket_portEnvironmentalDetector="
				+ websocket_portEnvironmentalDetector + "]";
	}
	
}
