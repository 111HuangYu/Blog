package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.RoleDto;
import com.my.blog.domain.dto.RoleDto2;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.entity.Role;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.IRoleService;
import com.my.blog.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class SysRoleController {
    @Autowired
    private IRoleService roleService;
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, String roleName,String status){
        return roleService.pageRoleList(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult<String> changeStatus(@RequestBody RoleDto roleDto){
        return roleService.changeStatus(roleDto.getRoleId(),roleDto.getStatus());
    }
    //新增角色
    @PostMapping
    public ResponseResult<String> insert(@RequestBody RoleDto2 roleDto2){
        return roleService.insertRole(roleDto2);
    }
    //删除角色
    @DeleteMapping("/{id}")
    public ResponseResult<String> delete(@PathVariable("id") Long id){

        return roleService.deleteRole(id);
    }
    //根据id查询
    @GetMapping("/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable("id") Long id){

        return roleService.getRoleById(id);
    }
    //更新角色
    @PutMapping
    public ResponseResult<String> update(@RequestBody RoleDto2 roleDto2){
        return roleService.updateRole(roleDto2);
    }

}
