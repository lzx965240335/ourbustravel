package com.cykj.mapper;

import com.cykj.bean.Power;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerMapper {
    int deleteByPrimaryKey(Integer powerid);

    int insert(Power record);

    int insertSelective(Power record);

    Power selectByPrimaryKey(Integer powerid);

    int updateByPrimaryKeySelective(Power record);

    int updateByPrimaryKey(Power record);
}