package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Article;
import com.my.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class AdminArticleController {
    @Autowired
    IArticleService articleService;
    @GetMapping("/list")
    @PreAuthorize("@permissionService.hasPermission('content:article:list')")
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize,String title, String summary) {
        ResponseResult articles = articleService.getAdminArticleList(pageNum,pageSize, title, summary);
        return articles;
    }

    //根据id获取文章
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id) {
        return articleService.getAdminArticleById(id);
    }
    //修改文章
    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article) {
        return articleService.updateAdminArticle(article);
    }
    //删除文章
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id) {

        return articleService.deleteAdminArticle(id);
    }
}