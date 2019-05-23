package com.xiangzi.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description
 * author:张翔翔
 * Date:2019/5/21
 * Time:17:00
 */
@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return registration;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      /*  registry.addInterceptor(new MyHandlerInterceptor()).
                addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/genCaptcha").excludePathPatterns("/static/**")
                .excludePathPatterns("/logout");*/

    }
}
