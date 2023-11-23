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
public class LoginUserWithCode implements Serializable {

    private static final long serialVersionUID = -5100464565667956619L;

    @ApiModelProperty(value = "用户id", hidden = true)
    private String userId;

    @Pattern(
            regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",
            message = "邮箱不符合规范")
    @ApiModelProperty(value = "用户邮箱", position = 1)
    private String email;

    @ApiModelProperty(value = "验证码", position = 2)
    @NotBlank(message = "验证码不能为空")
    private String code;
}
