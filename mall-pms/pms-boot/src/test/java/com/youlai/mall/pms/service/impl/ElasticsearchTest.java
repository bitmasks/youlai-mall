package com.youlai.mall.pms.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.youlai.mall.pms.pojo.dto.elasticsearch.ElasticsearchProductDTO;
import com.youlai.mall.pms.service.elasticsearch.ElasticsearchSpuService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Title: es客户端测试类
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author <刘小杰>
 * @date 2021年12月29日
 * @since 1.8
 */
@SpringBootTest
@Slf4j
public class ElasticsearchTest {

    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private ElasticsearchSpuService elasticsearchSpuService;

    @Test
    public void createIndex() throws IOException {
        elasticsearchClient.indices().create(c -> c.index("test"));
    }

    @Test
    public void createDocument() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "abaaba");
        map.put("age", "18");
        CreateResponse response = elasticsearchClient.create(d -> d.index("test").document(map).id("2"));
    }

    @Test
    public void updateDocument() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "2342342");
        map.put("age", "22");
        UpdateResponse<HashMap> response = elasticsearchClient.update(u -> u.index("test").id("2").doc(map), HashMap.class);
    }

    @Test
    public void deleteDocument() throws IOException {
        DeleteResponse response = elasticsearchClient.delete(d -> d.index("test").id("1"));
    }

    @Test
    public void bulk() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "abaaba");
        map.put("age", "18");
        BulkResponse response = elasticsearchClient.bulk(b -> b.index("test")
                .operations(o -> o
                        .create(c -> c
                                .id("1")
                                .document(map)
                        )
                )
        );
    }

    @Test
    public void deleteIndex() throws IOException {
        elasticsearchClient.indices().delete(d -> d.index("text"));
    }

    @Test
    public void search() throws IOException {
        SearchResponse<ElasticsearchProductDTO> product = elasticsearchClient.search(s -> s
                        .index("product")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("bicycle"))
                                )
                        )
                , ElasticsearchProductDTO.class
        );

        for (Hit<ElasticsearchProductDTO> hit : product.hits().hits()) {
            log.info(hit.source().toString());
        }
    }

    @Test
    public void test() throws IOException {
        Set<ElasticsearchProductDTO> elasticsearchProducts = elasticsearchSpuService.listBySpuId(76L);
        System.out.println(elasticsearchProducts);
        CreateResponse response = elasticsearchClient.create(c -> c.index("product").id("1").document(elasticsearchProducts.stream().findFirst()));
        System.out.println(response);
    }

}
