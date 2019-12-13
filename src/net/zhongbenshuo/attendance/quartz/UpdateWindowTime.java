package net.zhongbenshuo.attendance.quartz;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.zhongbenshuo.attendance.bean.Face;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.mapper.LoginMapper;

@Component
@EnableScheduling
public class UpdateWindowTime {
	@Autowired
	LoginMapper  loginMapper;
	
	@Scheduled(cron = "0 1 0 ? * ?")//修改系统时间
	public void run() {
		System.out.println("进入定时任务");
		SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simtime=new SimpleDateFormat("HH:mm:ss");
		Date date=getNetworkTime("http://www.baidu.com");
		//Date date = new Date();
		String stime=simtime.format(date);
		String sdate=simdate.format(date);
		System.out.println("北京时间:"+sdate+" "+stime);
		Runtime run=Runtime.getRuntime();	
		try{
			run.exec("cmd /c time "+stime);
			run.exec("cmd /c date "+sdate);
			System.out.println("设置成功");
			Thread.sleep(3000);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		String dates = sdate+" "+sdate;
		List<Face> faceList = new ArrayList<Face>();
		faceList = loginMapper.findFaceList();
		
		try {
			WorkingTime time = OnLineInitial.workingTime;
			time = loginMapper.findWorkingTime(sdate);
			OnLineInitial.workingTime = time;
			System.out.println(time.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Face face : faceList){
			String endDate = face.getEndDate();
			int id = face.getId();
			if(dates.compareTo(endDate)>0){
				loginMapper.updateFaceEffective(id);
			}
		}
	}
	public static Date getNetworkTime(String url1){
		try{
			URL url=new URL(url1);
			URLConnection urlc=url.openConnection();
			urlc.connect();
			long time=urlc.getDate();
			Date date=new Date(time);
			return date;
		}catch(MalformedURLException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return null;
	}
}
