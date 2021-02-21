package com.cykj.mapper;

import com.cykj.bean.ChatInf;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatInfMapper {
    int deleteByPrimaryKey(Integer chatid);

    int insert(ChatInf record);

    int insertSelective(ChatInf record);

    List<ChatInf> selectByUserId(int userId);

    List<ChatInf> selectByAdminId(int adminId);

    int updateByAdminId(int adminId);

    int updateByUserId(int userId);

    int updateOneToOne(int userId, int adminId, char role);

    int updateByPrimaryKey(ChatInf record);
}