package com.cykj.service.impl;

import com.alibaba.fastjson.JSON;
import com.cykj.bean.NewsInf;
import com.cykj.mapper.NewsInfMapper;
import com.cykj.service.NewsService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsInfMapper newsInfMapper;

    @Override
    public int addNewsInf(NewsInf newsInf) {
        return newsInfMapper.addNewsInf(newsInf);
    }

    @Override
    public boolean deleteNewsInf(NewsInf newsInf) {
        return newsInfMapper.deleteNewsInf(newsInf.getNewsId())>0;
    }

    @Override
    public int updateNewsInf(NewsInf newsInf) {
        return newsInfMapper.updateNewsInf(newsInf);
    }


    @Override
    public LayuiJson selectNewsInfs(Map map) {
        List<NewsInf> selectNewsInfs = newsInfMapper.selectNewsInfs(map);
        System.out.println(JSON.toJSONString(selectNewsInfs));
        int newsInfsCount = newsInfMapper.newsInfsCount(map);
        return new LayuiJson(newsInfsCount,selectNewsInfs);
    }

    @Override
    public int updateState(NewsInf newsInf) {
        return newsInfMapper.updateState(newsInf);
    }
}
