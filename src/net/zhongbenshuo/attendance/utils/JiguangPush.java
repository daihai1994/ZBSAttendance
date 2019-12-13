
package net.zhongbenshuo.attendance.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

/**
 * java后台极光推送方式一：使用Http API
 * 此种方式需要自定义http请求发送客户端:HttpClient
 */
@SuppressWarnings({ "deprecation", "restriction" })
public class JiguangPush {
    private static final Logger log = LoggerFactory.getLogger(JiguangPush.class);
    private static String masterSecret = "2ea40af21dc1427de2035fb0";
    private static String appKey = "ee692474afac267ab51e28e0";
    private static String pushUrl = "https://api.jpush.cn/v3/push";    
    private static boolean apns_production = true;    
    private static int time_to_live = 86400*7;
    private static final String ALERT = "推送信息";  
    
    /**
     * 极光推送
     */
    public static JSONObject jiguangPush(JSONObject audience,String alert,String title,String message){
        try{
            String result = push(pushUrl,audience,alert,appKey,masterSecret,apns_production,time_to_live,title,message);
            JSONObject resData = JSONObject.fromObject(result);
                if(resData.containsKey("error")){
                    JSONObject error = JSONObject.fromObject(resData.get("error"));
                    log.info("错误信息为：" + error.get("message").toString());
                }
            return resData;
        }catch(Exception e){
            log.error("针对别名为" + audience.toString() + "的信息推送失败！",e);
        }
        return new JSONObject();
    }
    
    /**
     * 组装极光推送专用json串
     * @param alias
     * @param alert
     * @return json
     */
    public static JSONObject generateJson(JSONObject audience,String alert,boolean apns_production,int time_to_live,String title,String message){
        JSONObject json = new JSONObject();
        JSONArray platform = new JSONArray();//平台
        platform.add("android");
        platform.add("ios");
        
//        JSONObject audience = new JSONObject();//推送目标
//        JSONArray alias1 = new JSONArray();
//        alias1.add(alias);
       // audience.put("alias", alias1);
        
        JSONObject notification = new JSONObject();//通知内容
        JSONObject android = new JSONObject();//android通知内容
        android.put("alert", message);
        android.put("title", title);
        android.put("builder_id", 1);
//        JSONObject android_extras = new JSONObject();//android额外参数
//        android_extras.put("data", alert);
        android.put("extras", alert);
        
        JSONObject ios = new JSONObject();//ios通知内容
        ios.put("alert", alert);
        ios.put("sound", "default");
        ios.put("badge", "+1");
        JSONObject ios_extras = new JSONObject();//ios额外参数
        ios_extras.put("type", "infomation");
        ios.put("extras", ios_extras);
        notification.put("android", android);
        notification.put("ios", ios);
        
        JSONObject options = new JSONObject();//设置参数
        options.put("time_to_live", Integer.valueOf(time_to_live));
        options.put("apns_production", apns_production);
        
        json.put("platform", platform);
      
        json.put("audience", audience);
       
        json.put("notification", notification);
        json.put("options", options);
        return json;
        
    }
    
    /**
     * 推送方法-调用极光API
     * @param reqUrl
     * @param alias
     * @param alert
     * @return result
     */
    public static String push(String reqUrl,JSONObject audience,String alert,String appKey,String masterSecret,boolean apns_production,int time_to_live,String title,String message){
        String base64_auth_string = encryptBASE64(appKey + ":" + masterSecret);
        String authorization = "Basic " + base64_auth_string;
        return sendPostRequest(reqUrl,generateJson(audience,alert,apns_production,time_to_live,title,message).toString(),"UTF-8",authorization);
    }
    
    /**
     * 发送Post请求（json格式）
     * @param reqURL
     * @param data
     * @param encodeCharset
     * @param authorization
     * @return result
     */
    @SuppressWarnings({ "resource" })
    public static String sendPostRequest(String reqURL, String data, String encodeCharset,String authorization){
        HttpPost httpPost = new HttpPost(reqURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        String result = "";
        try {
        	System.out.println("极光推送数据:"+data);
             StringEntity entity = new StringEntity(data, encodeCharset);
             entity.setContentType("application/json");
             httpPost.setEntity(entity);
             httpPost.setHeader("Authorization",authorization.trim());
             response = client.execute(httpPost);
             result = EntityUtils.toString(response.getEntity(), encodeCharset);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);  
        }finally{
            client.getConnectionManager().shutdown();
        }
        return result;
    }
     /** 
　　　　* BASE64加密工具
　　　　*/
     public static String encryptBASE64(String str) {
         byte[] key = str.getBytes();
       BASE64Encoder base64Encoder = new BASE64Encoder();
       String strs = base64Encoder.encodeBuffer(key);
         return strs;
     }
}
