package com.cykj.mapper;

import com.cykj.bean.City;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface CityMapper {

    //新增城市
    int addCity(City city);

    //修改城市信息
    int updateCity(City city);

    //删除城市信息
    int deleteCity(int cityId);

    //查询数据库所有的数据,分页
    List<City> selectCities(Map<String, Object> condition);

    //总数
    int findCities(Map<String, Object> condition);

    //查询所有城市
    List<City> getCities(Map<String, Object> condition);
}