package net.zhongbenshuo.attendance.utils;

/**
 * 网络请求返回码对照
 * Created at 2019/6/4 10:55
 *
 * @author LiYuliang
 * @version 1.0
 */

public class ErrorCode {

    // 请求成功
    public static final int SUCCESS = 1001;

    // 客户端持有的服务端RSA公钥过期（更新的服务端公钥并重新请求）
    public static final int KEY_RSA_SERVER_EXPIRED = 1002;

    // 服务端持有的客户端RSA公钥过期（重新登录，交换新的RSA公钥）
    public static final int KEY_RSA_CLIENT_EXPIRED = 1003;

    // RSA解密AES密钥失败
    public static final int DECRYPT_AES_KEY_FAILED = 1004;

    // AES解密请求体失败
    public static final int DECRYPT_REQUEST_BODY_FAILED = 1005;

    // AES解密返回体失败
    public static final int DECRYPT_RESPONSE_BODY_FAILED = 1015;

    // 请求体Hash值验证失败
    public static final int VERIFY_HASH_VALUE_FAILED = 1006;

    // 服务端错误
    public static final int SERVER_ERROR = 1007;

    // 服务端Aes密钥使用客户端RSA公钥加密失败
    public static final int ENCRYPT_AES_KEY_FAILED = 1008;

    // 获取请求体错误
    public static final int GET_REQUEST_BODY_FAILED = 1009;

    // 未知错误
    public static final int UNKNOWN_ERROR = 2001;

    // 请求失败
    public static final int FAIL = 1010;

    // 用户已存在（注册用户）
    public static final int USER_ALREADY_EXITS = 1012;

    // 服务端加密用户密码失败（注册用户）
    public static final int ENCRYPT_PASSWORD_FAILED = 1013;

    // 用户不存在（登录、更新用户信息）
    public static final int USER_DOES_NOT_EXITS = 1014;

}
