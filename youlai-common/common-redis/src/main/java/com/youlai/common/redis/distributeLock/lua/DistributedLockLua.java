package com.youlai.common.redis.distributeLock.lua;

import cn.hutool.core.lang.Assert;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 21:02
 * @description: Lua加锁并执行业务实现工具类
 */
@Slf4j
@Component
@Setter
public class DistributedLockLua implements IDistributedLockLua {


    @Autowired
    private IBaseLockLua distributeLock;

    @Override
    public void callBack(String lockKey, LuaLockFunction callBack) {
        Assert.notNull(lockKey,"lockKey can't not be null");
        Assert.notNull(callBack,"callBack can't not be null");
        try{
            //获取锁
            String key = distributeLock.lock(lockKey);
            //执行业务
            callBack.execute();
            log.debug("加锁业务执行成功，lockKey:{}，准备释放锁",key);
        }finally{
            //释放锁
            distributeLock.unlock(lockKey);
        }
    }

    @Override
    public <T> T callBack(String lockKey, LuaLockReturnFunction<T> callBack) {
        Assert.notNull(lockKey,"lockKey can't not be null");
        Assert.notNull(callBack,"callBack can't not be null");
        try{
            //获取锁
            String key = distributeLock.lock(lockKey);
            //执行业务
            T t = callBack.execute();
            log.debug("加锁业务执行成功，lockKey:{}，准备释放锁",key);
            return t;
        }finally{
            //释放锁
            distributeLock.unlock(lockKey);
        }
    }
}
