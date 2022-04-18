package com.youlai.common.redis.config;

import com.youlai.common.redis.distributeLock.lua.DistributedLockLua;
import com.youlai.common.redis.distributeLock.lua.IBaseLockLua;
import com.youlai.common.redis.distributeLock.lua.IDistributedLockLua;
import com.youlai.common.redis.utils.DistributedLockLuaUtils;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 分布式锁全局配置信息
 */
@Configuration
@ConfigurationProperties(prefix = "lua")
@AutoConfigureBefore(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
public class LuaLockAutoConfiguration {
    /**
     * 锁key的前缀
     */
    private String lockPre = "";
    /**
     * 单位s，加锁操作持有锁的最大时间
     */
    private int expiredTime = 30;
    /**
     * 获取锁的重试次数
     */
    private int retryCount = 2;

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getLockPre() {
        return lockPre;
    }

    public void setLockPre(String lockPre) {
        this.lockPre = lockPre;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    IBaseLockLua luaDistributedLock(LuaLockAutoConfiguration lockProperties){
        LuaDistributedLockInitialize luaDistributedLock = new LuaDistributedLockInitialize();

        //lock script
        DefaultRedisScript<Long> lockScript = new DefaultRedisScript<Long>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/lock.lua")));
        lockScript.setResultType(Long.class);

        //unlock script
        DefaultRedisScript<Long>  unlockScript = new DefaultRedisScript<Long>();
        unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/unlock.lua")));
        unlockScript.setResultType(Long.class);

        luaDistributedLock.setLockScript(lockScript);
        luaDistributedLock.setUnlockScript(unlockScript);
        luaDistributedLock.setLockProperties(lockProperties);
        return luaDistributedLock;
    }

    @Bean
    IDistributedLockLua distributedLockLua(IBaseLockLua luaBaseLock){
        DistributedLockLua distributedLockLua = new DistributedLockLua();
        distributedLockLua.setDistributeLock(luaBaseLock);
        return distributedLockLua;
    }

    @Bean
    @ConditionalOnClass(IDistributedLockLua.class)
    DistributedLockLuaUtils luaLockUtils(IDistributedLockLua distributedLockLua){
        DistributedLockLuaUtils luaLockUtils = new DistributedLockLuaUtils();
        luaLockUtils.setLocker(distributedLockLua);
        return luaLockUtils;
    }
}
