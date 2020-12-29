package com.cykj.mapper;

import com.cykj.bean.Site;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteMapper {
    int deleteByPrimaryKey(Integer siteid);

    int insert(Site record);

    int insertSelective(Site record);

    Site selectByPrimaryKey(Integer siteid);

    int updateByPrimaryKeySelective(Site record);

    int updateByPrimaryKey(Site record);
}