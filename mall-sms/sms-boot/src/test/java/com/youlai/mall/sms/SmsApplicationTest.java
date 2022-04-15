package com.youlai.mall.sms;

import com.youlai.mall.sms.api.admin.DistributedIdLeafSegmentRemoteService;
import com.youlai.mall.sms.api.admin.DistributedIdLeafSnowflakeRemoteService;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSession;
import com.youlai.mall.sms.pojo.vo.SmsSeckillSkuVO;
import com.youlai.mall.sms.service.impl.SmsSeckillServiceImpl;
import com.youlai.mall.sms.service.impl.SmsSeckillSessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

}
