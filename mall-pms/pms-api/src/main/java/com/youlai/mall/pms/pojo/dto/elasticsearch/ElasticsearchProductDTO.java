package com.youlai.mall.pms.pojo.dto.elasticsearch;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("品牌图片")
    private String brandLogo;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("是否有库存")
    private Boolean hasStock;

    @ApiModelProperty("销量")
    private Long saleCount;

    @ApiModelProperty("商品库存ID")
    private Long skuId;

    @ApiModelProperty("商品名称")
    private String skuName;

    @ApiModelProperty("商品图片")
    private String skuPicture;

    @ApiModelProperty("商品价格")
    private String skuPrice;

    @ApiModelProperty("商品ID")
    private Long spuId;

    @ApiModelProperty("商品属性")
    private List<ElasticsearchProductAttributeDTO> attributes;
}
