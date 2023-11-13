package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "角色实体类", description = "用于权限控制，如：管理员、普通用户、超级管理员")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @ApiModelProperty("角色ID")
    @TableId(type = IdType.AUTO)
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty(value = "逻辑删除标识")
    private Integer delFlag;
}
