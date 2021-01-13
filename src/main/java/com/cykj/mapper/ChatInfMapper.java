package com.cykj.mapper;

import com.cykj.bean.ChatInf;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatInfMapper {
    int deleteByPrimaryKey(Integer chatid);

    int insert(ChatInf record);

    int insertSelective(ChatInf record);

    ChatInf selectByPrimaryKey(Integer chatid);

    int updateByPrimaryKeySelective(ChatInf record);

    int updateByPrimaryKey(ChatInf record);
}