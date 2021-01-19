package com.cykj.service.impl;
import com.cykj.bean.BusInf;
import com.cykj.mapper.BusInfMapper;
import com.cykj.service.BusService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BusServiceImpl implements BusService {
    DecimalFormat df = new DecimalFormat("0.00%");
    @Autowired
    private BusInfMapper busInfMapper;

    @Override
    public int addBus(BusInf busInf) {
        return busInfMapper.addBus(busInf);
    }

    @Override
    public boolean deleteBus(BusInf busInf) {
        return busInfMapper.deleteBus(busInf.getBusId())>0;
    }

    @Override
    public int updateBus(BusInf busInf) {
        return busInfMapper.updateBus(busInf);
    }

    @Override
    public LayuiJson selectBus(Map map) {
        List<BusInf> busInfs = busInfMapper.selectBus(map);
        int count = busInfMapper.count(map);
        return new LayuiJson(count,busInfs);
    }

    @Override
    public int updateState(BusInf busInf) {
        return busInfMapper.updateState(busInf);
    }

    @Override
    public LayuiJson selectBusWeiXiu(Map map) {
        List<BusInf> busInfs = busInfMapper.selectBusWeiXiu(map);
        for (BusInf busInf: busInfs) {

        }
        int count = busInfMapper.count(map);
        return new LayuiJson(count,busInfs);
    }
}
