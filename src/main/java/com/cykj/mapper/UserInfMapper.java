package com.cykj.mapper;

import com.cykj.bean.UserInf;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserInfMapper {

    //查询
    List<UserInf> selectUser(Map map);

    //总数
    int count(Map map);


    int deleteByPrimaryKey(Integer userid);

    int insert(UserInf userInf);

    int insertSelective(UserInf record);

    UserInf selectByPrimaryKey(Integer userid);

    int updateUserInf(UserInf record);

    int updateByPrimaryKey(UserInf record);

    List<UserInf> selectUsers(UserInf userInf);
}