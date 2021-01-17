package com.cykj.service;

import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.util.LayuiJson;

import java.util.List;
import java.util.Map;

public interface RouteService {
    //条件查询线路
    LayuiJson selectRoutes(Map map);

    //增加线路
    boolean addRoute(Route route);
}
