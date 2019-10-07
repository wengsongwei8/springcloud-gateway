package com.weng.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动类
 *
 **/
@EnableZuulProxy
@EnableFeignClients
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
	}
}
