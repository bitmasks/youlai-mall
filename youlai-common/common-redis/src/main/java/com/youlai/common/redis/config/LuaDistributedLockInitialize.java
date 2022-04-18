package com.youlai.common.redis.config;

import com.youlai.common.redis.distributeLock.lua.IBaseLockLua;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 20:41
 * @description: lua分布式锁基础工具类
 */

@Slf4j
@Component
@Setter
public class LuaDistributedLockInitialize implements IBaseLockLua, InitializingBean {


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private LuaLockAutoConfiguration lockProperties;
    /**
     * 锁脚本
     */
    private DefaultRedisScript<Long> lockScript;
    /**
     * 解锁脚本
     */
    private DefaultRedisScript<Long> unlockScript;

    private ThreadLocal<String> threadKeyId = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return UUID.randomUUID().toString().replace("-", "");
        }
    };

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }
    /**
     * 生成操作redis的lua脚本操作实例
     */
    private void initialize() {
        //lock script
        lockScript = new DefaultRedisScript<Long>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/lock.lua")));
        lockScript.setResultType(Long.class);
        log.debug("初始化加锁脚本成功:\n:{}",lockScript.getScriptAsString());

        //unlock script
        unlockScript = new DefaultRedisScript<Long>();
        unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/unlock.lua")));
        unlockScript.setResultType(Long.class);
        log.debug("初始化释放锁脚本成功:\n:{}",unlockScript.getScriptAsString());
    }

    @Override
    public String lock(String lock) {
        Assert.notNull(lock, "lock can't be null");
        String key = getLockKey(lock);
        List<String> keyList = new ArrayList<String>();
        keyList.add(key);
        keyList.add(threadKeyId.get());
        /**
         * 获取锁次数
         */
        int lockTryCount = 0;
        log.debug("加锁成功，lockKey:{},准备执行业务操作",key);
        while (true) {
            if (lockProperties.getRetryCount() > 0 && lockTryCount > lockProperties.getRetryCount()) {
                /**
                 * 加锁失败超过设定重试次数，抛出异常表示加锁失败
                 */
                throw new RuntimeException("access to distributed lock more than retries："+lockProperties.getRetryCount());
            }
            if(lockTryCount>0){
                log.debug("重试获取锁:{}操作:{}次",key,lockTryCount);
            }
            if (redisTemplate.execute(lockScript, keyList, String.valueOf(lockProperties.getExpiredTime() * 1000)) > 0) {
                /**
                 * 返回结果大于0，表示加锁成功
                 */
                log.debug("加锁成功，lockKey:{},准备执行业务操作",key);
                return key;
            } else {
                try {
                    Thread.sleep(10, (int) (Math.random() * 500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            lockTryCount++;
        }
    }

    @Override
    public String unlock(String lock) {
        final String lockKey = getLockKey(lock);
        List<String> keyList = new ArrayList<String>();
        keyList.add(lockKey);
        keyList.add(threadKeyId.get());
        redisTemplate.execute(unlockScript, keyList);
        log.debug("成功释放锁，lockKey:{}",lockKey);
        return lockKey;
    }

    /**
     * 生成key
     * @param lock
     * @return
     */
    private String getLockKey(String lock) {
        if(StringUtils.isBlank(lockProperties.getLockPre())){
            return lock;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(lockProperties.getLockPre()).append(":").append(lock);
        return sb.toString();
    }

}