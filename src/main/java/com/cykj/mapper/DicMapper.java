package com.cykj.mapper;

import com.cykj.bean.Dic;

public interface DicMapper {
    int deleteByPrimaryKey(Integer dicid);

    int insert(Dic record);

    int insertSelective(Dic record);

    Dic selectByPrimaryKey(Integer dicid);

    int updateByPrimaryKeySelective(Dic record);

    int updateByPrimaryKey(Dic record);
}