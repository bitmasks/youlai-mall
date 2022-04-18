package com.youlai.common.mybatis.config;

import com.youlai.common.mybatis.lock.DistributedLockMysql;
import com.youlai.common.mybatis.lock.IDistributedLockMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author xiezhiyan
 * 2022/04/18 17:08
 */
@Configuration
public class MysqlDistributeLockAutoConfiguration {

    @Autowired(required = false)
    private DataSource dataSource;

    @Bean("distributedLockMysql")
    public IDistributedLockMysql distributedLockMysql() {
        return new DistributedLockMysql(dataSource);
    }

    @Bean("distributedLockMysqlUtils")
    @ConditionalOnClass(IDistributedLockMysql.class)
    public DistributedLockMysqlUtils distributedLockMysqlUtils(IDistributedLockMysql distributedLockMysql) {
        DistributedLockMysqlUtils distributedLockMysqlUtils = new DistributedLockMysqlUtils();
        distributedLockMysqlUtils.setLocker(distributedLockMysql);
        return distributedLockMysqlUtils;
    }
}
