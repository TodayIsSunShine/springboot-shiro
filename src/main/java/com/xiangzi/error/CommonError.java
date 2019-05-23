package com.xiangzi.error;

/**
 * description
 * Date:2019/5/7
 * Time:16:51
 */
public interface CommonError {

    public int getErrCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);
}
