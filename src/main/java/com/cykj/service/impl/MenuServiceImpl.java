package com.cykj.service.impl;


import com.cykj.bean.MenuInf;
import com.cykj.bean.Power;
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
    public List<MenuInf> findByPid(int pid) {
        List<MenuInf> menus = menuInfMapper.findByPid(0);
        return menus;
    }


    //查询所有菜单
    @Override
    public HashMap<String, List<MenuInf>> findMenus() {
        //查询父菜单
        List<MenuInf> menus = menuInfMapper.findByPid(0);
        HashMap menuMap = new HashMap();
        //通过父菜单 角色id 查询子菜单
        for (MenuInf menuInf : menus) {
            List<MenuInf> menu1 = menuInfMapper.findMenuByRole(menuInf.getMenuId());
            menuMap.put(menuInf.getMenuName(),menu1);
        }

        return menuMap;
    }

    //根据角色返回菜单
    @Override
    public HashMap<String,List<MenuInf>> powerByRole(int roleId) {
        //存放角色菜单
        HashMap<String,List<MenuInf>> map = new HashMap<>();
        //根据角色查询所持有的菜单
        List<Power> powers = menuInfMapper.powerByRole(roleId) ;
        List<MenuInf> menus = findByPid(0);
        //所有父级菜单
        for (int i = 0; i < menus.size(); i++) {
            //根据pid查询父菜单存放进map
            for (int j = 0; j < powers.size(); j++) {
                if (powers.get(j).getMenuId().equals(menus.get(i).getMenuId())){
                    List<MenuInf> menus1 = findByPid(0);
                    menus1.add(0,menus.get(i));
                    map.put(menus.get(i).getMenuName(),menus1);
                }
            }
        }
        return null;
    }



    //增加权限
    @Override
    public boolean updateRole(int roleId, String[] menuIds) {
        List<Integer> list=new ArrayList<>();
        for (int i = 0; i < menuIds.length; i++) {
            list.add(Integer.parseInt(menuIds[i]));
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("role",roleId);
        map.put("lists",list);
        int i = menuInfMapper.updateRole(map);
        return i > 0;
    }


}
