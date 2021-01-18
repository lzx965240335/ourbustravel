package com.cykj.controller;

import com.cykj.bean.AdminInf;
import com.cykj.bean.MenuInf;
import com.cykj.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RequestMapping(value = "/MenuController", produces = "application/json;charset=UTF-8")
@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;


    //动态菜单
    @RequestMapping("/initMenu")
    @ResponseBody
    public ModelAndView initMenu(HttpServletRequest request) {
        ModelAndView modelAndView=new ModelAndView();
        AdminInf adminInf = (AdminInf) request.getSession().getAttribute("adminInf");
        HashMap<String,List<MenuInf>>hashMap= menuService.getMenuByRoleId(adminInf.getRoleId());
        modelAndView.setViewName("AfterView");
        modelAndView.addObject("hasMap",hashMap);
        return  modelAndView;
    }

    //查询所有菜单（权限分配）
    @RequestMapping("/allMenu")
    @ResponseBody
    public HashMap<String,List<MenuInf>> allMenu()  {
        return menuService.findMenus();
    }

    //查询角色所有菜单（权限分配）
    @RequestMapping("/allMenuByRole")
    @ResponseBody
    public HashMap<String, List<MenuInf>> allMenuByRole(HttpServletRequest req) {
        String roleId = req.getParameter("roleId");
        return menuService.initMenu(Integer.parseInt(roleId));
    }

    //修改权限
    @RequestMapping("/updateRole")
    @ResponseBody
    public String updateRole(HttpServletRequest req)  {
        String[] sourceArr = req.getParameter("menuIds").split(",");
        String roleId = req.getParameter("roleId");
        String state = req.getParameter("state");
        boolean b = menuService.updateRole(Integer.parseInt(roleId), sourceArr,Integer.parseInt(state));
        return b ? "修改成功" : "修改失败";
    }


}

