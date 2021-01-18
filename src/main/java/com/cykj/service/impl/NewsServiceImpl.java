package com.cykj.service.impl;

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

    /**
     * 增加新闻
     * @param newsInf
     * @return
     */
    @Override
    public int addNewsInf(NewsInf newsInf) {
        return newsInfMapper.addNewsInf(newsInf);
    }

    /**
     * 删除新闻
     * @param newsInf
     * @return
     */
    @Override
    public boolean deleteNewsInf(NewsInf newsInf) {
        return newsInfMapper.deleteNewsInf(newsInf.getNewsId())>0;
    }

    /**
     * 修改新闻
     * @param newsInf
     * @return
     */
    @Override
    public int updateNewsInf(NewsInf newsInf) {
        return newsInfMapper.updateNewsInf(newsInf);
    }

    /**
     * 查询新闻所有信息
     * @param map
     * @return
     */
    @Override
    public LayuiJson selectNewsInfs(Map map) {
        List<NewsInf> selectNewsInfs = newsInfMapper.selectNewsInfs(map);
        int newsInfsCount = newsInfMapper.newsInfsCount(map);
        return new LayuiJson(newsInfsCount,selectNewsInfs);
    }

    /**
     * 修改新闻状态
     * @param newsInf
     * @return
     */
    @Override
    public int updateState(NewsInf newsInf) {
        return newsInfMapper.updateState(newsInf);
    }

    /**
     * 发送新闻数据
     * @return
     */
    @Override
    public List<NewsInf> newsInfMsg() {
        return newsInfMapper.newsInfMsg();
    }
}
