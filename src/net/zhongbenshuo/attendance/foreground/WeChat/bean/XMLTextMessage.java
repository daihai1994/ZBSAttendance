package net.zhongbenshuo.attendance.foreground.WeChat.bean;


public class XMLTextMessage extends XMLMessage {

	private static final long serialVersionUID = 2457998440521370652L;

	private String content;

	public XMLTextMessage(String toUserName, String fromUserName, String content) {
		super(toUserName, fromUserName, "text");
		this.content = content;
	}

	@Override
	public String subXML() {
		return "<Content><![CDATA[" + content + "]]></Content>";
	}

	@Override
	public Message convert() {
		return new TextMessage(toUserName, content);
	}

}
