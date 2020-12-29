package com.cykj.mapper;

import com.cykj.bean.Advert;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertMapper {
    int deleteByPrimaryKey(Integer advertid);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(Integer advertid);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKey(Advert record);
}