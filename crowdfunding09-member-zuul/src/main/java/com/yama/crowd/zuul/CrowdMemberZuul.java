package com.yama.crowd.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class CrowdMemberZuul {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMemberZuul.class,args);
    }
}
