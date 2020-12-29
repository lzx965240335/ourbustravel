package com.cykj.mapper;

import com.cykj.bean.billInf;

public interface billInfMapper {
    int deleteByPrimaryKey(Integer billid);

    int insert(billInf record);

    int insertSelective(billInf record);

    billInf selectByPrimaryKey(Integer billid);

    int updateByPrimaryKeySelective(billInf record);

    int updateByPrimaryKey(billInf record);
}