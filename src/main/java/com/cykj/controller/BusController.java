package com.cykj.controller;
import com.cykj.bean.BusInf;
import com.cykj.service.BusService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/busMain")
public class BusController {

    @Autowired
    private BusService busService;

    @RequestMapping(value = "/getBusView")
    public String getView() {
        return "busMain";
    }

    @RequestMapping(value = "/getBusTable")
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
        LayuiJson LayuiJson = busService.selectBus(map);
        return LayuiJson;
    }

    //删除
    @RequestMapping("/deleteBus")
    @ResponseBody
    public String deleteBus(HttpServletRequest req) {
        BusInf busInf = new BusInf();
        busInf.setBusId(Integer.parseInt(req.getParameter("busId")));
        boolean deleteResult = busService.deleteBus(busInf);
        return deleteResult == true ? "删除成功" : "删除失败";

    }

    //新增
    @RequestMapping("/addBus")
    @ResponseBody
    public String addBus(HttpServletRequest req, @RequestBody BusInf busInf) {

        int addResult = busService.addBus(busInf);
        return addResult > 0 ? "添加成功" : "添加失败";
    }

    //修改
    @RequestMapping("/updateBus")
    @ResponseBody
    public String updateBus(HttpServletRequest req, @RequestBody BusInf busInf) {
        int updateResult = busService.updateBus(busInf);
        return updateResult > 0 ? "修改成功" : "修改失败";
    }

    //修改状态
    @RequestMapping("/updateState")
    @ResponseBody
    public String  changeStaupdateStatete(HttpServletRequest request, @RequestBody BusInf busInf){
        int flag=busService.updateState(busInf);
        if(flag>0){
            return "success";
        }
        return "fail";
    }
}
