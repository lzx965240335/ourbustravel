package com.cykj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.RouteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//线的Service
@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private SiteMapper siteMapper;

    //条件查询线路
    public LayuiJson selectRoutes(Map map){
        List<Route> routes = routeMapper.selectRoutes(map);
        int count = routeMapper.routeCount(map);
        return new LayuiJson(count,routes);
    }



    @Override
    public boolean addRoute(Route route) {
        int maxId = routeMapper.getMaxId("t_site");

        StringBuilder position= new StringBuilder(maxId + "#");
        for (int i = 1; i < route.getSites().size(); i++) {
            position.append(maxId + i);
            if (i<route.getSites().size()-1){
                position.append("#");
            }
        }
        System.out.println(position.toString());
        route.setPosition(position.toString());
        return routeMapper.addRoute(route)>0;
    }
}
