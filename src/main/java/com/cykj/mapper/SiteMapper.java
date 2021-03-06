package com.cykj.mapper;

import com.cykj.bean.City;
import com.cykj.bean.Site;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    //查询所有城市
    List<Site> getSites(Map map);

    //新增点
    int addDots(List list);

    List<Site>getDots(Map map);

    List<Site> selectSiteByIds(ArrayList<Integer> sitesId);


}