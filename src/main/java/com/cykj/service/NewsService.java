package com.cykj.service;

import com.cykj.bean.NewsInf;
import com.cykj.util.LayuiJson;

import java.util.Map;

public interface NewsService {

    int addNewsInf(NewsInf newsInf);
    boolean deleteNewsInf(NewsInf newsInf);
    int updateNewsInf(NewsInf newsInf);
    LayuiJson selectNewsInfs(Map map);
    int updateState(NewsInf newsInf);
}
