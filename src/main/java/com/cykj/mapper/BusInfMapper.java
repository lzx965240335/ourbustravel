package com.cykj.mapper;

import com.cykj.bean.BusInf;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BusInfMapper {
    //新增车辆
    int addBus(BusInf busInf);

    //修改
    int updateBus(BusInf busInf);

    //删除
    int deleteBus(int busId);

    //查询数据库所有的数据,分页
    List<BusInf> selectBus(Map<String, Object> condition);

    //总数
    int count(Map map);

    //改状态
    public int updateState(BusInf busInf);

    //查询数据库所有的数据,分页
    List<BusInf> selectBusWeiXiu(Map<String, Object> condition);

    //    <!--    查找驶的公交-->
    List<BusInf> selectBusByMap(Map map);

    List<BusInf> selectBusByRouteId(Map map);

}