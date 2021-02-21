package com.cykj.mapper;

import com.cykj.bean.Driver;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface DriverMapper {
    int deleteByPrimaryKey(Integer drivid);

    int insert(Driver record);

    int insertSelective(Driver record);

    Driver selectByPrimaryKey(Integer drivid);

    int updateByPrimaryKeySelective(Driver record);

    int updateByPrimaryKey(Driver record);
    List<Driver> selectByLogin(Driver driver);
    List<Driver> getSigns(HashMap map);
    int newMonth(HashMap map);
    int do_clock(HashMap map);
}