package com.cykj.service;

import com.cykj.bean.NewsInf;
import com.cykj.util.LayuiJson;

import java.util.List;
import java.util.Map;

public interface NewsService {
    //增加新闻
    int addNewsInf(NewsInf newsInf);
    //删除新闻
    boolean deleteNewsInf(NewsInf newsInf);
    //修改新闻
    int updateNewsInf(NewsInf newsInf);
    //查询所有新闻信息
    LayuiJson selectNewsInfs(Map map);
    //修改状态
    int updateState(NewsInf newsInf);
    //发送新闻数据
    List<NewsInf> newsInfMsg();

}
