package cn.codeonce.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐菜品关系
 */
@ApiModel("套餐菜品")
@Data
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    //套餐id
    @ApiModelProperty("套餐ID")
    private Long setmealId;

    //菜品id
    @ApiModelProperty("菜品ID")
    private Long dishId;

    //菜品名称 （冗余字段）
    @ApiModelProperty("菜品名称")
    private String name;

    //菜品原价
    @ApiModelProperty("菜品原价")
    private BigDecimal price;

    //份数
    @ApiModelProperty("份数")
    private Integer copies;

    //排序
    @ApiModelProperty("排序")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    //是否删除
    @ApiModelProperty("是否删除")
    private Integer isDeleted;
}
