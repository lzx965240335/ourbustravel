package com.cykj.mapper;

import com.cykj.bean.LogInf;
import org.springframework.stereotype.Repository;

@Repository
public interface LogInfMapper {
    int deleteByPrimaryKey(Integer logid);

    int insert(LogInf record);

    int insertSelective(LogInf record);

    LogInf selectByPrimaryKey(Integer logid);

    int updateByPrimaryKeySelective(LogInf record);

    int updateByPrimaryKey(LogInf record);
}