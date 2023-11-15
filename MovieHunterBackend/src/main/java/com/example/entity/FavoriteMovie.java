package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("收藏夹-电影连接类")
public class FavoriteMovie {

    @TableId(type = IdType.NONE)
    private Long favoriteId;

    private Long movieId;

    @ApiModelProperty(value = "逻辑删除状态",hidden = true)
    @Max(value = 1, message = "格式错误")
    @Min(value = 0, message = "格式错误")
    private Integer delFlag;
}
