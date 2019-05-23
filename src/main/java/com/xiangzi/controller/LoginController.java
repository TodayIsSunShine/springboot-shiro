package com.xiangzi.controller;

import com.xiangzi.controller.base.BaseController;
import com.xiangzi.error.BusinessException;
import com.xiangzi.error.EmBusinessError;
import com.xiangzi.model.UserInfo;
import com.xiangzi.response.CommonReturnType;
import com.xiangzi.utils.Constants;
import com.xiangzi.utils.ShiroUtils;
import com.xiangzi.utils.VerifyCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * description
 * author:张翔翔
 * Date:2019/5/20
 * Time:16:04
 */
@RestController
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);


    /**
     * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
     */
    @GetMapping("/genCaptcha")
    public void genCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, null);
        //将验证码放到HttpSession里面
        request.getSession().setAttribute(Constants.VALIDATE_CODE, verifyCode);
        LOGGER.info("本次生成的验证码为[" + verifyCode + "],已存放到HttpSession中");
        //设置输出的内容的类型为JPEG图像
        response.setContentType("image/jpeg");
        BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 116, 36, 5, true, new Color(249, 205, 173), null, null);
        //写给浏览器
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
    }


    @PostMapping("/login")
    public CommonReturnType login(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        if (StringUtils.isBlank(userInfo.getUsername()) || StringUtils.isBlank(userInfo.getPassword())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名或密码不能为空");
        }
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("exception-->" + exception);
        //从session中取出验证码
        String verifyCode = (String) ShiroUtils.getSession().getAttribute(Constants.VALIDATE_CODE);
    /*    if (StringUtils.isBlank(verifyCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "验证码超时");
        }
        if (StringUtils.isBlank(userInfo.getValidateCode()) || !userInfo.getValidateCode().equalsIgnoreCase(verifyCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "验证码输入错误");
        }*/
        String error = null;
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userInfo.getUsername(), userInfo.getPassword());
            subject.login(usernamePasswordToken);
        } catch (IncorrectCredentialsException e) {
            error = "账号或密码错误";
        } catch (UnknownAccountException e) {
            error = "账号不存在";
        } catch (LockedAccountException e) {
            error = "账号被锁定,请联系管理员";
        } catch (DisabledAccountException e) {
            error = "账号被禁用";
        } catch (UnauthorizedException e) {
            error = "你没有得到相应的授权";
        }
        if (StringUtils.isBlank(error)) {
            return CommonReturnType.create(null);
        } else {
            return CommonReturnType.failure(error);
        }
    }

    @RequestMapping("/logout")
    public CommonReturnType logout() {
        ShiroUtils.logout();
        return CommonReturnType.create(null);
    }
}
