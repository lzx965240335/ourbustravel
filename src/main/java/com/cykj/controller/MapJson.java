package com.cykj.controller;

import com.cykj.service.CityService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/getMap")
public class MapJson {
    @Autowired
    private CityService cityService;
    @RequestMapping("/getCity")
    @ResponseBody
    public List findPage(){
        LayuiJson layuiJson=cityService.getCity(null);
        return (List) layuiJson.get("data");
    }
}
