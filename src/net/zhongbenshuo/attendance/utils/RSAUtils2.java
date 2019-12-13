package net.zhongbenshuo.attendance.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA: 既能用于数据加密也能用于数字签名的算法
 * RSA算法原理如下：
 * 1.随机选择两个大质数p和q，p不等于q，计算N=pq
 * 2.选择一个大于1小于N的自然数e，e必须与(p-1)(q-1)互素
 * 3.用公式计算出d：d×e = 1 (mod (p-1)(q-1))
 * 4.销毁p和q
 * 5.最终得到的N和e就是“公钥”，d就是“私钥”，发送方使用N去加密数据，接收方只有使用d才能解开数据内容
 * <p>
 * 基于大数计算，比DES要慢上几倍，通常只能用于加密少量数据或者加密密钥
 * 私钥加解密都很耗时，服务器要求解密效率高，客户端私钥加密，服务器公钥解密比较好一点
 */

public class RSAUtils2 {

    //用于封装随机产生的公钥与私钥
    private static HashMap<Integer, String> keyMap = new HashMap<>(2);

    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    public static void main(String[] args) {
        //生成公钥和私钥
//        genKeyPair(1024);
        //加密字符串
//        String message = "yeNNF6ieDklOaKwdiLF5Es9o9sWImf4k";
//        String messageEn = encryptByPublicKey(message, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSB6hHN8Jd02ymnzp5nuQwKmAVgiYhuyZ0GlCimkfgWzgXrp/pb/h008vrftOllA5ehHHaVvevrWRC2qzIOYpvNYc9G7agE8Mn/2192mQPP/UbadhDpu6sN6PBPnAgRmNpnY91GJMwzsO5ttDRQyrtQaMSlghEXJsO624fKheQ0wIDAQAB");
//        System.out.println("公钥加密后的字符串为:" + messageEn);
       // String messageDe = decryptByPrivateKey("cju4hI9D1R+TKHASRJshj/QQnGfYfiALJJ536hChdYMAQcMVtvx7L9BTWpRo6I5aEPOgXnV8ZZndHsaKsD3MieDe1TjMCBNgY+dlIESWFU+4bdS0Dlxw4G8iU+L3+A59/2wA1PDfSg7XEyFoyfz5t2qY6Z4JIYfnmU6XafUd7nY=", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJIHqEc3wl3TbKafOnme5DAqYBWCJiG7JnQaUKKaR+BbOBeun+lv+HTTy+t+06WUDl6EcdpW96+tZELarMg5im81hz0btqATwyf/bX3aZA8/9Rtp2EOm7qw3o8E+cCBGY2mdj3UYkzDOw7m20NFDKu1BoxKWCERcmw7rbh8qF5DTAgMBAAECgYEAjcdLdWHInynfimJ79orzB2SNBzTmasd2jTDjawuZj1VhP3T2o0FoG51qlgj72f3GebPjilX3gqQ4/MOtD3T2VostMSRczgw77QcQ4HHoEQTR1VP9HmxevP4qsctkTE1TuIIgF/fFWz+WiqT5f9pqm6hFoYGEXBTd60umvBGf6ikCQQDXKre9l0zpROBp7IGNtJqFWxtfvzzbWljrK529OFYPK2+BQQt0dLdyTGZnBujPnDaJTZN7pKEF3l3gRqSEROp9AkEArb4dCWIjdx8ggnANqDOrszURA7ofbI5QJhmhiPVbqAMt+dDidkanqGUlqj2PIkvsGhuLophmyXIu2b9or6n5jwJBAL4owrqMPSzr8mmq29ShCO9S1Z6JJP+3mV497eqNH4HIoYNaOQVzIa2iV941/Qt+w7QVG4LqvGSzYcQUqtE7FwECQDnSgrFjwu/bISOoDTOEdsk+iPoXkGu9dTko4JTHK4jf/gYdGbFJVHT7rKxarm84m0wZDMDBPbwfTy5SwJFt3UsCQGT8/gNS9vzHawJvLCa2GEusM5AEKjYNOAm/ju+o6m/b6Gby9PeE4x4wIbPu2ydXtBLaAVUqXNmw7pilnSnR8+A=");
       // System.out.println("私钥还原后的字符串为:" + messageDe);


//        String sign = signature(message, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYUe5uZjwiIsmKJpGtUWbFoFNlTiQ7RVMnYrqX2izq3u0KnVqWvIO65jtriTZ76fKIgb66pRcVm2aIsKne70abR/6JAOQOM8eZPKDoUP4QMBDzBbCJ7V2vvgPnF6U4Wez/ybNAfD/0MaL3iNaQh5Up6JdE7gbXX0zLzYL5CBsSvQIDAQAB");
//        System.out.println("签名后的字符串为:" + sign);
//        System.out.println("签名后的字符串为:" + verify(message, keyMap.get(0), sign));
    	
    	String xx = signature("{\"userId\":\"10000001\",\"password\":\"123456\"}", "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI22AkS1ORdXHDttCUIPhdvRRmLgo2plXbwxAaEfQEedCo7ZzOZ70sfBnPjypjDfUa738OOvjJYjkQglkJApy7ftK9aE1nSOPbylPY+1ZWFzgy3NGT82pwTIGUI0UD3suL+rc77uGilO9o5k07IzWp7Fskktj02HCu7ANd0wyFNDAgMBAAECgYEAgjN2RhaTi+RXZio6VV3ZySuwlex7eKgFxYDpVFZYQvQA3VszrZibAznatciL9V9Zt37K7wc+DjxDZs7M0gf0yXpymNj7uXf+ZYvubOAloJK0DsdZq/wUgJmG5WhbbEwPOP/laOlsP7SQ9GAkpt72CwPSUkBobutJxWmjmC+7A6ECQQDVeBmio8XXpL2yBEfLcGilEUMn/1qN9tXBl6B6hSDPibm1KqX5K8+HA83Lq02P+3PpmWTA98eimwPSS95KqBSZAkEAqfHqMHuTqpm4ec0K77hGPeza8UCM+hxmB/8deSIN3syWhoDW+nhXWIFK8ADMftsf1Pl7W6JMl32UPDbFJBC0OwJAcIE3oYwG/TwJ7gRrgkDgJf8PEcl3BFMPHVS4c1oujLLdeSndB5GbcmEC2VCTNEsmL/t0Km9se7qPLQpnQZmMIQI/Go2yCeFFMWLTcDSZgNtKHmLj0s5DONE3IAi8kjC/+g/9dQaskHY9L5cEHnwBd9DFKIHSxH6XeRQmFUJ5iykRAkAQPyw9kFQbek6IQvJOT3w5bnrn+Ug+loQfLqQLoAbMWJmsD75VXYV1b2GQR9r7HWWw5rxECXERDhGHSoAkBJc7");
    	System.out.println(xx);
    }

    /**
     * 随机生成密钥对
     *
     * @param length 密钥长度
     */
    public static void genKeyPair(int length) {
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(length, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
            String publicKeyString = base64Encode(publicKey.getEncoded());
            // 得到私钥字符串
            String privateKeyString = base64Encode(privateKey.getEncoded());
            // 将公钥和私钥保存到Map
            keyMap.put(0, publicKeyString);   //0表示公钥
            keyMap.put(1, privateKeyString);  //1表示私钥
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * RSA公钥加密
     *
     * @param str       待加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encryptByPublicKey(String str, String publicKey) throws  Exception{
            //base64编码的公钥
            byte[] decoded = base64Decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return base64Encode(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
      
    }

    /**
     * RSA私钥加密
     *
     * @param str        待加密字符串
     * @param privateKey 私钥
     * @return 密文
     */
    public static String encryptByPrivateKey(String str, String privateKey) {
        try {
            //base64编码的公钥
            byte[] decoded = base64Decode(privateKey);
            RSAPrivateKey pubKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return base64Encode(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 加密，返回BASE64编码的字符串
     *
     * @param key 公钥/私钥
     * @param str 待加密字符串
     * @return 密文
     */
    public static String encryptByPublicKey(String str, Key key) {
        try {
            //RSA加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] enBytes = cipher.doFinal(str.getBytes());
            return base64Encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static String decryptByPrivateKey(String str, String privateKey) throws Exception {
       
            //64位解码加密后的字符串
            byte[] inputByte = base64Decode(str);
            //base64编码的私钥
            byte[] decoded = base64Decode(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(inputByte));
      
    }

    /**
     * RSA公钥解密
     *
     * @param str       字符串
     * @param publicKey 公钥
     * @return 明文
     */
    public static String decryptByPublicKey(String str, String publicKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = base64Decode(str);
            //base64编码的私钥
            byte[] decoded = base64Decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return new String(cipher.doFinal(inputByte));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密
     *
     * @param key   公钥/私钥
     * @param enStr 加密字符串
     * @return 明文
     */
    public static String decryptByPrivateKey(String enStr, Key key) {
        try {
            //RSA解密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] deBytes = cipher.doFinal(base64Decode(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算签名
     *
     * @param data       原始数据
     * @param privateKey RSA私钥
     * @return 签名
     */
    public static String signature(String data, String privateKey) {
        try {
            //base64编码的私钥
            byte[] decoded = base64Decode(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return base64Encode(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 验证签名
     *
     * @param data      原始数据
     * @param publicKey RSA公钥
     * @param sign      私钥签名
     * @return 是否一致
     */
    public static boolean verify(String data, String publicKey, String sign) {
        try {
            //base64编码的私钥
            byte[] decoded = base64Decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            // 验证签名是否有效
            return signature.verify(base64Decode(sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * base64 编码
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base64 解码
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     */
    private static byte[] base64Decode(String base64Code) {
        return base64Code == null || "".equals(base64Code) ? null : Base64.getDecoder().decode(base64Code);
    }

}
