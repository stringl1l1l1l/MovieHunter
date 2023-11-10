package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long userInfoId;

    private String userId;

    private String introduction;

    @URL(message = "请输入URL")
    private String avatar;

    private String name;

    private List<String> roles;

    public UserInfo(Long userInfoId, String userId, String introduction, String avatar, String name) {
        this.userInfoId = userInfoId;
        this.userId = userId;
        this.introduction = introduction;
        this.avatar = avatar;
        this.name = name;
    }
}
