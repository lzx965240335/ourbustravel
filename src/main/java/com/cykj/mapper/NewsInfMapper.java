package com.cykj.mapper;

import com.cykj.bean.NewsInf;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface NewsInfMapper {
    //增加新闻
    int addNewsInf(NewsInf newsInf);
    //删除新闻
    int deleteNewsInf(int newsInfId);
    //修改新闻
    int updateNewsInf(NewsInf newsInf);
    //获取所有新闻信息
    List<NewsInf> selectNewsInfs(Map<String, Object> condition);
    //新闻总数
    int newsInfsCount(Map<String, Object> condition);
    //修改状态（启用、禁用）
    int updateState(NewsInf newsInf);
    //发送数据到前端
    List<NewsInf> newsInfMsg();
}