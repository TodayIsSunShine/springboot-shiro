package com.xiangzi.controller;

import com.xiangzi.controller.base.BaseController;
import com.xiangzi.model.SysRole;
import com.xiangzi.response.CommonReturnType;
import com.xiangzi.service.RoleService;
import com.xiangzi.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 * author:张翔翔
 * Date:2019/5/21
 * Time:9:57
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService service;


    @PostMapping("/add")
    public CommonReturnType roleAdd(@RequestBody SysRole sysRole) {
        service.roleAdd(sysRole);
        return CommonReturnType.create(null);
    }

    @PostMapping("/edit")
    public CommonReturnType roleEdit(@RequestBody SysRole sysRole) {
        service.roleEdit(sysRole);
        return CommonReturnType.create(null);
    }
}
