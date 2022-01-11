package com.youlai.mall.pms.pojo.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.youlai.mall.pms.pojo.entity.PmsSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * Title: 商品分页查询结果
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author <刘小杰>
 * @date 2022年01月08日
 * @since 1.8
 */
@ApiModel("商品分页查询结果")
@Data
public class GoodsPageVO {

    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("原价")
    private Long originPrice;

    @ApiModelProperty("现价")
    private Long price;

    @ApiModelProperty("销量")
    private Integer sales;

    @ApiModelProperty("商品主图")
    private String picUrl;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("商品状态")
    private Integer status;

}
