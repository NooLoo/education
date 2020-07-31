package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        //跳转到登录页面
        String url = "/member/to/login/page.html";
        String view = "member-login";
        registry.addViewController(url).setViewName(view);

        //访问主页面
        url = "/index.html";
        view = "portal-page";
        registry.addViewController(url).setViewName(view);

        // 跳转到同意协议页面
        url = "/project/to/agree/page";
        view = "project-1-start";
        registry.addViewController(url).setViewName(view);
    }

}
