package net.zhongbenshuo.attendance.utils;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import net.zhongbenshuo.attendance.mapper.LoginMapper;

@Component("OnLineInitialRSAKey")
public class OnLineInitialRSAKey implements ApplicationListener<ContextRefreshedEvent> {
	public  static WebRsaKey webRsaKey= new WebRsaKey();
	
	public static HashMap<Integer, String> map = new HashMap<Integer, String>();//存安卓登入的userId和rsa的公钥
	
	@Autowired
	private LoginMapper loginMapper;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		//项目启动的时候，随机生成web的rsa的公钥和私钥
		try {
			webRsaKey = loginMapper.webRsaKey();
			System.out.println("公钥私钥生成成功!");
		} catch (Exception e) {
			System.out.println("公钥私钥生成失败!");
			System.out.println(e);
		}
		
		
	}

}
