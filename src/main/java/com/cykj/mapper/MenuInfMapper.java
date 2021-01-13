package com.cykj.mapper;

import com.cykj.bean.MenuInf;
import com.cykj.bean.Power;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuInfMapper {

    //查询所有角色拥有的菜单
    List<MenuInf> findMenuByRole(int menuId);

    //查询父菜单
    List<MenuInf> findByPid(int pid);

    //根据角色查询菜单
    List<Power> powerByRole(int roleId);

    //增加权限
    public int updateRole(Map map);
}