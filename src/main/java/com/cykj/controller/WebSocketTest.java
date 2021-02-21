package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.bean.ChatInf;
import com.cykj.bean.MySpringConfigurator;
import com.cykj.mapper.ChatInfMapper;
import com.cykj.mapper.UserInfMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint(value = "/websocket/{username}", configurator = MySpringConfigurator.class)
public class WebSocketTest {

    @Autowired
    private ChatInfMapper chatInfMapper;

    @Autowired
    private UserInfMapper userInfMapper;

    private ChatInf chatInf;

    private static Logger log = Logger.getLogger(WebSocketTest.class);
    // 在线人数
    private static int onlineCount = 0;
    // 在线用户列表
    private static Map<String, Session> clients = new ConcurrentHashMap<String, Session>();
    // 在线用户对应群聊编号集合
//    private static Map<String, String> clientGroup = new ConcurrentHashMap<String, String>();
    private Session session;
    private String username;

    // 连接Socket触发
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        this.username = username;
        System.out.println(username + "已上线");
        this.session = session;
        // 在线人数+1
        addOnlineCount();
        clients.put(username, session);

        log.info(username + "已上线");

    }

    // 离线或关闭窗口、异常关闭浏览器触发
    @OnClose
    public void onClose() throws IOException {
        // 将当前用户从在线用户列表中移除
        clients.remove(username);
        // 在线人数-1
        subOnlineCount();
        // 推送离线通知给加我为好友的在线用户
        log.info(this.username + "已离线");
    }

    // 服务端向客户端推送消息
    @OnMessage
    public void onMessage(String messageJson) throws IOException {
        System.out.println(messageJson);
//        JSONObject json = JSONObject.parseObject(messageJson);
        JSONObject msg =JSONObject.parseObject(messageJson);
        String type = msg.getString("type");

        JSONObject data = msg.getJSONObject("data");

        JSONObject mine = data.getJSONObject("mine");
        JSONObject to = data.getJSONObject("to");
        String userName =to.getString("username");
        String role = msg.getString("role");
        System.out.println(role);
        
        if(type.equals("friendMessage")){
            //优先存储聊天信息
            if (role.equals("user")){
                chatInf = new ChatInf(101,Integer.valueOf(mine.getString("id")),mine.getString("content"),"text","U");
            }else if (role.equals("admin")){
                chatInf = new ChatInf(Integer.valueOf(mine.getString("id")),to.getInteger("id"),mine.getString("content"),"text","A");
            }
            System.out.println(JSON.toJSONString(chatInf));
            chatInfMapper.insert(chatInf);
            // 推送给固定用户
            sendMessageTo(userName,mine,to);
        }
    }

    // 异常接收
    @OnError
    public void onError(Session session, Throwable error) {
        log.info(this.username + "发生异常");
    }

//    /**
//     * 推送给所有在线用户
//     * @param message 推送信息
//     */
//    public void sendMessageAll(String message) throws IOException {
//        for (WebSocketTest item : clients.values()) {
//            item.session.getAsyncRemote().sendText(message);
//        }
//    }

    /**
     * 推送给固定用户
     */
    public void sendMessageTo(String userName,JSONObject mine,JSONObject to) throws IOException {
        for (String item : clients.keySet()) {
            if (item.equals(userName.equals("在线客服")? "zxkf":userName)){

                Map map = new HashMap<String, String>();
                map.put("username", mine.getString("username"));
                map.put("name", mine.getString("name"));
                map.put("avatar", mine.getString("avatar"));
                map.put("id", mine.getString("id"));
                map.put("fromid", mine.getString("fromid"));
                map.put("type", to.getString("type"));
                map.put("content",mine.getString("content"));
                clients.get(item).getAsyncRemote().sendText(JSON.toJSONString(map));
                System.out.println(JSON.toJSONString(map));
            }
        }
    }

    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }
}