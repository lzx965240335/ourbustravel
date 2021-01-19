package com.cykj.util;

import com.cykj.bean.AdminInf;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class AdminChatJson extends HashMap implements Map {

    public AdminChatJson(HttpSession session) {
        Map mine = new HashMap<String ,String >();
        Map data = new HashMap();
        AdminInf adminInf = (AdminInf) session.getAttribute("adminInf");
        mine.put("username",adminInf.getAccount());
        mine.put("id",adminInf.getAdminId());
        mine.put("sign","HelloWorld!!!");
        mine.put("status", "online");
        put("code", 0);
        put("msg", "");
        data.put("mine",mine);
        put("data", data);
    }
}
