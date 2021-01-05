package com.cykj.mapper;

import com.cykj.bean.Route;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RouteMapper {
   List<Route> getRoutes(Map map);
}