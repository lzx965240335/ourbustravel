package com.cykj.controller;

import com.cykj.bean.Advert;
import com.cykj.service.AdvertService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/Advert")
@Controller
public class AdvertController {

    @Autowired
    private AdvertService advertService;


    //表格展示
    @RequestMapping(value = "/getSelectTable")
    @ResponseBody
    public Map<String, Object> getSelectTable(@RequestParam String limit, @RequestParam String page, HttpServletRequest request) {
        int newPage = Integer.parseInt(page);
        int newLimit = Integer.parseInt(limit);
        String beginTime = request.getParameter("beginTime");
        String endingTime = request.getParameter("endingTime");
        String selState = request.getParameter("selState");
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("beginTime", beginTime);
        hasMap.put("endingTime", endingTime);
        hasMap.put("selState", selState);
        RowBounds rb = new RowBounds((newPage - 1) * newLimit, newLimit);
        List<Advert> adverts = advertService.advertSelectTable(hasMap, rb);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", advertService.advertSelectCount(hasMap));
        map.put("data", adverts);
        return map;
    }

    //删除
    @RequestMapping(value = "/getDelete")
    @ResponseBody
    public String getDelete(HttpServletRequest request) {
        Advert advert = new Advert();
        advert.setAdvertId(Integer.parseInt(request.getParameter("advertId")));
        boolean flag = advertService.deleteAdvert(advert);
        return flag == true ? "删除成功" : "删除失败";
    }

    //新增
    @RequestMapping(value = "/getAdd")
    @ResponseBody
    public String getAdd(HttpServletRequest request, @RequestBody Advert advert) {
        boolean flag = advertService.insertAdvert(advert);
        return flag == true ? "新增成功" : "新增失败";
    }

    //修改
    @RequestMapping(value = "/getReset")
    @ResponseBody
    public String getReset(HttpServletRequest request, @RequestBody Advert advert) {
        boolean flag = advertService.updateAdvert(advert);
        return flag == true ? "修改成功" : "修改失败";
    }

    //禁用启用
    @RequestMapping(value = "/changeState")
    @ResponseBody
    public String changeState(HttpServletRequest request, @RequestBody Advert advert) {
        int flag = advertService.updateState(advert);
        if (flag > 0) {
            return "success";
        }
        return "fail";
    }

    //发送数据到前端
    @RequestMapping(value = "/selAdvertMsg")
    @ResponseBody
    public List<Advert> selAdvertMsg(int advertUrl) {
        List<Advert> list = advertService.selAdvertMsg(advertUrl);
        return list;
    }


}

