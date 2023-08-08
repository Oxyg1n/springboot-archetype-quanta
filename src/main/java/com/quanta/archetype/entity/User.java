package com.quanta.archetype.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * @author quanta
 * @since 2023-03-01
 */
@Getter
@Setter
@TableName("user")
public class User implements Serializable {

    // 序列化版本
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "role")
    private Integer role;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @TableField("gmt_modify")
    private LocalDateTime gmtModify;


}
