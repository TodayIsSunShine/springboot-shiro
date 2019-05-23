package com.xiangzi.response;

import lombok.Getter;
import lombok.Setter;

/**
 * description
 * author:张翔翔
 * Date:2019/5/20
 * Time:14:42
 */
@Getter
@Setter
public class CommonReturnType {

    private Integer code;
    private String status;
    private Object data;
    private String message;

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, 0, "success");
    }

    public static CommonReturnType create(Object result, int code, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setCode(code);
        type.setStatus(status);
        return type;
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

    public static CommonReturnType failure(String message) {
        CommonReturnType type = new CommonReturnType();
        type.setCode(-1);
        type.setStatus("failure");
        type.setMessage(message);
        return type;
    }
}
