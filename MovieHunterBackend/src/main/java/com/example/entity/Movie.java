package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@ApiModel("电影实体类")
public class Movie implements Serializable {
    private static final long serialVersionUID = 79646607668961303L;

    @ApiModelProperty(value = "电影ID")
    @TableId(type = IdType.AUTO)
    private Long moveId;

    @ApiModelProperty(value = "电影名称",position = 1)
    private String name;

    @ApiModelProperty(value = "电影别名",position = 2)
    private String alias;

    @ApiModelProperty(value = "主演",position = 3)
    private String actors;

    @ApiModelProperty(value = "海报链接",position = 4)
    private String cover;

    @ApiModelProperty(value = "导演",position = 5)
    private String directors;

    @ApiModelProperty(value = "电影评分",position = 6)
    private Float score;

    @ApiModelProperty(value = "电影分类",position = 7)
    private Movie category;

    @ApiModelProperty(value = "语言",position = 8)
    private String languages;

    @ApiModelProperty(value = "时长（分钟）",position = 10)
    private Integer duration;

    @ApiModelProperty(value = "制片地区",position = 11)
    private String regions;

    @ApiModelProperty(value = "电影上映时间",position = 12)
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "简介",position = 13)
    private String introduction;

    @ApiModelProperty(value = "标签",position = 14)
    private String tags;

    @ApiModelProperty(value = "年份",position = 15)
    private Integer year;

}
