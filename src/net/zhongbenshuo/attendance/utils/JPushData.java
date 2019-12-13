package net.zhongbenshuo.attendance.utils;

public class JPushData {
	
	private int id;//激光推送消息的ID

    // 推送人（公司ID）
    private int pusher;

    // 推送类型（全部推送、别名推送、标签推送）
    private String pushType;

    // 推送接收人（全部：all，别名：[10000001,10000002],标签：[C1,D5,female]）
    private String receiver;

    // 数据类型（参照PushType类）
    private int dataType;

    // 推送内容（传给手机端的内容实体类转String）
    private String content;

    // 极光返回的推送结果码（200：成功，400：失败）
    private int resultCode;

    // 极光返回的推送结果内容（示例：{"msg_id":47287808075860258,"sendno":1329974775,"statusCode":0}）
    private String resultContent;

    // 推送时间
    private String pushTime;
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPusher() {
        return pusher;
    }

    public void setPusher(int pusher) {
        this.pusher = pusher;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

	@Override
	public String toString() {
		return "JPushData [id=" + id + ", pusher=" + pusher + ", pushType=" + pushType + ", receiver=" + receiver
				+ ", dataType=" + dataType + ", content=" + content + ", resultCode=" + resultCode + ", resultContent="
				+ resultContent + ", pushTime=" + pushTime + "]";
	}
    
}
