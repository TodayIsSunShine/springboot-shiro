package com.xiangzi.utils;


import com.xiangzi.model.UserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 *
 * @author
 * @email
 */
public class ShiroUtils {
    /**
     * 加密算法
     */
    public final static String hashAlgorithmName = "MD5";
    /**
     * 循环次数
     */
    public final static int hashIterations = 2;

    public static String MD5(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static UserInfo getUserEntity() {
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }

    public static Integer getUserId() {
        return getUserEntity().getUid();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static void main(String[] args) {
        String salt = RandomStringUtils.randomAlphanumeric(30);
        String newSalt = Encodes.encodeHex(salt.getBytes());
        String password = MD5("123456", newSalt);
        System.out.println("newSalt:" + newSalt);
        System.out.println("password:" + password);
    }

}
