package com.cykj.service.impl;


import com.cykj.bean.City;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.SiteService;
import com.cykj.util.LayuiJson;
import com.cykj.util.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private RouteMapper routeMapper;

    @Override
    public int addSite(Site site) {
        return siteMapper.addSite(site);
    }

    @Override
    public boolean deleteSite(Site site) {
        return siteMapper.deleteSite(site.getSiteId())>0;
    }

    @Override
    public int updateSite(Site site) {
        return 0;
    }

    @Override
    public LayuiJson selectAllSite(Map map) {
        List<Site> sites=null;
        List<Route> routes=null;
        System.out.println(map);
        if (map!=null){
            sites = siteMapper.selectAllSite(map);
        }else {
            sites=siteMapper.getSites(map);
            routes=routeMapper.getRoutes(map);
            List list= Station.getStation(sites,routes,null);
            return new LayuiJson(0,list);
        }
        int count = siteMapper.siteCount(map);
        return new LayuiJson(count,sites);
    }
}
