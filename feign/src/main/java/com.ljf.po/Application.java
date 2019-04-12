package com.ljf.po;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringCloudApplication
@EnableFeignClients//开启OpenFeign
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
