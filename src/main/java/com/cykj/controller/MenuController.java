package com.cykj.controller;

import com.cykj.bean.MenuInf;
import com.cykj.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@RequestMapping(value = "/hello")
@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;


    //初始化角色菜单
    @RequestMapping("/initMenu")
    @ResponseBody
    public ModelAndView initMenu() {
        ModelAndView modelAndView=new ModelAndView();
        HashMap<String, List<MenuInf>>hashMap= menuService.findMenus();
        modelAndView.setViewName("AfterView");
        modelAndView.addObject("hasMap",hashMap);
        return  modelAndView;
    }


}
