package net.zhongbenshuo.attendance.controller;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.IOUtils;
import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;

import net.sf.json.JSONObject;
import net.zhongbenshuo.attendance.FaceEngine.FaceServer;
import net.zhongbenshuo.attendance.bean.AllUserInfo;
import net.zhongbenshuo.attendance.bean.AllUserInfoAddressBook;
import net.zhongbenshuo.attendance.bean.AllUserInfoStatus;
import net.zhongbenshuo.attendance.bean.Announcement;
import net.zhongbenshuo.attendance.bean.AppealAttendanceRecord;
import net.zhongbenshuo.attendance.bean.AppealAttendanceRecordAudit;
import net.zhongbenshuo.attendance.bean.AttendanceFaceRecord;
import net.zhongbenshuo.attendance.bean.AttendanceRecord;
import net.zhongbenshuo.attendance.bean.AttendanceRule;
import net.zhongbenshuo.attendance.bean.Department;
import net.zhongbenshuo.attendance.bean.Face;
import net.zhongbenshuo.attendance.bean.FaceRecord;
import net.zhongbenshuo.attendance.bean.FaceWebsocket;
import net.zhongbenshuo.attendance.bean.OpenAndCloseDoorRecord;
import net.zhongbenshuo.attendance.bean.OutAttendanceRecord;
import net.zhongbenshuo.attendance.bean.OutAttendanceRecordAudit;
import net.zhongbenshuo.attendance.bean.OutAttendanceRecordCopy;
import net.zhongbenshuo.attendance.bean.OvertimeType;
import net.zhongbenshuo.attendance.bean.Position;
import net.zhongbenshuo.attendance.bean.Status;
import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.bean.VacationType;
import net.zhongbenshuo.attendance.bean.VisitorInfo;
import net.zhongbenshuo.attendance.bean.VisitorSubscribe;
import net.zhongbenshuo.attendance.bean.WeChatInfo;
import net.zhongbenshuo.attendance.bean.WorkingTime;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.MINIPROGRAM;
import net.zhongbenshuo.attendance.foreground.WeChat.bean.VIEW;
import net.zhongbenshuo.attendance.foreground.WeChat.controller.WeChatSend;
import net.zhongbenshuo.attendance.foreground.advertisement.bean.Advertisement;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.AppealAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.BusinessTraveIRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.BusinessTraveIRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OutGoingRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OutGoingRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.OverTimeRecordCopy;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.VacationRecordAudit;
import net.zhongbenshuo.attendance.foreground.applyRecord.bean.VacationRecordCopy;
import net.zhongbenshuo.attendance.foreground.attendanceRecord.bean.WorkingTimeUser;
import net.zhongbenshuo.attendance.foreground.common.service.AndroidService;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;
import net.zhongbenshuo.attendance.foreground.configInfor.mapper.ConfigInforMapper;
import net.zhongbenshuo.attendance.foreground.homePagePicture.bean.HomePagePicture;
import net.zhongbenshuo.attendance.foreground.listener.OnLineInitial;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.BusinessTraveIRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutAttendanceInfo;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OutGoingRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.OverTimeRecord;
import net.zhongbenshuo.attendance.foreground.outAttendance.bean.VacationRecord;
import net.zhongbenshuo.attendance.foreground.version.bean.VersionInfo;
import net.zhongbenshuo.attendance.foreground.version.service.VersionService;
import net.zhongbenshuo.attendance.foreground.wage.bean.Wage;
import net.zhongbenshuo.attendance.mapper.LoginMapper;
import net.zhongbenshuo.attendance.netty.WebSocketFrameHandler;
import net.zhongbenshuo.attendance.service.IUserService;
import net.zhongbenshuo.attendance.utils.AesCBC;
import net.zhongbenshuo.attendance.utils.AndroidHeaderInfo;
import net.zhongbenshuo.attendance.utils.Futil;
import net.zhongbenshuo.attendance.utils.GetKey;
import net.zhongbenshuo.attendance.utils.GsonUtils;
import net.zhongbenshuo.attendance.utils.HttpClientUtil;
import net.zhongbenshuo.attendance.utils.JPushData;
import net.zhongbenshuo.attendance.utils.JiguangPush;
import net.zhongbenshuo.attendance.utils.OnLineInitialRSAKey;
import net.zhongbenshuo.attendance.utils.PushData;
import net.zhongbenshuo.attendance.utils.PushType;
import net.zhongbenshuo.attendance.utils.RSAUtils2;
import net.zhongbenshuo.attendance.utils.TimeUtils;
import net.zhongbenshuo.attendance.utils.WebRsaKey;

@Controller
@CrossOrigin
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class AndroidCotroller {
	public static Logger logger = LogManager.getLogger(AndroidCotroller.class);
	
	private static  final int FACECOUNT = 1;//人脸图片最大值
	
	private static final int ATTENDANCE_TYPE_QIANDAO = 1, ATTENDANCE_TYPE_QIANTUI = 2;//1是签到，2是签退
	
	private static final int RESULT_QIANDAO_CHENGGONG = 1, RESULT_QIANTUI_CHENGGONG = 2, RESULT_CHIDAO_DAKA = 3,
			RESULT_ZAOTUI_DAKA = 4, DAISHENPI = 5, SHENPIZHONG = 6,
			SHENPICHENGONG = 7,SHENPISHIBAI = 8;
	//1：签到成功；2：签退成功；3：迟到打卡；4：早退打卡；5待审批；6审批中；7审批成功;8审批失败
	
	private static final int OUT_RECORD_WEISHENPI = 0,OUT_RECORD_KAISHISHENPI =1,OUT_RECORD_SHENPITONGGUO = 2,OUT_RECORD_SHENPIBUTONGGUO = 3;
	//外勤审打开记录审批状态，0是未审批，1是开始审批，2是审批通过，3是审批不通过
	
	
	private static final String webUrl="http://www.baidu.com";
	
	private static final int AESKeyLength = 32;
	
	private static final String APPID = "wxbd38640dfe24b6d5";//公众号的唯一标识
	
	private static final String XAPPID = "wx1e0994429cda642f";//小程序appid
	
	
	//private static final String XSECRET = "0c4e24217b102a0a7a78021ebcd1c712";//小程序AppSecret 个人
	private static final String XSECRET = "1d4aee7461b16fb0f110ea6dbb858916";//小程序AppSecret  公司
	
	private static final String SECRET = "c13d263a5e63274ae3361668aee9f005";//公众号的appsecret
	
	private static final String grant_type = "authorization_code";//填写为authorization_code
	
	private static final String Oauth2_Access_token = "http://api.weixin.qq.com/sns/oauth2/access_token";//页面授权获取code后，请求以下链接获取access_token
	
	private static final String create_menu = "https://api.weixin.qq.com/cgi-bin/menu/create";//创建微信公众号菜单
	
	
	private static final String jscode2session = "https://api.weixin.qq.com/sns/jscode2session";//页面授权获取code后，请求以下链接获取access_token
	private static final String appId = "FxsTMG6wwVwtL4dChvPz11suaVwhJGgYyjdhv43XqKkP";
	private static final String sdkKey = "FRkjDK3oj54tjvEv55Zui91XxJu19xhDm66FPiXoNiVZ";

	@Autowired
	private  IUserService iuserService;

	@Autowired
	VersionService versionService;

	@Autowired
	AndroidService androidService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private LoginMapper loginMapper;
	
	@Autowired
	ConfigInforMapper configInforMapper;

	/***
	 * aes加密
	 * @param map
	 * @param key
	 * @return
	 */
	private String getAesCBCencrypt(Map<String, Object> map,String key) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", map.get("code"));
		map.remove("code");
		jsonObject.put("data", map);
		System.out.println(jsonObject.toString());
		String res  = "";
		try {
			res= AesCBC.encrypt(GsonUtils.convertJSON(jsonObject),"utf-8",key,AesCBC.ivParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	/***
	 * aes加密
	 * @param map
	 * @param key
	 * @return
	 */
	private String getAesCBCencryptObject(String code,String json,String key) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code",code);
		jsonObject.put("data", json);
		System.out.println(jsonObject.toString());
		String res  = "";
		try {
			res= AesCBC.encrypt(GsonUtils.convertJSON(jsonObject),"utf-8",key,AesCBC.ivParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private String getAesCBCencryptInt(Map<String, Object> map,String key) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", map.get("code"));
		map.remove("code");
		jsonObject.put("data", map.get("status"));
		String res  = "";
		try {
			res= AesCBC.encrypt(GsonUtils.convertJSON(jsonObject),"utf-8",key,AesCBC.ivParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/***
	 *  aes解密
	 * @param body
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	private String getAesCBCdecrypt(String body,String key) throws Exception {

		String res  = "";
			res= AesCBC.decrypt(body,"utf-8",key,AesCBC.ivParameter);
	
		return res;
	}
	/***
	 * 返回安卓web的rsa公钥
	 * @return
	 */
	@RequestMapping(value = "/rsaPublicKey.do")
	@ResponseBody
	public String rsaPublicKey(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code",1001);
		JSONObject jsonObjectData = new JSONObject();
		String timenew = TimeUtils.getCurrentDate();
		List<Advertisement> advertisementList= new ArrayList<Advertisement>();
		advertisementList = iuserService.findAdvertisementList(timenew);
		String pictureUrl = "";
		String adUrl = "";
		int time = 1;
		if(advertisementList!=null&&advertisementList.size()>0){
			if(advertisementList.get(0).getForce()==1){
				pictureUrl = advertisementList.get(0).getPicUrl().split("webapps")[1];
				adUrl = advertisementList.get(0).getAdUrl();
				time = advertisementList.get(0).getShowTime();
			}else{
				int size = advertisementList.size();
				int sizes = new Random().nextInt(size);
				System.out.println(size);
				Advertisement advertisement= advertisementList.get(sizes);
				pictureUrl = advertisement.getPicUrl().split("webapps")[1];
				adUrl = advertisement.getAdUrl();
				time = advertisement.getShowTime();
			}
		}
		jsonObjectData.put("rsaPublicKey", OnLineInitialRSAKey.webRsaKey.getRsaPublicKey());
		jsonObjectData.put("pictureUrl",pictureUrl);
		jsonObjectData.put("adUrl",adUrl);
		jsonObjectData.put("time",time);
		jsonObject.put("data",jsonObjectData);
		return GsonUtils.convertJSON(jsonObject);
	}
	/**
	 * 获取微信accesstoken等信息
	 * @return
	 */
	@RequestMapping(value = "/weChatInfo.do")
	@ResponseBody
	public String weChatInfo(){
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
			/**
			 * 计算jsapi_ticket
			 */
			if(StringUtils.isNotBlank(weChatInfo.getTicket())){
				Date date=new Date();
				String sdate=simdate.format(date);
				String expires_in = weChatInfo.getTicket_time();
				if(sdate.compareTo(expires_in)>0){
					String expires = TimeUtils.addTime(7200);
					String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
					weChatInfo.setTicket(ticket);
					weChatInfo.setTicket_time(expires);
					configInforMapper.updateWeChatInfoTicket(expires,ticket);
				}
			}else{
				String expires = TimeUtils.addTime(7200);
				String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
				weChatInfo.setTicket(ticket);
				weChatInfo.setTicket_time(expires);
				configInforMapper.updateWeChatInfoTicket(expires,ticket);
			}
		}
		return GsonUtils.convertJSON(weChatInfo);
	}
	
	/**
	 * 获取微信基本配置信息
	 * @return
	 */
	@RequestMapping(value = "/jssdkConfig.do")
	@ResponseBody
	public String jssdkConfig(String url,HttpServletResponse response,HttpSession session,HttpServletRequest request){
		System.out.println("进入jssdkConfig方法");
		logger.info("获取微信基本配置信息前台传入url:"+url);
		String urls = request.getParameter("url");
		logger.info("获取微信基本配置信息前台传入urls:"+urls);
		if(StringUtils.isBlank(url)){
			url = "http://www.zhongbenshuo.com/dist/signpage";
		}
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
			/**
			 * 计算jsapi_ticket
			 */
			if(StringUtils.isNotBlank(weChatInfo.getTicket())){
				Date date=new Date();
				String sdate=simdate.format(date);
				String expires_in = weChatInfo.getTicket_time();
				if(sdate.compareTo(expires_in)>0){
					String expires = TimeUtils.addTime(7200);
					String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
					weChatInfo.setTicket(ticket);
					weChatInfo.setTicket_time(expires);
					configInforMapper.updateWeChatInfoTicket(expires,ticket);
				}
			}else{
				String expires = TimeUtils.addTime(7200);
				String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
				weChatInfo.setTicket(ticket);
				weChatInfo.setTicket_time(expires);
				configInforMapper.updateWeChatInfoTicket(expires,ticket);
			}
		}
		HashMap<String, String> jssdk=new HashMap<String, String>();
		try {
			jssdk = JSSDK_Config.jsSDK_Sign(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GsonUtils.convertJSON(jssdk);
	}
	
	/**
	 * 强制初始化微信的accesstoken和jsapiticket
	 * @return
	 */
	@RequestMapping(value = "/weChatConfig.do")
	@ResponseBody
	public String weChatConfig(){
		SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simtime=new SimpleDateFormat("HH:mm:ss");
		Date date=getNetworkTime("https://www.baidu.com");
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
		
		WeChatInfo weChatInfo = OnLineInitial.weChatInfo;//微信公众号基础信息
		
		String expires = TimeUtils.addTime(7200);
		String access_token = WeChatSend.getWeiXinAccessToken();
		weChatInfo.setAccess_token(access_token);
		weChatInfo.setAccess_token_time(expires);
		configInforMapper.updateWeChatInfoAccessToken(expires,access_token);
		
		String expiress = TimeUtils.addTime(7200);
		String ticket = WeChatSend.getWeiXinJsapiTicket(weChatInfo.getAccess_token());
		weChatInfo.setTicket(ticket);
		weChatInfo.setTicket_time(expiress);
		configInforMapper.updateWeChatInfoTicket(expiress,ticket);
		
		return GsonUtils.convertJSON(weChatInfo);
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
	/***
	 * 解析AndroidHeaderInfo
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public AndroidHeaderInfo androidHeaderInfo(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException, IOException{
		AndroidHeaderInfo androidHeaderInfo = new AndroidHeaderInfo();
		try {
			response.setHeader("Access-Control-Expose-Headers", "serverEncryptedKey,appPublicKey,serverPublicKey,code");
			String aesKey  = GetKey.generateAESKey(AESKeyLength);//获取随机的aesKey
			androidHeaderInfo.setWebaesKey(aesKey);
			String appEncryptedKey = request.getHeader("appEncryptedKey");//安卓通过web返回的公钥进行加密的aesKey
			String appSignature = request.getHeader("appSignature");//安卓用自己的私钥签名的
			String appPublicKey = request.getHeader("appPublicKey");//安卓的rsa公钥（对于web返回的aesKey的加密）
			response.addHeader("appPublicKey",appPublicKey);//返回安卓的公钥
			//服务端RSA公钥的对比
			String serverPublicKey = request.getHeader("serverPublicKey");//安卓返回的server端的公钥
			
			String webEncryptedAesKey = "";
			try {
				webEncryptedAesKey = RSAUtils2.encryptByPublicKey(aesKey,appPublicKey);//对web端的aeskey进行安卓的公钥加密
			} catch (Exception e) {
				androidHeaderInfo.setCode(1008);
				e.printStackTrace();
				System.out.println("serverAesKey进行安卓公钥加密错误");
			}
			response.addHeader("serverEncryptedKey", webEncryptedAesKey);//返回对aesKey进行安卓的公钥加密
			
			WebRsaKey webRsaKey= new WebRsaKey();
			webRsaKey = loginMapper.webRsaKey();
			if(webRsaKey!=null){
				if(!webRsaKey.getRsaPublicKey().equals(serverPublicKey)){
					OnLineInitialRSAKey.webRsaKey = webRsaKey;
					androidHeaderInfo.setCode(1002);//服务端公钥过期
					response.addHeader("serverPublicKey", OnLineInitialRSAKey.webRsaKey.getRsaPublicKey());//返回server的公钥
					return androidHeaderInfo;
				}
			}
			response.addHeader("serverPublicKey", OnLineInitialRSAKey.webRsaKey.getRsaPublicKey());//返回server的公钥
			
		
			
			 @SuppressWarnings("deprecation")
	   		String body = "";
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
				body = IOUtils.readAll(reader);//获取请求体的数据
			} catch (Exception e) {
				androidHeaderInfo.setCode(1009);
				e.printStackTrace();
				System.out.println("获取body错误");
				return androidHeaderInfo;
			}
	   		String appAesKey = "";
	   		try {
	   			appAesKey= 	RSAUtils2.decryptByPrivateKey(appEncryptedKey,OnLineInitialRSAKey.webRsaKey.getRsaPrivateKey());//通过web的私钥对appEncryptedKey进行解密
			} catch (Exception e) {
				androidHeaderInfo.setCode(1004);
				e.printStackTrace();
				System.out.println("私钥RSA解密AES秘钥失败");
				return androidHeaderInfo;
			}
	   			
	   		String encryptData = "";
	   			try {
					encryptData =getAesCBCdecrypt(body,appAesKey);
				} catch (Exception e) {
					androidHeaderInfo.setCode(1005);
					e.printStackTrace();
					System.out.println("AES解密请求体失败");
					return androidHeaderInfo;
				}//对请求体进行aesCBC解密
	   		boolean verify = RSAUtils2.verify(encryptData, appPublicKey, appSignature);//安卓的公钥进行校验
	   		if(!verify){
	   			androidHeaderInfo.setCode(1006);//公钥验证失败
	   			return androidHeaderInfo;
	   		}
	   		androidHeaderInfo.setWebEncryptedAesKey(webEncryptedAesKey);
	   		androidHeaderInfo.setVerify(verify);
			androidHeaderInfo.setAppAesKey(appAesKey);
			androidHeaderInfo.setAppEncryptedKey(appEncryptedKey);
			androidHeaderInfo.setAppPublicKey(appPublicKey);
			androidHeaderInfo.setAppSignature(appSignature);
			androidHeaderInfo.setBody(body);
			androidHeaderInfo.setEncryptData(encryptData);
			return androidHeaderInfo;
		} catch (Exception e) {
			androidHeaderInfo.setCode(2001);
			e.printStackTrace();
			System.out.println("未知错误");
			return androidHeaderInfo;
		}
		
	}
	
	// 注册账号
	@RequestMapping(value = "/register.do")
	@ResponseBody
	public String register(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		String ipAddress = Futil.getIpAddr(request);
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		map.put("user", user);
		 @SuppressWarnings("deprecation")
	   		String body = "";
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
				body = IOUtils.readAll(reader);//获取请求体的数据
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("获取body错误");
			}
			JSONObject object = new JSONObject();
			 object = JSONObject.fromObject(body);
			 String phoneNumber = object.optString("phoneNumber");
			 String password = object.optString("password");
		 password = AesCBC.encrypt(password,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
		int n = iuserService.registerUser(phoneNumber, password, ipAddress);
		System.out.println("操作结果：" + n);
		int code = 2001 ;
		switch (n) {
		case 0:
			code = 1010;
			break;
		case 1:
			code = 1001;
			user = iuserService.searchUser("PhoneNumber", phoneNumber);
			break;
		case 2:
			code = 1012;
			break;
		default:
			break;
		}
		map.put("code", code);
		map.put("user", user);
	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", map.get("code"));
		map.remove("code");
		jsonObject.put("data", map);
		return GsonUtils.convertJSON(jsonObject);
	}
	
	
	// 登录
	@RequestMapping(value = "/login.do")
	@ResponseBody
	public String login(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws JSONException, UnsupportedEncodingException, IOException {
			Map<String, Object> map = new HashMap<>();	
			User user = new User();
			VersionInfo versionInfo = new VersionInfo();
			List<Company> company = new ArrayList<Company>();
			map.put("user", user);
			map.put("version", versionInfo);
			map.put("company", company);
			AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
			androidHeaderInfos = androidHeaderInfo(request,response);
			map.put("code",androidHeaderInfos.getCode());
			if(androidHeaderInfos.getCode()==1001){
				try {
					JSONObject object = new JSONObject();
					 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
					 String userId = object.optString("userId");
					 String password = object.optString("password");
					 try {
						password = AesCBC.encrypt(password,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
					} catch (Exception e) {
						map.put("code", 1013);
						System.out.println("加密报错!");
						e.printStackTrace();
					}  
					user = iuserService.loginUser(userId, password);
					if(user!=null){
						int companyid = user.getCompany_id();
						if(companyid!=-1){
							company = iuserService.findCompanyList(companyid);
						}
					}
					versionInfo = versionService.findVersionInfo();
					if (user == null) {
						user = new User();
						map.put("code", 1014);
					} else {
						OnLineInitialRSAKey.map.put(user.getUser_id(), androidHeaderInfos.getAppPublicKey());
						map.put("code", 1001);
					}
					map.put("user", user);
					map.put("version", versionInfo);
					map.put("company", company);
				} catch (Exception e) {
					map.put("code",2001);
				}
			}
			String code =  String.valueOf(map.get("code"));
			response.addHeader("code",code);//返回安卓的公钥
			return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 更新用户信息
	@RequestMapping(value = "/updateInfo.do")
	@ResponseBody
	public String updateInfo(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("user", user);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				String userId = object.optString("userId");
				String key = object.optString("key");
				String value = object.optString("value");
				int n = iuserService.updateUser(userId, key, value);
				System.out.println("操作结果：" + n);
				int code = 2001;
				switch (n) {
				case 0:
					code = 1010;
					break;
				case 1:
					code = 1001;
					user = iuserService.searchUser("UserId", userId);
					break;
				case 2:
					code = 1014;
					break;
				case 3:
					code = 1016;
					break;
				case 4:
					code = 1018;
					break;
				default:
					break;
				}
				map.put("user", user);
				map.put("code",code);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	
	/***
	 * 重置密码
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetPassword.do")
	@ResponseBody
	public String resetPassword(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				String phoneNumber = object.optString("phoneNumber");
				String password = object.optString("password");
				 password = AesCBC.encrypt(password,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
				int n = iuserService.updateUserPassword(phoneNumber,password);
				System.out.println("操作结果：" + n);
				int code = 2001;
				switch (n) {
				case 0:
					code = 1010;
					break;
				case 1:
					code = 1001;
					break;
				case 2:
					code = 1017;
					break;
				default:
					break;
				}
				map.put("code",code);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/***
	 * 修改密码的方法
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updatePassword.do")
	@ResponseBody
	public String updatePassword(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				String userId = object.optString("userId");
				String newpassword = object.optString("newpassword");
				String oldpassword = object.optString("oldpassword");
				oldpassword = AesCBC.encrypt(oldpassword,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
				newpassword = AesCBC.encrypt(newpassword,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
				int c = iuserService.findUserInfoByUserId(userId);
				int code = 2001;
				if(c<1){
					code = 1014;
				}else{
					int b = iuserService.findUserInfoByUserIdAndPassWord(userId,oldpassword);
					if(b<1){
						code = 1018;
					}else{
						int n = iuserService.updatePassword(userId,oldpassword,newpassword);
						System.out.println("操作结果：" + n);
						switch (n) {
						case 0:
							code = 1010;
							break;
						case 1:
							code = 1001;
							break;
						case 2:
							code = 1018;
							break;
						default:
							break;
						}
					}
				}
				
				map.put("code",code);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	// 更新用户的部门信息
	@RequestMapping(value = "/updateDepartment.do")
	@ResponseBody
	public String updateDepartment(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		map.put("user", user);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				String userId = object.optString("userId");
				int departmentIdOne = object.optInt("departmentIdOne");
				int positionIdOne = object.optInt("positionIdOne");
				int departmentIdTwo = object.optInt("departmentIdTwo");
				int positionIdTwo = object.optInt("positionIdTwo");
				int n = iuserService.updateDepartment(userId, departmentIdOne, positionIdOne, departmentIdTwo, positionIdTwo);
				System.out.println("操作结果：" + n);
				int code = 2001;
				switch (n) {
				case 0:
					code = 1010;
					break;
				case 1:
					code = 1001;
					user = iuserService.searchUser("UserId", userId);
					break;
				case 2:
					code = 1014;
					break;
				default:
					break;
				}
				map.put("code", code);
				map.put("user", user);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 更新用户的紧急联系人
	@RequestMapping(value = "/updateEmergencyContact.do")
	@ResponseBody
	public String updateEmergencyContact(HttpServletResponse response,HttpSession session,HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		map.put("user", user);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				String userId = object.optString("userId");
				String emergencyUserName = object.optString("emergencyUserName");
				String emergencyPhone = object.optString("emergencyPhone");
				int n = iuserService.updateEmergencyContact(userId, emergencyUserName, emergencyPhone);
				System.out.println("操作结果：" + n);
				int code = 2001;
				switch (n) {
				case 0:
					code = 1010;
					break;
				case 1:
					code = 1001;
					user = iuserService.searchUser("UserId", userId);
					break;
				case 2:
					code = 1014;
					break;
				default:
					break;
				}
				map.put("code", code);
				map.put("user", user);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 根据用户ID查询用户
	@RequestMapping(value = "/searchUser.do")
	@ResponseBody
	public String searchUser(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		map.put("user", user);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				String userId = object.optString("userId");
				user = iuserService.searchUser("UserId", userId);
				if (user == null) {
					user = new User();
					map.put("code", 1014);
				} else {
					map.put("code", 1001);
				}
				map.put("user", user);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 查询所有用户信息
	@RequestMapping(value = "/searchAllUser.do")
	@ResponseBody
	public String searchAllUser(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		List<AllUserInfoAddressBook> allUserList = new ArrayList<AllUserInfoAddressBook>();
		Map<String, Object> map = new HashMap<>();
		map.put("departmentList", allUserList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int companyId = object.optInt("companyId");
				String authority = object.optString("authority");//人员权限
				try {
					allUserList.addAll(iuserService.searchAllUser(companyId,authority));
					map.put("code", 1001);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("code", 1010);
				}
				map.put("departmentList", allUserList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 查询部门列表
	@RequestMapping(value = "/searchDepartment.do")
	@ResponseBody
	public String searchAllDepartmrnt(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Department> departmentList = new ArrayList<Department>();
		List<Position> positionList = new ArrayList<Position>();
		map.put("department", departmentList);
		map.put("position", positionList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int companyId = object.optInt("companyId");
				 departmentList = iuserService.searchDepartment(companyId);
				 positionList = iuserService.searchPosition(companyId);
				if (departmentList == null) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
				map.put("department", departmentList);
				map.put("position", positionList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 查询所有用户状态
	@RequestMapping(value = "/searchUserStatus.do")
	@ResponseBody
	public String searchUserStatus() throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Status> recordList  = new ArrayList<Status>();
		map.put("department", recordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				 recordList = iuserService.searchUserStatus();
					if (recordList == null) {
						map.put("code", 1010);
					} else {
						map.put("code", 1001);
					}
					map.put("department", recordList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 添加考勤规则
	@RequestMapping(value = "/addAttendanceRule.do")
	@ResponseBody
	public String addAttendanceRule(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				AttendanceRule rule = (AttendanceRule) JSONObject.toBean(object, AttendanceRule.class);
				int i = iuserService.addAttendanceRule(rule);
				if (i == -1) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 删除考勤规则
	@RequestMapping(value = "/deleteAttendanceRule.do")
	@ResponseBody
	public String deleteAttendanceRule(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int ruleId = object.optInt("ruleId");
				int i = iuserService.deleteAttendanceRule(ruleId);
				if (i == -1) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	// 修改考勤规则
	@RequestMapping(value = "/modifyAttendanceRule.do")
	@ResponseBody
	public String modifyAttendanceRule(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				AttendanceRule rule = (AttendanceRule) JSONObject.toBean(object, AttendanceRule.class);
				int i = iuserService.modifyAttendanceRule(rule);
				if (i == -1) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 查询考勤规则
	@RequestMapping(value = "/searchAttendanceRules.do")
	@ResponseBody
	public String searchAttendanceRules(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<AttendanceRule> ruleList = new ArrayList<AttendanceRule>();
		map.put("attendanceRule", ruleList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int manager = object.optInt("manager");//管理员是1，普通的是0
				int companyId = object.optInt("companyId");
				int id = object.optInt("ruleId");//规则ID
				ruleList = iuserService.searchAttendanceRules(manager,companyId,id);
				if (ruleList == null) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
				map.put("attendanceRule", ruleList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	/**
	 * 新增补卡记录
	 * @param files
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addAppealAttendanceRecord.do")
	@ResponseBody
	public String addAppealAttendanceRecord(@RequestParam("picture") MultipartFile[] files,
			HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
		Map<String, Object> map = new HashMap<>();
		List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
		List<AttendanceRecord> topList = new ArrayList<AttendanceRecord>();
		map.put("attendanceRecord", recordList);
		map.put("attendanceTopRecord", topList);
		String information = request.getParameter("information");
		AttendanceRecord record = new AttendanceRecord();
		//AttendanceRule currentRule = new AttendanceRule();
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(information);
				 record = (AttendanceRecord) JSONObject.toBean(object, AttendanceRecord.class);
				// currentRule = iuserService.searchAttendanceRuleByRuleId(record.getRule_id());
					//存储审批人
					int father_audit_id = 0;//审批人父级ID，默认是0
					List<String> audit_userList = record.getAudit_user();
				// 根据考勤的类型判断补卡的类型
					// 补卡签到
					AppealAttendanceRecord appealAttendanceRecord = new AppealAttendanceRecord();//补卡记录详情
					appealAttendanceRecord.setRemarks(record.getRemarks());
					appealAttendanceRecord.setStatus(OUT_RECORD_WEISHENPI);
					appealAttendanceRecord.setAppeal_time(record.getAppeal_time());
					iuserService.insertAppealAttendanceRecord(appealAttendanceRecord);
					int appeal_attendance_id = appealAttendanceRecord.getId();//补卡详情记录ID
					if(appeal_attendance_id==0){
						System.out.println(appeal_attendance_id);
					}
					record.setAppeal_attendance_id(appeal_attendance_id);
					
					for(int i = 0;i<audit_userList.size();i++){
						AppealAttendanceRecordAudit appealAttendanceRecordAudit = new AppealAttendanceRecordAudit();//补卡审批人
						appealAttendanceRecordAudit.setAppeal_attendance_id(appeal_attendance_id);
						System.out.println(audit_userList.get(i));
						appealAttendanceRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
						appealAttendanceRecordAudit.setFather_audit_id(father_audit_id);
						if(i ==0){
							appealAttendanceRecordAudit.setAudit_status(1);
							JSONObject websocket = new JSONObject();
							websocket.put("key",PushType.ATTENDANCE_APPEAL_APPLY);
							websocket.put("user_id", audit_userList.get(0));
							WebSocketFrameHandler.sendData(websocket);
						}else{
							appealAttendanceRecordAudit.setAudit_status(0);
						}
						iuserService.insertAppealAttendanceRecordAudit(appealAttendanceRecordAudit);
						father_audit_id = appealAttendanceRecordAudit.getId();
					}
					record.setResult_id(DAISHENPI);
					if(files!=null){
						for(int size = 0;size<files.length;size++){
							try {
								String fileName=files[size].getOriginalFilename();
								//3.由于文件个数多,采用分文件存储
								String dateDir=
										new SimpleDateFormat("yyyy/MM/dd")
												.format(new Date());
							
								//生成对应的文件夹
								String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
								int p = path.indexOf("webapps");
								String realpath = path.substring(0, p + 8) +"AppealAttendanceRecord"+dateDir + "\\";
								realpath = realpath.replaceAll("%20", " ");
								//判断是否存在
								File file=new File(realpath);
								if(!file.exists()){
									//生成文件夹
									file.mkdirs();
								}
								//防止图片上传量过大引起的重名问题
								String  uuidName=
										UUID.randomUUID()
											.toString().replace("-", "");	
								String  randomNum=((int)(Math.random()*99999))+"";
								//获取文件后缀名
								String fileType=
										fileName.substring(
												fileName.lastIndexOf("."));
								String prefix=fileName.substring(0, fileName.lastIndexOf("."));
								//路径拼接(文件真实的存储路径)
								String  fileDirPath=
										realpath+"/"+prefix+uuidName+randomNum+fileType;
								//存储数据
								int i = 0;
								i = iuserService.AppealAttendanceRecordPic(appeal_attendance_id,fileDirPath);
								if(i!=1){
									map.put("code", 1020);
								}
								//文件上传
								files[size].transferTo(new File(fileDirPath));
							} catch (Exception e) {
								e.printStackTrace();
								map.put("code", 1021);
							}
						}
					}
					String newTime = appealAttendanceRecord.getAppeal_time();
					record.setAttendance_time(newTime);
					try {
						iuserService.addAppealAttendanceRecord(record);
						map.put("code", 1001);
					} catch (Exception e) {
						e.printStackTrace();
						map.put("code", 1010);
					}
					
				JSONObject audience = new JSONObject();
				JSONArray alias1 = new JSONArray();
				if(audit_userList!=null&&audit_userList.size()>0){
					alias1.add(String.valueOf(audit_userList.get(0)));
				}
				 audience.put("alias", alias1);
				 JSONObject alert = new JSONObject();
				 alert.put("type",PushType.ATTENDANCE_APPEAL_APPLY);
				 alert.put("data", JSONObject.fromObject(record));
				 PushData pushData = new PushData();
				 pushData.setType(PushType.ATTENDANCE_APPEAL_APPLY);
				 pushData.setData(record.getId());
				 String message = "您收到了一条补卡申请!";
				JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.ATTENDANCE_APPEAL_APPLY),message);
				int companyId = getCompangId(record.getUser_id());
				int setResultCode = 400;
				if(resDate.containsKey("error")){
					setResultCode = 400;
				}else{
					setResultCode = 200;
				}
				List<Integer> userIdList = new ArrayList<Integer>();
				if(audit_userList!=null&&audit_userList.size()>0){
					userIdList.add(Integer.valueOf(audit_userList.get(0)));
				}
				insertJPushData(alert.toString(),PushType.ATTENDANCE_APPEAL_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
						JSONArray.toJSONString(userIdList).toString());
				
					
					 recordList = iuserService.searchAttendanceRecord(record.getUser_id(),
							TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
					map.put("attendanceRecord", recordList);
					 topList = iuserService.searchAttendanceTopRecord(
							TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
					map.put("attendanceTopRecord", topList);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", 2001);
			}
			JSONObject jsonObject = new JSONObject();
		String code =  String.valueOf(map.get("code"));
		map.remove("code");
		jsonObject.put("code", code);
		jsonObject.put("data", map);
		response.addHeader("code",code);//返回安卓的公钥
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
		return GsonUtils.convertJSON(jsonObject);
		//return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	// 增加考勤记录
//	@RequestMapping(value = "/addAttendanceRecord.do")
//	@ResponseBody
//	public String addAttendanceRecord(@RequestParam("picture") MultipartFile[] files,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
//		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
//		Map<String, Object> map = new HashMap<>();
//		List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
//		//List<AttendanceRecord> topList = new ArrayList<AttendanceRecord>();
//		map.put("attendanceRecord", recordList);
//		//map.put("attendanceTopRecord", topList);
//		String information = request.getParameter("information");
//		AttendanceRecord record = new AttendanceRecord();
//		AttendanceRule currentRule = new AttendanceRule();
//			try {
//				JSONObject object = new JSONObject();
//				object = JSONObject.fromObject(information);
//				 record = (AttendanceRecord) JSONObject.toBean(object, AttendanceRecord.class);
//				 currentRule = iuserService.searchAttendanceRuleByRuleId(record.getRule_id());
//					String newTime = TimeUtils.getCurrentDateTime();
//					record.setAttendance_time(newTime);
//				// 根据时间和考勤类型判断考勤状态
//					if (record.getOut_attendance() == 1) {
//						//存储外勤打卡记录（获取外勤打卡记录ID）
//						OutAttendanceRecord outAttendanceRecord = new OutAttendanceRecord();//外勤记录详情
//						outAttendanceRecord.setRemarks(record.getRemarks());
//						outAttendanceRecord.setStatus(OUT_RECORD_WEISHENPI);
//						iuserService.insertOutAttendanceRecord(outAttendanceRecord);
//						int out_attendance_id = outAttendanceRecord.getId();//外勤打卡详情记录ID
//						record.setOut_attendance_id(out_attendance_id);
//						
//						//存储审批人
//						int father_audit_id = 0;//审批人父级ID，默认是0
//						List<String> audit_userList = record.getAudit_user();
//						for(int i = 0;i<audit_userList.size();i++){
//							OutAttendanceRecordAudit outAttendanceRecordAudit = new OutAttendanceRecordAudit();//外勤打卡审批人
//							outAttendanceRecordAudit.setOut_Attendance_Record_Id(out_attendance_id);
//							System.out.println(audit_userList.get(i));
//							outAttendanceRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
//							outAttendanceRecordAudit.setFather_audit_id(father_audit_id);
//							if(i ==0){
//								outAttendanceRecordAudit.setAudit_status(1);
//								JSONObject websocket = new JSONObject();
//								websocket.put("key",PushType.OUT_ATTENDANCE_APPLY);
//								websocket.put("user_id", audit_userList.get(0));
//								WebSocketFrameHandler.sendData(websocket);
//							}else{
//								outAttendanceRecordAudit.setAudit_status(0);
//							}
//							iuserService.insertOutAttendanceRecordAudit(outAttendanceRecordAudit);
//							father_audit_id = outAttendanceRecordAudit.getId();
//						}
//						// 外勤
//						record.setResult_id(DAISHENPI);
//						if(files!=null){
//							for(int size = 0;size<files.length;size++){
//								try {
//									String fileName=files[size].getOriginalFilename();
//									//3.由于文件个数多,采用分文件存储
//									String dateDir=
//											new SimpleDateFormat("yyyy/MM/dd")
//													.format(new Date());
//								
//									//生成对应的文件夹
//									String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
//									int p = path.indexOf("webapps");
//									String realpath = path.substring(0, p + 8) +"OutAttendanceRecord"+dateDir + "\\";
//									realpath = realpath.replaceAll("%20", " ");
//									//判断是否存在
//									File file=new File(realpath);
//									if(!file.exists()){
//										//生成文件夹
//										file.mkdirs();
//									}
//									//防止图片上传量过大引起的重名问题
//									String  uuidName=
//											UUID.randomUUID()
//												.toString().replace("-", "");	
//									String  randomNum=((int)(Math.random()*99999))+"";
//									//获取文件后缀名
//									String fileType=
//											fileName.substring(
//													fileName.lastIndexOf("."));
//									String prefix=fileName.substring(0, fileName.lastIndexOf("."));
//									//路径拼接(文件真实的存储路径)
//									String  fileDirPath=
//											realpath+"/"+prefix+uuidName+randomNum+fileType;
//									//存储数据
//									int i = 0;
//									i = iuserService.OutAttendanceRecordPic(out_attendance_id,fileDirPath);
//									if(i!=1){
//										map.put("code", 1020);
//									}
//									//文件上传
//									files[size].transferTo(new File(fileDirPath));
//								} catch (Exception e) {
//									e.printStackTrace();
//									map.put("code", 1021);
//								}
//							}
//						}
//						try {
//							iuserService.addAttendanceRecord(record);
//							map.put("code", 1001);
//							System.out.println("外勤签到打卡ID"+record.getId());
//						} catch (Exception e) {
//							e.printStackTrace();
//							map.put("code", 1010);
//						}
//					JSONObject audience = new JSONObject();
//					JSONArray alias1 = new JSONArray();
//					if(audit_userList!=null&&audit_userList.size()>0){
//						alias1.add(String.valueOf(audit_userList.get(0)));
//					}
//					 audience.put("alias", alias1);
//					 PushData pushData = new PushData();
//					 pushData.setType(PushType.OUT_ATTENDANCE_APPLY);
//					 pushData.setData(record.getId());
//					 String message = "您收到了一条外勤签到打卡申请!";
//					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPLY),message);
//					int companyId = getCompangId(record.getUser_id());
//					int setResultCode = 400;
//					if(resDate.containsKey("error")){
//						setResultCode = 400;
//					}else{
//						setResultCode = 200;
//					}
//					List<Integer> userIdList = new ArrayList<Integer>();
//					if(audit_userList!=null&&audit_userList.size()>0){
//						userIdList.add(Integer.valueOf(audit_userList.get(0)));
//					}
//					 JSONObject alert = new JSONObject();
//					 alert.put("type",PushType.OUT_ATTENDANCE_APPLY);
//					 alert.put("data", JSONObject.fromObject(record));
//					insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
//							JSONArray.toJSONString(userIdList).toString());
//					} else {
//						try {
//							iuserService.addAttendanceRecord(record);
//							map.put("code", 1001);
//						} catch (Exception e) {
//							e.printStackTrace();
//							map.put("code", 1010);
//						}
//					}
//					 recordList = iuserService.searchAttendanceRecord(record.getUser_id(),
//							TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
//					map.put("attendanceRecord", recordList);
//					// topList = iuserService.searchAttendanceTopRecord(
//							//TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
//					//map.put("attendanceTopRecord", topList);
//			} catch (Exception e) {
//				e.printStackTrace();
//				map.put("code", 2001);
//			}
//			JSONObject jsonObject = new JSONObject();
//		String code =  String.valueOf(map.get("code"));
//		map.remove("code");
//		jsonObject.put("code", code);
//		jsonObject.put("data", map);
//		response.addHeader("code",code);//返回安卓的公钥
//		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
//		return GsonUtils.convertJSON(jsonObject);
//		//return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
//	}
	
	// 增加考勤记录
		@RequestMapping(value = "/addAttendanceRecord.do")
		@ResponseBody
		public String addAttendanceRecord(@RequestParam("picture") MultipartFile[] files,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
			Map<String, Object> map = new HashMap<>();
			List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
			List<AttendanceRecord> topList = new ArrayList<AttendanceRecord>();
			map.put("attendanceRecord", recordList);
			map.put("attendanceTopRecord", topList);
			String information = request.getParameter("information");
			AttendanceRecord record = new AttendanceRecord();
			AttendanceRule currentRule = new AttendanceRule();
				try {
					JSONObject object = new JSONObject();
					object = JSONObject.fromObject(information);
					 record = (AttendanceRecord) JSONObject.toBean(object, AttendanceRecord.class);
					 currentRule = iuserService.searchAttendanceRuleByRuleId(record.getRule_id());
						String newTime = TimeUtils.getCurrentDateTime();
						record.setAttendance_time(newTime);
					// 根据时间和考勤类型判断考勤状态
						switch (record.getAttendance_type()) {
						case ATTENDANCE_TYPE_QIANDAO:
							// 考勤签到
							if (record.getOut_attendance() == 1) {
								//存储外勤打卡记录（获取外勤打卡记录ID）
								OutAttendanceRecord outAttendanceRecord = new OutAttendanceRecord();//外勤记录详情
								outAttendanceRecord.setRemarks(record.getRemarks());
								outAttendanceRecord.setStatus(OUT_RECORD_WEISHENPI);
								iuserService.insertOutAttendanceRecord(outAttendanceRecord);
								int out_attendance_id = outAttendanceRecord.getId();//外勤打卡详情记录ID
								record.setOut_attendance_id(out_attendance_id);
								
								//存储审批人
								int father_audit_id = 0;//审批人父级ID，默认是0
								List<String> audit_userList = record.getAudit_user();
								for(int i = 0;i<audit_userList.size();i++){
									OutAttendanceRecordAudit outAttendanceRecordAudit = new OutAttendanceRecordAudit();//外勤打卡审批人
									outAttendanceRecordAudit.setOut_Attendance_Record_Id(out_attendance_id);
									System.out.println(audit_userList.get(i));
									outAttendanceRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
									outAttendanceRecordAudit.setFather_audit_id(father_audit_id);
									if(i ==0){
										outAttendanceRecordAudit.setAudit_status(1);
										JSONObject websocket = new JSONObject();
										websocket.put("key",PushType.OUT_ATTENDANCE_APPLY);
										websocket.put("user_id", audit_userList.get(0));
										WebSocketFrameHandler.sendData(websocket);
									}else{
										outAttendanceRecordAudit.setAudit_status(0);
									}
									iuserService.insertOutAttendanceRecordAudit(outAttendanceRecordAudit);
									father_audit_id = outAttendanceRecordAudit.getId();
								}
								// 外勤
								record.setResult_id(DAISHENPI);
								if(files!=null){
									for(int size = 0;size<files.length;size++){
										try {
											String fileName=files[size].getOriginalFilename();
											//3.由于文件个数多,采用分文件存储
											String dateDir=
													new SimpleDateFormat("yyyy/MM/dd")
															.format(new Date());
										
											//生成对应的文件夹
											String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
											int p = path.indexOf("webapps");
											String realpath = path.substring(0, p + 8) +"OutAttendanceRecord"+dateDir + "\\";
											realpath = realpath.replaceAll("%20", " ");
											//判断是否存在
											File file=new File(realpath);
											if(!file.exists()){
												//生成文件夹
												file.mkdirs();
											}
											//防止图片上传量过大引起的重名问题
											String  uuidName=
													UUID.randomUUID()
														.toString().replace("-", "");	
											String  randomNum=((int)(Math.random()*99999))+"";
											//获取文件后缀名
											String fileType=
													fileName.substring(
															fileName.lastIndexOf("."));
											String prefix=fileName.substring(0, fileName.lastIndexOf("."));
											//路径拼接(文件真实的存储路径)
											String  fileDirPath=
													realpath+"/"+prefix+uuidName+randomNum+fileType;
											//存储数据
											int i = 0;
											i = iuserService.OutAttendanceRecordPic(out_attendance_id,fileDirPath);
											if(i!=1){
												map.put("code", 1020);
											}
											//文件上传
											files[size].transferTo(new File(fileDirPath));
										} catch (Exception e) {
											e.printStackTrace();
											map.put("code", 1021);
										}
									}
								}
								try {
									iuserService.addAttendanceRecord(record);
									map.put("code", 1001);
									System.out.println("外勤签到打卡ID"+record.getId());
								} catch (Exception e) {
									e.printStackTrace();
									map.put("code", 1010);
								}
							JSONObject audience = new JSONObject();
							JSONArray alias1 = new JSONArray();
							if(audit_userList!=null&&audit_userList.size()>0){
								alias1.add(String.valueOf(audit_userList.get(0)));
							}
							 audience.put("alias", alias1);
							 PushData pushData = new PushData();
							 pushData.setType(PushType.OUT_ATTENDANCE_APPLY);
							 pushData.setData(record.getId());
							 String message = "您收到了一条外勤签到打卡申请!";
							JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPLY),message);
							int companyId = getCompangId(record.getUser_id());
							int setResultCode = 400;
							if(resDate.containsKey("error")){
								setResultCode = 400;
							}else{
								setResultCode = 200;
							}
							List<Integer> userIdList = new ArrayList<Integer>();
							if(audit_userList!=null&&audit_userList.size()>0){
								userIdList.add(Integer.valueOf(audit_userList.get(0)));
							}
							 JSONObject alert = new JSONObject();
							 alert.put("type",PushType.OUT_ATTENDANCE_APPLY);
							 alert.put("data", JSONObject.fromObject(record));
							insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
									JSONArray.toJSONString(userIdList).toString());
							} else {
								if (TimeUtils.getTimeCompareSizeHHMM(TimeUtils.getCurrentTime(), currentRule.getRule_time_work()) > 1) {
									// 正常签到
									record.setResult_id(RESULT_QIANDAO_CHENGGONG);
								} else {
									// 迟到打卡
									record.setResult_id(RESULT_CHIDAO_DAKA);
								}
								try {
									iuserService.addAttendanceRecord(record);
									map.put("code", 1001);
								} catch (Exception e) {
									e.printStackTrace();
									map.put("code", 1010);
								}
							}
							break;
						case ATTENDANCE_TYPE_QIANTUI:
							// 考勤签退
							if (record.getOut_attendance() == 1) {
								//存储外勤打卡记录（获取外勤打卡记录ID）
								OutAttendanceRecord outAttendanceRecord = new OutAttendanceRecord();//外勤记录详情
								outAttendanceRecord.setRemarks(record.getRemarks());
								outAttendanceRecord.setStatus(OUT_RECORD_WEISHENPI);
								iuserService.insertOutAttendanceRecord(outAttendanceRecord);
								int out_attendance_id = outAttendanceRecord.getId();//外勤打卡详情记录ID
								record.setOut_attendance_id(out_attendance_id);
								
								//存储审批人
								int father_audit_id = 0;//审批人父级ID，默认是0
								List<String> audit_userList = record.getAudit_user();
								for(int i = 0;i<audit_userList.size();i++){
									OutAttendanceRecordAudit outAttendanceRecordAudit = new OutAttendanceRecordAudit();//外勤打卡审批人
									outAttendanceRecordAudit.setOut_Attendance_Record_Id(out_attendance_id);
									outAttendanceRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
									outAttendanceRecordAudit.setFather_audit_id(father_audit_id);
									if(i ==0){
										outAttendanceRecordAudit.setAudit_status(1);
										JSONObject websocket = new JSONObject();
										websocket.put("key",PushType.OUT_ATTENDANCE_APPLY);
										websocket.put("user_id", audit_userList.get(0));
										WebSocketFrameHandler.sendData(websocket);
									}else{
										outAttendanceRecordAudit.setAudit_status(0);
									}
									iuserService.insertOutAttendanceRecordAudit(outAttendanceRecordAudit);
									father_audit_id = outAttendanceRecordAudit.getId();
								}
								//存储抄送人
								List<String> copy_userList = record.getCopy_user();
								List<OutAttendanceRecordCopy> attendanceRecordCopies = new ArrayList<OutAttendanceRecordCopy>();
								for(int i = 0;i<copy_userList.size();i++){
									OutAttendanceRecordCopy attendanceRecordCopy = new OutAttendanceRecordCopy();
									attendanceRecordCopy.setOut_Attendance_Record_Id(out_attendance_id);
									attendanceRecordCopy.setUser_id(Integer.valueOf(copy_userList.get(i)));
									attendanceRecordCopies.add(attendanceRecordCopy);
								}
								iuserService.insertOutAttendanceRecordCopy(attendanceRecordCopies);
								
								
								// 外勤
								record.setResult_id(DAISHENPI);
								if(files!=null){
									for(int size = 0;size<files.length;size++){
										try {
											String fileName=files[size].getOriginalFilename();
											//3.由于文件个数多,采用分文件存储
											String dateDir=
													new SimpleDateFormat("yyyy/MM/dd")
															.format(new Date());
										
											//生成对应的文件夹
											String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
											int p = path.indexOf("webapps");
											String realpath = path.substring(0, p + 8) +"OutAttendanceRecord"+dateDir + "\\";
											realpath = realpath.replaceAll("%20", " ");
											//判断是否存在
											File file=new File(realpath);
											if(!file.exists()){
												//生成文件夹
												file.mkdirs();
											}
											//防止图片上传量过大引起的重名问题
											String  uuidName=
													UUID.randomUUID()
														.toString().replace("-", "");	
											String  randomNum=((int)(Math.random()*99999))+"";
											//获取文件后缀名
											String fileType=
													fileName.substring(
															fileName.lastIndexOf("."));
											String prefix=fileName.substring(0, fileName.lastIndexOf("."));
											//路径拼接(文件真实的存储路径)
											String  fileDirPath=
													realpath+"/"+prefix+uuidName+randomNum+fileType;
											//存储数据
											int i = 0;
											i = iuserService.OutAttendanceRecordPic(out_attendance_id,fileDirPath);
											if(i!=1){
												map.put("code", 1020);
											}
											//文件上传
											files[size].transferTo(new File(fileDirPath));
										} catch (Exception e) {
											e.printStackTrace();
											map.put("code", 1021);
										}
									}
								}
								try {
									iuserService.addAttendanceRecord(record);
									map.put("code", 1001);
								} catch (Exception e) {
									e.printStackTrace();
									map.put("code", 1010);
								}
								JSONObject audience = new JSONObject();
								JSONArray alias1 = new JSONArray();
								if(audit_userList!=null&&audit_userList.size()>0){
									alias1.add(String.valueOf(audit_userList.get(0)));
								}
								 audience.put("alias", alias1);
								 PushData pushData = new PushData();
								 pushData.setType(PushType.OUT_ATTENDANCE_APPLY);
								 pushData.setData(record.getId());
								 String message = "您收到了一条外勤签退打卡申请!";
								JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OUT_ATTENDANCE_APPLY),message);
								int companyId = getCompangId(record.getUser_id());
								int setResultCode = 400;
								if(resDate.containsKey("error")){
									setResultCode = 400;
								}else{
									setResultCode = 200;
								}
								List<Integer> userIdList = new ArrayList<Integer>();
								if(audit_userList!=null&&audit_userList.size()>0){
									userIdList.add(Integer.valueOf(audit_userList.get(0)));
								}
								 JSONObject alert = new JSONObject();
								 alert.put("type",PushType.OUT_ATTENDANCE_APPLY);
								 alert.put("data", JSONObject.fromObject(record));
								insertJPushData(alert.toString(),PushType.OUT_ATTENDANCE_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
										JSONArray.toJSONString(userIdList).toString());
							} else {
								if (TimeUtils.getTimeCompareSizeHHMM(TimeUtils.getCurrentTime(), currentRule.getRule_time_off_work()) < 3) {
									// 正常签退
									record.setResult_id(RESULT_QIANTUI_CHENGGONG);
								} else {
									// 早退打卡
									record.setResult_id(RESULT_ZAOTUI_DAKA);
								}
								try {
									iuserService.addAttendanceRecord(record);
									map.put("code", 1001);
								} catch (Exception e) {
									e.printStackTrace();
									map.put("code", 1010);
								}
							}
							break;
						default:
							break;
						}
						 recordList = iuserService.searchAttendanceRecord(record.getUser_id(),
								TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
						map.put("attendanceRecord", recordList);
						 topList = iuserService.searchAttendanceTopRecord(
								TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
						map.put("attendanceTopRecord", topList);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("code", 2001);
				}
				JSONObject jsonObject = new JSONObject();
			String code =  String.valueOf(map.get("code"));
			map.remove("code");
			jsonObject.put("code", code);
			jsonObject.put("data", map);
			response.addHeader("code",code);//返回安卓的公钥
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
			return GsonUtils.convertJSON(jsonObject);
			//return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
		}
	// 增加加班申请记录
		@RequestMapping(value = "/addOvertimeRecord.do")
		@ResponseBody
		public String addOvertimeRecord(@RequestParam("picture") MultipartFile[] files,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
			Map<String, Object> map = new HashMap<>();
			String information = request.getParameter("information");
			OverTimeRecord overTimeRecord = new OverTimeRecord();
				try {
					JSONObject object = new JSONObject();
					object = JSONObject.fromObject(information);
					overTimeRecord = (OverTimeRecord) JSONObject.toBean(object, OverTimeRecord.class);
					overTimeRecord.setResult_id(DAISHENPI);
					iuserService.insertOverTimeRecord(overTimeRecord);//新增加班详情信息
					int id = overTimeRecord.getId();//加班详情的ID
					
					//存储审批人
					int father_audit_id = 0;//审批人父级ID，默认是0
					List<String> audit_userList = overTimeRecord.getAudit_user();
					for(int i = 0;i<audit_userList.size();i++){
						OverTimeRecordAudit overTimeRecordAudit = new OverTimeRecordAudit();//外勤打卡审批人
						overTimeRecordAudit.setOver_time_record_id(id);
						System.out.println(audit_userList.get(i));
						overTimeRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
						overTimeRecordAudit.setFather_audit_id(father_audit_id);
						if(i ==0){
							overTimeRecordAudit.setAudit_status(1);
							JSONObject websocket = new JSONObject();
							websocket.put("key",PushType.OVERTIME_APPLY);
							websocket.put("user_id", audit_userList.get(0));
							WebSocketFrameHandler.sendData(websocket);
						}else{
							overTimeRecordAudit.setAudit_status(0);
						}
						iuserService.insertOverTimeRecordAudit(overTimeRecordAudit);//新增加班审批人信息
						father_audit_id = overTimeRecordAudit.getId();
					}
					//存储抄送人
					List<String> copy_userList = overTimeRecord.getCopy_user();
					List<OverTimeRecordCopy> overTimeRecordCopies = new ArrayList<OverTimeRecordCopy>();
					for(int i = 0;i<copy_userList.size();i++){
						OverTimeRecordCopy overTimeRecordCopy = new OverTimeRecordCopy();
						overTimeRecordCopy.setOver_time_record_id(id);
						overTimeRecordCopy.setUser_id(Integer.valueOf(copy_userList.get(i)));
						overTimeRecordCopies.add(overTimeRecordCopy);
					}
					try {
						iuserService.insertOverTimeRecordCopy(overTimeRecordCopies);//新增加班抄送人信息
					} catch (Exception e) {
						
					}
					
					// 加班申请图片
					if(files!=null){
						for(int size = 0;size<files.length;size++){
							try {
								String fileName=files[size].getOriginalFilename();
								//3.由于文件个数多,采用分文件存储
								String dateDir=
										new SimpleDateFormat("yyyy/MM/dd")
												.format(new Date());
							
								//生成对应的文件夹
								String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
								int p = path.indexOf("webapps");
								String realpath = path.substring(0, p + 8) +"OverTime"+dateDir + "\\";
								realpath = realpath.replaceAll("%20", " ");
								//判断是否存在
								File file=new File(realpath);
								if(!file.exists()){
									//生成文件夹
									file.mkdirs();
								}
								//防止图片上传量过大引起的重名问题
								String  uuidName=
										UUID.randomUUID()
											.toString().replace("-", "");	
								String  randomNum=((int)(Math.random()*99999))+"";
								//获取文件后缀名
								String fileType=
										fileName.substring(
												fileName.lastIndexOf("."));
								String prefix=fileName.substring(0, fileName.lastIndexOf("."));
								//路径拼接(文件真实的存储路径)
								String  fileDirPath=
										realpath+"/"+prefix+uuidName+randomNum+fileType;
								//存储数据
								int i = 0;
								i = iuserService.OverTimeRecordPic(id,fileDirPath);//新增加班图片
								if(i!=1){
									map.put("code", 1020);
								}
								//文件上传
								files[size].transferTo(new File(fileDirPath));
							} catch (Exception e) {
								e.printStackTrace();
								map.put("code", 1021);
							}
						}
					}
				JSONObject audience = new JSONObject();
				JSONArray alias1 = new JSONArray();
				if(audit_userList!=null&&audit_userList.size()>0){
					alias1.add(String.valueOf(audit_userList.get(0)));
				}
				 audience.put("alias", alias1);
				 JSONObject alert = new JSONObject();
				 alert.put("type",PushType.OVERTIME_APPLY);
				 alert.put("data", JSONObject.fromObject(overTimeRecord));
				 PushData pushData = new PushData();
				 pushData.setType(PushType.OVERTIME_APPLY);
				 pushData.setData(overTimeRecord.getId());
				 String message = "您收到了一条加班申请!";
				JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.OVERTIME_APPLY),message);
				int companyId = getCompangId(overTimeRecord.getUser_id());
				int setResultCode = 400;
				if(resDate.containsKey("error")){
					setResultCode = 400;
				}else{
					setResultCode = 200;
				}
				List<Integer> userIdList = new ArrayList<Integer>();
				if(audit_userList!=null&&audit_userList.size()>0){
					userIdList.add(Integer.valueOf(audit_userList.get(0)));
				}
				insertJPushData(alert.toString(),PushType.OVERTIME_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
						JSONArray.toJSONString(userIdList).toString());
				map.put("code", 1001);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("code", 2001);
				}
				JSONObject jsonObject = new JSONObject();
			String code =  String.valueOf(map.get("code"));
			map.remove("code");
			jsonObject.put("code", code);
			jsonObject.put("data", map);
			response.addHeader("code",code);//返回安卓的公钥
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
			return GsonUtils.convertJSON(jsonObject);
		}
		
		// 增加假期申请记录
		@RequestMapping(value = "/addVacationRecord.do")
		@ResponseBody
		public String addVacationRecord(@RequestParam("picture") MultipartFile[] files,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
			Map<String, Object> map = new HashMap<>();
			String information = request.getParameter("information");
			VacationRecord vacationRecord = new VacationRecord();
				try {
					JSONObject object = new JSONObject();
					object = JSONObject.fromObject(information);
					vacationRecord = (VacationRecord) JSONObject.toBean(object, VacationRecord.class);
					vacationRecord.setResult_id(DAISHENPI);
					iuserService.insertVacationRecord(vacationRecord);//新增假期详情信息
					int id = vacationRecord.getId();//假期详情的ID
					
					//存储审批人
					int father_audit_id = 0;//审批人父级ID，默认是0
					List<String> audit_userList = vacationRecord.getAudit_user();
					for(int i = 0;i<audit_userList.size();i++){
						VacationRecordAudit vacationRecordAudit = new VacationRecordAudit();//假期打卡审批人
						vacationRecordAudit.setVacation_record_id(id);
						System.out.println(audit_userList.get(i));
						vacationRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
						vacationRecordAudit.setFather_audit_id(father_audit_id);
						if(i ==0){
							vacationRecordAudit.setAudit_status(1);
							JSONObject websocket = new JSONObject();
							websocket.put("key",PushType.VACATION_APPLY);
							websocket.put("user_id", audit_userList.get(0));
							WebSocketFrameHandler.sendData(websocket);
						}else{
							vacationRecordAudit.setAudit_status(0);
						}
						iuserService.insertVacationRecordAudit(vacationRecordAudit);//新增假期审批人信息
						father_audit_id = vacationRecordAudit.getId();
					}
					//存储抄送人
					List<String> copy_userList = vacationRecord.getCopy_user();
					List<VacationRecordCopy> vacationRecordCopies = new ArrayList<VacationRecordCopy>();
					for(int i = 0;i<copy_userList.size();i++){
						VacationRecordCopy vacationRecordCopy = new VacationRecordCopy();
						vacationRecordCopy.setVacation_record_id(id);
						vacationRecordCopy.setUser_id(Integer.valueOf(copy_userList.get(i)));
						vacationRecordCopies.add(vacationRecordCopy);
					}
					try {
						iuserService.insertVacationRecordCopy(vacationRecordCopies);//新增假期抄送人信息
					} catch (Exception e) {
						
					}
					
					// 加班申请图片
					if(files!=null){
						for(int size = 0;size<files.length;size++){
							try {
								String fileName=files[size].getOriginalFilename();
								//3.由于文件个数多,采用分文件存储
								String dateDir=
										new SimpleDateFormat("yyyy/MM/dd")
												.format(new Date());
							
								//生成对应的文件夹
								String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
								int p = path.indexOf("webapps");
								String realpath = path.substring(0, p + 8) +"Vacation"+dateDir + "\\";
								realpath = realpath.replaceAll("%20", " ");
								//判断是否存在
								File file=new File(realpath);
								if(!file.exists()){
									//生成文件夹
									file.mkdirs();
								}
								//防止图片上传量过大引起的重名问题
								String  uuidName=
										UUID.randomUUID()
											.toString().replace("-", "");	
								String  randomNum=((int)(Math.random()*99999))+"";
								//获取文件后缀名
								String fileType=
										fileName.substring(
												fileName.lastIndexOf("."));
								String prefix=fileName.substring(0, fileName.lastIndexOf("."));
								//路径拼接(文件真实的存储路径)
								String  fileDirPath=
										realpath+"/"+prefix+uuidName+randomNum+fileType;
								//存储数据
								int i = 0;
								i = iuserService.VacationRecordPic(id,fileDirPath);//新增假期图片
								if(i!=1){
									map.put("code", 1020);
								}
								//文件上传
								files[size].transferTo(new File(fileDirPath));
							} catch (Exception e) {
								e.printStackTrace();
								map.put("code", 1021);
							}
						}
					}
				JSONObject audience = new JSONObject();
				JSONArray alias1 = new JSONArray();
				if(audit_userList!=null&&audit_userList.size()>0){
					alias1.add(String.valueOf(audit_userList.get(0)));
				}
				 audience.put("alias", alias1);
				 JSONObject alert = new JSONObject();
				 alert.put("type",PushType.VACATION_APPLY);
				 alert.put("data", JSONObject.fromObject(vacationRecord));
				 PushData pushData = new PushData();
				 pushData.setType(PushType.VACATION_APPLY);
				 pushData.setData(vacationRecord.getId());
				 String message = "您收到了一条假期申请!";
				JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.VACATION_APPLY),message);
				int companyId = getCompangId(vacationRecord.getUser_id());
				int setResultCode = 400;
				if(resDate.containsKey("error")){
					setResultCode = 400;
				}else{
					setResultCode = 200;
				}
				List<Integer> userIdList = new ArrayList<Integer>();
				if(audit_userList!=null&&audit_userList.size()>0){
					userIdList.add(Integer.valueOf(audit_userList.get(0)));
				}
				insertJPushData(alert.toString(),PushType.VACATION_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
						JSONArray.toJSONString(userIdList).toString());
				map.put("code", 1001);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("code", 2001);
				}
				JSONObject jsonObject = new JSONObject();
			String code =  String.valueOf(map.get("code"));
			map.remove("code");
			jsonObject.put("code", code);
			jsonObject.put("data", map);
			response.addHeader("code",code);//返回安卓的公钥
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
			return GsonUtils.convertJSON(jsonObject);
		}
		
		
			// 增加外出申请记录
			@RequestMapping(value = "/addOutGoingRecord.do")
			@ResponseBody
			public String addOutGoingRecord(@RequestParam("picture") MultipartFile[] files,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
				System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
				Map<String, Object> map = new HashMap<>();
				String information = request.getParameter("information");
				OutGoingRecord outGoingRecord = new OutGoingRecord();
					try {
						JSONObject object = new JSONObject();
						object = JSONObject.fromObject(information);
						outGoingRecord = (OutGoingRecord) JSONObject.toBean(object, OutGoingRecord.class);
						outGoingRecord.setResult_id(DAISHENPI);
						iuserService.insertOutGoingRecord(outGoingRecord);//新增外出详情信息
						int id = outGoingRecord.getId();//外出详情的ID
						
						//存储审批人
						int father_audit_id = 0;//审批人父级ID，默认是0
						List<String> audit_userList = outGoingRecord.getAudit_user();
						for(int i = 0;i<audit_userList.size();i++){
							OutGoingRecordAudit outGoingRecordAudit = new OutGoingRecordAudit();//外出打卡审批人
							outGoingRecordAudit.setOutGoing_record_id(id);
							System.out.println(audit_userList.get(i));
							outGoingRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
							outGoingRecordAudit.setFather_audit_id(father_audit_id);
							if(i ==0){
								outGoingRecordAudit.setAudit_status(1);
								JSONObject websocket = new JSONObject();
								websocket.put("key",PushType.GO_OUT_APPLY);
								websocket.put("user_id", audit_userList.get(0));
								WebSocketFrameHandler.sendData(websocket);
							}else{
								outGoingRecordAudit.setAudit_status(0);
							}
							iuserService.insertOutGoingRecordAudit(outGoingRecordAudit);//新增外出审批人信息
							father_audit_id = outGoingRecordAudit.getId();
						}
						//存储抄送人
						List<String> copy_userList = outGoingRecord.getCopy_user();
						List<OutGoingRecordCopy> outGoingRecordCopies = new ArrayList<OutGoingRecordCopy>();
						for(int i = 0;i<copy_userList.size();i++){
							OutGoingRecordCopy outGoingRecordCopy = new OutGoingRecordCopy();
							outGoingRecordCopy.setOutGoing_record_id(id);
							outGoingRecordCopy.setUser_id(Integer.valueOf(copy_userList.get(i)));
							outGoingRecordCopies.add(outGoingRecordCopy);
						}
						try {
							iuserService.insertOutGoingRecordCopy(outGoingRecordCopies);//新增外出抄送人信息
						} catch (Exception e) {
							
						}
						
						// 加班申请图片
						if(files!=null){
							for(int size = 0;size<files.length;size++){
								try {
									String fileName=files[size].getOriginalFilename();
									//3.由于文件个数多,采用分文件存储
									String dateDir=
											new SimpleDateFormat("yyyy/MM/dd")
													.format(new Date());
								
									//生成对应的文件夹
									String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
									int p = path.indexOf("webapps");
									String realpath = path.substring(0, p + 8) +"OutGoing"+dateDir + "\\";
									realpath = realpath.replaceAll("%20", " ");
									//判断是否存在
									File file=new File(realpath);
									if(!file.exists()){
										//生成文件夹
										file.mkdirs();
									}
									//防止图片上传量过大引起的重名问题
									String  uuidName=
											UUID.randomUUID()
												.toString().replace("-", "");	
									String  randomNum=((int)(Math.random()*99999))+"";
									//获取文件后缀名
									String fileType=
											fileName.substring(
													fileName.lastIndexOf("."));
									String prefix=fileName.substring(0, fileName.lastIndexOf("."));
									//路径拼接(文件真实的存储路径)
									String  fileDirPath=
											realpath+"/"+prefix+uuidName+randomNum+fileType;
									//存储数据
									int i = 0;
									i = iuserService.OutGoingRecordPic(id,fileDirPath);//新增外出图片
									if(i!=1){
										map.put("code", 1020);
									}
									//文件上传
									files[size].transferTo(new File(fileDirPath));
								} catch (Exception e) {
									e.printStackTrace();
									map.put("code", 1021);
								}
							}
						}
					JSONObject audience = new JSONObject();
					JSONArray alias1 = new JSONArray();
					if(audit_userList!=null&&audit_userList.size()>0){
						alias1.add(String.valueOf(audit_userList.get(0)));
					}
					 audience.put("alias", alias1);
					 JSONObject alert = new JSONObject();
					 alert.put("type",PushType.GO_OUT_APPLY);
					 alert.put("data", JSONObject.fromObject(outGoingRecord));
					 PushData pushData = new PushData();
					 pushData.setType(PushType.GO_OUT_APPLY);
					 pushData.setData(outGoingRecord.getId());
					 String message = "您收到了一条外出申请!";
					JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.GO_OUT_APPLY),message);
					int companyId = getCompangId(outGoingRecord.getUser_id());
					int setResultCode = 400;
					if(resDate.containsKey("error")){
						setResultCode = 400;
					}else{
						setResultCode = 200;
					}
					List<Integer> userIdList = new ArrayList<Integer>();
					if(audit_userList!=null&&audit_userList.size()>0){
						userIdList.add(Integer.valueOf(audit_userList.get(0)));
					}
					insertJPushData(alert.toString(),PushType.GO_OUT_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
							JSONArray.toJSONString(userIdList).toString());
					map.put("code", 1001);
					} catch (Exception e) {
						e.printStackTrace();
						map.put("code", 2001);
					}
					JSONObject jsonObject = new JSONObject();
				String code =  String.valueOf(map.get("code"));
				map.remove("code");
				jsonObject.put("code", code);
				jsonObject.put("data", map);
				response.addHeader("code",code);//返回安卓的公钥
				System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
				return GsonUtils.convertJSON(jsonObject);
			}
			
			
		// 增加出差申请记录
		@RequestMapping(value = "/addBusinessTraveIRecord.do")
		@ResponseBody
		public String addBusinessTraveIRecord(@RequestParam("picture") MultipartFile[] files,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
			Map<String, Object> map = new HashMap<>();
			String information = request.getParameter("information");
			BusinessTraveIRecord businessTraveIRecord = new BusinessTraveIRecord();
				try {
					JSONObject object = new JSONObject();
					object = JSONObject.fromObject(information);
					businessTraveIRecord = (BusinessTraveIRecord) JSONObject.toBean(object, BusinessTraveIRecord.class);
					businessTraveIRecord.setResult_id(DAISHENPI);
					iuserService.insertBusinessTraveIRecord(businessTraveIRecord);//新增出差详情信息
					int id = businessTraveIRecord.getId();//出差详情的ID
					
					//存储审批人
					int father_audit_id = 0;//审批人父级ID，默认是0
					List<String> audit_userList = businessTraveIRecord.getAudit_user();
					for(int i = 0;i<audit_userList.size();i++){
						BusinessTraveIRecordAudit businessTraveIRecordAudit = new BusinessTraveIRecordAudit();//出差打卡审批人
						businessTraveIRecordAudit.setBusinessTraveI_record_id(id);
						System.out.println(audit_userList.get(i));
						businessTraveIRecordAudit.setUser_id(Integer.valueOf(audit_userList.get(i)));
						businessTraveIRecordAudit.setFather_audit_id(father_audit_id);
						if(i ==0){
							businessTraveIRecordAudit.setAudit_status(1);
							JSONObject websocket = new JSONObject();
							websocket.put("key",PushType.BUSINESS_TRIP_APPLY);
							websocket.put("user_id", audit_userList.get(0));
							WebSocketFrameHandler.sendData(websocket);
						}else{
							businessTraveIRecordAudit.setAudit_status(0);
						}
						iuserService.insertBusinessTraveIRecordAudit(businessTraveIRecordAudit);//新增出差审批人信息
						father_audit_id = businessTraveIRecordAudit.getId();
					}
					//存储抄送人
					List<String> copy_userList = businessTraveIRecord.getCopy_user();
					List<BusinessTraveIRecordCopy> businessTraveIRecordCopies = new ArrayList<BusinessTraveIRecordCopy>();
					for(int i = 0;i<copy_userList.size();i++){
						BusinessTraveIRecordCopy businessTraveIRecordCopy = new BusinessTraveIRecordCopy();
						businessTraveIRecordCopy.setBusinessTraveI_record_id(id);
						businessTraveIRecordCopy.setUser_id(Integer.valueOf(copy_userList.get(i)));
						businessTraveIRecordCopies.add(businessTraveIRecordCopy);
					}
					try {
						iuserService.insertBusinessTraveIRecordCopy(businessTraveIRecordCopies);//新增出差抄送人信息
					} catch (Exception e) {
						
					}
					
					// 加班申请图片
					if(files!=null){
						for(int size = 0;size<files.length;size++){
							try {
								String fileName=files[size].getOriginalFilename();
								//3.由于文件个数多,采用分文件存储
								String dateDir=
										new SimpleDateFormat("yyyy/MM/dd")
												.format(new Date());
							
								//生成对应的文件夹
								String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
								int p = path.indexOf("webapps");
								String realpath = path.substring(0, p + 8) +"OutGoing"+dateDir + "\\";
								realpath = realpath.replaceAll("%20", " ");
								//判断是否存在
								File file=new File(realpath);
								if(!file.exists()){
									//生成文件夹
									file.mkdirs();
								}
								//防止图片上传量过大引起的重名问题
								String  uuidName=
										UUID.randomUUID()
											.toString().replace("-", "");	
								String  randomNum=((int)(Math.random()*99999))+"";
								//获取文件后缀名
								String fileType=
										fileName.substring(
												fileName.lastIndexOf("."));
								String prefix=fileName.substring(0, fileName.lastIndexOf("."));
								//路径拼接(文件真实的存储路径)
								String  fileDirPath=
										realpath+"/"+prefix+uuidName+randomNum+fileType;
								//存储数据
								int i = 0;
								i = iuserService.BusinessTraveIRecordPic(id,fileDirPath);//新增出差图片
								if(i!=1){
									map.put("code", 1020);
								}
								//文件上传
								files[size].transferTo(new File(fileDirPath));
							} catch (Exception e) {
								e.printStackTrace();
								map.put("code", 1021);
							}
						}
					}
				JSONObject audience = new JSONObject();
				JSONArray alias1 = new JSONArray();
				if(audit_userList!=null&&audit_userList.size()>0){
					alias1.add(String.valueOf(audit_userList.get(0)));
				}
				 audience.put("alias", alias1);
				 JSONObject alert = new JSONObject();
				 alert.put("type",PushType.BUSINESS_TRIP_APPLY);
				 alert.put("data", JSONObject.fromObject(businessTraveIRecord));
				 PushData pushData = new PushData();
				 pushData.setType(PushType.BUSINESS_TRIP_APPLY);
				 pushData.setData(businessTraveIRecord.getId());
				 String message = "您收到了一条出差申请!";
				JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPLY),message);
				int companyId = getCompangId(businessTraveIRecord.getUser_id());
				int setResultCode = 400;
				if(resDate.containsKey("error")){
					setResultCode = 400;
				}else{
					setResultCode = 200;
				}
				List<Integer> userIdList = new ArrayList<Integer>();
				if(audit_userList!=null&&audit_userList.size()>0){
					userIdList.add(Integer.valueOf(audit_userList.get(0)));
				}
				insertJPushData(alert.toString(),PushType.BUSINESS_TRIP_APPLY,companyId,TimeUtils.getCurrentFormatDateTime(),"别名",resDate.toString(),setResultCode,
						JSONArray.toJSONString(userIdList).toString());
				map.put("code", 1001);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("code", 2001);
				}
				JSONObject jsonObject = new JSONObject();
			String code =  String.valueOf(map.get("code"));
			map.remove("code");
			jsonObject.put("code", code);
			jsonObject.put("data", map);
			response.addHeader("code",code);//返回安卓的公钥
			System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
			return GsonUtils.convertJSON(jsonObject);
		}
	/***
	 * 新增推送消息
	 * @param alert 内容
	 * @param outAttendanceApply 推送类型
	 * @param companyId 公司ID
	 * @param currentFormatDateTime 时间
	 * @param pushType  别名推送，全部推送，标签推送
	 * @param resDate 激光返回结果
	 * @param setResultCode 激光返回码
	 * @param userIdList 推送给谁的list
	 */
	public   void insertJPushData(String alert, int outAttendanceApply, int companyId, String currentFormatDateTime,
			String pushType, String resDate, int setResultCode, String userIdList) {
		JPushData jpushData = new JPushData();
		jpushData.setContent(alert);
		jpushData.setDataType(outAttendanceApply);
		jpushData.setPusher(companyId);
		jpushData.setPushTime(currentFormatDateTime);
		jpushData.setPushType(pushType);
		jpushData.setResultCode(setResultCode);
		jpushData.setResultContent(resDate);
		jpushData.setReceiver(userIdList);
		iuserService.insertJPushData(jpushData);
	}

	/**
	 * 查询公司ID
	 * @param user_id
	 * @return
	 */
	public  int getCompangId(int user_id) {
		int comPanyId = 0;
		comPanyId = iuserService.getCompangIdByUserId(user_id);
		return comPanyId;
	}

	// 查询考勤记录
	@RequestMapping(value = "/searchAttendanceRecord.do")
	@ResponseBody
	public String searchAttendanceRecord(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
		List<WorkingTime> workingTimes = new ArrayList<WorkingTime>();
		List<WorkingTimeUser> workingTimeUser = new ArrayList<WorkingTimeUser>();
		map.put("attendanceRecord", recordList);
		map.put("holidayRecord", workingTimes);
		map.put("workingTimeUser", workingTimeUser);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int userId = object.optInt("userId");
				int companyId = object.optInt("companyId");
				String startTime = object.optString("startTime")+" 00:00:00";
				String endTime = object.optString("endTime") +" 23:59:59";
				recordList = iuserService.searchAttendanceRecord(userId, startTime, endTime);
				workingTimes = iuserService.searchHolidayRecord(object.optString("startTime"),object.optString("endTime"),companyId);
				workingTimeUser = iuserService.searchWorkingTimeUser(userId, object.optString("startTime"), object.optString("endTime"),companyId);
				if (recordList == null) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
				map.put("attendanceRecord", recordList);
				map.put("holidayRecord", workingTimes);
				map.put("workingTimeUser", workingTimeUser);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	/**
	 * 查询是否是工作日
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchHoliday.do")
	@ResponseBody
	public String searchHoliday(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("status", 0);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				String date = object.optString("date");
				int companyId = object.optInt("companyId");
				int status = 0;
				status = iuserService.searchHoliday(date,companyId);
				map.put("status", status);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencryptInt(map,androidHeaderInfos.getWebaesKey());
	}
	// 查询考勤规则和考勤记录
	@RequestMapping(value = "/searchAttendanceRulesAndRecord.do")
	@ResponseBody
	public String searchAttendanceRulesAndRecord(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
		List<AttendanceRecord> topList = new ArrayList<AttendanceRecord>();
		List<AttendanceRule> ruleList = new ArrayList<AttendanceRule>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int userId = object.optInt("userId");
				String startTime = object.optString("startTime")+" 00:00:00";
				String endTime = object.optString("endTime")+" 23:59:59";
				int company_id = object.optInt("companyId");
				 recordList = iuserService.searchAttendanceRecord(userId, startTime, endTime);
				 topList = iuserService.searchAttendanceTopRecord(startTime, endTime);
				 ruleList = iuserService.searchAttendanceRules(0,company_id,0);
				if (ruleList == null || recordList == null) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
				map.put("attendanceRule", ruleList);
				map.put("attendanceRecord", recordList);
				map.put("attendanceTopRecord", topList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	// 安卓用户更新头像
	@RequestMapping(value = "/uploadHeadPortrait.do")
	@ResponseBody
	public String uploadHeadPortrait(@RequestParam("uploadfile") MultipartFile[] files, HttpServletRequest request,
			HttpServletResponse response, HttpSession httpSession) {
		String returnData = "";
		returnData = iuserService.uploadHeadPortrait(files, request, response, httpSession);
		return returnData;
	}

	// 安卓上传日志文件
	@RequestMapping(value = "/uploadAndroidLog.do")
	@ResponseBody
	public String uploadAndroidLog(@RequestParam("uploadfile") MultipartFile[] files, HttpServletRequest request,
			HttpServletResponse response, HttpSession httpSession) {
		String returnData = "";
		returnData = androidService.uploadAndroidLog(files, request, response, httpSession);
		return returnData;
	}

	// 查询休假类型
	@RequestMapping(value = "/searchVacationTypes.do")
	@ResponseBody
	public String searchVacationTypes(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<VacationType> typeList = new ArrayList<VacationType>();
		map.put("vacationType", typeList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int manager = object.optInt("manager");//1是管理员，0是用户
				 typeList = iuserService.searchVacationTypes(manager);
					if (typeList == null) {
						map.put("code", 1010);
					} else {
						map.put("code", 1001);
					}
					map.put("vacationType", typeList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 查询加班类型
	@RequestMapping(value = "/searchOvertimeTypes.do")
	@ResponseBody
	public String searchOvertimeTypes(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<OvertimeType> typeList = new ArrayList<OvertimeType>();
		map.put("overtimeType", typeList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int manager = object.optInt("manager");
				 typeList = iuserService.searchOvertimeTypes(manager);
					if (typeList == null) {
						map.put("code", 1010);
					} else {
						map.put("code", 1001);
					}
					map.put("overtimeType", typeList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}

	// 查询工资
	@RequestMapping(value = "/searchWageByUserId.do")
	@ResponseBody
	public String searchWageByUserId(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Wage> wageList = new ArrayList<Wage>();
		map.put("wage", wageList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");
				 int startMonth = object.optInt("startMonth");
				 int endMonth = object.optInt("endMonth");
				wageList = iuserService.searchWageByUserId(userId, startMonth, endMonth);
				if (wageList == null) {
					map.put("code",1010);
				} else {
					map.put("code",1001);
				}
				map.put("wage", wageList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	// 查询通知公告
	@RequestMapping(value = "/searchAnnouncement.do")
	@ResponseBody
	public String searchAnnouncement() throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Announcement> announcementList = new ArrayList<Announcement>();
		 map.put("announcement", announcementList);
			AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
			androidHeaderInfos = androidHeaderInfo(request,response);
			map.put("code",androidHeaderInfos.getCode());
			if(androidHeaderInfos.getCode()==1001){
				try {
					JSONObject object = new JSONObject();
					 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
					 int companyId = object.optInt("companyId");
					 int bNum = object.optInt("bNum");//从第几条开始
					 int rows = object.optInt("rows");//查询几条
					 announcementList = iuserService.searchAnnouncement(companyId,bNum,rows);
					 if (announcementList == null) {
						 map.put("code",1010);
					} else {
							map.put("code",1001);
					}
					map.put("announcement", announcementList);
				} catch (Exception e) {
					map.put("code",2001);
				}
			}
			String code =  String.valueOf(map.get("code"));
			response.addHeader("code",code);//返回安卓的公钥
			return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询外勤打卡审批
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchOutAttendance.do")
	@ResponseBody
	public String searchOutAttendance(HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		map.put("outAttendanceInfo", outAttendanceInfoList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//审批人ID
				 int bNum = object.optInt("bNum");//从第几条开始
				 int rows = object.optInt("rows");//查询几条
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 String applicant_user_id = object.optString("applicantUserId");//申请人Name
				 boolean approved = object.optBoolean("approved");//true已审批，false是未审批
				 if(approved){
					 outAttendanceInfoList = iuserService.serachauditOutAttendanceHistoricalRecords(userId,bNum,rows,bt,et,applicant_user_id);
				 }else{
					 outAttendanceInfoList = iuserService.searchOutAttendance(userId,bNum,rows,bt,et,applicant_user_id);
					 }
				 if (outAttendanceInfoList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("outAttendanceInfo", outAttendanceInfoList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询补卡审批
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchAppealAttendance.do")
	@ResponseBody
	public String searchAppealAttendance(HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		map.put("appealAttendanceInfo", appealAttendanceInfoList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//审批人ID
				 int bNum = object.optInt("bNum");//从第几条开始
				 int rows = object.optInt("rows");//查询几条
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 String applicant_user_id = object.optString("applicantUserId");//申请人ID
				 boolean approved = object.optBoolean("approved");//true已审批，false是未审批
				 if(approved){
					 appealAttendanceInfoList = iuserService.searchAppealAttendance(userId,bNum,rows,bt,et,applicant_user_id);
				 }else{
					 appealAttendanceInfoList = iuserService.serachauditAppealAttendanceHistoricalRecords(userId,bNum,rows,bt,et,applicant_user_id);
					 }
				 if (appealAttendanceInfoList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("appealAttendanceInfo", appealAttendanceInfoList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询加班审批
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchOvertime.do")
	@ResponseBody
	public String searchOvertime(HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		map.put("overtimeRecordList", overTimeRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//审批人ID
				 int bNum = object.optInt("bNum");//从第几条开始
				 int rows = object.optInt("rows");//查询几条
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 String applicant_user_id = object.optString("applicantUserId");//申请人ID
				 boolean approved = object.optBoolean("approved");//true已审批，false是未审批
				 if(approved){
					 overTimeRecordList = iuserService.searchOverTimeHistoricalRecords(userId,bNum,rows,bt,et,applicant_user_id);
				 }else{
					 overTimeRecordList = iuserService.serachOverTimeRecords(userId,bNum,rows,bt,et,applicant_user_id);
					 }
				 if (overTimeRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("overtimeRecordList", overTimeRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询外出审批
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchOutGoing.do")
	@ResponseBody
	public String searchOutGoing(HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		map.put("outGoingRecordList", outGoingRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//审批人ID
				 int bNum = object.optInt("bNum");//从第几条开始
				 int rows = object.optInt("rows");//查询几条
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 String applicant_user_id = object.optString("applicantUserId");//申请人ID
				 boolean approved = object.optBoolean("approved");//true已审批，false是未审批
				 if(approved){
					 outGoingRecordList = iuserService.searchOutGoingHistoricalRecords(userId,bNum,rows,bt,et,applicant_user_id);
				 }else{
					 outGoingRecordList = iuserService.serachOutGoingRecords(userId,bNum,rows,bt,et,applicant_user_id);
					 }
				 if (outGoingRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("outGoingRecordList", outGoingRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询出差审批
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchBusinessTraveI.do")
	@ResponseBody
	public String searchBusinessTraveI(HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		map.put("businessTraveIRecordList",businessTraveIRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//审批人ID
				 int bNum = object.optInt("bNum");//从第几条开始
				 int rows = object.optInt("rows");//查询几条
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 String applicant_user_id = object.optString("applicantUserId");//申请人ID
				 boolean approved = object.optBoolean("approved");//true已审批，false是未审批
				 if(approved){
					 businessTraveIRecordList = iuserService.searchBusinessTraveIHistoricalRecords(userId,bNum,rows,bt,et,applicant_user_id);
				 }else{
					 businessTraveIRecordList = iuserService.serachBusinessTraveIRecords(userId,bNum,rows,bt,et,applicant_user_id);
					 }
				 if (businessTraveIRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("businessTraveIRecordList", businessTraveIRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 查询假期审批
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchVacation.do")
	@ResponseBody
	public String searchVacation(HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		map.put("vacationRecordList", vacationRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//审批人ID
				 int bNum = object.optInt("bNum");//从第几条开始
				 int rows = object.optInt("rows");//查询几条
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 String applicant_user_id = object.optString("applicantUserId");//申请人ID
				 boolean approved = object.optBoolean("approved");//true已审批，false是未审批
				 if(approved){
					 vacationRecordList = iuserService.searchVacationHistoricalRecords(userId,bNum,rows,bt,et,applicant_user_id);
				 }else{
					 vacationRecordList = iuserService.serachVacationRecords(userId,bNum,rows,bt,et,applicant_user_id);
					 }
				 if (vacationRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("vacationRecordList", vacationRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	/***
	 * 外勤打卡审批结果处理
	 * @param outAttendanceRecordAudit_id
	 * @param out_attendance_id
	 * @param result_id
	 * @param audit_status
	 * @param audit_resmarks
	 * @param session
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/submitApprovalOutAttendance.do")
	@ResponseBody
	public String submitApprovalOutAttendance(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int outAttendanceRecordAuditId = object.optInt("outAttendanceRecordAuditId");//外勤审批Id
				 int outAttendanceId = object.optInt("outAttendanceId");//外勤详情ID
				 int id = object.optInt("id");//考勤记录ID
				 int resultId = object.optInt("resultId");//当前审批记录结果状态
				 int auditStatus = object.optInt("auditStatus");//审批结果 2通过 3不通过
				 String auditRemarks = object.optString("auditRemarks");//审批备注、
				 int userId = object.optInt("userId");//审批人、
				jsonObject = iuserService.submitApprovalOutAttendance(String.valueOf(outAttendanceRecordAuditId),String.valueOf(outAttendanceId),String.valueOf(resultId),
						String.valueOf(auditStatus),auditRemarks,userId,id,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 提交补卡审批结果
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitApprovalAppealAttendance.do")
	@ResponseBody
	public String submitApprovalAppealAttendance(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int appealAttendanceRecordAuditId = object.optInt("appealAttendanceRecordAuditId");//补卡审批Id
				 int appealAttendanceId = object.optInt("appealAttendanceId");//补卡详情ID
				 int id = object.optInt("id");//考勤记录ID
				 int resultId = object.optInt("resultId");//当前审批记录结果状态
				 int auditStatus = object.optInt("auditStatus");//审批结果 2通过 3不通过
				 String auditRemarks = object.optString("auditRemarks");//审批备注、
				 int userId = object.optInt("userId");//审批人、
				jsonObject = iuserService.submitApprovalAppealAttendance(String.valueOf(appealAttendanceRecordAuditId),String.valueOf(appealAttendanceId),String.valueOf(resultId),
						String.valueOf(auditStatus),auditRemarks,userId,id,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 提交加班审批结果
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitApprovalOvertime.do")
	@ResponseBody
	public String submitApprovalOvertime(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int audit_id = object.optInt("audit_id");//加班审批记录ID
				 int id = object.optInt("id");//加班详情ID
				 int resultId = object.optInt("resultId");//当前审批记录结果状态
				 int auditStatus = object.optInt("auditStatus");//审批结果 2通过 3不通过
				 String auditRemarks = object.optString("auditRemarks");//审批备注、
				 int userId = object.optInt("userId");//审批人、
				jsonObject = iuserService.submitApprovalOverTime(String.valueOf(audit_id),String.valueOf(id),String.valueOf(resultId),
						String.valueOf(auditStatus),auditRemarks,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 提交外出审批结果
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitApprovalOutGoing.do")
	@ResponseBody
	public String submitApprovalOutGoing(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int audit_id = object.optInt("audit_id");//外出审批记录ID
				 int id = object.optInt("id");//外出详情ID
				 int resultId = object.optInt("resultId");//当前审批记录结果状态
				 int auditStatus = object.optInt("auditStatus");//审批结果 2通过 3不通过
				 String auditRemarks = object.optString("auditRemarks");//审批备注、
				 int userId = object.optInt("userId");//审批人、
				jsonObject = iuserService.submitApprovalOutGoing(String.valueOf(audit_id),String.valueOf(id),String.valueOf(resultId),
						String.valueOf(auditStatus),auditRemarks,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 提交出差审批结果
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitApprovalBusinessTraveI.do")
	@ResponseBody
	public String submitApprovalBusinessTraveI(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int audit_id = object.optInt("audit_id");//出差审批记录ID
				 int id = object.optInt("id");//出差详情ID
				 int resultId = object.optInt("resultId");//当前审批记录结果状态
				 int auditStatus = object.optInt("auditStatus");//审批结果 2通过 3不通过
				 String auditRemarks = object.optString("auditRemarks");//审批备注、
				 int userId = object.optInt("userId");//审批人、
				jsonObject = iuserService.submitApprovalBusinessTraveI(String.valueOf(audit_id),String.valueOf(id),String.valueOf(resultId),
						String.valueOf(auditStatus),auditRemarks,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 提交假期审批结果
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitApprovalVacation.do")
	@ResponseBody
	public String submitApprovalVacation(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int audit_id = object.optInt("audit_id");//假期审批记录ID
				 int id = object.optInt("id");//假期详情ID
				 int resultId = object.optInt("resultId");//当前审批记录结果状态
				 int auditStatus = object.optInt("auditStatus");//审批结果 2通过 3不通过
				 String auditRemarks = object.optString("auditRemarks");//审批备注、
				 int userId = object.optInt("userId");//审批人、
				jsonObject = iuserService.submitApprovalVacation(String.valueOf(audit_id),String.valueOf(id),String.valueOf(resultId),
						String.valueOf(auditStatus),auditRemarks,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 撤销外勤打卡申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitRevokeOutAttendance.do")
	@ResponseBody
	public String submitRevokeOutAttendance(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int id = object.optInt("id");//外勤申请详情记录ID
				 int userId = object.optInt("userId");//提交人
				jsonObject = iuserService.submitRevokeOutAttendance(id,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 撤销补卡申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitRevokeAppealAttendance.do")
	@ResponseBody
	public String submitRevokeAppealAttendance(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int id = object.optInt("id");//补卡申请详情记录ID
				 int userId = object.optInt("userId");//提交人
				jsonObject = iuserService.submitRevokeAppealAttendance(id,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 申请人撤销加班申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitRevokeOvertime.do")
	@ResponseBody
	public String submitRevokeOvertime(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int id = object.optInt("id");//加班申请详情记录ID
				 int userId = object.optInt("userId");//提交人
				jsonObject = iuserService.submitRevokeOverTime(id,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 申请人撤销外出申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitRevokeOutGoing.do")
	@ResponseBody
	public String submitRevokeOutGoing(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int id = object.optInt("id");//外出申请详情记录ID
				 int userId = object.optInt("userId");//提交人
				jsonObject = iuserService.submitRevokeOutGoing(id,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 申请人撤销出差申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitRevokeBusinessTraveI.do")
	@ResponseBody
	public String submitRevokeBusinessTraveI(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int id = object.optInt("id");//外出申请详情记录ID
				 int userId = object.optInt("userId");//提交人
				jsonObject = iuserService.submitRevokeBusinessTraveI(id,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 申请人撤销假期申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/submitRevokeVacation.do")
	@ResponseBody
	public String submitRevokeVacation(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		 JSONObject jsonObject = new JSONObject();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int id = object.optInt("id");//假期申请详情记录ID
				 int userId = object.optInt("userId");//提交人
				jsonObject = iuserService.submitRevokeVacation(id,userId,session,request);
				if(jsonObject.get("result").equals("fail")){
					 map.put("code",1010);
				}else{
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 申请人查询外勤打卡申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/searchApprovalAttendanceRecord.do")
	@ResponseBody
	public String searchApprovalAttendanceRecord(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<OutAttendanceInfo> outAttendanceInfoList = new ArrayList<OutAttendanceInfo>();
		map.put("outAttendanceInfo", outAttendanceInfoList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");
				 int bNum = object.optInt("bNum");
				 int rows = object.optInt("rows");
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
					 outAttendanceInfoList = iuserService.searchApprovalAttendanceRecord(userId,bNum,rows,bt,et);
				 if (outAttendanceInfoList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("outAttendanceInfo", outAttendanceInfoList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 申请人查询补卡申请记录
	 * @param session
	 * @param requestc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchApprovalAppealAttendanceRecord.do")
	@ResponseBody
	public String searchApprovalAppealAttendanceRecord(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<AppealAttendanceInfo> appealAttendanceInfoList = new ArrayList<AppealAttendanceInfo>();
		map.put("appealAttendanceInfo", appealAttendanceInfoList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//申请人ID
				 int bNum = object.optInt("bNum");
				 int rows = object.optInt("rows");
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 appealAttendanceInfoList = iuserService.searchApprovalAppealAttendanceRecord(userId,bNum,rows,bt,et);
				 if (appealAttendanceInfoList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("appealAttendanceInfo", appealAttendanceInfoList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 申请人查询加班申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchApprovalOvertime.do")
	@ResponseBody
	public String searchApprovalOvertime(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<OverTimeRecord> overTimeRecordList = new ArrayList<OverTimeRecord>();
		map.put("overtimeRecordList", overTimeRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");
				 int bNum = object.optInt("bNum");
				 int rows = object.optInt("rows");
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 overTimeRecordList = iuserService.searchApprovalOverTime(userId,bNum,rows,bt,et);
				 if (overTimeRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("overtimeRecordList", overTimeRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 申请人查询外出申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchApprovalOutGoing.do")
	@ResponseBody
	public String searchApprovalOutGoing(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<OutGoingRecord> outGoingRecordList = new ArrayList<OutGoingRecord>();
		map.put("outGoingRecordList", outGoingRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");
				 int bNum = object.optInt("bNum");
				 int rows = object.optInt("rows");
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 outGoingRecordList = iuserService.searchApprovalOutGoing(userId,bNum,rows,bt,et);
				 if (outGoingRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("outGoingRecordList", outGoingRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 申请人查询出差申请记录
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchApprovalBusinessTraveI.do")
	@ResponseBody
	public String searchApprovalBusinessTraveI(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<BusinessTraveIRecord> businessTraveIRecordList = new ArrayList<BusinessTraveIRecord>();
		map.put("businessTraveIRecordList", businessTraveIRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");
				 int bNum = object.optInt("bNum");
				 int rows = object.optInt("rows");
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 businessTraveIRecordList = iuserService.searchApprovalBusinessTraveI(userId,bNum,rows,bt,et);
				 if (businessTraveIRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("businessTraveIRecordList", businessTraveIRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 申请人查询假期申请
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchApprovalVacation.do")
	@ResponseBody
	public String searchApprovalVacation(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<VacationRecord> vacationRecordList = new ArrayList<VacationRecord>();
		map.put("vacationRecordList", vacationRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");
				 int bNum = object.optInt("bNum");
				 int rows = object.optInt("rows");
				 String bt = object.optString("bt")+" 00:00:00";
				 String et = object.optString("et")+" 23:59:59";
				 vacationRecordList = iuserService.searchApprovalVacation(userId,bNum,rows,bt,et);
				 if (vacationRecordList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("vacationRecordList", vacationRecordList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询用户可以用的调休时间
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchUserOvertimeTime.do")
	@ResponseBody
	public String searchUserOvertimeTime(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		JSONObject object = new JSONObject();
		JSONObject objecthours = new JSONObject();
		float hours = 0;
		if(androidHeaderInfos.getCode()==1001){
			try {
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");//用户ID
				 int company_id = object.optInt("companyId");//公司ID
				 hours = iuserService.findUserBaseOvertime(userId,company_id);//查询用户的加班时间
				 map.put("code",1001);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		objecthours.put("hours", hours);
		map.put("hours", hours);
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 根据详情记录ID，类型查询不同的申请记录详情
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchRecordDetails.do")
	@ResponseBody
	public String searchRecordDetails(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		JSONObject object = new JSONObject();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		OutAttendanceInfo outAttendanceInfoList = new OutAttendanceInfo();//外勤打卡
		VacationRecord vacationRecordList = new VacationRecord();//假期
		OverTimeRecord overTimeRecordList = new OverTimeRecord();//加班
		OutGoingRecord outGoingRecordList = new OutGoingRecord();//外出
		BusinessTraveIRecord businessTraveIRecordList = new BusinessTraveIRecord();//出差
		AppealAttendanceInfo appealAttendanceInfoList = new AppealAttendanceInfo();//补卡
		map.put("data", null);
		if(androidHeaderInfos.getCode()==1001){
			try {
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int id = object.optInt("id");//详情记录ID
				 int type = object.optInt("type");//类型
				 int userId = object.optInt("userId");//审批人ID
				switch (type) {
				case PushType.OUT_ATTENDANCE_APPLY:// 外勤审批
				case PushType.OUT_ATTENDANCE_APPROVAL://外勤申请
					outAttendanceInfoList = iuserService.serachOutAttendanceById(id,type,userId);
					map.put("data", outAttendanceInfoList);
					break;
				case PushType.VACATION_APPLY:// 休假
				case PushType.VACATION_APPROVAL:
					vacationRecordList = iuserService.serachVacationById(id,type,userId);
					map.put("data", vacationRecordList);
					break;
				case PushType.OVERTIME_APPLY:// 加班
				case PushType.OVERTIME_APPROVAL:
					overTimeRecordList = iuserService.serachOvertimeById(id,type,userId);
					map.put("data", overTimeRecordList);
					break;
				case PushType.GO_OUT_APPLY:// 外出
				case PushType.GO_OUT_APPROVAL:
					outGoingRecordList = iuserService.serachOutGoingById(id,type,userId);
					map.put("data", outGoingRecordList);
					break;
				case PushType.BUSINESS_TRIP_APPLY:// 出差
				case PushType.BUSINESS_TRIP_APPROVAL:
					businessTraveIRecordList = iuserService.serachBusinessTraveIById(id,type,userId);
					map.put("data", businessTraveIRecordList);
					break;
				case PushType.ATTENDANCE_APPEAL_APPLY://考勤补卡
				case PushType.ATTENDANCE_APPEAL_APPROVAL:
					appealAttendanceInfoList = iuserService.serachAppealAttendanceById(id,type,userId);
					map.put("data", appealAttendanceInfoList);
					break;
				default:
					break;
				}
				 map.put("code",1001);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	public static void main(String[] args) {
		JSONObject audience = new JSONObject();
		JSONArray alias1 = new JSONArray();
		alias1.add("10000020");
		 audience.put("alias", alias1);
		 JSONObject test = new JSONObject();
		 test.put("test", "测试极光推送");
		 PushData pushData = new PushData();
		 pushData.setType(PushType.BUSINESS_TRIP_APPLY);
		 pushData.setData(test);
		 System.out.println(GsonUtils.convertJSON(pushData));
		 String ceshi ="测试极光推送alert";
		JSONObject resDate =  	JiguangPush.jiguangPush(audience,GsonUtils.convertJSON(pushData),PushType.getPushTypeName(PushType.BUSINESS_TRIP_APPLY),ceshi);
		System.out.println(resDate.toString());
	}
	/**
	 * 查询版本信息
	 * @return
	 */
	@RequestMapping(value = "/getNewVersionUpdateLog.do")
	@ResponseBody
	public String getNewVersionUpdateLog(HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<VersionInfo> list = new ArrayList<VersionInfo>();
		map.put("versionInfoList", list);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int apkTypeId = object.optInt("apkTypeId");
				list = iuserService.getNewVersionUpdateLog(apkTypeId);
				 if (list == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("versionInfoList", list);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@RequestMapping(value = "/getEmployeeStatus.do")
	@ResponseBody
	public String getEmployeeStatus(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<AllUserInfoStatus> userList = new ArrayList<AllUserInfoStatus>();
		map.put("employeeStatusList", userList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int companyId = object.optInt("companyId");
				
				 userList = iuserService.getEmployeeStatus(companyId);
				 if (userList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("employeeStatusList", userList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 安卓查询轮播图
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchHomePagePicture.do")
	@ResponseBody
	public String searchHomePagePicture(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<HomePagePicture> homePagePictureList = new ArrayList<HomePagePicture>();
		map.put("homePagePictureList", homePagePictureList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int companyId = object.optInt("companyId");
				
				 homePagePictureList = iuserService.searchHomePagePicture(companyId);
				 if (homePagePictureList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("homePagePictureList", homePagePictureList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	/**
	 * 查询考勤信息
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/serachUserInfoAll.do")
	@ResponseBody
	public String serachUserInfoAll(HttpSession session,HttpServletRequest request) throws Exception{
		JSONObject object = new JSONObject();
		object = iuserService.serachUserInfoAll();
		return object.toString();
	}
	/**
	 * 人脸终端查询所有人和每个人图片的个数
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/serachAllFaceOfTerminal.do")
	@ResponseBody
	public String serachAllFaceOfTerminal(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<AllUserInfo> userList = new ArrayList<AllUserInfo>();
		map.put("userList", userList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int companyId = object.optInt("companyId");
				
				 userList = iuserService.serachAllFaceOfTerminal(companyId);
				 if (userList == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				 map.put("userList", userList);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	
	}
	/**
	 * 查询环境检测仪的数据
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/echartsInit.do")
//	@ResponseBody
//	public String echartsInit(HttpSession session,HttpServletRequest request) throws Exception{
//		JSONObject object = new JSONObject();
//		object = iuserService.echartsInit();
//		return object.toString();
//	}
	
	
	/**
	 * 新增人脸识别
	 * @param files
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addFace.do")
	@ResponseBody
	public String addFace(@RequestParam("picture") MultipartFile[] files,
			HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
		Map<String, Object> map = new HashMap<>();
		String information = request.getParameter("information");
		Face faceNew = new Face();
		map.put("face", faceNew);
		Face face = new Face();
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(information);
				face = (Face) JSONObject.toBean(object, Face.class);
				int count = 0;
				count = iuserService.findFaceCountByUserId(face.getUser_id());
				if(count>=FACECOUNT){
					map.put("code", 1023);
				}else{
					if(files!=null){
						for(int size = 0;size<files.length;size++){
							try {
								String fileName=files[size].getOriginalFilename();
								//3.由于文件个数多,采用分文件存储
								String dateDir=
										new SimpleDateFormat("yyyy/MM/dd")
												.format(new Date());
							
								//生成对应的文件夹
								String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
								int p = path.indexOf("webapps");
								String realpath = path.substring(0, p + 8) +"faceFile"+dateDir + "\\";
								realpath = realpath.replaceAll("%20", " ");
								//判断是否存在
								File file=new File(realpath);
								if(!file.exists()){
									//生成文件夹
									file.mkdirs();
								}
								//防止图片上传量过大引起的重名问题
								String  uuidName=
										UUID.randomUUID()
											.toString().replace("-", "");	
								String  randomNum=((int)(Math.random()*99999))+"";
								//获取文件后缀名
								String fileType=
										fileName.substring(
												fileName.lastIndexOf("."));
								String prefix=fileName.substring(0, fileName.lastIndexOf("."));
								//路径拼接(文件真实的存储路径)
								String  fileDirPath=
										realpath+"/"+prefix+uuidName+randomNum+"--"+face.getUser_id()+fileType;
								face.setUrl(fileDirPath);
								//存储数据
								try {
									iuserService.AddFace(face);
									int id = face.getId();
									System.out.println(id);
									faceNew = iuserService.findface(id);
									if(faceNew!=null){
										String pictureUrl = faceNew.getUrl().split("webapps")[1];
										faceNew.setUrl(pictureUrl);
										map.put("face", faceNew);
									}
									Face facenew = new Face();
									facenew = iuserService.findface(id);
									String pictureUrl = facenew.getUrl().split("webapps")[1];
									facenew.setUrl(pictureUrl);
									FaceWebsocket faceWebsocket = new FaceWebsocket();
									faceWebsocket.setType("add");
									faceWebsocket.setId(id);
									faceWebsocket.setFace(facenew);
									JSONObject websocket = new JSONObject();
									websocket.put("key",PushType.SERVER_CLIENT);
									websocket.put("message", faceWebsocket);
									WebSocketFrameHandler.sendData(websocket);
									map.put("code", 1001);
									//文件上传
									files[size].transferTo(new File(fileDirPath));
								} catch (Exception e) {
									e.printStackTrace();
									map.put("code", 1010);
								}
							} catch (Exception e) {
								e.printStackTrace();
								map.put("code", 1021);
							}
						}
					}
				}
					
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", 2001);
			}
			JSONObject jsonObject = new JSONObject();
		String code =  String.valueOf(map.get("code"));
		map.remove("code");
		jsonObject.put("code", code);
		jsonObject.put("data", map);
		response.addHeader("code",code);//返回安卓的公钥
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
		return GsonUtils.convertJSON(jsonObject);
	}
	/**
	 * 查询人脸识别
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchFace.do")
	@ResponseBody
	public String searchFace(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<Face> face = new ArrayList<Face>();
		map.put("face", face);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 int userId = object.optInt("userId");
				
				 face = iuserService.searchFace(userId);
				 if (face == null) {
					 map.put("code",1010);
				 } else {
					 map.put("code",1001);
				 }
				map.put("face", face);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询全部的人脸识别
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchAllFace.do")
	@ResponseBody
	public String searchAllFace(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<Face> face = new ArrayList<Face>();
		map.put("face", face);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				 face = iuserService.searchAllFace();
				 if (face == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("face", face);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	/**
	 * 查询全部的没有处理的人脸
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchAllFaceUntreated.do")
	@ResponseBody
	public String searchAllFaceUntreated(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<Face> face = new ArrayList<Face>();
		map.put("face", face);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				 face = iuserService.searchAllFaceUntreated();
				 if (face == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("face", face);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 新增人脸识别进出记录
	 * @param files
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addFaceRecord.do")
	@ResponseBody
	public String addFaceRecord(@RequestParam("picture") MultipartFile[] files,
			HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
		Map<String, Object> map = new HashMap<>();
		String information = request.getParameter("information");
		AttendanceFaceRecord record = new AttendanceFaceRecord();
		String  fileDirPath="";
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(information);
				 record = (AttendanceFaceRecord) JSONObject.toBean(object, AttendanceFaceRecord.class);
				 int userId = record.getUser_id();
				 String createTime = record.getAttendance_time();
				 String remarks = record.getRemarks();
				 String user_name = record.getUser_name();
					if(files!=null){
						for(int size = 0;size<files.length;size++){
							try {
								String fileName=files[size].getOriginalFilename();
								//3.由于文件个数多,采用分文件存储
								String dateDir=
										new SimpleDateFormat("yyyy/MM/dd")
												.format(new Date());
								//生成对应的文件夹
								String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
								int p = path.indexOf("webapps");
								String realpath = path.substring(0, p + 8) +"faceRecordFile"+dateDir + ""
										+ "\\";
								realpath = realpath.replaceAll("%20", " ");
								//判断是否存在
								File file=new File(realpath);
								if(!file.exists()){
									//生成文件夹
									file.mkdirs();
								}
								//防止图片上传量过大引起的重名问题
								String  uuidName=
										UUID.randomUUID()
											.toString().replace("-", "");	
								String  randomNum=((int)(Math.random()*99999))+"";
								//获取文件后缀名
								String fileType=
										fileName.substring(
												fileName.lastIndexOf("."));
								String prefix=fileName.substring(0, fileName.lastIndexOf("."));
								//路径拼接(文件真实的存储路径)
								  fileDirPath=
										realpath+"/"+prefix+uuidName+randomNum+"--"+userId+fileType;
								//存储数据
								//文件上传
								files[size].transferTo(new File(fileDirPath));
							} catch (Exception e) {
								e.printStackTrace();
								map.put("code", 1021);
							}
						}
					}
					int i = 0;
					i = iuserService.addFaceRecord(fileDirPath,userId,createTime,remarks,user_name);
					if(i==1){
						map.put("code", 1022);
					}
					if(record.isStatus()){
						try {
							AttendanceRule currentRule = new AttendanceRule();
							currentRule = iuserService.searchAttendanceRuleByRuleId(record.getRule_id());
							if (TimeUtils.getTimeCompareSizeHHMM(TimeUtils.getCurrentTime(), currentRule.getRule_time_work()) > 1) {
								// 正常签到
								record.setAttendance_type(RESULT_QIANDAO_CHENGGONG);
							} else {
								// 迟到打卡
								record.setAttendance_type(RESULT_CHIDAO_DAKA);
							}
							iuserService.addAttendanceFaceRecord(record);
							map.put("code", 1001);
						} catch (Exception e) {
							e.printStackTrace();
							map.put("code", 1010);
						}
					}
					
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", 2001);
			}
			JSONObject jsonObject = new JSONObject();
		String code =  String.valueOf(map.get("code"));
		map.remove("code");
		jsonObject.put("code", code);
		jsonObject.put("data", map);
		response.addHeader("code",code);//返回安卓的公钥
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
		return GsonUtils.convertJSON(jsonObject);
	}
	
	/**
	 * 删除人脸识别图片
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteFace.do")
	@ResponseBody
	public String deleteFace(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		Face face = new Face();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
			face = (Face) JSONObject.toBean(object, Face.class);
			try {
				int i = 0;
				if(face.getEffective()==0){//假删
					i = iuserService.deleteFace(face.getId());
				}else if(face.getEffective()==1){//真删
					i = iuserService.deleteFaceTrue(face.getId());
				}
				if(i>0){
					if(face.getEffective()==1){
						String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
						int p = path.indexOf("webapps");
						String realpath = path.substring(0, p + 8);
						String oldPath = realpath+face.getUrl();
						if (oldPath != "" && oldPath != null) {
							File delfile = new File(oldPath);
							// 路径为文件且不为空则进行删除
							if (delfile.isFile() && delfile.exists()) {
								delfile.delete();
								logger.info("用户" + face.getUser_id() + "删除旧的人脸图片！");
							}
						}
					}
					Face facenew = new Face();
					FaceWebsocket faceWebsocket = new FaceWebsocket();
					faceWebsocket.setType("delete");
					faceWebsocket.setId(face.getId());
					faceWebsocket.setFace(facenew);
					JSONObject websocket = new JSONObject();
					websocket.put("key",PushType.SERVER_CLIENT);
					websocket.put("message", faceWebsocket);
					WebSocketFrameHandler.sendData(websocket);
					map.put("code",1001);
				}else{
					map.put("code",1010);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	/**
	 * 查询openID是否有绑定用户
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchOpenId.do")
	@ResponseBody
	public String searchOpenId(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<User> userInfo = new ArrayList<User>();
		map.put("userInfo", userInfo);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 String openId = object.optString("openId");
				
				 userInfo = iuserService.searchOpenId(openId);
				 if (userInfo == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("userInfo", userInfo);
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 *	用户绑定openiD
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bindingOpenId.do")
	@ResponseBody
	public String bindingOpenId(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 String openId = object.optString("openId");
				 int userId = object.optInt("userId");
				
				int i = 0;
				i = iuserService.bindingOpenId(openId,userId);
				 if (i == 1) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/***
	 * 通过code换取网页授权access_token
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/access_token.do")
	@ResponseBody
	public String access_token(HttpSession session,HttpServletRequest request) throws Exception{
		String result = "";
		Map<String, Object> map = new HashMap<>();
		List<User> userInfo = new ArrayList<User>();
		List<VisitorInfo> visitorInfo = new ArrayList<VisitorInfo>();
		map.put("userInfo", userInfo);
		map.put("visitorInfo", visitorInfo);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				 object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				 String code = object.optString("code");
				 String type = object.optString("type");//是访客还是员工visitor/staff
				String apiURL = Oauth2_Access_token+"?appid="+APPID+"&secret="+SECRET+"&code="+code+"&grant_type="+grant_type;
				System.out.println(apiURL);
		         result = HttpClientUtil.httpGetRequest(apiURL);//
		        System.out.println(result);
		        if (StringUtils.isNotBlank(result)) {
		        	JSONObject jsonObj = JSONObject.fromObject(result);
		        	String openId = (String) jsonObj.get("openid");
		        	System.out.println(openId);
		        	map.put("openId",openId);
		        	if(StringUtils.isBlank(openId)){
		        		try {
		        			map.put("code",String.valueOf(jsonObj.get("errcode")) );
						} catch (Exception e) {
							e.printStackTrace();
						}
		        	}else{
		        		if(type.equals("visitor")){
		        			visitorInfo = iuserService.searchVisitorOpenId(openId);
		        			if (visitorInfo == null) {
							 	map.put("code",1010);
							} else {
								map.put("code",1001);
							}
							map.put("visitorInfo", visitorInfo);
		        		}else if(type.equals("staff")){
		        			 userInfo = iuserService.searchOpenId(openId);
							 if (userInfo == null) {
								 map.put("code",1010);
							} else {
									map.put("code",1001);
							}
							map.put("userInfo", userInfo);
		        		}
		        		
		        	}
		        }
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	
	
	/**
	 * 新增访客的基本信息
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addVisitorInfo.do")
	@ResponseBody
	public String addVisitorInfo(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		VisitorInfo infos = new VisitorInfo();
		map.put("visitorInfo",infos);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int user_id = getVisitorInfoUserId();//userId
				int company_id = object.optInt("company_id");//公司ID
				String user_name = object.optString("user_name");//姓名
				String company = object.optString("company");//公司
				String phone = object.optString("phone");//号码
				String position = object.optString("position");//职务
				String open_id = object.optString("open_id");//微信openid
				VisitorInfo info = new VisitorInfo();
				info.setUser_id(user_id);
				info.setCompany(company);
				info.setPhone(phone);
				info.setPosition(position);
				info.setUser_name(user_name);
				info.setOpen_id(open_id);
				info.setCompany_id(company_id);
				try {
					iuserService.addVisitorInfo(info);
					infos = iuserService.findVistorInfo(user_id);
					map.put("visitorInfo",info);
					map.put("code",1001);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("code",1010);
				}
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 查询全部的访客信息
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchVisitorInfoAll.do")
	@ResponseBody
	public String searchVisitorInfoAll(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<VisitorInfo> info = new ArrayList<VisitorInfo>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		map.put("visitorInfo", info);
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int company_id = object.optInt("companyId");//公司ID
				info = iuserService.serachVisitorInfoAll(company_id);
				 if (info == null) {
					 map.put("code",1010);
				} else {
						map.put("code",1001);
				}
				map.put("visitorInfo", info);
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 修改访客的基本信息
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateVisitorInfo.do")
	@ResponseBody
	public String updateVisitorInfo(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int user_id = object.optInt("user_id");//user_id
				String user_name = object.optString("user_name");//姓名
				String company = object.optString("company");//公司
				String phone = object.optString("phone");//号码
				String position = object.optString("position");//职务
				String open_id = object.optString("open_id");//微信openid
				VisitorInfo info = new VisitorInfo();
				info.setUser_id(user_id);
				info.setCompany(company);
				info.setPhone(phone);
				info.setPosition(position);
				info.setUser_name(user_name);
				info.setOpen_id(open_id);
				int i= iuserService.updateVisitorInfo(info);
				if (i !=1) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 修改face的有效期时间
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateFace.do")
	@ResponseBody
	public String updateFace(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				Face info = (Face) JSONObject.toBean(object, Face.class);
				System.out.println(info.toString());
				int i= iuserService.updateFace(info);
				if (i <=0) {
					 map.put("code",1010);
				} else {
					Face facenew = new Face();
					facenew = iuserService.findface(info.getId());
					String pictureUrl = facenew.getUrl().split("webapps")[1];
					facenew.setUrl(pictureUrl);
					FaceWebsocket faceWebsocket = new FaceWebsocket();
					faceWebsocket.setType("update");
					faceWebsocket.setId(info.getId());
					faceWebsocket.setFace(facenew);
					JSONObject websocket = new JSONObject();
					websocket.put("key",PushType.SERVER_CLIENT);
					websocket.put("message", faceWebsocket);
					WebSocketFrameHandler.sendData(websocket);
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 删除访客的基本信息
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteVisitorInfo.do")
	@ResponseBody
	public String deleteVisitorInfo(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int user_id = object.optInt("user_id");//user_id
				int i= iuserService.deleteVisitorInfo(user_id);
				if (i !=1) {
					 map.put("code",1010);
				} else {
					iuserService.deleteVisitorFace(user_id);
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	
	/**
	 * 查询访客的最后的userid
	 * @return
	 */
	private int getVisitorInfoUserId() {
		int userid = 0;
			userid = iuserService.getVisitorInfoUserId();
		return userid;
	}
	
	/**
	 * 新增访客预约信息
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addVisitorSubscribe.do")
	@ResponseBody
	public String addVisitorSubscribe(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			VisitorSubscribe visitorSubscribe = new VisitorSubscribe();
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				visitorSubscribe = (VisitorSubscribe) JSONObject.toBean(object, VisitorSubscribe.class);
				String stratDate = visitorSubscribe.getStartDate();
				String endDate = visitorSubscribe.getEndDate();
				int user_id = visitorSubscribe.getUser_id();
				List<VisitorSubscribe> visitorSubscribeList = new ArrayList<VisitorSubscribe>();
				visitorSubscribeList = iuserService.searchVisitorSubscribe(user_id);
				boolean isoverlap = false;
				for(VisitorSubscribe subscribe : visitorSubscribeList){
					String stratDateOld = subscribe.getStartDate();
					String enddStringOld = subscribe.getEndDate();
					if(TimeUtils.isOverlap(stratDate, endDate, stratDateOld, enddStringOld)){
						isoverlap = true;
						break;
					}
				}
				if(isoverlap){
					map.put("code", 1024);
				}else{
					VisitorInfo info = new VisitorInfo();
					info = iuserService.findVistorInfo(user_id);
					visitorSubscribe.setFaceUrl(info.getUrl());
					visitorSubscribe.setFaceFeature(info.getFaceFeature());
					int i = 0;
					i = iuserService.addVisitorSubscribe(visitorSubscribe);
					if (i !=1) {
						 map.put("code",1010);
					} else {
						map.put("code",1001);
					}
				}
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	/**
	 * 新增/修改访客的人脸
	 * @param files
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addVisitorSubscribeFace.do")
	@ResponseBody
	public String addVisitorSubscribeFace(@RequestParam("picture") MultipartFile[] files,
			HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
		Map<String, Object> map = new HashMap<>();
		String information = request.getParameter("information");
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(information);
				int userId  = object.optInt("userId");
				VisitorInfo info = new VisitorInfo();
				info = iuserService.findVistorInfo(userId);
				String oldPath = info.getUrl();
					if(files!=null){
						for(int size = 0;size<files.length;size++){
							try {
								String fileName=files[size].getOriginalFilename();
								//3.由于文件个数多,采用分文件存储
								String dateDir=
										new SimpleDateFormat("yyyy/MM/dd")
												.format(new Date());
								//生成对应的文件夹
								String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
								int p = path.indexOf("webapps");
								String realpath = path.substring(0, p + 8) +"visitorSubscribeFaceFile"+dateDir + "\\";
								realpath = realpath.replaceAll("%20", " ");
								//判断是否存在
								File file=new File(realpath);
								if(!file.exists()){
									//生成文件夹
									file.mkdirs();
								}
								//防止图片上传量过大引起的重名问题
								String  uuidName=
										UUID.randomUUID()
											.toString().replace("-", "");	
								String  randomNum=((int)(Math.random()*99999))+"";
								//获取文件后缀名
								String fileType=
										fileName.substring(
												fileName.lastIndexOf("."));
								String prefix=fileName.substring(0, fileName.lastIndexOf("."));
								//路径拼接(文件真实的存储路径)
								String  fileDirPath=
										realpath+"/"+prefix+uuidName+randomNum+"--"+userId+fileType;
								//文件上传
								files[size].transferTo(new File(fileDirPath));
								

						        FaceEngine faceEngine = new FaceEngine("d:\\arcsoft-lib");
						        //激活引擎
						        int activeCode = faceEngine.activeOnline(appId, sdkKey);

						        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
						            System.out.println("引擎激活失败");
						        }

						        //引擎配置
						        EngineConfiguration engineConfiguration = new EngineConfiguration();
						        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
						        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

						        //功能配置
						        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
						        functionConfiguration.setSupportAge(true);
						        functionConfiguration.setSupportFace3dAngle(true);
						        functionConfiguration.setSupportFaceDetect(true);
						        functionConfiguration.setSupportFaceRecognition(true);
						        functionConfiguration.setSupportGender(true);
						        functionConfiguration.setSupportLiveness(true);
						        functionConfiguration.setSupportIRLiveness(true);
						        engineConfiguration.setFunctionConfiguration(functionConfiguration);
						        
						      //初始化引擎
						        int initCode = faceEngine.init(engineConfiguration);

						        if (initCode != ErrorInfo.MOK.getValue()) {
						            System.out.println("初始化引擎失败");
						        }
								//人脸检测
						        ImageInfo imageInfo = getRGBData(new File(fileDirPath));
						        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
						        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
						        FaceServer.registerBgr24(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), String.valueOf(userId),faceInfoList.get(0).getRect(),fileDirPath);
						        FaceFeature faceFeature = new FaceFeature();
						        int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
								map.put("url", fileDirPath.split("webapps")[1]);
								//存储数据
								int i = 0;
								i = iuserService.addVisitorSubscribeFace(fileDirPath,userId,faceFeature.getFeatureData());
								if(i==1){
									map.put("code", 1001);
								}
								// 如果有旧图片地址，则删除
								if (oldPath != "" && oldPath != null) {
									File delfile = new File(oldPath);
									// 路径为文件且不为空则进行删除
									if (delfile.isFile() && delfile.exists()) {
										delfile.delete();
										logger.info("删除旧头像成功！");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								map.put("code", 1021);
							}
						}
					}
					
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", 2001);
			}
			JSONObject jsonObject = new JSONObject();
		String code =  String.valueOf(map.get("code"));
		map.remove("code");
		jsonObject.put("code", code);
		jsonObject.put("data", map);
		response.addHeader("code",code);//返回安卓的公钥
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
		return GsonUtils.convertJSON(jsonObject);
	}
	
	/**
	 * 查询一整年的假期情况
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchWorkingTimeByYear.do")
	@ResponseBody
	public String searchWorkingTimeByYear(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<WorkingTime> workingTimeList = new ArrayList<WorkingTime>();
		map.put("workingTimeList", workingTimeList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		String year = TimeUtils.getCurrentYear();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int companyId = object.optInt("companyId");//companyId
				workingTimeList = iuserService.searchWorkingTimeByYear(year,companyId);
				if (workingTimeList==null) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
				}
				map.put("workingTimeList", workingTimeList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 调用门铃接口
	 * @param files
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/callDoorbell.do")
	@ResponseBody
	public String callDoorbell(@RequestParam("picture") MultipartFile[] files,
			HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
		Map<String, Object> map = new HashMap<>();
			try {
				JSONObject websocket = new JSONObject();
				String imgUrl = "";  
				if(files!=null){
					for(int size = 0;size<files.length;size++){
						try {
							String fileName=files[size].getOriginalFilename();
							//3.由于文件个数多,采用分文件存储
							String dateDir=
									new SimpleDateFormat("yyyy/MM/dd")
											.format(new Date());
							//生成对应的文件夹
							String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
							int p = path.indexOf("webapps");
							String realpath = path.substring(0, p + 8) +"callDoorbellFaceFile"+dateDir + "\\";
							realpath = realpath.replaceAll("%20", " ");
							//判断是否存在
							File file=new File(realpath);
							if(!file.exists()){
								//生成文件夹
								file.mkdirs();
							}
							//防止图片上传量过大引起的重名问题
							String  uuidName=
									UUID.randomUUID()
										.toString().replace("-", "");	
							String  randomNum=((int)(Math.random()*99999))+"";
							//获取文件后缀名
							String fileType=
									fileName.substring(
											fileName.lastIndexOf("."));
							String prefix=fileName.substring(0, fileName.lastIndexOf("."));
							//路径拼接(文件真实的存储路径)
							String  fileDirPath=
									realpath+"/"+prefix+uuidName+randomNum+"--"+fileType;
							//文件上传
							files[size].transferTo(new File(fileDirPath));
							imgUrl = fileDirPath.split("webapps")[1];
						} catch (Exception e) {
							e.printStackTrace();
							map.put("code", 1021);
						}
					}
				}
				 System.out.println(imgUrl);
				websocket.put("key",PushType.CALLDOORBELL);
				websocket.put("message", imgUrl);
				WebSocketFrameHandler.sendData(websocket);
				map.put("code", 1001);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", 2001);
			}
			JSONObject jsonObject = new JSONObject();
		String code =  String.valueOf(map.get("code"));
		map.remove("code");
		jsonObject.put("code", code);
		jsonObject.put("data", map);
		response.addHeader("code",code);//返回安卓的公钥
		System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
		return GsonUtils.convertJSON(jsonObject);
	}
	
	/**
	 * 查询user_id的所有预约的信息
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchVisitorSubscribe.do")
	@ResponseBody
	public String searchVisitorSubscribe(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<VisitorSubscribe> visitorSubscribeList = new ArrayList<VisitorSubscribe>();
		map.put("visitorSubscribeList", visitorSubscribeList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int userId = object.optInt("userId");//userId
				visitorSubscribeList = iuserService.searchVisitorSubscribe(userId);
				if (visitorSubscribeList==null) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
				}
				map.put("visitorSubscribeList", visitorSubscribeList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 撤销预约
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteVisitorSubscribe.do")
	@ResponseBody
	public String deleteVisitorSubscribe(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int id = object.optInt("id");//id 预约主键
				int i = 0;
				i =  iuserService.deleteVisitorSubscribe(id);
				 if (i!=1) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
				}
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 修改访客预约信息
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateVisitorSubscribe.do")
	@ResponseBody
	public String updateVisitorSubscribe(HttpSession session,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code",androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			VisitorSubscribe visitorSubscribe = new VisitorSubscribe();
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				visitorSubscribe = (VisitorSubscribe) JSONObject.toBean(object, VisitorSubscribe.class);
				String stratDate = visitorSubscribe.getStartDate();
				String endDate = visitorSubscribe.getEndDate();
				int user_id = visitorSubscribe.getUser_id();
				List<VisitorSubscribe> visitorSubscribeList = new ArrayList<VisitorSubscribe>();
				visitorSubscribeList = iuserService.searchVisitorSubscribe(user_id);
				boolean isoverlap = false;
				for(VisitorSubscribe subscribe : visitorSubscribeList){
					String stratDateOld = subscribe.getStartDate();
					String enddStringOld = subscribe.getEndDate();
					if(TimeUtils.isOverlap(stratDate, endDate, stratDateOld, enddStringOld)){
						isoverlap = true;
						break;
					}
				}
				if(isoverlap){
					map.put("code", 1024);
				}else{
					int i = 0;
					i = iuserService.updateVisitorSubscribe(visitorSubscribe);
					if (i !=1) {
						 map.put("code",1010);
					} else {
						map.put("code",1001);
					}
				}
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询人脸进出记录
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchFaceRecord.do")
	@ResponseBody
	public String searchFaceRecord(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<FaceRecord> faceRecordList = new ArrayList<FaceRecord>();
		map.put("faceRecordList", faceRecordList);
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int userId = object.optInt("userId");//userId
				String startTime = object.optString("startTime")+" 00:00:00";
				String endTime = object.optString("endTime") +" 23:59:59";
				faceRecordList = iuserService.searchFaceRecord(userId,startTime,endTime);
				if (faceRecordList==null) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
				}
				map.put("faceRecordList", faceRecordList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 修改访客预约没有特征值的信息
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateVisitorInfoFaceFeature.do")
	@ResponseBody
	public String updateVisitorInfoFaceFeature(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Face face = new Face();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				face = (Face) JSONObject.toBean(object, Face.class);
				int userId = face.getUser_id();
				byte[] faceFeature = face.getFaceFeature();
				int i = 0;
				i = iuserService.updateVisitorInfoFaceFeature(userId,faceFeature);
				if (i!=1) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
				}
				
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 开门记录
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/openAndCloseDoorRecord.do")
	@ResponseBody
	public String openAndCloseDoorRecord(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		OpenAndCloseDoorRecord openAndCloseDoorRecord = new OpenAndCloseDoorRecord();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				openAndCloseDoorRecord = (OpenAndCloseDoorRecord) JSONObject.toBean(object, OpenAndCloseDoorRecord.class);
				int i = 0;
				i = iuserService.openAndCloseDoorRecord(openAndCloseDoorRecord);
				if (i!=1) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
					JSONObject websocket = new JSONObject();
					websocket.put("key",PushType.OPENANDCLOSEDOOR);
					JSONObject json = new JSONObject();
					json.put("status", openAndCloseDoorRecord.getStatus());
					websocket.put("message", json);
					WebSocketFrameHandler.sendData(websocket);
				}
				
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	
	/**
	 * 查询最新版本
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchNewVersion.do")
	@ResponseBody
	public String searchNewVersion(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		VersionInfo versionInfo = new VersionInfo();
		AndroidHeaderInfo androidHeaderInfos = new AndroidHeaderInfo();
		map.put("versionInfo", versionInfo);
		androidHeaderInfos = androidHeaderInfo(request,response);
		map.put("code", androidHeaderInfos.getCode());
		if(androidHeaderInfos.getCode()==1001){
			try {
				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(androidHeaderInfos.getEncryptData());
				int apkTypeId = object.optInt("apkTypeId");
				versionInfo = iuserService.searchNewVersion(apkTypeId);
				if (versionInfo==null) {
					 map.put("code",1010);
				} else {
					map.put("code",1001);
					map.put("versionInfo", versionInfo);
				}
				
			} catch (Exception e) {
				map.put("code", 2001);
			}
		}
		String code =  String.valueOf(map.get("code"));
		response.addHeader("code",code);//返回安卓的公钥
		return getAesCBCencrypt(map,androidHeaderInfos.getWebaesKey());
	}
	@RequestMapping(value = "/websockettext.do")
	@ResponseBody
	public String websockettext(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {

	        FaceEngine faceEngine = new FaceEngine("d:\\arcsoft-lib");
	        //激活引擎
	        int activeCode = faceEngine.activeOnline(appId, sdkKey);

	        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
	            System.out.println("引擎激活失败");
	        }

	        //引擎配置
	        EngineConfiguration engineConfiguration = new EngineConfiguration();
	        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
	        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

	        //功能配置
	        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
	        functionConfiguration.setSupportAge(true);
	        functionConfiguration.setSupportFace3dAngle(true);
	        functionConfiguration.setSupportFaceDetect(true);
	        functionConfiguration.setSupportFaceRecognition(true);
	        functionConfiguration.setSupportGender(true);
	        functionConfiguration.setSupportLiveness(true);
	        functionConfiguration.setSupportIRLiveness(true);
	        engineConfiguration.setFunctionConfiguration(functionConfiguration);
	        
	      //初始化引擎
	        int initCode = faceEngine.init(engineConfiguration);

	        if (initCode != ErrorInfo.MOK.getValue()) {
	            System.out.println("初始化引擎失败");
	        }
	        
		String pathfile = "D:\\门禁照片";		//要遍历的路径
		File files = new File(pathfile);		//获取其file对象
		File[] fs = files.listFiles();	//遍历path下的文件和目录，放在File数组中
		for(File f:fs){					//遍历File[]数组
			String name = f.getName();
			String prefixs=name.substring(0, name.lastIndexOf("."));
			String user_id = prefixs.split("--")[1];
			String fileName=name;
			//3.由于文件个数多,采用分文件存储
			String dateDir=
					new SimpleDateFormat("yyyy/MM/dd")
							.format(new Date());
		
			//生成对应的文件夹
			String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
			int p = path.indexOf("webapps");
			String realpath = path.substring(0, p + 8) +"faceFile"+dateDir + "\\";
			realpath = realpath.replaceAll("%20", " ");
			//判断是否存在
			File file=new File(realpath);
			if(!file.exists()){
				//生成文件夹
				file.mkdirs();
			}
			//防止图片上传量过大引起的重名问题
			String  uuidName=
					UUID.randomUUID()
						.toString().replace("-", "");	
			String  randomNum=((int)(Math.random()*99999))+"";
			//获取文件后缀名
			String fileType=
					fileName.substring(
							fileName.lastIndexOf("."));
			String prefix=fileName.substring(0, fileName.lastIndexOf("."));
			//路径拼接(文件真实的存储路径)
			String  fileDirPath=
					realpath+"/"+prefix+uuidName+randomNum+"--"+user_id+fileType;
			
			//人脸检测
	        ImageInfo imageInfo = getRGBData(f);
	        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
	        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
	        FaceServer.registerBgr24(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), user_id,faceInfoList.get(0).getRect(),fileDirPath);
	        FaceFeature faceFeature = new FaceFeature();
	        int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
	        System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
	        Face face = new Face();
	        face.setUser_id(Integer.valueOf(user_id));
	        face.setUrl(fileDirPath);
	        face.setFaceFeature(faceFeature.getFeatureData());
	        face.setStartDate("2019-09-25 00:00:00");
	        face.setStartTime("00:00:00");
	        face.setEndTime("23:59:59");
	        face.setIdentity(0);
	        face.setName(prefixs.split("--")[0]);
	        face.setEndDate("2099-09-25 00:00:00");
	        face.setEffectiveDay("1,2,3,4,5,6,7");
	        face.setWorkingDay(0);
	        face.setEffective(1);
	        face.setServerStatus(0);
	        System.out.println(prefixs+"----"+face.toString());
	        iuserService.inserAddface(face);
		}
		return null;
	}
	
	/**
	 * 直接发送开门指令
	 * @param response
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/testxx.do")
	@ResponseBody
	public void testxx(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
		JSONObject websocket = new JSONObject();
		websocket.put("key",PushType.OPENANDCLOSEDOOR);
		JSONObject json = new JSONObject();
		json.put("status", 1);
		websocket.put("message", json);
		WebSocketFrameHandler.sendData(websocket);
	}
	/*************************************************************微信小程序接口****************************************************************
	 * 通过code换取网页授权access_token
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/access_tokenPlaintext.do")
	@ResponseBody
	public String access_tokenPlaintext(String code,HttpSession session,HttpServletRequest request) throws Exception{
		System.out.println("进入access_tokenPlaintext");
		String result = "";
		Map<String, Object> map = new HashMap<>();
		List<User> userInfo = new ArrayList<User>();
		map.put("userInfo", userInfo);
		map.put("code",2001);
			try {
				String apiURL = jscode2session+"?appid="+XAPPID+"&secret="+XSECRET+"&js_code="+code+"&grant_type="+grant_type;
				System.out.println(apiURL);
		         result = HttpClientUtil.httpGetRequest(apiURL);
		        System.out.println(result);
		        if (StringUtils.isNotBlank(result)) {
		        	JSONObject jsonObj = JSONObject.fromObject(result);
		        	String openId = (String) jsonObj.get("openid");
		        	String unionid = "";
		        	if(StringUtils.isNotBlank((String) jsonObj.get("unionid"))){
		        		unionid = (String) jsonObj.get("unionid");
		        	}
		        	System.out.println(openId);
		        	map.put("openId",openId);
		        	map.put("unionid", unionid);
		        	if(StringUtils.isBlank(openId)){
		        		try {
		        			map.put("code",String.valueOf(jsonObj.get("errcode")) );
						} catch (Exception e) {
							e.printStackTrace();
						}
		        	}else{
		        		 userInfo = iuserService.searchOpenId(openId);
						 if (userInfo == null) {
							 map.put("code",1010);
						} else {
								map.put("code",1001);
						}
						map.put("userInfo", userInfo);
		        	}
		        }
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", map);
		return GsonUtils.convertJSON(jsonObject);
	}
	
	@RequestMapping(value = "/updateUnionid.do")
	@ResponseBody
	public String updateUnionid(String code,String user_id,HttpSession session,HttpServletRequest request) throws Exception{
		System.out.println("进入updateUnionid");
		String result = "";
		Map<String, Object> map = new HashMap<>();
		String unionid = "";
		map.put("unionid", unionid);
		map.put("code",2001);
			try {
				String apiURL = jscode2session+"?appid="+XAPPID+"&secret="+XSECRET+"&js_code="+code+"&grant_type="+grant_type;
				System.out.println(apiURL);
		         result = HttpClientUtil.httpGetRequest(apiURL);
		        System.out.println(result);
		        if (StringUtils.isNotBlank(result)) {
		        	JSONObject jsonObj = JSONObject.fromObject(result);
		        	String openId = (String) jsonObj.get("openid");
		        	
		        	if(StringUtils.isNotBlank((String) jsonObj.get("unionid"))){
		        		unionid = (String) jsonObj.get("unionid");
		        		map.put("unionid", unionid);
			        	iuserService.adduserInfoByOpenId(unionid,user_id,openId);
		        	}
		        	
		        	map.put("code",1001);
		        }
			} catch (Exception e) {
				map.put("code",2001);
				e.printStackTrace();
			}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", map);
		return GsonUtils.convertJSON(jsonObject);
	}
	/**
	 * 小程序 登录接口
	 * @param code
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/small_Program_login.do")
	@ResponseBody
	public String small_Program_login(String unionid , String user_id,String password,String openId,HttpSession session,HttpServletRequest request) throws Exception{
		System.out.println("进入small_Program_login");
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		List<User> userList = new ArrayList<User>();
		map.put("userInfo", userList);
		map.put("code",2001);
			try {
				password = AesCBC.encrypt(password,"utf-8",AesCBC.sKey,AesCBC.ivParameter);
				try {
					iuserService.adduserInfoByOpenId(unionid,user_id,openId);
					user = iuserService.serachUserByUserAndPasswork(user_id,password);
					if(user.getUser_id()!=0){
						userList.add(user);
						map.put("userInfo", userList);
						map.put("code",1001);
					}else{
						map.put("code",1010);
					}
				} catch (Exception e) {
					map.put("code",2001);
					e.printStackTrace();
					logger.info("小程序登录接口错误");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("小程序密码加密错误");
			}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", map);
		return GsonUtils.convertJSON(jsonObject);
	}
	
		// 小程序查询考勤规则和考勤记录
		@RequestMapping(value = "/small_Program_searchAttendanceRulesAndRecord.do")
		@ResponseBody
		public String small_Program_searchAttendanceRulesAndRecord(int userId,String startTime,String endTime,int company_id,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
			Map<String, Object> map = new HashMap<>();
			List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
			List<AttendanceRecord> topList = new ArrayList<AttendanceRecord>();
			List<AttendanceRule> ruleList = new ArrayList<AttendanceRule>();
			map.put("attendanceRule", ruleList);
			map.put("attendanceRecord", recordList);
			map.put("attendanceTopRecord", topList);
			map.put("code", 2001);
			try {
				 startTime = startTime+" 00:00:00";
				 endTime = endTime+" 23:59:59";
				 recordList = iuserService.searchAttendanceRecord(userId, startTime, endTime);
				 topList = iuserService.searchAttendanceTopRecord(startTime, endTime);
				 ruleList = iuserService.searchAttendanceRules(0,company_id,0);
				if (ruleList == null || recordList == null) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
				map.put("attendanceRule", ruleList);
				map.put("attendanceRecord", recordList);
				map.put("attendanceTopRecord", topList);
			} catch (Exception e) {
				map.put("code", 2001);
			}
			return GsonUtils.convertJSON(map);
		}
		
		// 小程序查询考勤记录
		@RequestMapping(value = "/small_Program_searchAttendanceRecord.do")
		@ResponseBody
		public String small_Program_searchAttendanceRecord(int userId,String startTime,String endTime,int companyId,HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
			Map<String, Object> map = new HashMap<>();
			List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
			List<WorkingTime> workingTimes = new ArrayList<WorkingTime>();
			List<WorkingTimeUser> workingTimeUser = new ArrayList<WorkingTimeUser>();
			map.put("attendanceRecord", recordList);
			map.put("holidayRecord", workingTimes);
			map.put("workingTimeUser", workingTimeUser);
			map.put("code", 2001);
			try {
				workingTimes = iuserService.searchHolidayRecord(startTime,endTime,companyId);
				workingTimeUser = iuserService.searchWorkingTimeUser(userId, startTime, endTime,companyId);
				startTime = startTime+" 00:00:00";
				 endTime = endTime+" 23:59:59";
				recordList = iuserService.searchAttendanceRecord(userId, startTime, endTime);
				if (recordList == null) {
					map.put("code", 1010);
				} else {
					map.put("code", 1001);
				}
				map.put("attendanceRecord", recordList);
				map.put("holidayRecord", workingTimes);
				map.put("workingTimeUser", workingTimeUser);
			} catch (Exception e) {
				map.put("code", 2001);
			}
			return GsonUtils.convertJSON(map);
		}
		
		// 小程序增加考勤记录
				@RequestMapping(value = "/small_Program_addAttendanceRecord.do")
				@ResponseBody
				public String small_Program_addAttendanceRecord(String attendance_address,double attendance_latitude ,
						double attendance_longitude,int attendance_type,int out_attendance,int user_id,int rule_id,
						HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
					System.out.println(TimeUtils.getCurrentDateTime()+"系统时间开始");
					Map<String, Object> map = new HashMap<>();
					List<AttendanceRecord> recordList = new ArrayList<AttendanceRecord>();
					List<AttendanceRecord> topList = new ArrayList<AttendanceRecord>();
					map.put("attendanceRecord", recordList);
					map.put("attendanceTopRecord", topList);
					
					AttendanceRecord record = new AttendanceRecord();
					record.setAttendance_address(attendance_address);
					record.setAttendance_latitude(attendance_latitude);
					record.setAttendance_longitude(attendance_longitude);
					record.setAttendance_type(attendance_type);
					record.setOut_attendance(out_attendance);
					record.setUser_id(user_id);
					record.setRule_id(rule_id);
					AttendanceRule currentRule = new AttendanceRule();
						try {
							 currentRule = iuserService.searchAttendanceRuleByRuleId(record.getRule_id());
								String newTime = TimeUtils.getCurrentDateTime();
								record.setAttendance_time(newTime);
							// 根据时间和考勤类型判断考勤状态
								switch (record.getAttendance_type()) {
								case ATTENDANCE_TYPE_QIANDAO:
									// 考勤签到
									if (TimeUtils.getTimeCompareSizeHHMM(TimeUtils.getCurrentTime(), currentRule.getRule_time_work()) > 1) {
										// 正常签到
										record.setResult_id(RESULT_QIANDAO_CHENGGONG);
									} else {
										// 迟到打卡
										record.setResult_id(RESULT_CHIDAO_DAKA);
									}
									try {
										iuserService.addAttendanceRecord(record);
										map.put("code", 1001);
									} catch (Exception e) {
										e.printStackTrace();
										map.put("code", 1010);
									}
									break;
								case ATTENDANCE_TYPE_QIANTUI:
									// 考勤签退
									if (TimeUtils.getTimeCompareSizeHHMM(TimeUtils.getCurrentTime(), currentRule.getRule_time_off_work()) < 3) {
										// 正常签退
										record.setResult_id(RESULT_QIANTUI_CHENGGONG);
									} else {
										// 早退打卡
										record.setResult_id(RESULT_ZAOTUI_DAKA);
									}
									try {
										iuserService.addAttendanceRecord(record);
										map.put("code", 1001);
									} catch (Exception e) {
										e.printStackTrace();
										map.put("code", 1010);
									}
									break;
								default:
									break;
								}
								 recordList = iuserService.searchAttendanceRecord(record.getUser_id(),
										TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
								map.put("attendanceRecord", recordList);
								 topList = iuserService.searchAttendanceTopRecord(
										TimeUtils.getCurrentDate() + " " + "00:00:00", TimeUtils.getCurrentDate() + " " + "23:59:59");
								map.put("attendanceTopRecord", topList);
						} catch (Exception e) {
							e.printStackTrace();
							map.put("code", 2001);
						}
						JSONObject jsonObject = new JSONObject();
					String code =  String.valueOf(map.get("code"));
					map.remove("code");
					jsonObject.put("code", code);
					jsonObject.put("data", map);
					response.addHeader("code",code);//返回安卓的公钥
					System.out.println(TimeUtils.getCurrentDateTime()+"系统时间结束");
					return GsonUtils.convertJSON(jsonObject);
					
				}
				
				
				/*****************************************************自定义微信菜单接口*************************************************************************************/
				/**
				 * 自定义微信菜单接口
				 * @param response
				 * @param session
				 * @param request
				 * @return
				 * @throws Exception
				 */
				@RequestMapping(value = "/createMenu.do")
				@ResponseBody
				public String createMenu(HttpServletResponse response,HttpSession session,HttpServletRequest request) throws Exception {
					JSONObject jsonObject = new JSONObject();
					JSONArray jsonArray = new JSONArray();
					MINIPROGRAM miniprogram = new MINIPROGRAM();
					miniprogram.setType("miniprogram");
					miniprogram.setAppid("wx1e0994429cda642f");
					miniprogram.setName("考勤打卡");
					miniprogram.setUrl("https://www.zhongbenshuo.com/dist");
					miniprogram.setPagepath("pages/index/index");
					jsonArray.add(miniprogram);
					VIEW view = new VIEW();
					view.setType("view");
					view.setName("公司官网");
					view.setUrl("https://www.zhongbenshuo.com");
					//view.setUrl("https://www.liyuliang.net/mobile/qiandao.php?vcode=5db9516b05514");
					jsonArray.add(view);
					jsonObject.put("button", jsonArray);
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
					String msg="";
					try {
						 String token=weChatInfo.getAccess_token();//获取access_token
				        String url = create_menu+"?access_token=" + token;
				        System.out.println(jsonObject.toString());
				         msg = HttpClientUtil.post(url, jsonObject.toString(),"application/json");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return msg;
				}
}
