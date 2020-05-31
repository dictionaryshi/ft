package com.ft.br.constant;

/**
 * RedisKey
 *
 * @author shichunyang
 */
public class RedisKey {
    private RedisKey() {
    }

    /**
     * 图片验证码redis key
     */
    public static final String REDIS_VERIFICATION_CODE = "ft_br_verification_code";

    /**
     * 登录token redis key
     */
    public static final String REDIS_LOGIN_TOKEN = "ft_br_redis_token";

    public static final String REDIS_SSO_LOGIN_LOCK = "ft_br_sso_login_lock";

    public static final String REDIS_GOODS_ADD_LOCK = "ft_br_goods_add_lock";

    public static final String REDIS_GOODS_UPDATE_LOCK = "ft_br_goods_update_lock";

    public static final String REDIS_ORDER_UPDATE_LOCK = "ft_br_order_update_lock";

    public static final String REDIS_ITEM_ADD_LOCK = "ft_br_item_add_lock";
}
