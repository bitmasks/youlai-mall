package com.youlai.common.redis.distributeLock.lua.manager;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 20:59
 * @description: 锁回调业务
 */
public interface ILockCallBack {
    void execute();
}
