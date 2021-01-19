package com.cykj.util;

import com.cykj.bean.AdminInf;
import com.cykj.bean.UserInf;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserChatJson extends HashMap implements Map {

    public UserChatJson(HttpSession session) {
        Map mine = new HashMap<String ,String >();
        Map data = new HashMap();
        List<Map<String ,Object >> friend = new ArrayList<>();
            UserInf loginner = (UserInf) session.getAttribute("loginner");
            mine.put("username",loginner.getAccount());
            mine.put("id",loginner.getUserId());
            mine.put("sign","Hello!!!World");
            mine.put("status", "online");

            List<Map<String ,String >> groupFriends = new ArrayList<>();
            Map groupFriend = new HashMap<String ,Object >();
            groupFriend.put("username","在线客服");
            groupFriend.put("id","101");
            groupFriend.put("avatar","http://tp1.sinaimg.cn/1571889140/180/40030060651/1");
            groupFriend.put("sign","你好");
            groupFriends.add(groupFriend);

            Map group = new HashMap<String ,Object >();
            group.put("groupname","在线客服");
            group.put("id",1);
            group.put("online",2);
            group.put("list",groupFriends);
            friend.add(group);
            data.put("friend",friend);
        put("code", 0);
        put("msg", "");
        data.put("mine",mine);
        put("data", data);
    }
}
