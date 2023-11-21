package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("电影实体类")
public class Movie implements Serializable {
    private static final long serialVersionUID = 79646607668961303L;

    @ApiModelProperty(value = "电影ID")
    @TableId(type = IdType.AUTO)
    private Long movieId;

    @ApiModelProperty(value = "电影名称", position = 1)
    private String name;

    @ApiModelProperty(value = "电影别名", position = 2)
    private String alias;

    @ApiModelProperty(value = "海报链接", position = 3)
    private String cover;

    @ApiModelProperty(value = "导演", position = 4)
    private String directors;

    @ApiModelProperty(value = "主演", position = 5)
    private String actors;

    @ApiModelProperty(value = "电影评分", position = 6)
    private Float score;

    @ApiModelProperty(value = "电影分类", position = 7)
    private String genres;

    @ApiModelProperty(value = "电影分类掩码", position = 8)
    private Integer genresMask;

    @ApiModelProperty(value = "时长（分钟）", position = 9)
    private Integer duration;

    @ApiModelProperty(value = "语言", position = 10)
    private String languages;

    @ApiModelProperty(value = "制片地区", position = 11)
    private String regions;

    @ApiModelProperty(value = "地区掩码", position = 12)
    private Integer regionsMask;

    @ApiModelProperty(value = "电影上映日期", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy年MM月dd日")
    private LocalDate releaseDate;

    @ApiModelProperty(value = "简介", position = 14)
    private String storyline;

    @ApiModelProperty(value = "标签", position = 15)
    private String tags;

    @ApiModelProperty(value = "年份", position = 16)
    private Integer year;

    @ApiModelProperty(value = "逻辑删除状态", hidden = true)
    @Max(value = 1, message = "格式错误")
    @Min(value = 0, message = "格式错误")
    private Integer delFlag;

}
