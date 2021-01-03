package com.cykj.service;

import com.cykj.bean.City;
import com.cykj.util.LayuiJson;


import java.util.Map;

public interface CityService {

    int addCity(City city);
    boolean deleteCity(City city);
    int updateCity(City city);
    LayuiJson selectCities(Map map);
}
