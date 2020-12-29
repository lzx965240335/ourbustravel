package com.cykj.mapper;

import com.cykj.bean.BusInf;
import org.springframework.stereotype.Repository;

@Repository
public interface BusInfMapper {
    int deleteByPrimaryKey(Integer busid);

    int insert(BusInf record);

    int insertSelective(BusInf record);

    BusInf selectByPrimaryKey(Integer busid);

    int updateByPrimaryKeySelective(BusInf record);

    int updateByPrimaryKey(BusInf record);
}