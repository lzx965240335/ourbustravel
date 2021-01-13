package com.cykj.service;

import com.cykj.bean.MenuInf;
import com.cykj.bean.Power;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface MenuService {

    //查询父菜单
    List<MenuInf> findByPid(int pid);

    //查询所有菜单
    HashMap<String,List<MenuInf>> findMenus();

    //根据角色查询菜单
    HashMap<String,List<MenuInf>> powerByRole(int roleId);

    //增加权限
    public boolean updateRole(int role, String[] menuIds);
}
