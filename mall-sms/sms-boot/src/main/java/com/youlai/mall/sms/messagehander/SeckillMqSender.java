package com.youlai.mall.sms.messagehander;

import com.youlai.common.rabbitmq.queue.SeckillQueue;
import com.youlai.common.redis.utils.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author xiezhiyan
 * 2022/03/01 19:42
 * mq消息发送
 */
@Service
@Slf4j
@AllArgsConstructor
public class SeckillMqSender {


    private final RabbitTemplate amqpTemplate;

    private final RedisUtils redisUtils;

    public void sendSeckillMessage(String message){
        try {
            String msg = redisUtils.beanToString(message);
            log.info("mq发送订单信息：msg{}",msg);
            amqpTemplate.convertAndSend(SeckillQueue.SECKILL_QUEUE,msg);
        } catch (AmqpException e) {
            throw new AmqpException("***mq信息发送失败!***");
        }
    }

    public void send(Object message) {
        String msg = redisUtils.beanToString(message);
        log.info("mq发送订单信息：msg{}",msg);
        amqpTemplate.convertAndSend(SeckillQueue.QUEUE, msg);
    }

    public void sendTopic(Object message) {
        String msg = redisUtils.beanToString(message);
        log.info("send topic message:"+msg);
        amqpTemplate.convertAndSend(SeckillQueue.TOPIC_EXCHANGE, "topic.key1", msg+"1");
        amqpTemplate.convertAndSend(SeckillQueue.TOPIC_EXCHANGE, "topic.key2", msg+"2");
    }

}
