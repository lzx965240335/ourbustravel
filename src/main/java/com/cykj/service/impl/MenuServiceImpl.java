package com.cykj.service.impl;


import com.cykj.bean.MenuInf;
import com.cykj.mapper.MenuInfMapper;

import com.cykj.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuInfMapper menuInfMapper;

    //查询父菜单
    @Override
    public List<MenuInf> findByPid() {
        List<MenuInf> menus = menuInfMapper.findByPid(0);
        return menus;
    }

    //查询某角色所拥有的所有菜单，后台遍历菜单用
    @Override
    public HashMap<String, List<MenuInf>> getMenuByRoleId(int roleId) {
        //存放角色菜单
        HashMap<String, List<MenuInf>> map = new HashMap<>();
        //根据角色查询所持有的所有子菜单
        List<MenuInf> menus = findByPid();
        //所有父级菜单
        for (int i = 0; i < menus.size(); i++) {
            //子菜单
            List<MenuInf> menuInfs = menuInfMapper.findMenuByRole(menus.get(i).getMenuId(), roleId);
            if (menuInfs!=null){
                map.put(menus.get(i).getMenuName(), menuInfs);
            }
        }
        return map;
    }


    /*
    * 底下权限分配用
    *
    *
    * */

    //查询所有菜单
    @Override
    public HashMap<String, List<MenuInf>> findMenus() {
        //查询父菜单
        List<MenuInf> menus = menuInfMapper.findByPid(0);
        HashMap menuMap = new HashMap();
        //通过父菜单 角色id 查询子菜单
        for (MenuInf menuInf : menus) {
            List<MenuInf> menu1 = menuInfMapper.findMenus(menuInf.getMenuId());
            menu1.add(0,menuInf);
            menuMap.put(menuInf.getMenuName(), menu1);
        }
        return menuMap;
    }


    //根据角色返回角色所拥有的菜单
    @Override
    public HashMap<String, List<MenuInf>> initMenu(int roleId) {
        //存放角色菜单
        HashMap<String, List<MenuInf>> map = new HashMap<>();
        //根据角色查询所持有的所有子菜单
        List<MenuInf> menus = findByPid();
        //所有父级菜单
        for (int i = 0; i < menus.size(); i++) {
            //子菜单
            List<MenuInf> menuInfs = menuInfMapper.findMenuByRole(menus.get(i).getMenuId(), roleId);
            menuInfs.add(0, menus.get(i));
            if (menuInfs!=null){
                map.put(menus.get(i).getMenuName(), menuInfs);
            }
        }
        return map;
    }


    //增加权限
    @Override
    public boolean updateRole(int roleId, String[] menuIds, int state) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < menuIds.length; i++) {
            list.add(Integer.parseInt(menuIds[i]));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("state", state);
        map.put("lists", list);
        int i = menuInfMapper.updateRole(map);
        if (i > 0) {
            return true;
        }
        return false;
    }
}
