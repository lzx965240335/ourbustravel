package com.cykj.mapper;

import com.cykj.bean.Route;
import com.cykj.util.LayuiJson;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RouteMapper {
   List<Route> getRoutes();

   //条件查询线路
   List<Route> selectRoutes(Map map);

   //查询某条线路

   //条件查询线路总数
   int routeCount(Map map);

   //增加线路
   int addRoute(Route route);

   //获取当前表的最大自增长id
   int getMaxId(String tableName);
}