package com.youlai.mall.sms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.youlai.common.constant.RedisConstants;
import com.youlai.common.constant.ZookeeperContants;
import com.youlai.common.redis.utils.RedisUtils;
import com.youlai.common.redis.utils.DistributedLockRedissonUtils;
import com.youlai.common.result.Result;
import com.youlai.common.result.ResultCode;
import com.youlai.common.web.exception.BizException;
import com.youlai.common.zookeeper.ProductSoutOutMap;
import com.youlai.common.zookeeper.SoutOutWatcher;
import com.youlai.common.zookeeper.ZkApiUtils;
import com.youlai.mall.sms.extention.KeyMethod;
import com.youlai.mall.sms.messagehander.SeckillMessage;
import com.youlai.mall.sms.messagehander.SeckillMqSender;
import com.youlai.mall.sms.pojo.domain.SmsSeckillOrder;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSession;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSkuRelation;
import com.youlai.mall.sms.pojo.to.SeckillSkuRedisTO;
import com.youlai.mall.sms.pojo.vo.SmsSeckillSkuVO;
import com.youlai.mall.sms.service.ISmsSeckillOrderService;
import com.youlai.mall.sms.service.ISmsSeckillService;
import com.youlai.mall.sms.service.ISmsSeckillSessionService;
import com.youlai.mall.sms.service.ISmsSeckillSkuRelationService;
import com.youlai.mall.sms.util.BeanMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author huawei
 * @desc 秒杀模块业务实现类
 * @email huawei_code@163.com
 * @date 2021/3/5
 */
@Service
@Slf4j
@AllArgsConstructor
public class SmsSeckillServiceImpl implements ISmsSeckillService {


    public static final int LOCK_EXPIRE = 120;
    public static final String FALSE = "false";

    private final ISmsSeckillSessionService seckillSessionService;

    private final ISmsSeckillSkuRelationService seckillSkuRelationService;

    private final ISmsSeckillOrderService seckillOrderService;

    private final RedisUtils redisUtils;

    private final DistributedLockRedissonUtils redissionLockUtils;

    private final ZkApiUtils zkApiUtils;

    private final SeckillMqSender seckillMQSenders;


    @Override
    public void updateSeckillSkuLatest3Days() {
        // 1、获取未来三天即将开始秒杀的活动
        DateTime startTime = DateUtil.beginOfDay(new Date());
        DateTime endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), 3));
        List<SmsSeckillSession> seckillSessions = seckillSessionService.selectByTime(startTime, endTime);
        if (CollectionUtil.isEmpty(seckillSessions)) {
            log.info("秒杀活动列表为空，startTime={}，endTime={}", startTime, endTime);
            return;
        }
        seckillSessions = seckillSessions.stream().peek(session -> {
            List<SmsSeckillSkuRelation> relations = seckillSkuRelationService.selectBySessionId(session.getId());
            session.setRelations(relations);
        }).collect(Collectors.toList());
        // 2、将秒杀信息缓存到redis中
        saveSessionInfos(seckillSessions);
        saveSessionSkuInfos(seckillSessions);
    }

    @Override
    public List<SmsSeckillSkuVO> getSeckillSessionSkuInfo(Long seckillSessionId) {
        //正则表达式从redis获取活动信息
        Set<String> listkeys = redisUtils.keys(RedisConstants.RedisKeyPrefix.SECKILL_SESSION_CACHE_PREFIX  + seckillSessionId + "*");
        if (CollectionUtil.isEmpty(listkeys)){
            return Lists.newArrayList();
        }
        ArrayList<SmsSeckillSkuVO> list = Lists.newArrayList();
        listkeys.forEach(key -> {
            ArrayList<String> relations= (ArrayList<String>) redisUtils.lGetIndex(key, 0);
            relations.forEach(val->{
                Object skuRedisTO = redisUtils.hget(RedisConstants.RedisKeyPrefix.SECKILL_SKU_CACHE_PREFIX, val);
                SmsSeckillSkuVO skuinfo= BeanMapperUtils.map(skuRedisTO,SmsSeckillSkuVO.class);
                list.add(skuinfo);
            });
        });
        return list;
    }

    @Override
    public SmsSeckillSkuVO getCurrentSessionSkuInfo(Long skuId,Long sessionId) {
        Object skuRedisTO = redisUtils.hget(RedisConstants.RedisKeyPrefix.SECKILL_SKU_CACHE_PREFIX, skuId + "" + sessionId);
        return BeanMapperUtils.map(skuRedisTO,SmsSeckillSkuVO.class);
    }

    /**
     * 缓存活动信息
     * list(seckill:sessions:1_16555565_165888888  sessionId-skuid)
     */
    private void saveSessionInfos(List<SmsSeckillSession> seckillSessions) {
        seckillSessions.forEach(session -> {
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            String key = RedisConstants.RedisKeyPrefix.SECKILL_SESSION_CACHE_PREFIX + session.getId() + "_" + startTime + "_" + endTime;
            List<String> relations = session.getRelations().stream().map(sku -> sku.getSessionId() + "-" + sku.getSkuId()).collect(Collectors.toList());
            if (!redisUtils.hasKey(key)) {
                redisUtils.lSet(key, relations);
            }
        });
    }

    /**
     * 缓存商品信息
     * hash(seckill:skus,sessionId_skuid,skuinfo)
     * @param seckillSessions
     */
    private void saveSessionSkuInfos(List<SmsSeckillSession> seckillSessions) {
        seckillSessions.forEach(session -> {
            List<SmsSeckillSkuRelation> relations = session.getRelations();
            if (CollectionUtil.isNotEmpty(relations)) {
                relations.forEach(sku -> {
                    String randomCode = RandomUtil.randomNumbers(8);
                    // 4、保存商品信息到redis中
                    if (!redisUtils.hHasKey(RedisConstants.RedisKeyPrefix.SECKILL_SKU_CACHE_PREFIX, sku.getSessionId() + "-" + sku.getSkuId())) {
                        SeckillSkuRedisTO skuRedisTO = BeanMapperUtils.map(sku, SeckillSkuRedisTO.class);
                        /**
                         * 1、TODO sku基本信息
                         *  Long skuId = sku.getSkuId();
                         *  Result<SkuDTO> skuInfo = productFeignService.getSkuById(skuId);
                         *  if (skuInfo != null && skuInfo.getCode().equals(ResultCode.SUCCESS.getCode()) && skuInfo.getData() != null) {
                         *      skuRedisTO.setSkuInfo(skuInfo.getData());
                         *  } else {
                         *      log.error("根据商品ID获取详情详情失败，skuId={}，data={}", skuId, skuInfo);
                         *  }
                         */
                        //2、sku秒杀信息
                        skuRedisTO.setStartTime(session.getStartTime().getTime());
                        skuRedisTO.setEndTime(session.getEndTime().getTime());
                        //3、随机码
                        skuRedisTO.setRandomCode(randomCode);
                        //4.存入商品信息redis (seckill:skus,sesssionId_skuid,info)
                        redisUtils.hset(RedisConstants.RedisKeyPrefix.SECKILL_SKU_CACHE_PREFIX, sku.getSessionId() + "-" + sku.getSkuId(), skuRedisTO);
                        //5. 设置秒杀库存
                        //5-1.存入活动商品库存
                        redisUtils.lSet(RedisConstants.RedisKeyPrefix.SECKILL_SKU_SEMAPHORE +sku.getSessionId() + "-" + sku.getSkuId(), skuRedisTO.getSeckillCount());
                        //5-2.设置秒杀库存信号量
                        // RSemaphore 虽然可以做分布式信号量，但还是有一个致命问题，一旦意外服务中断，重启，锁就不会自动释放，特别是对于任务时间不确定的情况下，又需要控制全局并发量，该问题就尤为突出
                        //RSemaphore semaphore = redissionLockUtils.getSemaphore(RedisConstants.RedisKeyPrefix.SECKILL_SKU_SEMAPHORE +sku.getSessionId() + "-" + sku.getSkuId());
                        //semaphore.trySetPermits(skuRedisTO.getSeckillCount());
                    }
                });
            }
        });
    }


    @Override
    public Result startSeckilRedisLock(Long skuId, Long memberId,Long sessionId) {
        boolean res=false;
        try {
            //加锁
            res=redissionLockUtils.tryLock(skuId+"",TimeUnit.SECONDS,3, 20);
            if(res){
                //扣减库存
                Result<Boolean> result = deductStockCache(sessionId, skuId);
                if( Result.isSuccess(result)){
                    return result;
                }
            }else{
                return Result.failed("秒杀结束");
            }
        } catch (Exception e) {
            log.warn("秒杀失败，sku={}，member={}", skuId, memberId);
            throw new BizException("秒杀失败");
        } finally {
            if(res){
                //释放锁
                redissionLockUtils.unlock(skuId+"");
            }
        }
        return null;
    }

    @Override
    public Result startSeckilZookeeperLock(Long skuId, Long memberId,Long sessionId) {
        //1.校验秒杀路径path

        //2.商品秒杀结束标记map,内存标记 相比用redis里的库存来判断减少了与redis的交互次数所有涉及秒杀的接口首先判断是否秒杀结束，如果结束，直接返回key: 1-2
        if (ProductSoutOutMap.productSoldOutMap.get(sessionId + "-" + skuId) != null) {
            return Result.failed(
                    ResultCode.SEC_KILL_LOCAL_GOODS_NO, ResultCode.SEC_KILL_LOCAL_GOODS_NO.getMsg());
        }
        //3.设置排队标记，超时时间根据业务情况决定，类似分布式锁 返回排队中，获取订单key，setNx排队进入,持有锁的用户，只有释放锁之后，后面的线程才能抢占锁，继续后面流程
        String orderRedisKey = KeyMethod.getSecKillOrderRedisKey(memberId, sessionId, skuId);
        if (!redisUtils.setNx(orderRedisKey, LOCK_EXPIRE)) {
            return Result.failed(ResultCode.SEC_KILL_ING, ResultCode.SEC_KILL_ING.getMsg());
        }
        //4.校验时间 防止刷时间，从缓存获取当前正在进行秒杀的货品信息，比较系统时间和活动开始时间
        SmsSeckillSkuVO currentSessionSkuInfo = getCurrentSessionSkuInfo(skuId,sessionId);
        if(Objects.isNull(currentSessionSkuInfo)){
            return Result.failed(ResultCode.SEC_KILL_NOT_CURRENT, ResultCode.SEC_KILL_NOT_CURRENT.getMsg());
        }
        //5.校验是否已经秒杀到
        QueryWrapper<SmsSeckillOrder> queryWrapper = new QueryWrapper<SmsSeckillOrder>().eq("member_id", memberId).eq("sku_id", skuId).eq("session_id", sessionId);
        SmsSeckillOrder seckillOrder = seckillOrderService.getOne(queryWrapper);
        if (!Objects.isNull(seckillOrder)) {
            return Result.failed(ResultCode.REPEATE_SEC_KILL, ResultCode.REPEATE_SEC_KILL.getMsg());
        }
        //6.扣减redis库存 +  ZK 内存级别标识
        Result<Boolean> result = deductStockCache(sessionId, skuId);
        if(!Result.isSuccess(result)){
            return Result.failed(ResultCode.SEC_KILL_FAIL, result.getMsg());
        }
        //7.扣减数据库库存：使用mq消息异步处理，放入队列扣除实际库存
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setSessionId(sessionId);
        seckillMessage.setSkuId(skuId);
        seckillMessage.setMemberId(memberId);
        seckillMQSenders.send(seckillMessage);
        return Result.success(ResultCode.SEC_KILL_ING);
    }

    /**
     * redis缓存扣减库存，redis信号量限定共享库存数
     * @param sessionId
     * @param skuId
     * @return
     */
    @Override
    public Result<Boolean> deductStockCache(Long sessionId,Long skuId) {
        //1.0 扣减该场次该商品的库存
        try {
            Long stock = redisUtils.decr(RedisConstants.RedisKeyPrefix.SECKILL_SKU_SEMAPHORE +sessionId+ "-" + skuId,1);
            if (null == stock) {
                log.error("***数据还未准备好***");
                return Result.failed(ResultCode.SEC_KILL_DEDUCT_FAIL, ResultCode.SEC_KILL_DEDUCT_FAIL.getMsg());
            }
            if (stock < 0) {
                log.info("***stock 扣减减少*** stock:{}",stock);
                redisUtils.incr(RedisConstants.RedisKeyPrefix.SECKILL_SKU_SEMAPHORE +sessionId+ "-" + skuId,1);
                //内存标记该场次该商品已秒杀完
                ProductSoutOutMap.productSoldOutMap.put(sessionId + "-" + skuId, true);
                //创建秒杀结束根节点： /product_sold_out
                if (zkApiUtils.exists(ZookeeperContants.ZookeeperPathPrefix.PRODUCT_SOLD_OUT, false) == null) {
                    zkApiUtils.createNode(ZookeeperContants.ZookeeperPathPrefix.PRODUCT_SOLD_OUT,"");
                }
                //没有节点,创建对应场次商品持久化节点： /product_sold_out/1-2
                if (zkApiUtils.exists(ZookeeperContants.ZookeeperPathPrefix.getZKSoldOutProductPath(sessionId,skuId), true) == null) {
                    zkApiUtils.createNode(ZookeeperContants.ZookeeperPathPrefix.getZKSoldOutProductPath(sessionId,skuId), "true");
                }
                // zookeeper监听机制，客户端注册监听它关心的目录节点，当目录节点发生变化（数据改变、被删除、子目录节点增加删除）等，zookeeper会通知客户端
                // 监听指定场次商品节点变动，获取该节点值，不存在就更新该节点的已秒杀完
                if (FALSE.equals(zkApiUtils.getData(ZookeeperContants.ZookeeperPathPrefix.getZKSoldOutProductPath(sessionId, skuId), new SoutOutWatcher()))) {
                    //更新售完标记节点
                    zkApiUtils.updateNode(ZookeeperContants.ZookeeperPathPrefix.getZKSoldOutProductPath(sessionId,skuId), "true");
                    //判断指定节点是否存在
                    zkApiUtils.exists(ZookeeperContants.ZookeeperPathPrefix.getZKSoldOutProductPath(sessionId,skuId), true);
                }
            }
        } catch (Exception e) {
            return Result.failed(ResultCode.SEC_KILL_OVER, ResultCode.SEC_KILL_OVER.getMsg());
        }
        return Result.success();
    }

    /**
     * 实际库存扣减，写入成功订单
     * @param sessionId
     * @param skuid
     * @param memberId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<SmsSeckillOrder> deductStock(Long sessionId, Long skuid,Long memberId) {
        try {
            //扣减实际库存
            Result<Boolean> booleanResult = seckillSkuRelationService.reduceStock(sessionId, skuid);
            if(!Result.isSuccess(booleanResult)){
                return Result.failed(ResultCode.SEC_KILL_REDUCE_FAIL);
            }
            //写入秒杀订单
            SmsSeckillOrder smsSeckillOrder = new SmsSeckillOrder();
            smsSeckillOrder.setSessionId(sessionId);
            smsSeckillOrder.setMemberId(memberId);
            smsSeckillOrder.setSkuId(skuid);
            smsSeckillOrder.setOrderId(UuidUtils.generateUuid());
            seckillOrderService.save(smsSeckillOrder);
            return Result.success(smsSeckillOrder);
        } catch (Exception e) {
            log.error("***秒杀下订单失败*** error:{}",e);
            throw new BizException(ResultCode.SEC_KILL_FAIL);
        }
    }

}
