package com.cykj.mapper;

import com.cykj.bean.City;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CityMapper {
    int deleteByPrimaryKey(Integer cityid);

    int insert(City record);

    int insertSelective(City record);

    City selectByPrimaryKey(Integer cityid);

    int updateByPrimaryKeySelective(City record);

    int updateByPrimaryKey(City record);

    //新增城市
    int addCity(City city);

    //修改城市信息
    int updateCity(City city);

    //删除城市信息
    int deleteCity(int cityId);

    //查询数据库所有的数据,分页
    List<City> selectCity(Map map);

    //总数
    int count(Map map);



}