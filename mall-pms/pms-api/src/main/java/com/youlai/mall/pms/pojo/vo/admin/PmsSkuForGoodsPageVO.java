package com.youlai.mall.pms.pojo.vo.admin;

import com.youlai.common.base.BaseEntity;
import lombok.Data;

@Data
public class PmsSkuForGoodsPageVO {
    private Long id;
    private String sn;
    private String specIds;
    private Long price;
    private Integer stock;
    private Integer lockedStock;
    private String picUrl;
}
