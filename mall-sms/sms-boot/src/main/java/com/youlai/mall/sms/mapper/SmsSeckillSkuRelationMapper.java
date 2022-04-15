package com.youlai.mall.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSkuRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author huawei
 * @email huawei_code@163.com
 * @date 2021/3/4
 */
@Mapper
public interface SmsSeckillSkuRelationMapper extends BaseMapper<SmsSeckillSkuRelation> {

    @Update("update sms_seckill_sku_relation set seckill_count = seckill_count - 1 where sku_id = #{skuId} and  session_id=#{sessionId} and seckill_count > 0")
    int reduceStock(Long sessionId,Long skuId);
}