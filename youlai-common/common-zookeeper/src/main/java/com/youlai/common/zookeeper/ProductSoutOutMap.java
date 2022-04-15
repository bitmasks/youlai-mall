package com.youlai.common.zookeeper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/24 9:09
 * @description: 售完商品容器
 */
public class ProductSoutOutMap {

    public static final ConcurrentHashMap<String, Boolean> productSoldOutMap = new ConcurrentHashMap<>();

    public static  ConcurrentHashMap<String, Boolean> getProductSoldOutMap() {
        return productSoldOutMap;
    }
}
