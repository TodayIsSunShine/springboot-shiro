package com.xiangzi.service.impl;

import com.xiangzi.dao.SysUserRoleMapper;
import com.xiangzi.dao.UserInfoMapper;
import com.xiangzi.error.BusinessException;
import com.xiangzi.error.EmBusinessError;
import com.xiangzi.model.SysUserRole;
import com.xiangzi.model.UserInfo;
import com.xiangzi.service.UserService;
import com.xiangzi.utils.Encodes;
import com.xiangzi.utils.ShiroUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * description
 * author:张翔翔
 * Date:2019/5/20
 * Time:16:03
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userAdd(UserInfo userInfo) throws BusinessException {
        userInfo.setState(new Byte("0"));
        userInfo.setCreatedTime(new Date());
        String salt = Encodes.encodeHex(RandomStringUtils.randomAlphanumeric(30).getBytes());
        userInfo.setSalt(salt);
        userInfo.setPassword(ShiroUtils.MD5(userInfo.getPassword(), salt));
        try {
            userInfoMapper.insertSelective(userInfo);
            List<SysUserRole> userRoleList = userInfo.getUserRoleList();
            if (CollectionUtils.isEmpty(userRoleList)) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
            }
            userRoleList.forEach(u -> {
                u.setUid(userInfo.getUid());
                u.setRoleId(u.getRoleId());
            });
            sysUserRoleMapper.bulkInsert(userRoleList);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "该账号已存在");
        }

    }

    @Override
    public UserInfo findUserByUserId(Integer userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }
}
