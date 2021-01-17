package com.cykj.service.impl;

import com.cykj.mapper.AdminInfMapper;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    public RouteMapper routeMapper;

    @Autowired
    public SiteMapper siteMapper;
    @Override
    public int addDots(List list) {
        return siteMapper.addDots(list);
    }
}
