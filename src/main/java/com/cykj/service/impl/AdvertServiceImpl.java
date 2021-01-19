package com.cykj.service.impl;

import com.cykj.bean.Advert;
import com.cykj.mapper.AdvertMapper;
import com.cykj.service.AdvertService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdvertServiceImpl implements AdvertService {

   @Autowired
    AdvertMapper advertMapper;

    @Override
    public List<Advert> advertSelectTable(Map<String, Object> hasMap, RowBounds rb) {
        List<Advert> advert = advertMapper.advertSelectTable(hasMap,rb);
        return advert;
    }

    @Override
    public int advertSelectCount(Map<String, Object> hasMap) {
        int flag = advertMapper.advertSelectCount(hasMap);
        return flag;
    }

    @Override
    public boolean insertAdvert(Advert advert) {
        boolean flag = advertMapper.insertAdvert(advert);
        return flag;
    }

    @Override
    public boolean updateAdvert(Advert advert) {
        boolean flag = advertMapper.updateAdvert(advert);
        return flag;
    }

    @Override
    public boolean deleteAdvert(Advert advert) {
        boolean flag = advertMapper.deleteAdvert(advert);
        return flag;
    }

    @Override
    public int updateState(Advert advert) {
        int flag = advertMapper.updateState(advert);
        return flag;
    }

    @Override
    public List<Advert>  selAdvertMsg(int advertUrl) {

        return advertMapper.selAdvertMsg(advertUrl);
    }
}
