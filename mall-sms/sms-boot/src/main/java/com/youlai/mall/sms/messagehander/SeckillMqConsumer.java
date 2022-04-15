package com.youlai.mall.sms.messagehander;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youlai.common.constant.RedisConstants;
import com.youlai.common.constant.ZookeeperContants;
import com.youlai.common.rabbitmq.queue.SeckillQueue;
import com.youlai.common.redis.utils.RedisUtils;
import com.youlai.common.result.Result;
import com.youlai.common.result.ResultCode;
import com.youlai.common.web.exception.BizException;
import com.youlai.common.zookeeper.ProductSoutOutMap;
import com.youlai.common.zookeeper.ZkApiUtils;
import com.youlai.mall.sms.extention.KeyMethod;
import com.youlai.mall.sms.pojo.domain.SmsSeckillOrder;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSkuRelation;
import com.youlai.mall.sms.service.ISmsSeckillOrderService;
import com.youlai.mall.sms.service.ISmsSeckillService;
import com.youlai.mall.sms.service.ISmsSeckillSkuRelationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author xiezhiyan
 * 2022/03/02 9:23
 * 秒杀库存扣减消费
 */
@Service
@Slf4j
@AllArgsConstructor
public class SeckillMqConsumer {

    private final ISmsSeckillSkuRelationService smsSeckillSkuRelationService;

    private final ISmsSeckillOrderService smsSeckillOrderService;

    private final ISmsSeckillService smsSeckillService;

    private final RedisUtils redisUtils;

    private final ZkApiUtils zkApiUtils;


    @RabbitListener(queues = SeckillQueue.SECKILL_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        SeckillMessage msg = redisUtils.stringToBean(message, SeckillMessage.class);
        Long sessionId = msg.getSessionId();
        Long memberId = msg.getMemberId();
        Long skuId = msg.getSkuId();

        //查询场次商品库存关联信息
        QueryWrapper<SmsSeckillSkuRelation> skuRelationWrapper = new QueryWrapper<SmsSeckillSkuRelation>().eq("session_id", sessionId).eq("sku_id", skuId);
        SmsSeckillSkuRelation seckillSkuRelation = smsSeckillSkuRelationService.getOne(skuRelationWrapper);
        Integer stock = seckillSkuRelation.getSeckillCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到
        QueryWrapper<SmsSeckillOrder> orderWrapper = new QueryWrapper<SmsSeckillOrder>().eq("session_id", sessionId).eq("sku_id", skuId).eq("member_id", memberId);
        SmsSeckillOrder seckillOrder = smsSeckillOrderService.getOne(orderWrapper);
        if (!Objects.isNull(seckillOrder)) {
            throw new BizException(ResultCode.SEC_KILL_GOOD_EXIST);
        }
        //扣减库存 下订单 写入秒杀订单,事先本地事务处理 todo 分布式事务
        Result<SmsSeckillOrder> deductStockOrder = smsSeckillService.deductStock(sessionId, skuId, memberId);
        if (!Result.isSuccess(deductStockOrder)){
            //************************ 秒杀失败 回退操作 **************************************
            redisUtils.incr(RedisConstants.RedisKeyPrefix.SECKILL_SKU_SEMAPHORE +sessionId+ "-" + skuId,1);
            //移除内存标记
            if (ProductSoutOutMap.productSoldOutMap.get(sessionId + "-" + skuId) != null) {
                ProductSoutOutMap.productSoldOutMap.remove(sessionId + "-" + skuId);
            }
            //修改zk的商品售完标记为false
            try {
                if (zkApiUtils.exists(ZookeeperContants.ZookeeperPathPrefix.getZKSoldOutProductPath(sessionId,skuId), true) != null) {
                    zkApiUtils.updateNode(ZookeeperContants.ZookeeperPathPrefix.getZKSoldOutProductPath(sessionId,skuId), "false");
                }
            } catch (Exception e) {
                log.error("修改zk商品售完标记异常", e);
            }
            return;
        }
        //******************  如果成功则进行保存redis + flag ****************************
        String orderRedisKey = KeyMethod.getSecKillOrderRedisKey(memberId, sessionId, skuId);
        redisUtils.set(orderRedisKey,deductStockOrder.getData());
    }




}
