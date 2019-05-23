package com.xiangzi.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * description
 * author:zhangxx
 * Date:2019/5/23
 * Time:13:52
 */
@Configuration
public class FreeMarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct //被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，
    // 并且只会被服务器调用一次，类似于Servlet的init()方法。被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
    public void setSharedVariable() {
        try {
            configuration.setSharedVariable("shiro", new ShiroTags());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
