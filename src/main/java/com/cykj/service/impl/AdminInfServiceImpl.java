package com.cykj.service.impl;

import com.cykj.service.AdminInfService;
import com.cykj.bean.AdminInf;
import com.cykj.mapper.AdminInfMapper;
import com.cykj.util.LayuiJson;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminInfServiceImpl implements AdminInfService {

    @Autowired
    AdminInfMapper adminInfMapper;

    @Override
    public List<AdminInf> adminSelectTable(Map<String, Object> hasMap, RowBounds rb) {
        List<AdminInf> adminInf = adminInfMapper.adminSelectTable(hasMap,rb);
        return adminInf;
    }

    @Override
    public int adminSelectCount(Map<String, Object> hasMap) {
        int flag = adminInfMapper.adminSelectCount(hasMap);
        return flag;
    }

    @Override
    public boolean insertAdminInf(AdminInf adminInf) {
        boolean flag = adminInfMapper.insertAdminInf(adminInf);
        return flag;
    }

    @Override
    public boolean updateAdminInf(AdminInf adminInf) {
        boolean flag = adminInfMapper.updateAdminInf(adminInf);
        return flag;
    }

    @Override
    public boolean deleteAdminInf(AdminInf adminInf) {
        boolean flag = adminInfMapper.deleteAdminInf(adminInf);
        return flag;
    }

    @Override
    public int resetPwd(String account) {
        int flag = adminInfMapper.resetPwd(account);
        return flag;
    }

    //新增
    @Override
    public int addAdmin(AdminInf adminInf) {
        return adminInfMapper.addAdmin(adminInf);
    }

    //删除
    @Override
    public boolean deleteAdmin(AdminInf adminInf) {
        return adminInfMapper.deleteAdmin(adminInf.getAdminId())>0;
    }

    //修改用户状态
    @Override
    public int updateAdmin(AdminInf adminInf) {
        return adminInfMapper.updateAdmin(adminInf);
    }


    @Override
    public LayuiJson selectAdmin(Map map) {
        List<AdminInf> adminInfs = adminInfMapper.selectAdmin(map);
        int count = adminInfMapper.count(map);
        return new LayuiJson(count,adminInfs);
    }

    @Override
    public int updateState(AdminInf adminInf) {
        int flag = adminInfMapper.updateState(adminInf);
        return flag;
    }

    //登录
    @Override
    public AdminInf selectAdminInf(AdminInf adminInf) {
        return adminInfMapper.selectAdminInf(adminInf);
    }
}
