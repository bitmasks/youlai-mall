package com.youlai.common.constant;

public interface RedisConstants {

    String BUSINESS_NO_PREFIX = "business_no:";

    /**
     * 优惠券码KEY前缀
     */
    String SMS_COUPON_TEMPLATE_CODE_KEY = "sms_coupon_template_code_";

    /**
     * 用户当前所有可用优惠券key
     */
    String SMS_USER_COUPON_USABLE_KEY = "sms_user_coupon_usable_";

    /**
     * 用户当前所有已使用优惠券key
     */
    String SMS_USER_COUPON_USED_KEY = "sms_user_coupon_used_";

    /**
     * 用户当前所有已过期优惠券key
     */
    String SMS_USER_COUPON_EXPIRED_KEY = "sms_user_coupon_expired_";

    /**
     * 秒杀redis-key
     */
    public static final class RedisKeyPrefix {
        /**
         * 秒杀顺序
         */
        public static final String SECKILL_ORDER = "seckill_order";
        /**
         * 秒杀排队顺序
         */
        public static final String SECKILL_ORDER_WAIT = "seckill_order_wait";
        /**
         * 秒杀校验码
         */
        public static final String SECKILL_VERIFY_CODE = "seckill_verify_code";
        /**
         * 秒杀令牌
         */
        public static final String SECKILL_ORDER_TOKEN = "seckill_order_token";
        /**
         * 秒杀场次
         */
        public static final String SECKILL_SESSION_CACHE_PREFIX = "seckill:sessions:";
        /**
         * 秒杀sku
         */
        public static final String SECKILL_SKU_CACHE_PREFIX = "seckill:skus";
        /**
         * 秒杀库存
         */
        public static final String SECKILL_SKU_SEMAPHORE = "seckill:stock:";

    }




}
