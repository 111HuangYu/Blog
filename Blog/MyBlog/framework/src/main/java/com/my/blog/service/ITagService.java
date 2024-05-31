package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author PTU
 * @since 2024-04-09
 */
public interface ITagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult delTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(com.my.blog.entity.Tag tag);

    ResponseResult listAllTag();
}
