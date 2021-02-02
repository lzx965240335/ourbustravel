package com.cykj.mapper;

import com.cykj.bean.Departuretime;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DeparturetimeMapper {

    //发车表格显示
    public List<Departuretime> departuretimeSelectTable(Map<String, Object> hasMap, RowBounds rb);
    //查总数
    public int departuretimeSelectCount(Map<String, Object> hasMap);
}
