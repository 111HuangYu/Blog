package com.my.blog.domain.vo;

import lombok.Data;

@Data
public class HotArticleVo {
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
