package com.my.blog.domain.dto;

import com.my.blog.domain.entity.Menu;
import com.my.blog.domain.vo.MenuVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuRoleDto {
    private List<MenuVo> menus;
    private List<String> checkedKeys;
}
