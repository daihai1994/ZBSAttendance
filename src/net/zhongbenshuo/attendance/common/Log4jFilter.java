package net.zhongbenshuo.attendance.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

public class Log4jFilter implements Filter {
	
	public static Logger logger = LogManager.getLogger(Log4jFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		try {
			String ip = req.getRemoteAddr();
			// String userName =
			// (String)((HttpServletRequest)req).getSession().getAttribute("userName");//获取用户名
			MDC.put("ip", ip);
			// MDC.put("userName", userName);
			// logger.info("MDC打印连接ip："+ip);
			chain.doFilter(req, res);
		} finally {
			MDC.remove("ip");
			// MDC.remove("userName");
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
