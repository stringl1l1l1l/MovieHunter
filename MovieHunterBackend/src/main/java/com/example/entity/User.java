package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.jsr303.LoginOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户实体类")
public class User implements Serializable {
    private static final long serialVersionUID = 1528247366805849494L;

    @Min(value = 0,message = "格式错误")
    @ApiModelProperty(value = "用户ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    @Pattern(
            regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$",
            message = "手机号不符合规范")
    @ApiModelProperty(value = "用户手机号",position = 1)
    private String phone;

    @Pattern(
            regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",
            message = "邮箱不符合规范")
    @ApiModelProperty(value = "用户邮箱",position = 2)
    private String email;

    @Length(max = 30, message = "用户名过长")
    @NotBlank(message = "用户名不能为空", groups = {LoginOperation.class})
    @ApiModelProperty(value = "用户名",position = 3)
    private String username;

    @NotBlank(message = "密码不能为空", groups = {LoginOperation.class})
    @ApiModelProperty(value = "密码",position = 4)
    private String password;

    @ApiModelProperty(value = "验证码",hidden = true)
    private String code;

    @ApiModelProperty(value = "逻辑删除状态",hidden = true)
    @Max(value = 1, message = "格式错误")
    @Min(value = 0, message = "格式错误")
    private Integer delFlag;
}
