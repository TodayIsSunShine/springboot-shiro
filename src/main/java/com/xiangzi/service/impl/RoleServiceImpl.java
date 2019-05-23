package com.xiangzi.service.impl;

import com.xiangzi.dao.SysRoleMapper;
import com.xiangzi.model.SysRole;
import com.xiangzi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * description
 * author:张翔翔
 * Date:2019/5/21
 * Time:9:58
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleAdd(SysRole sysRole) {
        sysRole.setCreatedTime(new Date());
        sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleEdit(SysRole sysRole) {
        sysRole.setModifyTime(new Date());
        sysRole.setId(sysRole.getId());
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }
}
