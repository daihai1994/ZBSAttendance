package net.zhongbenshuo.attendance.foreground.WeChat.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.bean.WeChatInfo;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.DefaultExpireKey;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.ExpireKey;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.XMLMessage;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.XMLTextMessage;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.service.IUserService;
import net.zhongbenshuo.attendance.utils.HttpClientUtil;
import net.zhongbenshuo.attendance.utils.TimeUtils;

@Controller
@RequestMapping(value = "/WeChatController", produces = "text/html;charset=UTF-8")
public class WeChatController {
	private static String access_token = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxbd38640dfe24b6d5&secret=c13d263a5e63274ae3361668aee9f005";//获取token
	private static String createMenu =  "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=c13d263a5e63274ae3361668aee9f005";//自定义菜单url
	private static String getUnionid = "https://api.weixin.qq.com/cgi-bin/user/info";//开发者可通过OpenID来获取用户基本信息。请使用https协议。
	private static ExpireKey expireKey = new DefaultExpireKey();
	private final static String TOKEN = "zbs1234";//服务器设置token
	public static Logger logger = LogManager.getLogger(WeChatController.class);
	@Autowired
	private  IUserService iuserService;
	
	@Autowired
	ConfigInforMapper configInforMapper;
	/**
	 * 校验接口
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 */
	@RequestMapping(value = "/signature.do")
	@ResponseBody
	public String signature(@RequestParam String signature, @RequestParam String timestamp,
	                        @RequestParam String nonce, @RequestParam String echostr){
	 
		logger.info("wx-token校验接口："+signature+"，"+timestamp+","+nonce+","+echostr);
	    String[] strings = new String[]{TOKEN,timestamp,nonce};
	    StringBuilder builder = new StringBuilder();
	    Arrays.sort(strings);
	    for (int i=0;i<strings.length;i++){
	        builder.append(strings[i]);
	    }
	    String res = sha1(builder.toString());
	    logger.info("加密后："+res);
	    if(signature.equalsIgnoreCase(res)){
	    	logger.info("成功！");
	        return echostr;
	    }
	    logger.info("失败！");
	    return "";
	}
	
	private String sha1(String str){
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        byte[] bytes = digest.digest(str.getBytes());
	        return toHex(bytes);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	 
	private String toHex(byte[] bytes){
	 
	    String str = "";
	    for(byte b : bytes){
	        char[] chars = new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	        char[] temp = new char[2];
	        temp[0] = chars[(b>>>4)&0x0F];
	        temp[1] = chars[b&0x0F];
	 
	        str += new String(temp);
	    }
	    return str;
	}
	/**
	 * 事件推送接口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@PostMapping("/signature.do")
	public void wechatCallbackApi(HttpServletRequest request, HttpServletResponse response) throws Exception {//@RequestBody String notifyData,
 
		ServletOutputStream outputStream = response.getOutputStream(); 
		logger.info("1.收到微信服务器消息");
        Map<String, String> wxdata=parseXml(request);
//        XMLMessage xmlTextMessage = new XMLTextMessage(
//                wxdata.get("FromUserName"),
//                wxdata.get("ToUserName"),
//                "");
//        //回复 
//        xmlTextMessage.outputStreamWrite(outputStream);
        if(wxdata.get("MsgType")!=null){
            if("event".equals(wxdata.get("MsgType"))){
            	String openId = wxdata.get("FromUserName");
                if( "subscribe".equals(wxdata.get("Event"))){//d
                	WeChatInfo weChatInfo = OnLineInitial.weChatInfo;//微信公众号基础信息
            		SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            		if(weChatInfo!=null){
            			/**
            			 * 计算accesstoken
            			 */
            			if(StringUtils.isNotBlank(weChatInfo.getAccess_token())){
            				Date date=new Date();
            				String sdate=simdate.format(date);
            				String expires_in = weChatInfo.getAccess_token_time();
            				if(sdate.compareTo(expires_in)>0){
            					String expires = TimeUtils.addTime(7200);
            					String access_token = WeChatSend.getWeiXinAccessToken();
            					weChatInfo.setAccess_token(access_token);
            					weChatInfo.setAccess_token_time(expires);
            					configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
            				}
            			}else{
            				String expires = TimeUtils.addTime(7200);
            				String access_token = WeChatSend.getWeiXinAccessToken();
            				weChatInfo.setAccess_token(access_token);
            				weChatInfo.setAccess_token_time(expires);
            				configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
            			}
            		}
                	String apiURL = getUnionid+"?access_token="+weChatInfo.getAccess_token()+"&openid="+openId+"&lang=zh_CN";
    				System.out.println(apiURL);
    		        String result = HttpClientUtil.httpGetRequest(apiURL);
    		        System.out.println(result);
    		        if (StringUtils.isNotBlank(result)) {
    		        	 JSONObject jsonObj = JSONObject.fromObject(result);
    			        String unionid = (String) jsonObj.get("unionid");
    			        configInforMapper.addUnionId(openId,unionid);
    		        }
                }
                if( "unsubscribe".equals(wxdata.get("Event"))){
                	 configInforMapper.deleteUnionId(openId);
                }
                if("view_miniprogram".equals(wxdata.get("Event"))){
                	WeChatInfo weChatInfo = OnLineInitial.weChatInfo;//微信公众号基础信息
            		SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            		if(weChatInfo!=null){
            			/**
            			 * 计算accesstoken
            			 */
            			if(StringUtils.isNotBlank(weChatInfo.getAccess_token())){
            				Date date=new Date();
            				String sdate=simdate.format(date);
            				String expires_in = weChatInfo.getAccess_token_time();
            				if(sdate.compareTo(expires_in)>0){
            					String expires = TimeUtils.addTime(7200);
            					String access_token = WeChatSend.getWeiXinAccessToken();
            					weChatInfo.setAccess_token(access_token);
            					weChatInfo.setAccess_token_time(expires);
            					configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
            				}
            			}else{
            				String expires = TimeUtils.addTime(7200);
            				String access_token = WeChatSend.getWeiXinAccessToken();
            				weChatInfo.setAccess_token(access_token);
            				weChatInfo.setAccess_token_time(expires);
            				configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
            			}
            		}
                	String apiURL = getUnionid+"?access_token="+weChatInfo.getAccess_token()+"&openid="+openId+"&lang=zh_CN";
    				System.out.println(apiURL);
    		        String result = HttpClientUtil.httpGetRequest(apiURL);
    		        System.out.println(result);
    		        if (StringUtils.isNotBlank(result)) {
    		        	 JSONObject jsonObj = JSONObject.fromObject(result);
    			        String unionid = (String) jsonObj.get("unionid");
    			        configInforMapper.addUnionId(openId,unionid);
    		        }
                }
            }
        }
      return;
	}
	 /**
     * 数据流输出
     * @param outputStream
     * @param text
     * @return
     */
    private boolean outputStreamWrite(OutputStream outputStream, String text){
        try {
            outputStream.write(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * dom4j 解析 xml 转换为 map
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }

    /**
     * 回复微信服务器"文本消息"
     * @param response
     * @param returnvaleue
     */
    public void output(HttpServletResponse response, String returnvaleue) {
        try {
            PrintWriter pw = response.getWriter();
            pw.write(returnvaleue);
            logger.info("****************return valeue***************="+returnvaleue);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
}
