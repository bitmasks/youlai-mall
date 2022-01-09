package com.youlai.mall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youlai.common.result.Result;
import com.youlai.mall.pms.pojo.dto.admin.GoodsFormDTO;
import com.youlai.mall.pms.pojo.dto.admin.GoodsPageDTO;
import com.youlai.mall.pms.pojo.vo.admin.GoodsPageVO;
import com.youlai.mall.pms.pojo.entity.PmsSpu;
import com.youlai.mall.pms.pojo.vo.admin.GoodsDetailVO;

import java.io.IOException;
import java.util.List;


public interface IPmsSpuService extends IService<PmsSpu> {

    Result<List<GoodsPageVO>> listFromElasticsearch(GoodsPageDTO queryDTO);

    boolean addGoods(GoodsFormDTO goodsFormDTO) throws IOException;

    boolean removeByGoodsIds(List<Long> spuIds);

    boolean updateGoods(GoodsFormDTO goodsFormDTO);

    GoodsDetailVO getGoodsById(Long id);
}
