package com.cykj.mapper;

import com.cykj.bean.AdminInf;

public interface AdminInfMapper {
    int deleteByPrimaryKey(Integer adminid);

    int insert(AdminInf record);

    int insertSelective(AdminInf record);

    AdminInf selectByPrimaryKey(Integer adminid);

    int updateByPrimaryKeySelective(AdminInf record);

    int updateByPrimaryKey(AdminInf record);
}