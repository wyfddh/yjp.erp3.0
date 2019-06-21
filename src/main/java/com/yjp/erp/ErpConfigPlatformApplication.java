package com.yjp.erp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@MapperScan("com.yjp.erp.mapper")
public class ErpConfigPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErpConfigPlatformApplication.class, args);
    }

}
