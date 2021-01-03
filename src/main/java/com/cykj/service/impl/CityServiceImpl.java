package com.cykj.service.impl;

import com.cykj.bean.City;

import com.cykj.mapper.CityMapper;
import com.cykj.service.CityService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityMapper cityMapper;


    @Override
    public int addCity(City city) {
        return cityMapper.addCity(city) ;
    }

    @Override
    public boolean deleteCity(City city) {
//        int deleteCity = cityMapper.deleteCity(city.getCityId());
//        return deleteCity>0;
        return cityMapper.deleteCity(city.getCityId())>0;
    }

    @Override
    public int updateCity(City city) {
        return cityMapper.updateCity(city);
    }

    @Override
    public LayuiJson selectCities(Map map) {
        List<City> cities = cityMapper.selectCities(map);
        int count = cityMapper.findCities(map);
        return new LayuiJson(count,cities);
    }
}
