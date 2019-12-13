package net.zhongbenshuo.attendance.utils;

/**
 * 推送消息实体类
 * Created at 2018/8/28 0028 16:23
 *
 * @author LiYuliang
 * @version 1.0
 */

public class PushData {

    // 推送数据类型，参照PushType类
    private int type;
    
    // 推送数据内容
    private Object data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
