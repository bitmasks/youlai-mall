package com.youlai.common.redis.distributeLock.lua.manager;

import cn.hutool.core.lang.Assert;
import com.youlai.common.redis.distributeLock.lua.lock.ILuaBaseLock;
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
public class LuaRedisLockManager implements IIuaLockManager {


    @Autowired
    private ILuaBaseLock distributeLock;

    @Override
    public void callBack(String lockKey, ILockCallBack callBack) {
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
    public <T> T callBack(String lockKey, IReturnCallBack<T> callBack) {
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
