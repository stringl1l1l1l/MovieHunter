package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("评论实体类")
public class Comment implements Serializable {
    private static final long serialVersionUID = 601767974071095251L;

    @ApiModelProperty(value = "评论ID")
    @TableId(type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty(value = "电影ID",position = 1)
    private Long movieId;

    @ApiModelProperty(value = "用户ID",position = 2)
    private Long userId;

    @ApiModelProperty(value = "评论打分",position = 3)
    private Float score;

    @ApiModelProperty(value = "评论内容",position = 4)
    private String content;

    @ApiModelProperty(value = "评论发表时间",position = 5)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime releaseTime;
}
