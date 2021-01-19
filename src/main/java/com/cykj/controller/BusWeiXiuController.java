package com.cykj.controller;


import com.cykj.service.BusService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/getBusView")
public class BusWeiXiuController {
    @Autowired
    private BusService busService;

    @RequestMapping(value = "/getBusView")
    public String getView() {
        return "busWeiXiu";
    }

    @RequestMapping(value = "/getBusWeiXiu")
    @ResponseBody
    public LayuiJson getTableData(HttpServletRequest req){
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String busName = req.getParameter("busName");
        int start = (page-1)*limit;
        HashMap<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("end",limit);
        if (busName != null && !busName.equals("")){
            map.put("busName",busName);
        }
        LayuiJson LayuiJson = busService.selectBusWeiXiu(map);
        return LayuiJson;
    }
}
