package com.youlai.mall.pms.pojo.dto.admin;

import com.youlai.common.base.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Title: 商品分页查询参数
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author <刘小杰>
 * @date 2022年01月08日
 * @since 1.8
 */
@ApiModel("商品分页参数")
@Data
public class GoodsPageDTO extends BasePageQuery {

    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @ApiModelProperty("商品名称")
    private String name;

}
