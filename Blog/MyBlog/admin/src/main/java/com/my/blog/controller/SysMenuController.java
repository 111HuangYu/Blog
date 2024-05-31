package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.MenuRoleDto;
import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.vo.MenuVo;
import com.my.blog.domain.vo.RoutersVo;
import com.my.blog.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
    @Autowired
    private IMenuService menuService;
    // 获取菜单树接口
    @GetMapping("/treeselect")
    public ResponseResult<MenuVo> getMenuTree() {
        List<MenuVo> menus = menuService.selectMenuTree();

        return ResponseResult.okResult(menus);
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult<MenuRoleDto> roleMenuTreeSelect(@PathVariable Long id) {
        MenuRoleDto MenuRoleDto = menuService.selectMenuTreeSelect(id);
        return ResponseResult.okResult(MenuRoleDto);
    }
    //菜单列表接口
    @GetMapping("/list")
    public ResponseResult<List<Menu>> getMenuList(String status, String menuName) {
        return ResponseResult.okResult(menuService.selectMenuList(status, menuName));
    }
    //新增
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {

        return menuService.addMenu(menu);
    }
    //根据id查询菜单数据
    @GetMapping("/{id}")
    public ResponseResult<Menu> getMenuById(@PathVariable("id") Long id) {

        return menuService.getMenuById(id);
    }
    //修改
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }
    //删除
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long id) {
        return menuService.deleteMenu(id);
    }
}
