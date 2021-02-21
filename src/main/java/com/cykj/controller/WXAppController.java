package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cykj.bean.*;
import com.cykj.mapper.BusInfMapper;
import com.cykj.service.*;
import com.cykj.util.LayuiJson;
import com.cykj.util.wxapp.AesUtil;
import com.cykj.util.wxapp.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/WXAppController")
public class WXAppController {

    //城市对象service
    @Autowired
    private CityService cityService;
    //站点对象
    @Autowired
    private SiteService siteService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private BusService busService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserInfService userInfService;

    /**
     * 获取微信小程序的openid
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getOpenid")
    public Map<String,Object> getOpenid(String code, String encryptedData, String iv ){
        Map<String, Object> map = userInfService.getOpenid(code, encryptedData, iv);
        int status = (int) map.get("status");
        if (status==1){
            Map<String,Object> userInfo = (Map<String, Object>) map.get("userInfo");
            String openId = (String) userInfo.get("openId");
            HashMap<String, Object> map1 = userInfService.wxLogin(openId);
            return map1;
        }
        return map;
    }

//    登录
    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object> login(String openId){
        return userInfService.wxLogin(openId);
    }


    //获取文章信息
    @RequestMapping("/getArticles")
    @ResponseBody
    public List<Article> getArticles(String page){
      return articleService.selectArticles(page);
    }


    //获取文章信息
    @RequestMapping("/getArticleById")
    @ResponseBody
    public Article       getArticleById(String articleId){
        return articleService.selectArticleById(articleId);
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

    //查询所有站点
    @RequestMapping("/selectPaths")
    @ResponseBody
    public List<Map<String, Object>> selectPaths(String startSites, String endSites){
        List<Site> startList = JSONArray.parseArray(startSites, Site.class);
        List<Site> endList = JSONArray.parseArray(endSites, Site.class);
        System.out.println(JSON.toJSON(startList));
        System.out.println("------------------------------------------");
        System.out.println(JSON.toJSON(endList));
        return routeService.selectRoutePath(startList, endList);
    }


    //获取下一站信息
    @RequestMapping("/getBusLocation")
    @ResponseBody
    public List<HashMap<String, Object>> getBusLocation(@RequestBody List<Route> routes){
        System.out.println("开始================================================================");
        System.out.println(routes.toString());
        List<HashMap<String, Object>> list = busService.getMinimumBus(routes);
        return list;
    }

    //获取线路公交位置与时刻表信息
        @RequestMapping("/getBusInfoTime")
    @ResponseBody
    public Route getBusInfoTime(String routeId){
        System.out.println("获取线路公交位置与时刻表信息"+routeId);
        return routeService.selectRouteTime(routeId);
    }

    //更新公交信息
    @RequestMapping("/refreshBusInf")
    @ResponseBody
    public  HashMap<String, Object> refreshBusInf(String routeId,String siteId){
        System.out.println("更新公交信息"+routeId);
        return busService.refreshBusInf(routeId,siteId);
    }

    //获取行驶时间列表
    @RequestMapping("/getBusInfs")
    @ResponseBody
    public  HashMap<String, Object> getBusInfs(String routeId){
        System.out.println("定时更新所有公交信息"+routeId);
        return busService.getBusInfs(routeId);
    }


    //根据线路Id查找路线所有点
    @RequestMapping("/selectRouteById")
    @ResponseBody
    public Map<String, Object> selectRouteById(String routeId){
        return routeService.selectRouteById(routeId);
    }

    //
}
