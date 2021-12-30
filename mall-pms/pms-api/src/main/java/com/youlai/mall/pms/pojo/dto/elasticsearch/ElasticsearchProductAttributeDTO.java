package com.youlai.mall.pms.pojo.dto.elasticsearch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class ElasticsearchProductAttributeDTO {

    @ApiModelProperty("属性ID")
    private Long attributeId;

    @ApiModelProperty("商品属性关系ID")
    private Long attributeSpuRelationId;

    @ApiModelProperty("属性名称")
    private String attributeName;

    @ApiModelProperty("属性值")
    private String attributeValue;

}
