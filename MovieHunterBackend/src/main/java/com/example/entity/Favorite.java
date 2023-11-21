package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("收藏夹实体类")
public class Favorite implements Serializable {
    private static final long serialVersionUID = 8859659314757549854L;

    @ApiModelProperty(value = "收藏夹ID")
    @TableId(type = IdType.AUTO)
    private Long favoriteId;

    @ApiModelProperty(value = "用户ID", position = 1)
    private String userId;

    @ApiModelProperty(value = "收藏夹名称", position = 2)
    private String name;

    @ApiModelProperty(value = "最近更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy年MM月dd日 HH:mm:ss")
    private LocalDateTime updateTime;
}
