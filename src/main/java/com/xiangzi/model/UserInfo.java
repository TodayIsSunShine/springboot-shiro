package com.xiangzi.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserInfo {
    private Integer uid;
    private String password;
    private String salt;
    private Byte state;
    private String username;
    private Date createdTime;
    private Date modifyTime;
    private Integer createdBy;
    private Integer modifyBy;
    private List<SysUserRole> userRoleList;
    private String validateCode;

}