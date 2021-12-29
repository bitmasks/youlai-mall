package com.youlai.mall.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youlai.mall.pms.pojo.dto.elasticsearch.ElasticsearchProductDTO;
import com.youlai.mall.pms.pojo.entity.PmsSpu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 */
@Mapper
public interface PmsSpuMapper extends BaseMapper<PmsSpu> {
    List<PmsSpu> list(Page<PmsSpu> page, String name, Long categoryId);

    /**
     * <p>
     * Title: 根据商品ID查询
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param spuId 商品ID
     * @return java.util.List<com.youlai.mall.pms.pojo.dto.elasticsearch.ElasticsearchProductDTO>
     * @author 刘小杰
     * @date 2021年12月30日
     * @since 1.8
     */
    Set<ElasticsearchProductDTO> listBySpuId(@Param("spuId") Long spuId);
}
