package net.zhongbenshuo.attendance.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
* AES 是一种可逆加密算法，对用户的敏感信息加密处理
* 对原始数据进行AES加密后，在进行Base64编码转化；
* 正确
*/
public class AesCBC {
/*已确认
* 加密用的Key 可以用26个字母和数字组成
* 此处使用AES-128-CBC加密模式，key需要为16位。
*/
	public static String sKey="IZnqNJqgLwPLO9LxMP23xZNmHHq55AmB";
    public static String ivParameter="1234567890987654";
    private static AesCBC instance=null;
    //private static 
    private AesCBC(){

    }
    public static AesCBC getInstance(){
        if (instance==null)
            instance= new AesCBC();
        return instance;
    }
    // 加密
    public static String encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes("utf-8");;
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
}

    // 解密
    public static String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,encodingFormat);
            return originalString;
        } catch (Exception ex) {
        	ex.printStackTrace();
        	System.out.println(ex.toString());
            return null;
        }
}

    public static void main(String[] args) throws Exception {
//    	String key = GetKey.generateAESKey(32);
//    	System.out.println(key);
    String	key = "BoDmggUR71nTIXpFKhQmqEmtqxxncn8f";
    String xx = "[66, 111, 68, 109, 103, 103, 85, 82, 55, 49, 110, 84, 73, 88, 112, 70, 75, 104, 81, 109, 113, 69, 109, 116, 113, 120, 120, 110, 99, 110, 56, 102]";
   String xx1 = "[66, 111, 68, 109, 103, 103, 85, 82, 55, 49, 110, 84, 73, 88, 112, 70, 75, 104, 81, 109, 113, 69, 109, 116, 113, 120, 120, 110, 99, 110, 56, 102]";
    
    String yy = "[49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 57, 56, 55, 54, 53, 52]";
    String yy1 = "[49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 57, 56, 55, 54, 53, 52]";
    //        // 需要加密的字串
        String cSrc = "{'code':2001,'data':{'company':[],'user':{'authority':'','authority_id':'','company_id':-1,'company_name':'','contact_address':'','department':'','department_id':'','effective':0,'emergency_contact_name':'','emergency_contact_phone':'','entry_time':'','gender':'','gender_id':0,'icon_url':'','id':0,'ip_register':'','job_number':'','mail_address':'','phone_number':'','position':'','position_id':'','remarks':'','role':0,'size':0,'status':'','time_register':'','user_id':0,'user_name':'','user_pwd':''},'version':{'apkTypeId':0,'createTime':'','md5Value':'','versionCode':0,'versionCount':0,'versionFileName':'','versionFileUrl':'','versionId':0,'versionLog':'','versionName':'','versionSize':0,'versionType':'','versionUrl':''}}}";
        System.out.println("加密前的字串是："+cSrc);
//        // 加密
        String enString = AesCBC.getInstance().encrypt(cSrc,"utf-8",key,ivParameter);
        System.out.println("加密后的字串是："+ enString);
        
        
        // 解密
        String DeString = AesCBC.getInstance().decrypt(enString,"utf-8",key,ivParameter);
        System.out.println("解密后的字串是：" + DeString);
    }
}
