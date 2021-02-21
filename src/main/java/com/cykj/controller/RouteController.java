package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.service.RouteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//线路控制成
@Controller
@RequestMapping(value = "/routeController")
public class RouteController {

    @Autowired
    private RouteService routeService;

    //查找线路
    @RequestMapping("/routeList")
    @ResponseBody
    public LayuiJson routeList(HttpServletRequest req) {
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String routeName = req.getParameter("routeName");
        String startName = req.getParameter("startName");
        String endName = req.getParameter("endName");

        int start = (page - 1) * limit;
        HashMap<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", limit);
        if (routeName != null && !routeName.equals("")) {
            map.put("routeName", routeName);
        }
        if (startName != null && !startName.equals("")) {
            map.put("startName", startName);
        }

        if (endName != null && !endName.equals("")) {
            map.put("endName", endName);
        }
        LayuiJson layuiJson = routeService.selectRoutes(map);

        return layuiJson;
    }

    //保存新增线路
    @RequestMapping("/addRoute")
    @ResponseBody
    public boolean addRoute(@RequestBody Route route){
        return routeService.addRoute(route);
    }


    //修改线路页面
    @RequestMapping("/updateRoutePage")
    public String updateRoutePage(Model model,String routeId){
        List<Site> sites = routeService.getSites(routeId);
        Route route = routeService.getRouteById(routeId);
        System.out.println("=============================="+sites.size());
        System.out.println(JSON.toJSONString(sites));
        model.addAttribute("sites",sites);
        model.addAttribute("routeId",routeId);
        model.addAttribute("routeName",route.getRouteName());
        return "DrawRoute";
    }

    //修改线路页面
    @RequestMapping("/updateRoute")
    @ResponseBody
    public  List<Site> updateRoute(String routeId){
        System.out.println(routeId);
        return routeService.getPosition(routeId);
    }

    //保存修改
    //保存新增线路
    @RequestMapping("/updateRouteById")
    @ResponseBody
    public boolean updateRouteById(@RequestBody Route route){
        System.out.println("进来修改！");
        return routeService.updateRoute(route);
    }




    //删除线路
    @RequestMapping("/deleteRoute")
    @ResponseBody
    public boolean deleteRoute(String routeId){
        System.out.println(routeId);
        return routeService.deleteRoute(routeId);
    }
    //查找线路
    @RequestMapping("/getRoutes")
    @ResponseBody
    public List<Route> getRoutes(String[] startPos, String[] endPos) {
        return routeService.getRoutes(startPos,endPos);
    }

    //查找线路
    @RequestMapping("/Routes")
    @ResponseBody
    public List<Route> Routes() {
        return routeService.Routes();
    }
}
