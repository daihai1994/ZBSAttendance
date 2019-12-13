package net.zhongbenshuo.attendance.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.User;

public interface LoginService {

	public User checkUser(String username, String password, HttpServletRequest request); // 浏览器登录
	/***
	 * 主页面，查询公司combobox
	 * @param company_id
	 * @return
	 */
	public String compang_main(String company_id);
	/***
	 * 查询公司的删除密码
	 * @param company_id
	 * @return
	 */
	public String findDeletePwd(String company_id);

}
