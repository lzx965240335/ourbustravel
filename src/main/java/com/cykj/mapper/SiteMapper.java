package com.cykj.mapper;

import com.cykj.bean.Site;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SiteMapper {


    //新增城市
    int addSite(Site site);

    //修改城市信息
    int updateSite(Site site);

    //删除城市信息
    int deleteSite(int siteId);

    //查询数据库所有的数据,分页
    List<Site> selectAllSite(Map map);

    //总数
    int siteCount(Map map);
}