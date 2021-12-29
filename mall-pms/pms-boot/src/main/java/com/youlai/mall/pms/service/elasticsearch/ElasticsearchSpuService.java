package com.youlai.mall.pms.service.elasticsearch;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youlai.mall.pms.pojo.dto.elasticsearch.ElasticsearchProductDTO;
import com.youlai.mall.pms.pojo.entity.PmsSpu;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Title: ElasticsearchServiceImp
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author <刘小杰>
 * @date 2021年12月30日
 * @since 1.8
 */
public interface ElasticsearchSpuService extends IService<PmsSpu> {

    /**
     * <p>
     * Title: 根据商品ID查询
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param spuId 商品ID
     * @return com.youlai.mall.pms.pojo.dto.elasticsearch.ElasticsearchProductDTO
     * @author 刘小杰
     * @date 2021年12月30日
     * @since 1.8
     */
    Set<ElasticsearchProductDTO> listBySpuId(Long spuId);
}
