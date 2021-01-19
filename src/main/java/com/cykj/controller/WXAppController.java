package com.cykj.controller;

import com.cykj.bean.City;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.service.CityService;
import com.cykj.service.SiteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/WXAppController")
public class WXAppController {

    //城市对象service
    @Autowired
    private CityService cityService;
    //站点对象
    @Autowired
    private SiteService siteService;


    @RequestMapping("/login")
    public void login(String account){
        System.out.println(account);
    }


    //查找所有城市
    @RequestMapping("/selectCities")
    @ResponseBody
    public List<City> selectCities(){
        System.out.println("查询所有城市");
        LayuiJson layuiJson=cityService.selectCities(null);
        return (List<City>) layuiJson.get("data");
    }

    //查询所有站点
    @RequestMapping("/selectSiteList")
    @ResponseBody
    public List<Site> selectSiteList(){
        LayuiJson layuiJson=siteService.selectAllSite(null);
        return (List<Site>)layuiJson.get("data");
    }



    //路线查询
    @RequestMapping("/selectPath")
    @ResponseBody
    public List<Route> selectPath(String startLon, String startLat, String endLon, String endLat){
        System.out.println("查询站点");
        System.out.println("开始"+startLon+","+startLat);
        System.out.println("结束"+endLon+","+endLat);
        List<Route> routes=new ArrayList<>();
        Route route = new Route();
        route.setRouteName("129");
        route.setRouteId(1);
        List<Site> list = new ArrayList<>();
        String str="118.172796,24.493084,118.172897,24.493031,118.172961,24.493004,118.173218,24.492883,118.173352,24.492829,118.173681,24.492682,118.174218,24.492423,118.174581,24.49227" +
                ",118.174892,24.492126,118.174966,24.491899,118.175002,24.491604";
        String[] strs=str.split(",");
        for (int i = 0; i < strs.length; i+=2) {
            Site site = new Site();
            site.setLongitude(strs[i]);
            site.setLatitude(strs[i+1]);
            list.add(site);
        }
        route.setSites(list);
        routes.add(route);
        return routes;
    }

}
