package com.youlai.common.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.youlai.common.mybatis.lock.DistributedLockMysql;
import com.youlai.common.mybatis.lock.IDistributedLockMysql;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiezhiyan
 * 2022/04/18 17:08
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MysqlDistributeLockAutoConfiguration {

    @Bean("distributedLockMysql")
    @ConditionalOnClass(DruidDataSource.class)
    public IDistributedLockMysql distributedLockMysql() {
        return new DistributedLockMysql();
    }

    @Bean("distributedLockMysqlUtils")
    @ConditionalOnClass(IDistributedLockMysql.class)
    public DistributedLockMysqlUtils distributedLockMysqlUtils() {
        DistributedLockMysqlUtils distributedLockMysqlUtils = new DistributedLockMysqlUtils();
        return distributedLockMysqlUtils;
    }
}
