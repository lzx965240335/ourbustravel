package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.cykj.bean.DrivSign;
import com.cykj.bean.Driver;
import com.cykj.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 安详的苦丁茶
 * @date 2020/4/30 11:46
 */

@RestController
@RequestMapping("/driver")
public class WXDriverController {

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    @Autowired
    DriverService driverService;


    @PostMapping("/doLogin")
    public Map doLogin(String phone, String password){
        Map map = new HashMap();
        Driver driver = driverService.login(phone,password);
        if ((driver!=null)){
            map.put("code",200);
            map.put("result","登录成功");
            map.put("loginner",driver);
            System.out.println("登录成功");
        }else {
            map.put("result","no");
        }
        return map;
    }
    @PostMapping("/getSigns")
    public Map getSigns(String year, String month,int days,int drivId){
        System.out.println(drivId);
        Map map = new HashMap();
        Driver driver = driverService.getSigns(year,month,days,drivId);
        System.out.println(JSON.toJSONString(driver));
        if ((driver!=null)){
            map.put("code",200);
            map.put("result","获取成功");
            map.put("loginner",driver);
            System.out.println("获取成功");
        }else {
            map.put("result","no");
        }
        return map;
    }

    @PostMapping("/do_clock")
    public String getSigns(int drivId){
        driverService.do_clock(drivId);

        return null;
    }
}
