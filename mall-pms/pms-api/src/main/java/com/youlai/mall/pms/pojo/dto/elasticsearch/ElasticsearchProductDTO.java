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

    @ApiModelProperty("商品ID")
    private Long spuId;

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

    @ApiModelProperty("销量")
    private Long saleCount;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("商品主图")
    private String spuPicture;

    @ApiModelProperty("原价")
    private Long originPrice;

    @ApiModelProperty("商品价格")
    private Long spuPrice;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("商品状态")
    private Integer status;
}
