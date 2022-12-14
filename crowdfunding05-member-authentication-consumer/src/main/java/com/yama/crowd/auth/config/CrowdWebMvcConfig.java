package com.yama.crowd.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 在springboot中使用配置集成接口的方式，实现view-controller的效果
 * 用来，当点击注册按扭时将页面转发到注册页面，之后提交注册，通过ajax获取验证码，并保存到redis中
 */
@Configuration//通过配置类的形式，实现view-controller的效果
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器访问的地址
        String urlPath1="/auth/member/to/reg/page";

        String urlPath2 = "/auth/member/to/login/page";
        //目标视图的名称
        String viewName1="member-reg";
        String viewName2="member-login";

        //添加一个view-controller
        registry.addViewController(urlPath1).setViewName(viewName1);

        registry.addViewController(urlPath2).setViewName(viewName2);
        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
        registry.addViewController("/member/my/crowd").setViewName("member-crowd");
    }
}
