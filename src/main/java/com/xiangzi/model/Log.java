package com.xiangzi.model;

import lombok.Data;

import java.util.Date;

@Data
public class Log {
    private Long id;
    private String type;
    private String title;
    private String remoteAddr;
    private String username;
    private String requestUri;
    private String httpMethod;
    private String classMethod;
    private String sessionId;
    private Long useTime;
    private String browser;
    private String area;
    private String province;
    private String city;
    private String isp;
    private String createBy;
    private Date createDate;
    private Long updateBy;
    private Date updateDate;
    private String remarks;
    private Boolean delFlag;
    private String params;
    private String response;
    private String exception;

}