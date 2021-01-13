package com.cykj.service.impl;
import com.cykj.bean.BusInf;
import com.cykj.mapper.BusInfMapper;
import com.cykj.service.BusService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BusServiceImpl implements BusService {


    @Autowired
    private BusInfMapper busInfMapper;


    @Override
    public int addBus(BusInf busInf) {
        return 0;
    }

    @Override
    public boolean deleteBus(BusInf busInf) {
        return busInfMapper.deleteBus(busInf.getBusId())>0;
    }

    @Override
    public int updateBus(BusInf busInf) {
        return 0;
    }

    @Override
    public LayuiJson selectBus(Map map) {
        List<BusInf> busInfs = busInfMapper.selectBus(map);
        int count = busInfMapper.count(map);
        return new LayuiJson(count,busInfs);
    }

    @Override
    public int updateState(BusInf busInf) {
        return 0;
    }
}
