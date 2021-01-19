package com.cykj.service;

import com.cykj.bean.AdminInf;
import com.cykj.bean.Advert;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AdvertService {

    //广告表格显示
    public List<Advert> advertSelectTable(Map<String, Object> hasMap, RowBounds rb);
    //查总数
    public int advertSelectCount(Map<String, Object> hasMap);
    //增
    public boolean insertAdvert(Advert advert);
    //改
    public boolean updateAdvert(Advert advert);
    //删
    public boolean deleteAdvert(Advert advert);
    //改状态
    public int updateState(Advert advert);
    //发送数据
    public List<Advert> selAdvertMsg(int advertUrl);
}
