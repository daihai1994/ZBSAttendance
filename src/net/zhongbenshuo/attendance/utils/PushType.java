package net.zhongbenshuo.attendance.utils;

/**
 * 消息推送类型1
 * Created at 2019/6/21 14:09
 *
 * @author LiYuliang
 * @version 1.0
 */

public class PushType {

    // 外勤申请（审批人收到）
    public static final int OUT_ATTENDANCE_APPLY = 10001;

    // 外勤审批结果（申请人收到）
    public static final int OUT_ATTENDANCE_APPROVAL = 10002;

    // 休假申请（审批人收到）
    public static final int VACATION_APPLY = 10003;

    // 休假审批结果（申请人收到）
    public static final int VACATION_APPROVAL = 10004;

    // 加班申请（审批人收到）
    public static final int OVERTIME_APPLY = 10005;

    // 加班审批结果（申请人收到）
    public static final int OVERTIME_APPROVAL = 10006;

    // 外出申请（审批人收到）
    public static final int GO_OUT_APPLY = 10007;

    // 外出审批结果（申请人收到）
    public static final int GO_OUT_APPROVAL = 10008;

    // 出差申请（审批人收到）
    public static final int BUSINESS_TRIP_APPLY = 10009;

    // 出差审批结果（申请人收到）
    public static final int BUSINESS_TRIP_APPROVAL = 10010;

    // 考勤补卡申请（审批人收到）
    public static final int ATTENDANCE_APPEAL_APPLY = 10011;

    // 考勤补卡审批结果（申请人收到）
    public static final int ATTENDANCE_APPEAL_APPROVAL = 10012;

    // 公告制度
    public static final int ANNOUNCEMENT = 10013;

    // 工资信息
    public static final int SALARY = 10014;

    // 问卷调查
    public static final int QUESTIONNAIRE = 10015;

    // 新的会议通知
    public static final int NEW_MEETING = 10016;

    // 工作汇报（管理员收到）
    public static final int WORK_REPORT = 10017;

    // 其他通用的消息通知
    public static final int MORMAL_MESSAGE = 10018;
    //环境检测仪信息推送
    public static final int CONDITION_MESSAGE = 20000;
    
    //门铃推送信息
    public static final int CALLDOORBELL = 20001;
    //人脸服务端传给客户端的code
    public static final int SERVER_CLIENT = 90000;
    //开关门
    public static final int OPENANDCLOSEDOOR = 90001;

    /**
     * 获取推送消息对应的中文名称
     *
     * @param type 类型
     * @return 中文名称
     */
    public static String getPushTypeName(int type) {
        String typeName = " ";
        switch (type) {
            case PushType.OUT_ATTENDANCE_APPLY:
                typeName = "外勤申请";
                break;
            case PushType.OUT_ATTENDANCE_APPROVAL:
                typeName = "外勤审批";
                break;
            case PushType.VACATION_APPLY:
                typeName = "休假申请";
                break;
            case PushType.VACATION_APPROVAL:
                typeName = "休假审批";
                break;
            case PushType.OVERTIME_APPLY:
                typeName = "加班申请";
                break;
            case PushType.OVERTIME_APPROVAL:
                typeName = "加班审批";
                break;
            case PushType.GO_OUT_APPLY:
                typeName = "外出申请";
                break;
            case PushType.GO_OUT_APPROVAL:
                typeName = "外出审批";
                break;
            case PushType.BUSINESS_TRIP_APPLY:
                typeName = "出差申请";
                break;
            case PushType.BUSINESS_TRIP_APPROVAL:
                typeName = "出差审批";
                break;
            case PushType.ATTENDANCE_APPEAL_APPLY:
                typeName = "考勤补卡申请";
                break;
            case PushType.ATTENDANCE_APPEAL_APPROVAL:
                typeName = "考勤补卡审批";
                break;
            case PushType.ANNOUNCEMENT:
                typeName = "公告制度";
                break;
            case PushType.SALARY:
                typeName = "工资";
                break;
            case PushType.QUESTIONNAIRE:
                typeName = "问卷调查";
                break;
            case PushType.NEW_MEETING:
                typeName = "会议通知";
                break;
            case PushType.WORK_REPORT:
                typeName = "工作汇报";
                break;
            case PushType.MORMAL_MESSAGE:
                typeName = "其他消息";
                break;
            default:
                break;
        }
        return typeName;
    }

}
