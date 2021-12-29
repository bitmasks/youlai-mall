package com.youlai.mall.pms.service.elasticsearch.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youlai.mall.pms.mapper.PmsSpuMapper;
import com.youlai.mall.pms.pojo.dto.elasticsearch.ElasticsearchProductDTO;
import com.youlai.mall.pms.pojo.entity.PmsSpu;
import com.youlai.mall.pms.service.elasticsearch.ElasticsearchSpuService;
import org.springframework.stereotype.Service;

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
@Service
public class ElasticsearchSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpu> implements ElasticsearchSpuService {


    @Override
    public Set<ElasticsearchProductDTO> listBySpuId(Long spuId) {
        return this.getBaseMapper().listBySpuId(spuId);
    }
}
