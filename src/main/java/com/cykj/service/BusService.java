package com.cykj.service;

import com.cykj.bean.BusInf;
import com.cykj.util.LayuiJson;

import java.util.Map;

public interface BusService {
    //新增
    int addBus(BusInf busInf);

    //删除
    boolean deleteBus(BusInf busInf);

    //修改
    int updateBus(BusInf busInf);

    LayuiJson selectBus(Map map);

    public int updateState(BusInf busInf);

    //车辆维修列表显示
    LayuiJson selectBusWeiXiu(Map map);
}
