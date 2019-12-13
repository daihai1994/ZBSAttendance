package net.zhongbenshuo.attendance.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.netty.WebSocketFrameHandler;
import net.zhongbenshuo.attendance.utils.PushType;

class Product {
	double price;
}
public class Test {
	public void updatePrice(Product product,double price){
		price = price*2;
		product.price = product.price+price;
	}
	 
	 private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
	    private static List<String> getInitMonthMapWithZero() {
	        List<String> list = Lists.newArrayList();
	        LocalDate localDate = LocalDate.now();
	        int month = 12;
	        for (int j = 1; j <= month; j++) {
	            String date = "";
	            date = localDate.getYear() + (StringUtils.leftPad(String.valueOf(j), 2, "0"));
	            list.add(date);
	        }

	        return list;
	    }
	   

	    /** 

	    * @Title:main 

	    * @Description: 

	    * @param:@param args 

	    * @return: void 

	    * @throws 

	    */  

	    public static void main(String[] args){
	    	String xx = "丁忆--10000062.jpg";
	    	String[] yy = xx.split(".");
	    	String[] ll = xx.split("--");
	    }
	    
	   
}
