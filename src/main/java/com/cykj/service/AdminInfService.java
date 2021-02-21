package com.cykj.service;

import com.cykj.bean.AdminInf;
import com.cykj.util.LayuiJson;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AdminInfService {

    //用户表格显示
    public List<AdminInf> adminSelectTable(Map<String, Object> hasMap, RowBounds rb);
    //查总数
    public int adminSelectCount(Map<String, Object> hasMap);
    //增
    public boolean insertAdminInf(AdminInf adminInf);
    //改
    public boolean updateAdminInf(AdminInf adminInf);
    //删
    public boolean deleteAdminInf(AdminInf adminInf);
    //重置密码
    public int resetPwd(String account);

    //新增
    int addAdmin(AdminInf adminInf);

    //删除
    boolean deleteAdmin(AdminInf adminInf);

    //修改
    int updateAdmin(AdminInf adminInf);

    LayuiJson selectAdmin(Map map);

    public int updateState(AdminInf adminInf);

    AdminInf selectAdminInf(AdminInf adminInf);

    String chatLoad(int adminId);

}
