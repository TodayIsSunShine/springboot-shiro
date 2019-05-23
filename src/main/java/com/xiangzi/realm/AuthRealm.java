package com.xiangzi.realm;

import com.xiangzi.dao.SysPermissionMapper;
import com.xiangzi.dao.SysRoleMapper;
import com.xiangzi.dao.UserInfoMapper;
import com.xiangzi.model.SysPermission;
import com.xiangzi.model.SysRole;
import com.xiangzi.model.UserInfo;
import com.xiangzi.utils.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description
 * author:张翔翔
 * Date:2019/5/20
 * Time:15:38
 */
public class AuthRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRealm.class);

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOGGER.info("权限配置--> AuthRealm.doGetAuthorizationInfo() ");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo user = (UserInfo) principalCollection.getPrimaryPrincipal();
        List<SysRole> roles = sysRoleMapper.findRoles(user.getUid());
        for (SysRole role : roles) {
            if (null != role) {
                //添加角色
                authorizationInfo.addRole(role.getName());
            }
            //根据角色id查询权限
            List<SysPermission> sysPermissionList = sysPermissionMapper.findPermissions(role.getId());
            for (SysPermission permission : sysPermissionList) {
                if (null != permission) {
                    //添加权限
                    authorizationInfo.addStringPermission(permission.getPermission());
                }
            }
        }
        return authorizationInfo;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //获取用户输入的用户名
        String username = (String) token.getPrincipal();
        UserInfo userInfo = userInfoMapper.queryUserInfoByUsername(username);
        LOGGER.info("----->> useInfo=" + userInfo);
        if (null == userInfo) {
            throw new UnknownAccountException();
        }
        if (userInfo.getState().equals(1)) {
            throw new DisabledAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                userInfo.getPassword(), //密码
                ByteSource.Util.bytes(userInfo.getSalt()), //hex编码
                getName()
        );

        return authenticationInfo;
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}
