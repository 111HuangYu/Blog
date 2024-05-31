package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.MenuMapper;
import com.my.blog.dao.RoleMenuMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.MenuRoleDto;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.entity.RoleMenu;
import com.my.blog.domain.vo.MenuVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IMenuService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2024-03-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (id == 1L) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = menuMapper.selectList(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return menuMapper.selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
        //如果是，获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
        //否则，获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }
        //构建tree
        //先找出一级菜单，然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus);
        return menuTree;
    }

    @Override
    public List<MenuVo> selectMenuTree() {
        //查询menu里所有数据
        List<Menu> menus =  menuMapper.selectList(null);
        //转换为menusVo
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        for (MenuVo menuVo : menuVos){
            menuVo.setLabel(menuVo.getMenuName());
        }
        List<MenuVo> menuTree = builderMenuVoTree(menuVos);
        return menuTree;
    }

    @Override
    public MenuRoleDto selectMenuTreeSelect(Long id) {
        MenuRoleDto menuRoleDto = new MenuRoleDto();
        List<MenuVo> menuTree = selectMenuTree();
        menuRoleDto.setMenus(menuTree);
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",id);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);
        System.out.println(roleMenus);
        //将roleMenus中的menuId,存入[]中
        List<String> checkedKeys = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenus){
            checkedKeys.add(roleMenu.getMenuId().toString());
        }
        System.out.println(checkedKeys);
        menuRoleDto.setCheckedKeys(checkedKeys);
        return menuRoleDto;
    }

    @Override
    public List<Menu> selectMenuList(String status, String menuName) {

        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Menu::getStatus,status);
        wrapper.like(StringUtils.hasText(menuName), Menu::getMenuName,menuName);
        List<Menu> menus = menuMapper.selectList(wrapper);
        return menus;
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        if (!StringUtils.hasText(menu.getMenuName())){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        menuMapper.insert(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Menu> getMenuById(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Menu menu = menuMapper.selectById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (Objects.isNull(menu)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //如果把父菜单设置为当前菜单:

        if(menu.getParentId() == 0 && menu.getMenuType().equals("C") ){
            throw new SystemException(AppHttpCodeEnum.CANT_CHOOSE);
        }
        menuMapper.updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //如果要删除的菜单有子菜单则
        if (menuMapper.selectCount(new QueryWrapper<Menu>().eq("parent_id", id)) > 0){
            throw new SystemException(AppHttpCodeEnum.CANT_DELETE);
        }
        menuMapper.deleteById(id);
        return ResponseResult.okResult();
    }



    //获取menuVo的tree的一级菜单
    private List<MenuVo> builderMenuVoTree(List<MenuVo> menus) {
        List<MenuVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu ->
                    menu.setChildren(getMenuChildren(menus, menu.getId()))
                )
                .collect(Collectors.toList());

        System.out.println(menuTree);
        return menuTree;
    }
    private List<Menu> builderMenuTree(List<Menu> menus) {
        List<Menu> menuTree = menus.stream()
                // 获取一级菜单
                .filter(menu -> menu.getParentId().equals(0L))
                // 查询并设置一级菜单下的子菜单
                .map(menu -> menu.setChildren(getChildren(menus, menu.getId())))
                .collect(Collectors.toList());
        System.out.println(menuTree);
        return menuTree;
    }



    private List<Menu> getChildren(List<Menu> menus, Long menuId) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menuId))
                .collect(Collectors.toList());
        return childrenList;
    }

    private List<MenuVo> getMenuChildren(List<MenuVo> menus, Long menuId) {
        List<MenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menuId))
                .map(menu ->
                        menu.setChildren(getMenuChildren(menus, menu.getId()))
                )
                .collect(Collectors.toList());
        return childrenList;
    }


}
