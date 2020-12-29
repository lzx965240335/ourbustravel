package com.cykj.mapper;

import com.cykj.bean.GoodsInf;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsInfMapper {
    int deleteByPrimaryKey(Integer goodsid);

    int insert(GoodsInf record);

    int insertSelective(GoodsInf record);

    GoodsInf selectByPrimaryKey(Integer goodsid);

    int updateByPrimaryKeySelective(GoodsInf record);

    int updateByPrimaryKey(GoodsInf record);
}