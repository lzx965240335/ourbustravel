package com.cykj.service;

import com.cykj.bean.City;
import com.cykj.bean.LayuiJson;

import java.util.Map;

public interface CityService {

    int addCity(City city);
    boolean deleteCity(City city);
    int updateCity(City city);
    LayuiJson selectCity(Map map);
}
