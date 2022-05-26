package cn.codeonce.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@ApiModel("购物车")
@Data
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    //名称
    @ApiModelProperty("名称")
    private String name;

    //用户id
    @ApiModelProperty("用户ID")
    private Long userId;

    //菜品id
    @ApiModelProperty("菜品ID")
    private Long dishId;

    //套餐id
    @ApiModelProperty("套餐ID")
    private Long setmealId;

    //口味
    @ApiModelProperty("口味")
    private String dishFlavor;

    //数量
    @ApiModelProperty("数量")
    private Integer number;

    //金额
    @ApiModelProperty("金额")
    private BigDecimal amount;

    //图片
    @ApiModelProperty("图片")
    private String image;

    // 创建时间
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
