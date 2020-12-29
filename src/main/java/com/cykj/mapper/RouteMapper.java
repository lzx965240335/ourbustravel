package com.cykj.mapper;

import com.cykj.bean.Route;

public interface RouteMapper {
    int deleteByPrimaryKey(Integer routeid);

    int insert(Route record);

    int insertSelective(Route record);

    Route selectByPrimaryKey(Integer routeid);

    int updateByPrimaryKeySelective(Route record);

    int updateByPrimaryKey(Route record);
}