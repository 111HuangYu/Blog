package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.UserListDto;
import com.my.blog.domain.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author WH
 * @since 2024-03-05
 */
public interface IUserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listAllUsers(Integer pageNum, Integer pageSize, UserListDto userListDto);

    ResponseResult addUser(User user);

    ResponseResult deleteUser(Long id);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(User user);

}
