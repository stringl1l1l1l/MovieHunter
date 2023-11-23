package com.example.entity;

import com.example.jsr303.LoginOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserWithPwd implements Serializable {

    private static final long serialVersionUID = 633328115305436341L;

    @ApiModelProperty(value = "用户id", hidden = true)
    private String userId;

    @Pattern(
            regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",
            message = "邮箱不符合规范")
    @ApiModelProperty(value = "用户邮箱", position = 1)
    private String email;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "用户密码", position = 2)
    private String password;
}