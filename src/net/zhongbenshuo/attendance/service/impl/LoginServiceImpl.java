package net.zhongbenshuo.attendance.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.LoginLogInfo;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;
import net.zhongbenshuo.attendance.mapper.LoginMapper;
import net.zhongbenshuo.attendance.service.LoginService;
import net.zhongbenshuo.attendance.utils.Futil;

@Service
public class LoginServiceImpl implements LoginService {

	public static Logger logger = LogManager.getLogger(LoginServiceImpl.class);

	@Autowired
	LoginMapper loginMapper;
	
	

	@Override
	public User checkUser(String username, String password, HttpServletRequest request) {
		User user = new User();
		try {
			user = loginMapper.checkUser(username, password);

			// 登录成功后，将相关登录日志信息存入数据库中
			LoginLogInfo all = new LoginLogInfo();
			String ip = Futil.getIpAddr(request);
			all.setIp(ip);
			all.setUser_Id(username);
			all.setVersionCode(-1);
			all.setDevice_Type("pc");// 设备类型：pc
			all.setApkTypeId(-1);
			int i = loginMapper.addPCLoginLog(all);
			logger.info(i == 1 ? "日志记录成功" : "日志记录失败");
		} catch (Exception e) {
			logger.info("网页账号：" + username + "登录出错：" + e.getMessage());
		}
		return user;
	}
	/***
	 * 查询公司列表
	 */
	@Override
	public String compang_main(String company_id) {
		List<Combox> comboxList = new ArrayList<Combox>();
		comboxList = loginMapper.compang_main(company_id);
		return JSON.toJSONString(comboxList);
	}
	/**
	 * 查询公司的删除密码
	 */
	@Override
	public String findDeletePwd(String company_id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","000000");
		jsonObject.put("result","fail");
		String pwd = loginMapper.findDeletePwd(company_id);
		if(StringUtils.isNotBlank(pwd)){
			jsonObject.put("msg",pwd);
			jsonObject.put("result","success");
		}
		return jsonObject.toString();
	}

}
