package com.cykj.mapper;

import com.cykj.bean.Opinion;

public interface OpinionMapper {
    int deleteByPrimaryKey(Integer opinoinid);

    int insert(Opinion record);

    int insertSelective(Opinion record);

    Opinion selectByPrimaryKey(Integer opinoinid);

    int updateByPrimaryKeySelective(Opinion record);

    int updateByPrimaryKey(Opinion record);
}