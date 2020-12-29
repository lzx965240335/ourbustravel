package com.cykj.mapper;

import com.cykj.bean.GoodsInf;

public interface GoodsInfMapper {
    int deleteByPrimaryKey(Integer goodsid);

    int insert(GoodsInf record);

    int insertSelective(GoodsInf record);

    GoodsInf selectByPrimaryKey(Integer goodsid);

    int updateByPrimaryKeySelective(GoodsInf record);

    int updateByPrimaryKey(GoodsInf record);
}