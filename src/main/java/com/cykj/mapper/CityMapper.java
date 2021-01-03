package com.cykj.mapper;

import com.cykj.bean.City;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CityMapper {
    List<City> getCity(HashMap<String, Object> condition);

    int findCity(HashMap<String, Object> condition);
}
