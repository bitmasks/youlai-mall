package com.youlai.mall.sms.pojo.domain;

import lombok.Data;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/25 17:00
 * @description: 秒杀订单表
 */
@Data
public class SmsSeckillOrder {

    private Long id;

    private Long memberId;

    private String orderId;

    private Long skuId;

    private Long sessionId;

}
