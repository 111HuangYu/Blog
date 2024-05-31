package com.my.blog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private String createBy;
    private String createTime;
    private String delFlag;
    private String id;
    private String remark;
    private String roleKey;
    private String roleName;
    private String roleSort;
    private String status;
    private String updateBy;

}
