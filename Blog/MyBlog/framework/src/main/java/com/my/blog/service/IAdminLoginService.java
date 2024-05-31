package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;

public interface IAdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();

//    ResponseResult logout();
}
