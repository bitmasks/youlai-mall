package com.youlai.mall.sms.messagehander;

import lombok.Data;

/**
 * @author xiezhiyan
 * 2022/03/01 20:42
 * 秒杀消息体
 */
@Data
public class SeckillMessage {

    private Long sessionId;
    private Long memberId;
    private Long skuId;

}
