package com.youlai.common.redis.distributeLock.lua;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 21:00
 * @description: 带返回值回调
 */
@FunctionalInterface
public interface LuaLockReturnFunction<T> {
    T execute();
}
