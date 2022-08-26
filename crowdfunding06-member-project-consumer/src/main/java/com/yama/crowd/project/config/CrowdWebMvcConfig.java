package com.yama.crowd.project.config;

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
        // view-controller 是在 project-consumer 内部定义的，所以这里是一个不经过 Zuul访问的地址，
        // 所以这个路径前面不加路由规则中定义的前缀：“/project”
        registry.addViewController("/agree/protocol/page").setViewName("project-agree");
        registry.addViewController("/launch/project/page").setViewName("project-launch");
        registry.addViewController("/return/info/page").setViewName("project-return");
        registry.addViewController("/create/confirm/page.html").setViewName("project-confirm");
        registry.addViewController("/create/success").setViewName("project-success");
    }
}
