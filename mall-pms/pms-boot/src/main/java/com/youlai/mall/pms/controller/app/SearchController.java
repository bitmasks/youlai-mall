package com.youlai.mall.pms.controller.app;

import com.youlai.common.elasticsearch.service.ElasticSearchService;
import com.youlai.common.result.Result;
import com.youlai.mall.pms.pojo.domain.PmsProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author haoxr
 * @description TODO
 * @createTime 2021/3/7 22:55
 */
@Api(tags = "【移动端】商品搜索")
@RestController("AppSearchController")
@RequestMapping("/api.app/v1/search")
@AllArgsConstructor
public class SearchController {

    private ElasticSearchService elasticSearchService;

    @ApiOperation(value = "关键字搜索商品", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "关键字", paramType = "query", dataType = "String"),
    })
    @GetMapping("/{name}")
    public Result searchByName(@PathVariable String name) {
       /* SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("name", name));
        List<PmsProduct> list = elasticSearchService.search(builder.query(), PmsProduct.class, "pms_spu");
        return Result.success(list);*/

        return null;
    }

}
