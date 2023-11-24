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

    @Length(max = 32, message = "ID格式错误")
    @ApiModelProperty(value = "用户ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    @Length(max = 30, message = "用户名过长")
    @ApiModelProperty(value = "用户名", position = 3)
    private String username;

//    @Pattern(
//            regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$",
//            message = "手机号不符合规范")
//    @ApiModelProperty(value = "用户手机号",position = 1)
//    private String phoneNum;

    @ApiModelProperty(value = "用户邮箱", position = 2)
    private String email;

    @NotBlank(message = "密码不能为空", groups = {LoginOperation.class})
    @ApiModelProperty(value = "密码", position = 4)
    private String password;

    @ApiModelProperty(value = "用户头像", hidden = true)
    private String avatar;

    public User(LoginUserWithCodePwd user) {
        this.setPassword(user.getPassword());
        this.setEmail(user.getEmail());
    }
}
