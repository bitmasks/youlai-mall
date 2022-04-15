package com.youlai.common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/24 9:05
 * @description: zookeeper配置
 */
@Configuration
@Slf4j
public class ZookeeperConfig {
    @Value("${zookeeper.address}")
    private String connectString;


    @Value("${zookeeper.timeout}")
    private int timeout;

    @Bean
    public ZooKeeper zkClient() {

        ZooKeeper zooKeeper = null;
        try {
            long startTime = System.currentTimeMillis();
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        System.out.println("【初始化ZooKeeper连接成功....】");
                        //如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            log.info("【初始化ZooKeeper连接状态....】={}", zooKeeper.getState());
            long endTime = System.currentTimeMillis();
            System.out.println("totalTime:" + (endTime - startTime) / 1000 + "s");
        } catch (Exception e) {
            log.error("初始化ZooKeeper连接异常....】={}", e);
        }
        return zooKeeper;
    }


    @Bean
    @ConditionalOnClass(ZooKeeper.class)
    ZkApiUtils zkApiUtils(ZooKeeper zooKeeper){
        ZkApiUtils zkApiUtils = new ZkApiUtils();
        zkApiUtils.setZkClient(zooKeeper);
        return zkApiUtils;
    }



}
