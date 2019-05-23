package com.xiangzi.service;

import com.xiangzi.model.UserInfo;

/**
 * description
 * author:张翔翔
 * Date:2019/5/20
 * Time:16:03
 */
public interface UserService {

    void userAdd(UserInfo userInfo);

    UserInfo findUserByUserId(Integer userId);
}
