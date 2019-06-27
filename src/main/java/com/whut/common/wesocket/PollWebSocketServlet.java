package com.whut.common.wesocket;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yy
 * websocket访问接口
 * @time 2018-03-26
 */
@Component
@ServerEndpoint(value="/websocket/{id}")
public class PollWebSocketServlet {

    private static final Map<String,Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("id") String id, Session session) {

        Session oldSession = SESSION_MAP.get(id);
        if (oldSession!=null){
            try {
                oldSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SESSION_MAP.put(id,session);

        System.out.println("WebSocketOpen: id=" + id);
    }

    @OnMessage
    public void onMessage(String message,@PathParam("id") String id) {

        System.out.println("Message WebSocket: id=" + id + " message=" + message);
    }

    @OnClose
    public void onClose(@PathParam("id") String id) {
        SESSION_MAP.remove(id);
        System.out.println("Closed WebSocket: id=" + id);
    }

    @OnError
    public void onError(@PathParam("id") String id, Throwable throwable, Session session) {
        System.out.println("Error WebSocket: id=" + id + "Caused by " + throwable.getCause());
    }

    public void sendText(String id, String text) {
        Session session = SESSION_MAP.get(id);
        if(session == null) return;
        try {
            session.getBasicRemote().sendText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendJson(String id, JSONObject json) {
        Session session = SESSION_MAP.get(id);
        if(session == null) return;
        try {
            session.getBasicRemote().sendText(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
