package com.youlai.mall.sms.api.admin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 分布式ID服务客户端-Segment模式
 * @author : xiezhiyan
 * @date : 2022/3/5 21:38
 */
@FeignClient(name ="segment",url ="${youlai.id.segment.name:LeafSegment}")
public interface DistributedIdLeafSegmentRemoteService {
    @RequestMapping(value = "/api/segment/get/{key}")
    String getSegmentId(@PathVariable("key") String key);
}
