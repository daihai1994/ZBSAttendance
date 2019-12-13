package net.zhongbenshuo.attendance.quartz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.zhongbenshuo.attendance.bean.UserInfoStatus;
import net.zhongbenshuo.attendance.bean.WeChatInfo;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.WeChat.controller.WeChatSend;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.mapper.LoginMapper;
import net.zhongbenshuo.attendance.utils.GsonUtils;
import net.zhongbenshuo.attendance.utils.TimeUtils;
@Component
@EnableScheduling
public class ReminderForWork {

	@Autowired
	LoginMapper  loginMapper;
	
	@Autowired
	ConfigInforMapper configInforMapper;
	
	@Scheduled(cron = "0 25 8 * * ?")//
	public void run() {
		WorkingTime workingtime = OnLineInitial.workingTime;
		System.out.println("进入定时任务");
		List<UserInfoStatus> userInfoList = new ArrayList<UserInfoStatus>();
		String time = TimeUtils.getCurrentDateTime().substring(0, 10)+" 00:00:00";
		System.out.println(time);
		try {
			userInfoList = loginMapper.findUserOpenId(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String access_token = getAccessToken();
		System.out.println(access_token);
		if(workingtime!=null&&workingtime.getStatus()==0){
			for(UserInfoStatus user : userInfoList){
				try {
					if(user.getAttendanceRecords().size()==0){
						String result  = WeChatSend.sendNewsToUser(user.getOpen_id(),access_token,user.getUser_name());
						System.out.println(result);
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
	}

	private String getAccessToken() {
		WeChatInfo weChatInfo = OnLineInitial.weChatInfo;//微信公众号基础信息
		SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(weChatInfo!=null){
			/**
			 * 计算accesstoken
			 */
			if(StringUtils.isNotBlank(weChatInfo.getAccess_token())){
				Date date=new Date();
				String sdate=simdate.format(date);
				String expires_in = weChatInfo.getAccess_token_time();
				if(sdate.compareTo(expires_in)>0){
					String expires = TimeUtils.addTime(7200);
					String access_token = WeChatSend.getWeiXinAccessToken();
					weChatInfo.setAccess_token(access_token);
					weChatInfo.setAccess_token_time(expires);
					configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
				}
			}else{
				String expires = TimeUtils.addTime(7200);
				String access_token = WeChatSend.getWeiXinAccessToken();
				weChatInfo.setAccess_token(access_token);
				weChatInfo.setAccess_token_time(expires);
				configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
			}
			/**
			 * 计算jsapi_ticket
			 */
			if(StringUtils.isNotBlank(weChatInfo.getTicket())){
				Date date=new Date();
				String sdate=simdate.format(date);
				String expires_in = weChatInfo.getTicket_time();
				if(sdate.compareTo(expires_in)>0){
					String expires = TimeUtils.addTime(7200);
					String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
					weChatInfo.setTicket(ticket);
					weChatInfo.setTicket_time(expires);
					configInforMapper.updateWeChatInfoTicket(expires,ticket);
				}
			}else{
				String expires = TimeUtils.addTime(7200);
				String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
				weChatInfo.setTicket(ticket);
				weChatInfo.setTicket_time(expires);
				configInforMapper.updateWeChatInfoTicket(expires,ticket);
			}
		}
		return weChatInfo.getAccess_token();
	} 
}
