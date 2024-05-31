package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.PageVo;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author WH
 * @since 2024-03-05
 */
public interface ICategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult<PageVo> pageCategoryList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(Category category);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(Category category);

    ResponseResult deleteCategory(Long id);
}
