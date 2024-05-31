package com.my.blog.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.my.blog.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
@Accessors(chain = true)
public class MenuVo implements Serializable {
    @TableField(exist = false)
    private List<MenuVo> children;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String menuName;
    private Long parentId;
    private String label;
    @Override
    public String toString() {
        return "Menu{" +
                "id = " + id +
                ", label = " + label +
                ", parentId = " + parentId +
               ", children = " + children +
                '}';
    }
}
