package net.zhongbenshuo.attendance.foreground.WeChat.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.Article;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.First;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.Keyword1;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.Keyword2;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.MiniprogramSend;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.Remark;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.SubscribeMessage;
public class WeChatSend {
	public static Logger logger = LogManager.getLogger(WeChatSend.class);
	/**
	 * *获取accessToken
	 */
	 public static String getWeiXinAccessToken (){
		 String access_token = "";
		 try{
		  //封装请求数据
		  String params = "grant_type=client_credential" + 
		  "&secret=c13d263a5e63274ae3361668aee9f005"+ //小程序的 app_secret (在微信小程序管理后台获取)
		  "&appid=wxbd38640dfe24b6d5";//小程序唯一标识appid (在微信小程序管理后台获取)
		  //发送GET请求
		  String result = sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
		  // 解析相应内容（转换成json对象）
		  JSONObject jsonObject = JSONObject.fromObject(result);
		  String accessToken = (String) jsonObject.get("access_token");
		 
		  access_token = accessToken;
		  logger.info("获取微信定时AccessToken任务结束了");
		 }catch(Exception ex){
		  logger.error("获取微信定时AccessToken任务失败." , ex);
		  access_token = "-1";
		 }
		return access_token;
	 }
	 /***
	  * 获取JsapiTicket
	  * */
	 public static String getWeiXinJsapiTicket(String access_token){

		 String jsapi_ticket = "";
		 try{
		  //封装请求数据
		  String params = "access_token="+access_token+ 
		  "&type=jsapi";
		  //发送GET请求
		  String result = sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", params);
		  // 解析相应内容（转换成json对象）
		  JSONObject jsonObject = JSONObject.fromObject(result);
		  String ticket = (String) jsonObject.get("ticket");
		 
		  jsapi_ticket = ticket;
		  logger.info("获取微信定时JsapiTicket任务结束了");
		 }catch(Exception ex){
		  logger.error("获取微信定时JsapiTicket任务失败." , ex);
		  jsapi_ticket = "-1";
		 }
		return jsapi_ticket;
	 
	 }
	 
	 public static String sendGet(String url, String param) {
		    String result = "";
		    BufferedReader in = null;
		    try {
		      String urlNameString = url + "?" + param;
		      URL realUrl = new URL(urlNameString);
		      // 打开和URL之间的连接
		      URLConnection connection = realUrl.openConnection();
		      // 设置通用的请求属性
		      connection.setRequestProperty("accept", "*/*");
		      connection.setRequestProperty("connection", "Keep-Alive");
		      connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		      // 建立实际的连接
		      connection.connect();
		      // 获取所有响应头字段
		      Map<String, List<String>> map = connection.getHeaderFields();
		      // 遍历所有的响应头字段
		      for (String key : map.keySet()) {
		        System.out.println(key + "--->" + map.get(key));
		      }
		      // 定义 BufferedReader输入流来读取URL的响应
		      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		      String line;
		      while ((line = in.readLine()) != null) {
		        result += line;
		      }
		    } catch (Exception e) {
		      System.out.println("发送GET请求出现异常！" + e);
		      e.printStackTrace();
		    }
		    // 使用finally块来关闭输入流
		    finally {
		      try {
		        if (in != null) {
		          in.close();
		        }
		      } catch (Exception e2) {
		        e2.printStackTrace();
		      }
		    }
		    return result;
		  }
	  /**

     * 微信公共账号发送给账号

     * @param content 文本内容

     * @param toUser 微信用户  

     * @return

     */

    public static  String sendTextMessageToUser(String content,String toUser,String accessToken){
    	String result  = "";
       String json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"link\",\"url\": \"http://www.baidu.com\"}";//\"text\": {\"content\": \""+content+"\"}

       //获取请求路径

       String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;

       System.out.println("json:"+json);

       try {

          result =   connectWeiXinInterface(action,json);

       } catch (Exception e) {

           e.printStackTrace();

       }
       return result;
   }

    /**

     * 微信公共账号发送给账号(本方法限制使用的消息类型是语音或者图片)

     * @param mediaId 图片或者语音内容

     * @param toUser 微信用户  

     * @param messageType 消息类型

     * @return

     */

    public  void sendPicOrVoiceMessageToUser(String accessToken,String mediaId,String toUser,String msgType){

        String json=null;

        if(msgType.equals("image")){

             json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"image\", \"image\": {\"media_id\": \""+mediaId+"\"}}";

        }else if(msgType.equals("voice")){

            json = "{\"touser\": \""+toUser+"\",\"msgtype\": \"voice\", \"voice\": {\"media_id\": \""+mediaId+"\"}}";

        }


       //获取请求路径

       String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;

       try {

           connectWeiXinInterface(action,json);

       } catch (Exception e) {

           e.printStackTrace();

       }

   }

    /**

     *  发送图文给所有的用户

     * @param openId 用户的id
     * @return 

     */

    public static  String sendNewsToUser(String openId,String access_token,String name){

    	String result  = "";

       Article a = new Article();
	     First first = new First() ;
	   	 Keyword1 keyword1 = new Keyword1();
	   	 Keyword2 keyword2 = new Keyword2();
	   	 Remark remark = new Remark();
	   	first.setValue("还有5分钟就要上班了，别忘记打卡哦~");
	   	a.setFirst(first);
	   	keyword1.setValue("08:30");
	   	a.setKeyword1(keyword1);
	   	keyword2.setValue(name);
		a.setKeyword2(keyword2);
	   	remark.setValue("每天给自己一个希望，只为明天更美好！");
	   	a.setRemark(remark);
       String str = JSONObject.fromObject(a).toString();
       MiniprogramSend miniprogramSend = new MiniprogramSend();
       miniprogramSend.setAppid("wx1e0994429cda642f");
       miniprogramSend.setPagepath("pages/index/index");
       String miniprogramSendstr = JSONObject.fromObject(miniprogramSend).toString();
        String json = "{\"touser\":\""+openId+"\",\"template_id\":\"QkoUVF3yNliSlz3KcGviwQKYK6gMH7uDnaTsUjDengE\",\"url\":\"http://www.zhongbenshuo.com/dist\""
        		+ ",\"miniprogram\":"+miniprogramSendstr+",\"data\":" +str+"}";

        json = json.replace("picUrl", "picurl");

        System.out.println(json);
        
        
       



       //获取请求路径

       String action = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;

       try {

    	   result =   connectWeiXinInterface(action,json);

       } catch (Exception e) {

           e.printStackTrace();

       }
       return result;
    }
    
    public static  String sendNewsToUserSmallProgram(String openId,String access_token,String name){

    	String result  = "";
    	access_token = "27_B3jJN6Sg2txL1Y9uR2bfh8K-bHYW57oLvaXjEWgfR535CsyqNOVTMNVgUBGEz_FXbocAPmK9cwt2OqEeqXmevSUDIgSrD4HlGl3c1VU4W9rnK-vUFWAF5mLHhCsGVOgAFATIB";
    	SubscribeMessage a = new SubscribeMessage();
	    a.setDate2("08:30");
	    a.setPhrase4(name+" : 还有5分钟就要上班了，别忘记打卡哦~");
       String str = JSONObject.fromObject(a).toString();

        String json = "{\"touser\":\""+openId+"\",\"template_id\":\"aVDuTvBwQkWx-my4o6UQpl94x9EKgCAfGLwsmrdTgCY\",\"page\":\"index\",\"data\":" +

                str+"}";

        json = json.replace("picUrl", "picurl");

        System.out.println(json);
        
        
       



       //获取请求路径

       String action = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+access_token;

       try {

    	   result =   connectWeiXinInterface(action,json);

       } catch (Exception e) {

           e.printStackTrace();

       }
       return result;
    }

    /**

     * 连接请求微信后台接口

     * @param action 接口url

     * @param json  请求接口传送的json字符串

     */

    public static  String connectWeiXinInterface(String action,String json){

        URL url;
        String result = "";
       try {

           url = new URL(action);

           HttpURLConnection http = (HttpURLConnection) url.openConnection();

           http.setRequestMethod("POST");

           http.setRequestProperty("Content-Type",

                   "application/x-www-form-urlencoded");

           http.setDoOutput(true);

           http.setDoInput(true);

           System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒

           System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

           http.connect();

           OutputStream os = http.getOutputStream();

           os.write(json.getBytes("UTF-8"));// 传入参数

           InputStream is = http.getInputStream();

           int size = is.available();

           byte[] jsonBytes = new byte[size];

           is.read(jsonBytes);

            result = new String(jsonBytes, "UTF-8");

           System.out.println("请求返回结果:"+result);

           os.flush();

           os.close();
          

       } catch (Exception e) {

           e.printStackTrace();

       }
       return result;
    }
}
