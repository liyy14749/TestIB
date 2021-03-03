package com.stock.core.common;

/**
 * @description:
 * @author: YJH
 * @time: 2020/4/5 11:52
 */
public class StatusCode {
    /**
     * 请求成功
     */
    public static final int SUCCESS = 200;
    /**
     * 服务器错误
     */
    public static final int ERROR = 500;
    /**
     * 签名错误
     */
    public static final int SIGN_ERROR = 410;
    /**
     * 参数错误
     */
    public static final int PARAM_ERROR = 401;
    /**
     * 验证码错误
     */
    public static final int VERIFY_CODE_ERROR = 402;
    /**
     * token失效，请重新登录
     */
    public static final int TOKEN_ERROR = 403;
    /**
     * 邮箱已存在，绑定邮箱时用
     */
    public static final int EMAIL_EXIST = 404;
    /**
     * 邮箱不存在，忘记密码时用
     */
    public static final int EMAIL_NOT_EXIST = 405;
    /**
     * 用户已存在
     */
    public static final int USER_EXIST = 406;
    /**
     * 密码错误
     */
    public static final int PASSWORD_ERROR = 407;
    /**
     * 订单已绑定
     */
    public static final int ORDER_BINDED = 408;
    /**
     * 票据错误
     */
    public static final int RECEIPT_ERROR = 409;
    /**
     * 资源不够
     */
    public static final int RESOURCES_NOT_ENOUGH = 411;
    /**
     * 分数重复上传
     */
    public static final int SCORE_HAS_UPLOAD = 412;
}
