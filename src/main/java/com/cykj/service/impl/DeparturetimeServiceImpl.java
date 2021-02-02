package com.cykj.service.impl;

import com.cykj.bean.Departuretime;
import com.cykj.mapper.DeparturetimeMapper;
import com.cykj.service.DeparturetimeService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeparturetimeServiceImpl implements DeparturetimeService {

    @Autowired
    DeparturetimeMapper departuretimeMapper;


    @Override
    public List<Departuretime> departuretimeSelectTable(Map<String, Object> hasMap, RowBounds rb) {
        List<Departuretime> departuretimes = departuretimeMapper.departuretimeSelectTable(hasMap,rb);
        return departuretimes;
    }

    @Override
    public int departuretimeSelectCount(Map<String, Object> hasMap) {
        int flag = departuretimeMapper.departuretimeSelectCount(hasMap);
        return flag;
    }
}
