package com.cykj.controller;

import com.cykj.bean.City;
import com.cykj.bean.LayuiJson;
import com.cykj.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/background")
public class CityController {

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/getCityView")
    public String getCiteView() {
        return "cityConfiguration";
    }

//    @RequestMapping(value = "/getCityView")
//    public String getCiteView() {
//        return "city";
//    }

    @RequestMapping(value = "/getTable")
    @ResponseBody
    public LayuiJson getTableData(HttpServletRequest req) {
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String cityName = req.getParameter("cityName");
        String cityCode = req.getParameter("cityCode");
        int start = (page - 1) * limit;
        HashMap<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", limit);
        if (cityName != null && !cityName.equals("")) {
            map.put("cityName", cityName);
        }
        if (cityCode != null && !cityCode.equals("")) {
            map.put("cityCode", cityCode);
        }
        LayuiJson<City> LayuiJson = cityService.selectCity(map);
        return LayuiJson;
    }

    /**
     * 删除
     *
     * @param req
     * @return
     */
    @RequestMapping("/deleteCity")
    @ResponseBody
    public String deleteCity(HttpServletRequest req) {
        City city = new City();
        city.setCityId(Integer.parseInt(req.getParameter("cityId")));
        boolean deleteResult = cityService.deleteCity(city);
        return deleteResult == true ? "删除成功" : "删除失败";
    }

    /**
     * 新增
     *
     * @param req
     * @param city
     * @return
     */
    @RequestMapping("/addCity")
    @ResponseBody
    public String addCity(HttpServletRequest req, @RequestBody City city) {
        int addResult = cityService.addCity(city);
        return addResult > 0 ? "添加成功" : "添加失败";
    }

    /**
     * 修改
     *
     * @param req
     * @param city
     * @return
     */
    @RequestMapping("/updateCity")
    @ResponseBody
    public String updateCity(HttpServletRequest req,@RequestBody City city){
        int updateResult = cityService.updateCity(city);
        return updateResult > 0 ? "修改成功" : "修改失败";
    }

}
