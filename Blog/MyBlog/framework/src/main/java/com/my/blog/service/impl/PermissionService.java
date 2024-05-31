package com.my.blog.service.impl;

import com.my.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    public boolean hasPermission(String permission){
//如果是超级管理员，直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
//否则，获取当前登录用户所具有的权限列表
        List<String> permissions =
                SecurityUtils.getLoginUser().getPermissions();
        if(permissions == null)
            return false;
        return permissions.contains(permission);
    }
}
