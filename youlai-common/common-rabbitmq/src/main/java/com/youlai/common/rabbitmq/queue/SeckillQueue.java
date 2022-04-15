package com.youlai.common.rabbitmq.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiezhiyan
 * 2022/03/01 20:25
 * 秒杀队列
 */
@Component
@ConditionalOnProperty(prefix = "spring.application.name", value = "mall-sms")
@Slf4j
public class SeckillQueue {

    /**
     * 队列
     */
    public static final String SECKILL_QUEUE = "seckill.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE = "topic.queue";
    public static final String HEADER_QUEUE = "header.queue";
    /**
     * 交换机
     */
    public static final String TOPIC_EXCHANGE = "topicExchage";
    public static final String FANOUT_EXCHANGE = "fanoutxchage";
    public static final String HEADERS_EXCHANGE = "headersExchage";
    /**
     * 路由
     */
    public static final String TOPIC_KEY_1 = "topic.key1";
    public static final String TOPIC = "topic.#";

    /**
     * 自定义路由键值对
     */
    public static final String HEADER_1 = "header1";
    public static final String VALUE_1 = "value1";
    public static final String HEADER_2 = "header2";
    public static final String VALUE_2 = "value2";


    /**
     * Topic模式 交换机Exchange
     */
    @Bean
    public TopicExchange topicExchage(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * SECKILL_QUEUE，TOPIC_QUEUE绑定到topic交换机
     * @return
     */
    @Bean
    public Binding seckillBindingTopic() {
        return BindingBuilder.bind(new Queue(SECKILL_QUEUE, true)).to(new TopicExchange(TOPIC_EXCHANGE)).with(TOPIC_KEY_1);
    }
    @Bean
    public Binding topicBindingTopic() {
        return BindingBuilder.bind(new Queue(TOPIC_QUEUE, true)).to(new TopicExchange(TOPIC_EXCHANGE)).with(TOPIC);
    }



    /**
     * Fanout模式 交换机Exchange
     */
    @Bean
    public FanoutExchange fanoutExchage(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    /**
     * SECKILL_QUEUE，TOPIC_QUEUE绑定到Fanout交换机
     * @return
     */
    @Bean
    public Binding seckillFanoutBindingFanou() {
        return BindingBuilder.bind(new Queue(SECKILL_QUEUE, true)).to(new FanoutExchange(FANOUT_EXCHANGE));
    }
    @Bean
    public Binding topicFanoutBindingFanou() {
        return BindingBuilder.bind(new Queue(TOPIC_QUEUE, true)).to(new FanoutExchange(FANOUT_EXCHANGE));
    }



    /**
     * Header模式 交换机Exchange
     */
    @Bean
    public HeadersExchange headersExchage(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    /**
     * HEADER_QUEUE绑定到Header交换机
     * @return
     */
    @Bean
    public Binding headerBinding() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(HEADER_1, VALUE_1);
        map.put(HEADER_2, VALUE_2);
        return BindingBuilder.bind(new Queue(HEADER_QUEUE, true)).to(new HeadersExchange(HEADERS_EXCHANGE)).whereAll(map).match();
    }
}
