package com.youlai.common.redis.distributeLock.lua;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 20:59
 * @description: 锁回调业务
 */
@FunctionalInterface
public interface LuaLockFunction {
    void execute();
}
