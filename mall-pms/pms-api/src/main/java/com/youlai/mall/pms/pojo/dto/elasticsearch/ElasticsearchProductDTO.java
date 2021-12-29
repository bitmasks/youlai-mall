package com.youlai.mall.pms.pojo.dto.elasticsearch;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * Title: ElasticsearchProductDTO
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author <刘小杰>
 * @date 2021年12月29日
 * @since 1.8
 */
@Data
public class ElasticsearchProductDTO {

    private Long brandId;

    private String brandLogo;

    private Long categoryId;

    private String categoryName;

    private String brandName;

    private Boolean hasStock;

    private Long saleCount;

    private Long skuId;

    private String skuName;

    private String skuPicture;

    private String skuPrice;

    private Long spuId;

    private List<ElasticsearchProductAttributeDTO> attributes;
}
