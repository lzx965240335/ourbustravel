package com.cykj.mapper;

import com.cykj.bean.AdminInf;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AdminInfMapper {

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

    //新增人员
    int addAdmin(AdminInf adminInf);

    //修改
    int updateAdmin(AdminInf adminInf);

    //删除
    int deleteAdmin(int userId);

    //查询数据库所有的数据,分页
    List<AdminInf> selectAdmin(Map<String, Object> condition);

    //总数
    int count(Map map);

    //改状态
    public int updateState(AdminInf adminInf);

    AdminInf selectAdminInf(AdminInf adminInf);
}