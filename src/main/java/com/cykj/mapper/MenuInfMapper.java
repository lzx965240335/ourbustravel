package com.cykj.mapper;

import com.cykj.bean.MenuInf;

public interface MenuInfMapper {
    int deleteByPrimaryKey(Integer menuid);

    int insert(MenuInf record);

    int insertSelective(MenuInf record);

    MenuInf selectByPrimaryKey(Integer menuid);

    int updateByPrimaryKeySelective(MenuInf record);

    int updateByPrimaryKey(MenuInf record);
}