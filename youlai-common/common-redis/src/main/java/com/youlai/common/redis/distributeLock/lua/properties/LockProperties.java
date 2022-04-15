package com.youlai.common.redis.distributeLock.lua.properties;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 20:46
 * @description: lock配置
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁全局配置信息
 */
@Configuration
@ConfigurationProperties(prefix = "lua")
public class LockProperties {
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


}
