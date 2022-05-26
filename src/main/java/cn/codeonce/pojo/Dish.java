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
 * 菜品
 */
@ApiModel("菜品")
@Data
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    //菜品名称
    @ApiModelProperty("菜品名称")
    private String name;

    //菜品分类id
    @ApiModelProperty("菜品分类ID")
    private Long categoryId;

    //菜品价格
    @ApiModelProperty("菜品价格")
    private BigDecimal price;

    //商品码
    @ApiModelProperty("商品码")
    private String code;

    //图片
    @ApiModelProperty("图片")
    private String image;

    //描述信息
    @ApiModelProperty("描述信息")
    private String description;

    //0 停售 1 起售
    @ApiModelProperty("状态")
    private Integer status;

    //顺序
    @ApiModelProperty("顺序")
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
    private Integer isDeleted;

}
