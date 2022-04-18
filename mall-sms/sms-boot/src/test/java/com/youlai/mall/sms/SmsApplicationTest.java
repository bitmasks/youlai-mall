package com.youlai.mall.sms;

import com.youlai.common.idempotent.DistributedIdempotent;
import com.youlai.common.idempotent.aspect.Idempotent;
import com.youlai.common.idempotent.enums.ReadWriteTypeEnum;
import com.youlai.common.idempotent.exception.IdempotentException;
import com.youlai.mall.sms.api.admin.DistributedIdLeafSegmentRemoteService;
import com.youlai.mall.sms.api.admin.DistributedIdLeafSnowflakeRemoteService;
import com.youlai.mall.sms.handler.IdempotentHandler;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSession;
import com.youlai.mall.sms.pojo.vo.SmsSeckillSkuVO;
import com.youlai.mall.sms.service.impl.SmsSeckillServiceImpl;
import com.youlai.mall.sms.service.impl.SmsSeckillSessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xinyi
 * @desc: 营销系统测试
 * @date 2021/7/3
 */
@SpringBootTest
public class SmsApplicationTest {


    @Autowired
    private SmsSeckillServiceImpl seckillServiceImpl;

    @Autowired
    private SmsSeckillSessionServiceImpl smsSeckillSessionServiceImpl;

    @Autowired
    private DistributedIdLeafSegmentRemoteService distributedIdLeafSegmentService;

    @Autowired
    private DistributedIdLeafSnowflakeRemoteService distributedIdLeafSnowflakeService;

    @Autowired
    private DistributedIdempotent distributedIdempotent;


    @Test
    public void test1(){
        List<SmsSeckillSkuVO> seckillSession = seckillServiceImpl.getSeckillSessionSkuInfo(2l);
        System.out.println(seckillSession);
    }

    @Test
    public void test2(){
        SmsSeckillSkuVO seckillSession = seckillServiceImpl.getCurrentSessionSkuInfo(2l,186l);
        System.out.println(seckillSession);
    }

    @Test
    public void test3(){
        List<SmsSeckillSession> smsSeckillSessions = smsSeckillSessionServiceImpl.selectCurrentSeckillSession();
        System.out.println(smsSeckillSessions);
    }

    @Test
    public void test4(){
        //分布式id-leaf
        String segmentId = distributedIdLeafSegmentService.getSegmentId("test");
        System.out.println(segmentId);
        String snowflakeId = distributedIdLeafSnowflakeService.getSnowflakeId("test");
        System.out.println(snowflakeId);
    }

    /**
     * 代码方式幂等-无返回值
     */
    @Test
    public void test5(){
        distributedIdempotent.execute("test5", 10, 10, 50, TimeUnit.SECONDS, () -> {
            System.out.println("进来了。。。。"+Thread.currentThread().getName());
        }, () -> {
            System.out.println("重复了。。。。"+Thread.currentThread().getName());
        });
    }

    /**
     * 代码方式幂等-有返回值
     * @return
     */
    @Test
    public void test6() {
        String s = distributedIdempotent.execute("test6", 10, 10, 50, TimeUnit.SECONDS, ReadWriteTypeEnum.ORDER, () -> {
            System.out.println("进来了。。。。");
            return "success";
        }, () -> {
            System.out.println("重复了。。。。");
            return "fail";
        });
        System.out.println(s);
    }

    /**
     * 幂等只提供短时间内重复请求的防止幂等操作
     * 核心业务还是要在数据表中做幂等
     * 幂等的二级存储还没实现，理论上二级存储支持更长的时间存储，防止超长时间的幂等，能在业务中直接做的建议在业务中，比如订单的支付，根据支付记录做
     */
    @Test
    @Idempotent(idempotentHandler = "idempotentHandler", idempotentHandlerClass = IdempotentHandler.class)
    public void test7() {
        System.out.println("进来了。。。。");
    }

    /**
     * 注解方式幂等-指定幂等规则触发后执行的方法
     * @param key
     */
    @Test
    @Idempotent(spelKey = "#key", idempotentHandler = "idempotentHandler", readWriteType = ReadWriteTypeEnum.PARALLEL, secondLevelExpireTime = 60)
    public void test8(String key) {
        System.out.println("进来了。。。。");
    }

    public void idempotentHandler(String key, IdempotentException e) {
        System.out.println(key + ":idempotentHandler已经执行过了。。。。");
    }

}
