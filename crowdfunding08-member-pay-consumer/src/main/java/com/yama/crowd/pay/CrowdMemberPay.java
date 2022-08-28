package com.yama.crowd.pay;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.yama.crowd.api")
public class CrowdMemberPay {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMemberPay.class);
    }
}
