package com.cykj.mainmethod;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.cykj.mapper")
@ComponentScan({"com.cykj"})
public class OurbustravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurbustravelApplication.class, args);
    }

}
