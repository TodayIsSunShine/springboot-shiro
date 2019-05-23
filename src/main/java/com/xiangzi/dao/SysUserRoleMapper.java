package com.xiangzi.dao;

import com.xiangzi.model.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper {
    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    void bulkInsert(List<SysUserRole> list);
}