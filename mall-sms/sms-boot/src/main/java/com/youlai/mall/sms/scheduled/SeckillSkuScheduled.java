package com.youlai.mall.sms.scheduled;

import com.youlai.common.redis.utils.RedissionLockUtils;
import com.youlai.mall.sms.service.ISmsSeckillService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author huawei
 * @desc 秒杀商品的定时上架：每天晚上3点，上架最近三天需要秒杀的商品
 * @email huawei_code@163.com·
 * @date 2021/3/5
 */
@Component
@Slf4j
@AllArgsConstructor
public class SeckillSkuScheduled {

    private static final String SECKILL_SKU_LATEST_3_DAY= "seckillSkuLatest3Days";

    @Autowired
    private ISmsSeckillService seckillService;

    private RedissionLockUtils redissionLockUtils;

    @Scheduled(cron = "0 * * * * ?")
    public void updateSeckillSkuLatest3Days() {
        log.info("上架秒杀最近3天商品信息");
        // 1、重复上架无需处理
        // 使用分布式锁，在分布式场景下只允许一个服务启动上架流程
        redissionLockUtils.lock(SECKILL_SKU_LATEST_3_DAY,TimeUnit.SECONDS,10);
        try {
            seckillService.updateSeckillSkuLatest3Days();
        } finally {
            redissionLockUtils.unlock(SECKILL_SKU_LATEST_3_DAY);
        }
    }
}
