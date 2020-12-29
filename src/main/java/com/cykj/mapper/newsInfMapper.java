package com.cykj.mapper;

import com.cykj.bean.newsInf;

public interface newsInfMapper {
    int deleteByPrimaryKey(Integer newsid);

    int insert(newsInf record);

    int insertSelective(newsInf record);

    newsInf selectByPrimaryKey(Integer newsid);

    int updateByPrimaryKeySelective(newsInf record);

    int updateByPrimaryKey(newsInf record);
}