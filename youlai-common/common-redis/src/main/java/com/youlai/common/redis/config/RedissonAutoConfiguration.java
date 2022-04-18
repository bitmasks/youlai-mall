package com.youlai.common.redis.config;

import cn.hutool.core.util.StrUtil;
import com.youlai.common.redis.distributeLock.redission.DistributedLockRedisson;
import com.youlai.common.redis.distributeLock.redission.IDistributedLockRedisson;
import com.youlai.common.redis.utils.DistributedLockRedissonUtils;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 分布式锁 Redisson 配置
 *
 * @author huawei
 * @email huawei_code@163.com
 * @date 2021/2/22
 */

@Configuration
@ConditionalOnProperty(prefix = "redisson",name = "address")
@ConfigurationProperties(prefix = "redisson")
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedissonAutoConfiguration {

    @Setter
    private String address;
    @Setter
    private String password;
    @Setter
    private Integer database;
    /**
     * 默认最小空闲连接数
     */
    @Setter
    private Integer minIdle;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(address);
        singleServerConfig.setDatabase(database);
        singleServerConfig.setConnectionMinimumIdleSize(minIdle);
        if (StrUtil.isNotBlank(password)) {
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     * @return
     */


    @Bean("distributedLockRedisson")
    @Primary
    @ConditionalOnClass(RedissonClient.class)
    IDistributedLockRedisson redissonDistributedLocker(RedissonClient redissonClient) {
        return new DistributedLockRedisson(redissonClient);
    }

    /**
     * 装配IRedissionLocker类，并将实例注入到RedissionLockUtils中
     * @return
     */
    @Bean
    @ConditionalOnClass(IDistributedLockRedisson.class)
    DistributedLockRedissonUtils redissionLockUtils(IDistributedLockRedisson distributedLockRedisson){
        DistributedLockRedissonUtils redissionLockUtils = new DistributedLockRedissonUtils();
        redissionLockUtils.setLocker(distributedLockRedisson);
        return redissionLockUtils;
    }



}
