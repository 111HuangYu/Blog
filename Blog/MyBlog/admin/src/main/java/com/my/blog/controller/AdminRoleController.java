package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IRoleService;
import com.my.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/role")
public class AdminRoleController {
    @Autowired
    private IRoleService roleService;
    //查询的是所有状态正常的角色
    @GetMapping("/listAllRole")
    public ResponseResult listAllRoles(){
        return roleService.listAllRoles();
    }
}
