package com.youlai.common.redis.distributeLock.lua.manager;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 21:00
 * @description: 带返回值回调
 */
public interface IReturnCallBack<T> {
    T execute();
}
