package com.cykj.controller;


import com.cykj.bean.Site;
import com.cykj.service.SiteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/background")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @RequestMapping(value = "/getSiteView")
    public String getSiteView(){
        return "site";
    }

    @RequestMapping(value = "/getSiteTable")
    @ResponseBody
    public LayuiJson getTableData(HttpServletRequest req) {
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String siteName = req.getParameter("siteName");
        String peopleNum = req.getParameter("peopleNum");
        int start = (page - 1) * limit;
        HashMap<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", limit);
        if (siteName != null && !siteName.equals("")) {
            map.put("siteName", siteName);
        }
        if (peopleNum != null && !peopleNum.equals("")) {
            map.put("peopleNum", peopleNum);
        }
        LayuiJson layuiJson = siteService.selectAllSite(map);
        return layuiJson;
    }

    /**
     * 删除
     * @param req
     * @return
     */
    @RequestMapping("/deleteSite")
    @ResponseBody
    public String deleteSite(HttpServletRequest req) {
        Site site = new Site();
        site.setSiteId(Integer.parseInt(req.getParameter("siteId")));
        boolean deleteResult = siteService.deleteSite(site);
        return deleteResult == true ? "删除成功" : "删除失败";
    }

    /**
     * 新增
     * @param req
     * @param site
     * @return
     */
    @RequestMapping("/addSite")
    @ResponseBody
    public String addSite(HttpServletRequest req, @RequestBody Site site) {
        int addResult = siteService.addSite(site);
        return addResult > 0 ? "添加成功" : "添加失败";
    }

}
