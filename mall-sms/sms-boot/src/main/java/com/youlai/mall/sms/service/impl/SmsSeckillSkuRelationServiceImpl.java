package com.youlai.mall.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youlai.common.result.Result;
import com.youlai.common.result.ResultCode;
import com.youlai.mall.sms.mapper.SmsSeckillSkuRelationMapper;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSkuRelation;
import com.youlai.mall.sms.service.ISmsSeckillSkuRelationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huawei
 * @desc 秒杀活动场次商品关联业务实现类
 * @email huawei_code@163.com
 * @date 2021/3/5
 */
@Service
@Slf4j
@AllArgsConstructor
public class SmsSeckillSkuRelationServiceImpl extends ServiceImpl<SmsSeckillSkuRelationMapper, SmsSeckillSkuRelation> implements ISmsSeckillSkuRelationService {

    private final SmsSeckillSkuRelationMapper smsSeckillSkuRelationMapper;

    @Override
    public List<SmsSeckillSkuRelation> selectBySessionId(Long sessionId) {
        log.info("根据秒杀活动场次ID查询关联商品列表，sessionId={}",sessionId);
        QueryWrapper<SmsSeckillSkuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id",sessionId);
        return this.list(queryWrapper);
    }

    @Override
    public Result<Boolean> reduceStock(Long sessionId, Long skuId) {
        try {
            log.info("***秒杀扣减实际库存开始!");
            Boolean reduceResult = smsSeckillSkuRelationMapper.reduceStock(sessionId, skuId)>0;
            if(reduceResult==false){
                log.error(" *****秒杀扣减实际库存发生错误*****");
                return Result.failed(ResultCode.DATA_NOT_EXISTS);
            }
            log.info("***秒杀扣减实际库存成功!");
            return Result.success(reduceResult);
        } catch (Exception e) {
            log.error(" *****秒杀扣减实际库存发生错误***** error:{}",e);
            return Result.failed(ResultCode.DATA_NOT_EXISTS);
        }
    }
}
