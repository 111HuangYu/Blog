package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.vo.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author WH
 * @since 2024-03-21
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);
    List<Menu> selectAllRouterMenu();
    List<Menu> selectRouterMenuByUserId(Long userId);

}
