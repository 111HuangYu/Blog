package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.MenuRoleDto;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.vo.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author WH
 * @since 2024-03-21
 */
public interface IMenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);



    List<MenuVo> selectMenuTree();

    List<Menu> selectMenuList(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult<Menu> getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    MenuRoleDto selectMenuTreeSelect(Long id);
}
