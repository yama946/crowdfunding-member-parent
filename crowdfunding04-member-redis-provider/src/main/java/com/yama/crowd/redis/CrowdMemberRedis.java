package com.yama.crowd.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CrowdMemberRedis {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMemberRedis.class,args);
    }
}
