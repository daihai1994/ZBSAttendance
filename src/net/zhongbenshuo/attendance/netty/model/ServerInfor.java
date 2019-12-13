package net.zhongbenshuo.attendance.netty.model;

public class ServerInfor {
  private boolean execflage;
  private String ServerIP;
  private int ServerPort;
public boolean getExecflage() {
	return execflage;
}
public void setExecflage(boolean execflage) {
	this.execflage = execflage;
}
public String getServerIP() {
	return ServerIP;
}
public void setServerIP(String serverIP) {
	ServerIP = serverIP;
}
public int getServerPort() {
	return ServerPort;
}
public void setServerPort(int serverPort) {
	ServerPort = serverPort;
}
}
