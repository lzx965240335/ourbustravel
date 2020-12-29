package com.cykj.mapper;

import com.cykj.bean.Driver;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverMapper {
    int deleteByPrimaryKey(Integer drivid);

    int insert(Driver record);

    int insertSelective(Driver record);

    Driver selectByPrimaryKey(Integer drivid);

    int updateByPrimaryKeySelective(Driver record);

    int updateByPrimaryKey(Driver record);
}