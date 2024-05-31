package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.AddArticleDto;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.ArticleListVo;
import com.my.blog.domain.vo.HotArticleVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.entity.ArticleTag;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IArticleService;
import com.my.blog.service.IArticleTagService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2024-03-05
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IArticleTagService articleTagService;
    @Override
    public ResponseResult getHotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);

        Page<Article> articlePage = new Page<>(1, 10);
        articleMapper.selectPage(articlePage, queryWrapper);
        List<Article> records = articlePage.getRecords();

        ArrayList<HotArticleVo> hotArticleVos = new ArrayList<HotArticleVo>();
        for (Article article : records) {
            HotArticleVo hotArticleVo = new HotArticleVo();
            BeanUtils.copyProperties(article, hotArticleVo);
            hotArticleVos.add(hotArticleVo);
        }

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //①只能查询正式发布的文章 ②置顶的文章要显示在最前面
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null && categoryId != 0, Article::getCategoryId, categoryId)
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        articleMapper.selectPage(articlePage, queryWrapper);
        List<Article> articles = articlePage.getRecords();

        //封装为Vo
        ArrayList<ArticleListVo> articleListVos = new ArrayList<>();
        for (Article article : articles) {
            ArticleListVo articleListVo = new ArticleListVo();
            BeanUtils.copyProperties(article, articleListVo);
            //根据categoryId查询categoryName
            Category category = categoryMapper.selectById(article.getCategoryId());
            String name = category.getName();
            articleListVo.setCategoryName(name);
            articleListVos.add(articleListVo);
        }

        //封装为PageVo
        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = articleMapper.selectById(id);

        //封装为Vo
        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article, articleDetailVo);
        //根据categoryId查询categoryName
        Category category = categoryMapper.selectById(article.getCategoryId());
        String name = category.getName();
        articleDetailVo.setCategoryName(name);

        Integer viewCount = redisCache.getCacheMapValue("viewCount", id.toString());
        articleDetailVo.setViewCount(Long.parseLong(viewCount.toString()));
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.addCacheMapValue("viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAdminArticleList(Integer pageNum, Integer pageSize,String title, String summary) {
        //设置条件查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(title != null, Article::getTitle, title)
                .like(summary != null, Article::getSummary, summary)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);
        //设置分页查询
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        //查询文章列表
        articlePage = articleMapper.selectPage(articlePage, queryWrapper);
        //封装进VO
        List<ArticleListVo> adminArticleVoList = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(adminArticleVoList, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult add(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = addArticleDto.getTags().stream().map(tagId -> new ArticleTag(article.getId(), tagId)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAdminArticleById(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Article article =  articleMapper.selectById(id);

        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult updateAdminArticle(Article article) {
        if (Objects.isNull(article)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        articleMapper.updateById(article);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteAdminArticle(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        articleMapper.deleteById(id);
        return ResponseResult.okResult();
    }
}
