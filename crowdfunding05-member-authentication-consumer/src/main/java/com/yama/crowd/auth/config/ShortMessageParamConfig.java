package com.yama.crowd.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 使用注解@ConfigurationProperties映射
 * 通过注解@ConfigurationProperties(prefix="配置文件中的key的前缀")
 * 可以将配置文件中的配置自动与实体进行映射
 * 自动匹配，容器中的变量进行注入，适合较多的变量的配置
 * 注意：
 *      使用此注解进行映射时，需要通过set方式设置值，所有成员变量需要存在set方法
 */
@Data
@Component
@ConfigurationProperties(prefix = "message")
public class ShortMessageParamConfig {
    private String host;
    private String path;
    private String method;
    private String appcode;
    private String smsSignId;
    private String templateId;
}
