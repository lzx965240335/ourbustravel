package com.cykj.mapper;

import com.cykj.bean.City;
import com.cykj.bean.Site;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SiteMapper {

    //新增站点
    int addSite(Site site);

    //更新站点信息
    int updateSite(Site site);

    //删除站点
    int deleteSite(int siteId);

    //查询数据库所有的数据,分页
    List<Site> selectAllSite(Map map);

    //总数
    int siteCount(Map map);

    //查询所有站点
    List<Site> getSites(Map map);


}