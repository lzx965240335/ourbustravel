package com.cykj.service;

import com.cykj.util.LayuiJson;

import java.util.HashMap;

public interface CityService {
    public LayuiJson getCity(HashMap<String, Object> condition);
}
