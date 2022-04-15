package com.youlai.mall.sms.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youlai.mall.sms.mapper.SmsSeckillOrderMapper;
import com.youlai.mall.sms.pojo.domain.SmsSeckillOrder;
import com.youlai.mall.sms.service.ISmsSeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsSeckillOrderServiceImpl extends ServiceImpl<SmsSeckillOrderMapper, SmsSeckillOrder> implements ISmsSeckillOrderService {
}

