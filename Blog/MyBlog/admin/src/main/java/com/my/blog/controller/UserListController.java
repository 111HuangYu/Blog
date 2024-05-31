package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.UserListDto;
import com.my.blog.domain.entity.User;
import com.my.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserListController{
    @Autowired
    private IUserService userService;
    @GetMapping("/list")
    public ResponseResult listAllUsers(Integer pageNum, Integer pageSize, UserListDto userListDto){
        return userService.listAllUsers(pageNum,pageSize,userListDto);
    }

    //新增接口
    @PostMapping
    public ResponseResult addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);

    }
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }
    @PutMapping
    public ResponseResult updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }


}
