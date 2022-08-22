package com.yama.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CrowdMemberEureka {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMemberEureka.class,args);
    }
}
