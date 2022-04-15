package com.youlai.mall.sms.controller.app;

import com.youlai.common.result.Result;
import com.youlai.mall.sms.service.ISmsSeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author huawei
 * @desc 秒杀活动管理
 * @email huawei_code@163.com
 * @date 2021/3/7
 */
@Api(tags = "「移动端」秒杀活动管理")
@RestController("APPSeckillController")
@RequestMapping("/api-app/v1/seckill")
@Slf4j
public class SeckillController {

    @Autowired
    private  ISmsSeckillService seckillService;

    /**
     * 系统初始化商品，提前预热三天秒杀场次信息
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        seckillService.updateSeckillSkuLatest3Days();
    }


    @ApiOperation(value = "秒杀")
    @PostMapping("/seckill")
    public Result seckill(Long umsMemberId,String skuId) {







        return null;
    }
}
