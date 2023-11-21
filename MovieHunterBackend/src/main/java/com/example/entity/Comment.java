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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("评论实体类")
public class Comment implements Serializable {
    private static final long serialVersionUID = 601767974071095251L;

    @ApiModelProperty(value = "评论ID", hidden = true)
    @TableId(type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty(value = "电影ID", position = 1)
    private Long movieId;

    @ApiModelProperty(value = "用户ID", position = 2, hidden = true)
    private String userId;

    @ApiModelProperty(value = "评论打分", position = 3)
    private Integer rating;

    @ApiModelProperty(value = "评论内容", position = 4)
    @Length(max = 1000, message = "评论内容过长")
    private String content;

    @ApiModelProperty(value = "评论发表时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime commentTime;

    @ApiModelProperty(value = "评论点赞数", hidden = true)
    private Integer votes;

    @ApiModelProperty(value = "逻辑删除状态", hidden = true)
    @Max(value = 1, message = "格式错误")
    @Min(value = 0, message = "格式错误")
    private Integer delFlag;
}
