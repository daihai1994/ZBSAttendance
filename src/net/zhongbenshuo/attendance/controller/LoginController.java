package net.zhongbenshuo.attendance.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.service.IUserService;
import net.zhongbenshuo.attendance.service.LoginService;

@Controller
@RequestMapping(value = "/LoginController", produces = "text/html;charset=UTF-8")
public class LoginController {

	public static Logger logger = LogManager.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;
	
	@Autowired
	private IUserService iuserService;

	// 登陆
	@RequestMapping("/login")
	public ModelAndView login(HttpSession session, String username, String aes_pwd, HttpServletRequest request)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		user = iuserService.loginUser(username, aes_pwd);
		if (user == null) {
			modelAndView.addObject("errMsg", "用户名或密码错误");
			logger.info("浏览器登陆失败：" + username);
			modelAndView.setViewName("index");
		} else {
			logger.info("浏览器用户登陆成功：" + username);
			modelAndView.addObject("websocketspath",request.getServerName()+":"+OnLineInitial.configData.getWebsocketPort());
			session.setAttribute("user", user);
			modelAndView.addObject("user", user);
			modelAndView.setViewName("main");
		}
		
		return modelAndView;
	}
	/***
	 * 查询公司列表
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/compang_main.do")
	@ResponseBody
	public String compang_main(String company_id,HttpSession session,HttpServletRequest request) {
		String res = "";
		res = loginService.compang_main(company_id);
		return res;
	}
	/***
	 * 查询公司删除密码
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findDeletePwd.do")
	@ResponseBody
	public String findDeletePwd(String company_id,HttpSession session,HttpServletRequest request) {
		String res = "";
		res = loginService.findDeletePwd(company_id);
		return res;
	}
	
	
	// 退出
//	@RequestMapping("/logout")
//	public String logout(HttpSession session) throws Exception {
//		// 清除session
//		session.invalidate();
//		// 重定向到首页
//		return "index";
//		// return new ModelAndView("index");
//	}
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession session, String init) throws Exception {
		session.invalidate();
		ModelAndView modelAndView = new ModelAndView();
		if(StringUtils.isNotBlank(init)){
			if(init.equals("timeout")){
				modelAndView.addObject("errMsg", "用户已过期,请重新登陆！");
			}
		}
		modelAndView.setViewName("index");
		return modelAndView;
	}
	
	
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wechat.do")
	@ResponseBody
	public Map<String , String> wechat(HttpServletRequest request) {
		Map<String ,String> map = new HashMap<String , String>();
        try {
            InputStream inputStream =null;
            inputStream = request.getInputStream();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(inputStream);
            Element rootElement = doc.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element el:elements) {
                map.put(el.getName() , el.getText());
            }
            inputStream.close();
            return map ;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
	}

}
