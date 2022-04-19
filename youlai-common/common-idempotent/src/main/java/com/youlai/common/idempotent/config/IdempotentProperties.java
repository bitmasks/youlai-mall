package com.youlai.common.idempotent.config;


import com.youlai.common.idempotent.enums.IdempotentStorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "youlai.idempontent")
@EnableConfigurationProperties
public class IdempotentProperties {

    /**
     * 一级存储类型
     * @see IdempotentStorageTypeEnum
     */
    private String firstLevelType = IdempotentStorageTypeEnum.REDIS.name();

    /**
     * 二级存储类型
     * @see IdempotentStorageTypeEnum
     */
    private String secondLevelType;

}
