package com.cykj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/page")
public class GetPage {
    @RequestMapping("/whatPage")
    public String findPage(@RequestParam String pageName){
        System.out.println(pageName);
        return pageName;
    }
}
