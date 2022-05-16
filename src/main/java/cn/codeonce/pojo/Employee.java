package cn.codeonce.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工信息
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    // 标识ID
    private Long id;

    // 用户账号
    private String username;

    // 用户名
    private String name;

    // 密码
    private String password;

    // 手机
    private String phone;

    // 性别
    private String sex;

    // 身份证号码
    private String idNumber;

    // 账号状态
    private Integer status;

    // 插入时填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 更新时填充字段
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    // 更新时填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
