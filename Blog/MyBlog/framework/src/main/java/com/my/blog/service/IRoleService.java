package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.RoleDto2;
import com.my.blog.domain.entity.Role;
import com.my.blog.domain.vo.PageVo;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author WH
 * @since 2024-03-21
 */
public interface IRoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listAllRoles();

    ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult<String> changeStatus(String roleId, String status);

    ResponseResult<String> insertRole(RoleDto2 roleDto2);

    ResponseResult<String> deleteRole(Long id);

    ResponseResult<Role> getRoleById(Long id);

    ResponseResult<String> updateRole(RoleDto2 role);
}
