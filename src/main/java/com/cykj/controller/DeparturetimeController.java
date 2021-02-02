package com.cykj.controller;

import com.cykj.bean.Departuretime;
import com.cykj.service.DeparturetimeService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/Departuretime")
@Controller
public class DeparturetimeController {

    @Autowired
    private DeparturetimeService departuretimeService;

    //表格展示
    @RequestMapping(value = "/SelectTable")
    @ResponseBody
    public Map<String, Object> getSelectTable(@RequestParam String limit, @RequestParam String page, HttpServletRequest request) {
        int newPage = Integer.parseInt(page);
        int newLimit = Integer.parseInt(limit);
        String busName = request.getParameter("busName");
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("busName", busName);
        RowBounds rb = new RowBounds((newPage - 1) * newLimit, newLimit);
        List<Departuretime> departuretimes = departuretimeService.departuretimeSelectTable(hasMap,rb);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", departuretimeService.departuretimeSelectCount(hasMap));
        map.put("data", departuretimes);
        return map;
    }
}
