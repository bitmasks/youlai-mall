package com.youlai.mall.sms.extention;

import com.youlai.common.constant.RedisConstants;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/24 10:21
 * @description: key生成
 */
public class KeyMethod {

    /**
     * redis缓存秒杀订单
     * @param memberId
     * @param sessionId
     * @param skuId
     * @return
     */
    public static String getSecKillOrderRedisKey(Long memberId,Long sessionId, Long skuId) {
        return RedisConstants.RedisKeyPrefix.SECKILL_ORDER + "_" + memberId + "_" +sessionId+"_" + skuId;
    }

    public static String getSecKillOrderWaitFlagRedisKey(Long memberId,Long sessionId, Long skuId) {
        return RedisConstants.RedisKeyPrefix.SECKILL_ORDER_WAIT + "_" + memberId+ "_" +sessionId + "_" + skuId;
    }

    public static String getSecKillTokenRedisKey(Long memberId,Long sessionId, Long skuId) {
        return RedisConstants.RedisKeyPrefix.SECKILL_ORDER_TOKEN + "_" + memberId+ "_" +sessionId  + "_" + skuId;
    }

    public static String getSecKillVerifyCodeRedisKey(Long memberId,Long sessionId, Long skuId) {
        return RedisConstants.RedisKeyPrefix.SECKILL_VERIFY_CODE + "_" + memberId+ "_" +sessionId  + "_" + skuId;
    }
}
