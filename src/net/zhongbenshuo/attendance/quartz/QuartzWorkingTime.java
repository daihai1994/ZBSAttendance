package net.zhongbenshuo.attendance.quartz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;
import net.zhongbenshuo.attendance.mapper.LoginMapper;
import net.zhongbenshuo.attendance.utils.HttpClientUtil;

@Component
@EnableScheduling
public class QuartzWorkingTime {
	@Autowired
	LoginMapper  loginMapper;
	@Scheduled(cron = "0 0 0 1 1 ?")//每年计算节假日
	
	public void run() {
		Map<String,String> resultMap = new HashMap<String, String>();
	 	List<String> dateStr = getInitMonthMapWithZero();
        String apiURL = "http://www.easybots.cn/api/holiday.php?m=" + dateStr.stream().collect(Collectors.joining(","));
        System.out.println(apiURL);
        String result = HttpClientUtil.httpGetRequest(apiURL);
        System.out.println(result);
        if (StringUtils.isNotBlank(result)) {
            Map<String, Object> map = JSON.parseObject(result);
            Map<String, Object> orderByResult = new LinkedHashMap<>();
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered( x -> orderByResult.put(x.getKey(), x.getValue()));
            for (Map.Entry<String, Object> entry : orderByResult.entrySet()) {
                Map<String, Object> mapValue = JSON.parseObject(orderByResult.get(entry.getKey()).toString());
                Map<String, Object> orderByMapValueKeyResult = new LinkedHashMap<>();
                mapValue.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> orderByMapValueKeyResult.put(x.getKey(), x.getValue()));
                for (Map.Entry<String, Object> entryValue : orderByMapValueKeyResult.entrySet()) {
                    String holiday=LocalDate.parse(entry.getKey() + "" + entryValue.getKey(), DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
                    resultMap.put(holiday,entryValue.getValue().toString());
                }
            }

        }
		LocalDate localDate = LocalDate.now();
		int year = localDate.getYear();
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		c.set(year, 0, 1);
		while(true){
	    	if(c.get(Calendar.YEAR)!=year){
	    		break;
	    	}else{
		    	if(!resultMap.containsKey(df.format(c.getTime()))){
		    		resultMap.put(df.format(c.getTime()), "0");
		    	}
		    	c.set(Calendar.DATE,c.get(Calendar.DATE)+1);
	    	}
		}
		List<WorkingTime> workingTimes = new ArrayList<WorkingTime>();
		for(String key: resultMap.keySet()){
			WorkingTime time = new WorkingTime();
			time.setDate(key);
			time.setWeek(getDayofweek(key));
			time.setStatus(Integer.valueOf(resultMap.get(key)));
			time.setYear(Integer.valueOf(key.substring(0, 4)));
			time.setMonth(Integer.valueOf(key.substring(5, 7)));
			workingTimes.add(time);
		}
		
		
		List<Company> companies = new ArrayList<Company>();
		companies = loginMapper.findCompany();
		for(Company company : companies){
			for(WorkingTime time : workingTimes){
				time.setCompany_id(company.getId());
			}
			try {
				loginMapper.addWorkingTime(workingTimes);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		List<User> userInfos = new ArrayList<User>();
		userInfos =  loginMapper.findUser();
		for(User user : userInfos){
			for(WorkingTime time : workingTimes){
				time.setCompany_id(user.getCompany_id());
				time.setUser_id(user.getUser_id());
			}
			try {
				loginMapper.addWorkingTimeUser(workingTimes);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static String getDayofweek(String date){
		  Calendar cal = Calendar.getInstance();
		  if (date.equals("")) {
		   cal.setTime(new Date(System.currentTimeMillis()));
		  }else {
		   cal.setTime(new Date(getDateByStr2(date).getTime()));
		  }
		  String week = "";
		  switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				week = "周日";
				break;
			case 2:
				week = "周一";
				break;
			case 3:
				week = "周二";
				break;
			case 4:
				week = "周三";
				break;
			case 5:
				week = "周四";
				break;
			case 6:
				week = "周五";
				break;
			case 7:
				week = "周六";
				break;
			default:
				break;
			}
	   return week;
	 }
		public static Date getDateByStr2(String dd)
		 {
		 
		  SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		  Date date;
		  try {
		   date = sd.parse(dd);
		  } catch (Exception e) {
		   date = null;
		   e.printStackTrace();
		  }
		  return date;
		 }
	
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
}
