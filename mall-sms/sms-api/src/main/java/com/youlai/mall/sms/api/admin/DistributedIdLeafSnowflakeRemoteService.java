package com.youlai.mall.sms.api.admin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *分布式ID服务客户端-Snowflake模式
 * @author : xiezhiyan
 * @date : 2022/3/5 21:38
 */
@FeignClient(name ="snowflake",url ="${youlai.id.snowflake.name:LeafSnowflake}")
public interface DistributedIdLeafSnowflakeRemoteService {
    @RequestMapping(value = "/api/snowflake/get/{key}")
    String getSnowflakeId(@PathVariable("key") String key);
}
