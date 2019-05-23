package com.xiangzi.controller;

import com.xiangzi.annotation.SysLog;
import com.xiangzi.controller.base.BaseController;
import com.xiangzi.model.UserInfo;
import com.xiangzi.response.CommonReturnType;
import com.xiangzi.service.UserService;
import com.xiangzi.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description
 * author:张翔翔
 * Date:2019/5/21
 * Time:10:28
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @SysLog("保存用户")
    @PostMapping("/add")
    @RequiresPermissions("userInfo:add")
    public CommonReturnType userAdd(@RequestBody UserInfo userInfo) {
        userInfo.setCreatedBy(ShiroUtils.getUserId());
        userService.userAdd(userInfo);
        return CommonReturnType.create(null);
    }
}
