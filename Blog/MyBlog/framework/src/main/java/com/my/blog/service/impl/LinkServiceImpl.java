package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.LinkMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.entity.Link;
import com.my.blog.domain.vo.ArticleListVo;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.domain.vo.LinkVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.ILinkService;
import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author WH
 * @since 2024-03-05
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public ResponseResult getAllLink() {
        //要查询出所有的审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_PASS);
        List<Link> links = linkMapper.selectList(queryWrapper);

        //封装为Vo
        ArrayList<LinkVo> linkVos = new ArrayList<>();
        for(Link link: links){
            LinkVo linkVo = new LinkVo();
            BeanUtils.copyProperties(link, linkVo);
            linkVos.add(linkVo);
        }

        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<PageVo> pageLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        //分⻚查询
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(name), Link::getName,name);
        queryWrapper.eq(StringUtils.hasText(status), Link::getStatus,status);
        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(Link link) {
        if (!StringUtils.hasText(link.getName())){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        linkMapper.insert(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delLink(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        linkMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateLink(Link link) {
        if (Objects.isNull(link)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        linkMapper.updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLink(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Link link = linkMapper.selectById(id);
        return ResponseResult.okResult(link);
    }

    @Override
    public ResponseResult changeLinkStatus(Long id, String status) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Link link = linkMapper.selectById(id);
        link.setStatus(status);
        linkMapper.updateById(link);
        return ResponseResult.okResult();
    }


}
