package com.my.blog.runner;

import com.my.blog.dao.ArticleMapper;
import com.my.blog.domain.entity.Article;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
//查询博客信息 id viewCount
        List<Article> articles = articleMapper.selectList(null);
        HashMap<String, Integer> viewCountMap = new HashMap<>();
        for (Article article : articles) {
            viewCountMap.put(article.getId().toString(),article.getViewCount().intValue());
        }
        System.out.println(viewCountMap);
//存储到redis中
        redisCache.setCacheMap("viewCount",viewCountMap);
    }
}
