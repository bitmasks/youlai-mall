package com.youlai.common.redis.utils;

import com.youlai.common.redis.distributeLock.lua.IDistributedLockLua;
import com.youlai.common.redis.distributeLock.lua.LuaLockFunction;
import com.youlai.common.redis.distributeLock.lua.LuaLockReturnFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiezhiyan
 * 2022/04/18 16:37
 */
@Component
public class DistributedLockLuaUtils {

    @Autowired
    private IDistributedLockLua distributedLockLua;


    public void setLocker(IDistributedLockLua locker) {
        distributedLockLua = locker;
    }

    /**
     * 无返回值回调
     * @param lockKey
     * @param callBack
     */
    public void callBack(String lockKey, LuaLockFunction callBack) {
        distributedLockLua.callBack(lockKey,callBack);
    }


    /**
     * 有返回值回调
     * @param lockKey
     * @param callBack
     * @return
     * @param <T>
     */
    public <T> T callBack(String lockKey, LuaLockReturnFunction<T> callBack) {
      return  distributedLockLua.callBack(lockKey,callBack);
    }


}
