package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;

public interface IBlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}

