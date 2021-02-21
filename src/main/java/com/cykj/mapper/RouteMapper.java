package com.cykj.mapper;

import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.util.LayuiJson;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RouteMapper {
   List<Route> getRoutes(Map map);

   //条件查询线路
   List<Route> selectRoutes(Map map);

   //条件查询线路总数
   int routeCount(Map map);

   //增加线路
   int addRoute(Route route);

   //获取当前表的最大自增长id
   int getMaxId(String tableName);

   //根据线路查询所有点id
   public String getSiteId(int routeId);

   //根据线路查询所有点id
   public String getPositionId(int routeId);
   //根据点Id查询所有坐标
   public List<Site> getPosition(List<Integer> list);

   //根据id查route
   Route getRouteById(int routeId);

   //删除线路
   int deleteRoute(Map map);

   //查询某条线路
   List<Route> getRoutes();

   int updateRoute(Map map);

   //查找所有线路（包含时刻表）
   List<Route> selectRouteAll();

   //根据id查找线路，包含时刻表
   Route selectRouteTimeById(int routeId);
}