package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Link;
import com.my.blog.domain.vo.PageVo;

/**
 * <p>
 * 友链 服务类
 * </p>
 *
 * @author WH
 * @since 2024-03-05
 */
public interface ILinkService extends IService<Link> {

    ResponseResult getAllLink();


    ResponseResult<PageVo> pageLinkList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(Link link);

    ResponseResult delLink(Long id);

    ResponseResult updateLink(Link link);

    ResponseResult getLink(Long id);

    ResponseResult changeLinkStatus(Long id, String status);
}
