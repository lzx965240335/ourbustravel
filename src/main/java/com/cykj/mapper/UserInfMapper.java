package com.cykj.mapper;

import com.cykj.bean.UserInf;

public interface UserInfMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(UserInf record);

    int insertSelective(UserInf record);

    UserInf selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(UserInf record);

    int updateByPrimaryKey(UserInf record);
}