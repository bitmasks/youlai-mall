package com.youlai.mall.sms.service;

import com.youlai.common.result.Result;
import com.youlai.mall.sms.pojo.domain.SmsSeckillOrder;
import com.youlai.mall.sms.pojo.vo.SmsSeckillSkuVO;

import java.util.List;

/**
 * @author huawei
 * @desc 秒杀模块业务接口
 * @email huawei_code@163.com
 * @date 2021/3/5
 */
public interface ISmsSeckillService {

    /**
     *系统初始化商品预热失败
     */
    void updateSeckillSkuLatest3Days();

    /**
     * 根据活动id获取秒杀活动商品列表
     * @return
     */
    List<SmsSeckillSkuVO> getSeckillSessionSkuInfo(Long seckillSessionId);

    /**
     * 获取当前时间正在进行的秒杀活动的商品
     * @return
     */
    SmsSeckillSkuVO getCurrentSessionSkuInfo(Long skuId, Long sessionId);

    /**
     * 扣减缓存库存
     * @param skuid
     * @return
     */
    Result<Boolean> deductStockCache( Long sessionId,Long skuid);

    /**
     * 扣减实际库存,并写入秒杀订单
     * @param sessionId
     * @param skuid
     * @return
     */
    Result<SmsSeckillOrder> deductStock(Long sessionId, Long skuid, Long memberId);

    /**
     * 秒杀单个商品 redis+lua实现
     * @param skuId 秒杀商品ID
     * @param memberId 用户ID
     * @return
     */
    Result startSeckilRedisLock(Long skuId, Long memberId,Long sessionId);

    /**
     * 秒杀单个商品 zookeeper实现
     * @param skuId 秒杀商品ID
     * @param memberId 用户ID
     * @param sessionId 场次ID
     * @return
     */
    Result startSeckilZookeeperLock(Long skuId, Long memberId,Long sessionId);

}
