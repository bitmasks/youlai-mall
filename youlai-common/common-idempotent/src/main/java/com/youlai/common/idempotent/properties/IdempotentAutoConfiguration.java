package com.youlai.common.idempotent.properties;

import com.youlai.common.idempotent.DistributedIdempotent;
import com.youlai.common.idempotent.DistributedIdempotentImpl;
import com.youlai.common.idempotent.aspect.DistributedIdempotentAspect;
import com.youlai.common.idempotent.storage.IdempotentStorageFactory;
import com.youlai.common.idempotent.storage.IdempotentStorageMongo;
import com.youlai.common.idempotent.storage.IdempotentStorageMysql;
import com.youlai.common.idempotent.storage.IdempotentStorageRedis;
import com.youlai.common.mybatis.config.MysqlDistributeLockAutoConfiguration;
import com.youlai.common.redis.config.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 幂等自动配置
 */
@Configuration
@ImportAutoConfiguration(IdempotentProperties.class)
@AutoConfigureAfter({RedissonAutoConfiguration.class, MysqlDistributeLockAutoConfiguration.class})
public class IdempotentAutoConfiguration {

    @Autowired
    private DistributedIdempotent distributedIdempotent;

    @Bean
    public DistributedIdempotent distributedIdempotent() {
        return new DistributedIdempotentImpl();
    }

    @Bean
    public DistributedIdempotentAspect distributedIdempotentAspect() {
        return new DistributedIdempotentAspect(distributedIdempotent);
    }

    @Bean
    public IdempotentStorageFactory idempotentStorageFactory() {
        return new IdempotentStorageFactory();
    }

    @ConditionalOnClass(MongoTemplate.class)
    @Configuration
    protected static class MongoTemplateConfiguration {
        @Bean
        public IdempotentStorageMongo idempotentStorageMongo() {
            return new IdempotentStorageMongo();
        }
    }


    @ConditionalOnClass(JdbcTemplate.class)
    @Configuration
    protected static class JdbcTemplateConfiguration {
        @Bean
        public IdempotentStorageMysql idempotentStorageMysql() {
            return new IdempotentStorageMysql();
        }
    }

    @Bean
    public IdempotentStorageRedis idempotentStorageRedis() {
        return new IdempotentStorageRedis();
    }
}