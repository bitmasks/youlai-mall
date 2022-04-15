package com.youlai.mall.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youlai.mall.sms.pojo.domain.SmsSeckillOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiezhiyan
 * 2022/02/26 10:00
 * 秒杀成功订单mapper
 */
@Mapper
public interface SmsSeckillOrderMapper extends BaseMapper<SmsSeckillOrder> {
}
