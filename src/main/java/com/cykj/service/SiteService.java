package com.cykj.service;

import com.cykj.bean.LayuiJson;
import com.cykj.bean.Site;
import java.util.Map;

public interface SiteService {

    int addSite(Site site);
    boolean deleteSite(Site site);
    int updateSite(Site site);
    LayuiJson selectAllSite(Map map);
}
