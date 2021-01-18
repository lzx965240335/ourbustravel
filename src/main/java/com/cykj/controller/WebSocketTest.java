package com.cykj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cykj.bean.ChatInf;
import com.cykj.bean.MySpringConfigurator;
import com.cykj.mapper.ChatInfMapper;
import com.cykj.service.UserInfService;
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

    private static Logger log = Logger.getLogger(WebSocketTest.class);
    // 在线人数
    private static int onlineCount = 0;
    // 在线用户列表
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    // 在线用户对应群聊编号集合
//    private static Map<String, String> clientGroup = new ConcurrentHashMap<String, String>();
    private Session session;
    private String username;

    // 连接Socket触发
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        this.username = username;
        this.session = session;
        // 在线人数+1
        addOnlineCount();
        // 把当前用户添加到在线用户列表
        clients.put(username, session);
        // 访问数据库获取该用户所有群聊编号集合
//        clientGroup.put(username, "1,2,3,4,5");
        // 推送上线通知给加我为好友的在线用户
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
        JSONObject json = JSONObject.parseObject(messageJson);
        String type = json.getString("type");
        JSONObject data = json.getJSONObject("data");
//        if(type.equals("all")){
//            // 推送给所有在线用户
//            sendMessageAll(messageJson);
//        }else if(type.equals("groupMessage")){
//            // 推送给群在线用户
//            sendMessageGroup(messageJson, data.getString("fromGroupId"), false);
//        }else
            if(type.equals("friendMessage")){
            // 推送给固定用户
            sendMessageTo(messageJson, String.valueOf(data.getLong("toId")));
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
     * @param message 推送信息
     * @param To 好友编号
     */
    public void sendMessageTo(String message, String To) throws IOException {
        for (String item : clients.keySet()) {
            System.out.println(message);
            JSONObject msg =JSONObject.parseObject(message);
            JSONObject data = msg.getJSONObject("data");
            JSONObject mine = data.getJSONObject("mine");
            JSONObject to = data.getJSONObject("to");
            if (item.equals(to.getString("username"))){
                Map map = new HashMap<String, String>();
                map.put("username", mine.getString("username"));
                map.put("name", mine.getString("name"));
                map.put("avatar", mine.getString("avatar"));
                map.put("id", mine.getString("id"));
                map.put("fromid", mine.getString("fromid"));
                map.put("type", to.getString("type"));
                map.put("content",mine.getString("content"));
                clients.get(item).getAsyncRemote().sendText(JSON.toJSONString(map));
                ChatInf chatInf = new ChatInf();

//                if (){
//
//                }
                chatInf.setAdminid(to.getInteger("id"));
            }
        }
    }

//    /**
//     * 推送给群聊成员
//     * @param message 推送信息
//     * @param To 群聊编号
//     * @param isWhole 推送状态(1推送给所有成员，0推送给非自己外的所有成员)
//     */
//    public void sendMessageGroup(String message, String To, boolean isWhole) throws IOException {
//        String filterId = "," + To + ",";
//        if(isWhole){
//            for (WebSocketTest item : clients.values()) {
//                String groupIds = clientGroup.get(item.username);
//                if(groupIds.indexOf(filterId)>=0){
//                    item.session.getAsyncRemote().sendText(message);
//                }
//            }
//        }else{
//            for (WebSocketTest item : clients.values()) {
//                String groupIds = clientGroup.get(item.username);
//                if(groupIds.indexOf(filterId)>=0 && !this.username.equals(item.username)){
//                    item.session.getAsyncRemote().sendText(message);
//                }
//            }
//        }
//    }
    

//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }
//    public static synchronized Map<String, Session> getClients() {
//        return clients;
//    }
}