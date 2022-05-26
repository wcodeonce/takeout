package cn.codeonce.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@ApiModel("订单详情")
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    //名称
    private String name;

    //订单id
    @ApiModelProperty("订单ID")
    private Long orderId;


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
}
