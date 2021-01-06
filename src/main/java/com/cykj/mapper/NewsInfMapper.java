package com.cykj.mapper;

import com.cykj.bean.NewsInf;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface NewsInfMapper {
    int addNewsInf(NewsInf newsInf);

    int deleteNewsInf(int newsInfId);

    int updateNewsInf(NewsInf newsInf);

    List<NewsInf> selectNewsInfs(Map<String, Object> condition);

    int newsInfsCount(Map<String, Object> condition);

    int updateState(NewsInf newsInf);
}