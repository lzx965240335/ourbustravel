package com.cykj.mapper;

import com.cykj.bean.MenuInf;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;


@Repository
public interface MenuInfMapper {

    //查询所有角色拥有的子菜单
    List<MenuInf> findMenuByRole(int menuId, int roleId);

    //查询父菜单
    List<MenuInf> findByPid(int pid);

    //增加权限
    public int updateRole(HashMap<String, Object> map);

    List<MenuInf> findMenus(int menuId);

}