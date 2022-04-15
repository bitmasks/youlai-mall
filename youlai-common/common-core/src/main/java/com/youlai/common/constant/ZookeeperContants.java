package com.youlai.common.constant;

/**
 * @author xiezhiyan
 * 2022/03/01 15:20
 * zookeeper常量
 */
public class ZookeeperContants {

    public static final class ZookeeperPathPrefix {

    public static final String PRODUCT_SOLD_OUT = "/product_sold_out";

    public static String getZKSoldOutProductPath(Long sessionId, Long skuId) {
      return ZookeeperPathPrefix.PRODUCT_SOLD_OUT + "/" + sessionId + "/" + skuId;
    }
    }
}
