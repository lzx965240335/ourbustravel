package com.cykj.mainmethod;

import com.cykj.bean.BusInf;
import com.cykj.util.BusTravel;
import com.cykj.util.BusTravelTime;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.cykj.mapper")
@ComponentScan({"com.cykj"})
//@EnableScheduling//开启定时任务
public class OurbustravelApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OurbustravelApplication.class, args);
        BusTravelTime bean = run.getBean(BusTravelTime.class);
        bean.initBus();
        bean.startTravel();
    }

}
