package com.yama.crowd.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yama.crowd.api")
@SpringBootApplication
public class CrowdMemberProject {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMemberProject.class,args);
    }
}
