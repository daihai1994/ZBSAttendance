package net.zhongbenshuo.attendance.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

public class Utils {
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
	 * 获取当年的节假日
	 * @return
	 */
	private static Map<String,String> getHoliday(){
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
	return resultMap;
	
	}
	/**
	 * 字符串补零
	 * @param str
	 * @param strLength
	 * @return
	 */
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左补0
			str = sb.toString();
			strLen = str.length();
		}
			return str;
		}
	
	/**
	 * 
	 * @param d 
	 * @param newScale小数点几位
	 * @return
	 */
	public static Double roundValue(Double d, int newScale) {

	        Double retValue = null;
	        if (d != null) {
	            BigDecimal bd = new BigDecimal(d);
	            retValue = bd.setScale(newScale,BigDecimal.ROUND_HALF_UP).doubleValue();
	        }
	        return retValue;
	    }
	
	/**
	 * 
	 * @param d 
	 * @param newScale小数点几位
	 * @return
	 */
	public static Float roundValue(Float d, int newScale) {

		Float retValue = null;
	        if (d != null) {
	            BigDecimal bd = new BigDecimal(d);
	            retValue = bd.setScale(newScale,BigDecimal.ROUND_HALF_UP).floatValue();
	        }
	        return retValue;
	    }
	
	/**
	 * 去除小数末尾无用的0
	 *
	 * @param number 浮点型字符串
	 * @return 去0后的字符串
	 */
	public static String removeZero(Float d, int newScale) {
		String number = "";
		if (d != null) {
            BigDecimal bd = new BigDecimal(d);
            number = String.valueOf(bd.setScale(newScale,BigDecimal.ROUND_HALF_UP).floatValue());
        }
	    if (number.indexOf(".") > 0) {
	        //正则表达
	        //去掉后面无用的零
	        number = number.replaceAll("0+?$", "");
	        //如小数点后面全是零则去掉小数点
	        number = number.replaceAll("[.]$", "");
	    }
	    return number;
	}

    public static void main(String[] args) throws Exception {}
}
