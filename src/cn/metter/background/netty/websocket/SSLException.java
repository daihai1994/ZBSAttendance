package cn.metter.background.netty.websocket;

public class SSLException extends Exception {
	private String errMsg;

	  public SSLException(String errMsg) {
	    super(errMsg);
	    this.errMsg = errMsg;
	  }

	  public SSLException(Throwable cause) {
	    super(cause);
	  }
	
}
