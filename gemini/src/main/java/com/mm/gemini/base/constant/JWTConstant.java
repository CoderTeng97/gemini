package com.mm.gemini.base.constant;

/**
 * @author qiulong
 * @date 2019/1/17
 */
public class JWTConstant {

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;
    /**
     * 过期时间5 days
     */
    public static final long EXPIRATION_TIME = ONE_DAY * 5;

    /**
     * JWT密码
     */
    public static final String SECRET = "P@ssw02d";
    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer";
    /**
     * 存放Token的Header Key
     */
    public static final String HEADER_STRING = "Authorization";
}
