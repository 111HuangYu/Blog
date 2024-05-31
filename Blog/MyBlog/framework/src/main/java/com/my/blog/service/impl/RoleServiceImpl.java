package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.RoleMapper;
import com.my.blog.dao.RoleMenuMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.RoleDto2;
import com.my.blog.domain.entity.Role;
import com.my.blog.domain.entity.RoleMenu;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.domain.vo.RoleVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IRoleService;
import com.my.blog.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2024-03-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return roleMapper.selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listAllRoles() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        //查询status为0的角色信息
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = roleMapper.selectList(queryWrapper);
        //将得到的list,转为
        List<RoleVo> roleVos = new ArrayList<>();
        for (Role role : roles) {
            RoleVo roleVo = new RoleVo();
            BeanUtils.copyProperties(role, roleVo);
            roleVos.add(roleVo);
        }
        return ResponseResult.okResult(roleVos);
    }

    @Override
    public ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        //分⻚查询
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(roleName), Role::getRoleName,roleName);
        queryWrapper.eq(StringUtils.hasText(status), Role::getStatus,status);
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //变为RoleVo
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<String> changeStatus(String roleId, String status) {
        //System.out.println(roleId);
        Role role = roleMapper.selectById(roleId);
        role.setStatus(status);
        roleMapper.updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<String> insertRole(RoleDto2 roleDto2) {
        Role role = BeanCopyUtils.copyBean(roleDto2, Role.class);
        save(role);
        for (Long menuId : roleDto2.getMenuIds()){
            RoleMenu roleMenu = new RoleMenu(role.getId(),menuId);
            roleMenuMapper.insert(roleMenu);
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<String> deleteRole(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        roleMapper.deleteById(id);
        roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq("role_id",id));
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Role> getRoleById(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Role role = roleMapper.selectById(id);
        return ResponseResult.okResult(role);
    }

    @Override
    public ResponseResult<String> updateRole(RoleDto2 roleDto2) {
        if (Objects.isNull(roleDto2)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Role role = roleMapper.selectById(roleDto2.getId());
        roleMapper.updateById(role);

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,roleDto2.getId());
        roleMenuMapper.delete(queryWrapper);
        List<Long> menuIds = roleDto2.getMenuIds();
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(roleDto2.getId(),menuId);
            roleMenuMapper.insert(roleMenu);
        }
        return ResponseResult.okResult();
    }

}
