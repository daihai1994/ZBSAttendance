package net.zhongbenshuo.attendance.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getWebTime {
    public static void main(String[] args) {
        //获取当前网络时间
        String webUrl="http://www.baidu.com";//百度时间
        String webTime=getNetworkTime(webUrl);
        System.out.println("当前网络时间为："+webTime);
    }
    
    
    /*
     * 获取当前网络时间
     */
    public static String getNetworkTime(String webUrl) {
        try {
            URL url=new URL(webUrl);
            URLConnection conn=url.openConnection();
            conn.connect();
            long dateL=conn.getDate();
            Date date=new Date(dateL);
            
            SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
            System.out.println(dateFormat.format(date));
            return dateFormat.format(date);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /*
     * 获取当前网络时间
     */
    public static String getNetworkDate(String webUrl) {
        try {
            URL url=new URL(webUrl);
            URLConnection conn=url.openConnection();
            conn.connect();
            long dateL=conn.getDate();
            Date date=new Date(dateL);
            
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Thread.sleep(5000);
            return dateFormat.format(date);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        return "";
    }
}