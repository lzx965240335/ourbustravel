package com.cykj.service;

import com.cykj.bean.MenuInf;
import com.cykj.bean.Power;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface MenuService {

    //查询父菜单
    List<MenuInf> findByPid();

    //根据角色返回菜单供加载后台通用
    HashMap<String,List<MenuInf>> getMenuByRoleId(int roleId);

    //查询所有菜单
    HashMap<String,List<MenuInf>> findMenus();

    //根据角色返回菜单
    HashMap<String,List<MenuInf>> initMenu(int roleId);

    //增加权限
    public boolean updateRole(int roleId, String[] menuIds, int state);


}
