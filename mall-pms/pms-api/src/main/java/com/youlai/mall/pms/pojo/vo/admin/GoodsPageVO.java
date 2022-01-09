package com.youlai.mall.pms.pojo.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.youlai.mall.pms.pojo.entity.PmsSku;
import io.swagger.annotations.ApiModel;
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

    private Long id;
    private String name;
    private Long categoryId;
    private Long brandId;
    private Long originPrice;
    private Long price;
    private Integer sales;
    private String picUrl;
    private String[] album;
    private String unit;
    private String description;
    private String detail;
    private Integer status;

    private String categoryName;

    private String brandName;

    private List<PmsSku> skuList;

}
