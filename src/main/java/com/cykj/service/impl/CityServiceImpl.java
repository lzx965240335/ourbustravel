package com.cykj.service.impl;

import com.cykj.bean.City;
import com.cykj.mapper.CityMapper;
import com.cykj.service.CityService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


@Component
public class CityServiceImpl implements CityService {
    @Autowired
    private CityMapper cityMapper;
    @Override
    public LayuiJson getCity(HashMap<String, Object> condition) {
        List<City> cities = null;
        int totalRecords = 0;
        LayuiJson layuiJson = null;
        cities = cityMapper.getCity(condition);
        totalRecords = cityMapper.findCity(condition);
        layuiJson = new LayuiJson(totalRecords, cities);
        return layuiJson;
    }
}
