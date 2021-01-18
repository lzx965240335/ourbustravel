package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.cykj.bean.NewsInf;
import com.cykj.service.NewsService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/news")
public class NewsInfoController {

    @Autowired
    private NewsService newsService;


    /**
     * 获取所有新闻公告
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/getNewsInf")
    @ResponseBody
    public LayuiJson getNewsInf(HttpServletRequest req) {
        int page = Integer.parseInt(req.getParameter("page"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        String newTile = req.getParameter("newTile");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        String newsState = req.getParameter("newsState");
        int start = (page - 1) * limit;
        HashMap<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", limit);
        if (newTile != null && !newTile.equals("")) {
            map.put("newTitle", newTile);
        }
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        }
        if (endTime != null && !endTime.equals("")) {
            map.put("endTime", endTime);
        }
        if (newsState != null && !newsState.equals("")) {
            map.put("newsState", newsState);
        }
        LayuiJson LayuiJson = newsService.selectNewsInfs(map);
        System.out.println(JSON.toJSONString(LayuiJson));
        return LayuiJson;
    }

    /**
     * 新增新闻
     *
     * @param req
     * @param newsInf
     * @return
     */
    @RequestMapping(value = "/addNewsInf")
    @ResponseBody
    public String addNewsInf(HttpServletRequest req, @RequestBody NewsInf newsInf) {
        int addResult = newsService.addNewsInf(newsInf);
        return addResult > 0 ? "新增成功" : "新增失败";
    }

    /**
     * 删除新闻
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/deleteNews")
    @ResponseBody
    public String deleteNews(HttpServletRequest req) {
        NewsInf newsInf = new NewsInf();
        newsInf.setNewsId(Integer.parseInt(req.getParameter("newsId")));
        boolean deleteResult = newsService.deleteNewsInf(newsInf);
        return deleteResult == true ? "删除成功" : "删除失败";
    }

    /**
     * 修改新闻信息
     *
     * @param req
     * @param newsInf
     * @return
     */
    @RequestMapping("/updateNews")
    @ResponseBody
    public String updateCity(HttpServletRequest req, @RequestBody NewsInf newsInf) {
        int updateResult = newsService.updateNewsInf(newsInf);
        return updateResult > 0 ? "修改成功" : "修改失败";
    }

    /**
     * 新闻启用禁用状态
     *
     * @param req
     * @param newsInf
     * @return
     */
    @RequestMapping("/updateState")
    @ResponseBody
    public String updateState(HttpServletRequest req, @RequestBody NewsInf newsInf) {
        int updateStateResult = newsService.updateState(newsInf);
        return updateStateResult > 0 ? "success" : "fail";
    }


    @RequestMapping("/newsInfMsg")
    @ResponseBody
    public List<NewsInf> newsInfMsg() {
        List<NewsInf> newsInfs = newsService.newsInfMsg();
        return newsInfs;
    }

}
