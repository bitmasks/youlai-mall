package com.youlai.mall.pms.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <p>
 * Title: ElasticsearchClientConfig
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author <刘小杰>
 * @date 2021年12月29日
 * @since 1.8
 */
@Configuration
public class ElasticsearchClientConfig {
    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public RestClient restClient(){
        return RestClient.builder(new HttpHost("124.71.32.209",9200)).build();
    }

    @Bean
    public JacksonJsonpMapper jacksonJsonpMapper(){
        return new JacksonJsonpMapper(objectMapper);
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(){
        return new RestClientTransport(restClient(),jacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(){
        return new ElasticsearchClient(elasticsearchTransport());
    }



}
