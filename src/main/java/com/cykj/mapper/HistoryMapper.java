package com.cykj.mapper;

import com.cykj.bean.History;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryMapper {
    int deleteByPrimaryKey(Integer historyid);

    int insert(History record);

    int insertSelective(History record);

    History selectByPrimaryKey(Integer historyid);

    int updateByPrimaryKeySelective(History record);

    int updateByPrimaryKey(History record);
}