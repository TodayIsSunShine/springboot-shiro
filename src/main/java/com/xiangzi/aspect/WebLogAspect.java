package com.xiangzi.aspect;

import com.alibaba.fastjson.JSONObject;
import com.xiangzi.annotation.SysLog;
import com.xiangzi.dao.LogMapper;
import com.xiangzi.model.Log;
import com.xiangzi.utils.ShiroUtils;
import com.xiangzi.utils.ToolUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * description
 * author:zhangxx
 * Date:2019/5/23
 * Time:9:30
 */
@Component
@Order(1)
@Aspect
public class WebLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private Log sysLog = null;
    @Autowired
    private LogMapper logMapper;

    @Pointcut("@annotation(com.xiangzi.annotation.SysLog)")
    public void webLog() {
    }

    @Before("webLog()")
    public void before(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        //接收到请求,记录请求信息
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        sysLog = new Log();
        //请求的方法名
        LOGGER.info("typeName:{}" + joinPoint.getSignature().getDeclaringTypeName());
        LOGGER.info("name:{}" + joinPoint.getSignature().getName());
        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //请求方式 (post get...)
        sysLog.setHttpMethod(request.getMethod());
        //获取传入的参数
        Object[] args = joinPoint.getArgs();
        if (null != args) {
            for (int i = 0; i < args.length; i++) {
                Object o = args[i];
                if (o instanceof ServletRequest || (o instanceof ServletResponse) || o instanceof MultipartFile) {
                    args[i] = o.toString();
                }
            }
        }
        String str = JSONObject.toJSONString(args);
        //参数
        sysLog.setParams(str.length() > 5000 ? JSONObject.toJSONString("请求数据太长不过与显示") : str);
        //获取ip地址
        String ip = ToolUtil.getClientIp(request);
        if ("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        //ip地址
        sysLog.setRemoteAddr(ip);
        //请求地址
        sysLog.setRequestUri(request.getRequestURL().toString());
        //jsessionid
        if (session != null) {
            sysLog.setSessionId(session.getId());
        }
        //获取注解上的值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog myLog = method.getAnnotation(SysLog.class);
        if (myLog != null) {
            sysLog.setTitle(myLog.value());
        }
        Map<String, String> osAndBrowserInfo = ToolUtil.getOsAndBrowserInfo(request);
        sysLog.setBrowser(osAndBrowserInfo.get("os") + "-" + osAndBrowserInfo.get("browser"));
        if (!"127.0.0.1".equals(ip)) {
            Map<String, String> map = (Map<String, String>) session.getAttribute("addressIp");
            if (map == null) {
                map = ToolUtil.getAddressByIP(ToolUtil.getClientIp(request));
                session.setAttribute("addressIp", map);
            }
            sysLog.setArea(map.get("area"));
            sysLog.setProvince(map.get("province"));
            sysLog.setCity(map.get("city"));
            sysLog.setIsp(map.get("isp"));
        }
        //请求类型
        sysLog.setType(ToolUtil.isAjax(request) ? "ajax请求" : "普通请求");
        sysLog.setUsername(ShiroUtils.getUsername());
        sysLog.setCreateBy(ShiroUtils.getUserId().toString());
        sysLog.setCreateDate(new Date());
        sysLog.setDelFlag(true);
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object obj = proceedingJoinPoint.proceed();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            sysLog.setException(e.getMessage());
            throw e;
        }
    }

    @AfterReturning(returning = "object", pointcut = "webLog()")
    public void doAfterReturning(Object object) {
        String responseStr = JSONObject.toJSONString(object);
        sysLog.setResponse(responseStr.length() > 5000 ? JSONObject.toJSONString("返回数据太长不予显示") : responseStr);
        sysLog.setUseTime(System.currentTimeMillis() - startTime.get());
        logMapper.insertSelective(sysLog);

    }
}
