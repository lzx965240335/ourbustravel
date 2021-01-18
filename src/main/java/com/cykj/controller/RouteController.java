package com.cykj.controller;

import com.cykj.bean.Route;
import com.cykj.service.RouteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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



//    //查找线路
//    @RequestMapping("/routeList")
//    @ResponseBody
//    public Map<Integer, List<Route>> getRoutes(String startId, String endId) {
//        return routeService.getRoutes(startId,endId);
//    }
}
