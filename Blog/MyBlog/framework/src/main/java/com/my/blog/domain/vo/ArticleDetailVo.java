package com.my.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleDetailVo {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private Long viewCount;
    private String isComment;
    private LocalDateTime createTime;
    private String categoryName;
}
