package com.tah.graduate_run;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.tah.graduate_run.mapper")
public class GraduateRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraduateRunApplication.class, args);
    }
}
