package com.yama.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.yama.crowd.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class CrowdMemberMysql {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMemberMysql.class,args);
    }
}
