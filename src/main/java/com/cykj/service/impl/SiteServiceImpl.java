package com.cykj.service.impl;



import com.cykj.bean.Site;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.SiteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteMapper siteMapper;

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
        return siteMapper.updateSite(site);
    }

    @Override
    public LayuiJson selectAllSite(Map map) {
        List<Site> sites=null;
        if (map!=null){
            sites = siteMapper.selectAllSite(map);
        }else {
            sites=siteMapper.getSites(null);
            return new LayuiJson(0,sites);
        }
        int count = siteMapper.siteCount(map);
        return new LayuiJson(count,sites);
    }
}
