package net.zhongbenshuo.attendance.utils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.zhongbenshuo.attendance.bean.User;

public class OnlineUserListener implements HandlerInterceptor { 
    @Override
    public void postHandle(HttpServletRequest request, 
        HttpServletResponse response, Object handler, 
        ModelAndView modelAndView) throws Exception { 
    	System.out.println("1111");
    } 
      
    @Override
    public void afterCompletion(HttpServletRequest request, 
        HttpServletResponse response, Object handler, Exception ex) 
        throws Exception { 
    	System.out.println("2222");
    } 
      
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
        Object handler) throws Exception { 
    	User user = new User();
    	System.out.println(request.getSession().getServletContext().getContextPath());
    	user = (User) request.getSession().getAttribute("user");
        if (user==null ) {  
        	System.out.println("进入session过期");
        	if(request.getHeader("x-requested-with")!=null){
        		System.out.println("进入ajax session过期");
        		response.setHeader("sessionstatus", "timeout");
        		return false;
        	}else{
        		response.sendRedirect(request.getSession().getServletContext().getContextPath()+"/index.jsp");
        	}
        }  
        return true;
    }
}
