package com.xiangzi.error;

/**
 * description  返回错误码
 * Date:2019/5/7
 * Time:16:54
 */
public enum EmBusinessError implements CommonError {

    // 通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOWN_ERROR(10002, "未知错误"),

    // 20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "该账号暂未注册,请先去注册"),
    USER_LOGIN_FAIL(20002, "用户名或密码错误"),
    USER_NOT_LOGIN(20003, "用户还未登录"),
    ACCOUNT_NOT_AVAILABLE(20004, "账号被禁用,详情请联系管理员"),
    USER_ALREADY_REGISTER(20005, "手机号或邮箱已经被注册,请更换"),
    ACCOUNT_NOT_AUTHORIZED(20006, "你没有权限,请联系管理员!"),

    // 30000开头为交易信息错误定义
    STOCK_NOT_ENOUGH(30001, "库存不足"),
    ORDER_CREATE_FAIL(30002, "创建订单失败"),
    ORDER_DELETE_FAIL(30003, "订单删除失败");


    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    private int errCode;
    private String errMsg;


    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
