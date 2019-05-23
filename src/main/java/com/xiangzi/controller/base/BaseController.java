package com.xiangzi.controller.base;

import com.xiangzi.error.BusinessException;
import com.xiangzi.error.EmBusinessError;
import com.xiangzi.response.CommonReturnType;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * description
 * author:张翔翔
 * Date:2019/5/20
 * Time:14:40
 */
@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(RuntimeException ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return CommonReturnType.create(responseData, "fail");
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException() {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("errCode", EmBusinessError.USER_NOT_LOGIN.getErrCode());
        responseData.put("errMsg", EmBusinessError.USER_NOT_LOGIN.getErrMsg());
        return CommonReturnType.create(responseData, "fail");
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(AuthorizationException ex) {
        System.out.println(ex.getMessage());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("errCode", EmBusinessError.ACCOUNT_NOT_AUTHORIZED.getErrCode());
        responseData.put("errMsg", EmBusinessError.ACCOUNT_NOT_AUTHORIZED.getErrMsg());
        return CommonReturnType.create(responseData, "fail");
    }
}
