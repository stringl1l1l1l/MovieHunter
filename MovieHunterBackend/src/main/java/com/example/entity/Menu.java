package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "权限实体类", description = "存储角色的具体权限")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = 6113951375019450735L;

    @ApiModelProperty("权限ID")
    @TableId(type = IdType.AUTO)
    private Long menuId;

    @ApiModelProperty("权限名称")
    private String menuName;

    @ApiModelProperty("权限指令字符串")
    private String permission;

    @ApiModelProperty(value = "逻辑删除标识", hidden = true)
    private Integer delFlag;
}
