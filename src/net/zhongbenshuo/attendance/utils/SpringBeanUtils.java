package net.zhongbenshuo.attendance.utils;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class SpringBeanUtils implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	public SpringBeanUtils() {}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringBeanUtils.applicationContext = applicationContext;
		
	}
	/**
     * 获取applicationContext对象
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
    /**
     * 根据bean的id来查找对象
     * @param id
     * @return
     */
	public static Object getBean(String name){
		return applicationContext.getBean(name);
	}
	  /**
     * 根据bean的class来查找对象
     * @param c
     * @return
     */
    public static Object getBeanByClass(Class c){
        return applicationContext.getBean(c);
    }
    
    /**
     * 根据bean的class来查找所有的对象(包括子类)
     * @param c
     * @return
     */
    public static Map getBeansByClass(Class c){
        return applicationContext.getBeansOfType(c);
    }
    
	public static <T> T getBean(String name,Class<T> requiredType){
		return applicationContext.getBean(name, requiredType);
	}
}
