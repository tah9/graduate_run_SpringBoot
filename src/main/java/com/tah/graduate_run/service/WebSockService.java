package com.tah.graduate_run.service;
/*
 * @author: HaoGre173
 *
 * */

import com.alibaba.fastjson.JSONObject;
import com.tah.graduate_run.constant.RedisConst;
import com.tah.graduate_run.entity.ChatMsg;
import com.tah.graduate_run.untils.SpringUtil;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.Serializable;
import java.util.*;

@ServerEndpoint(value = "/webSocket/{userId}")
@Component
public class WebSockService {
    //单例不能autowrite
    private static RedisTemplate redisTemplate = SpringUtil.getBean(RedisTemplate.class);
    //存储当前Session
    private static Map<String, WebSockService> webSocketMap = new HashMap<>();
    //存储离线消息
    private Map<String, List<ChatMsg>> chatMsgs = new HashMap<>();
    private Session webSocketSession;
    private String userId;

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session webSocketSession){
        this.userId = userId;
        this.webSocketSession = webSocketSession;
        webSocketMap.put(userId, this);
        //获取消息
//        ReceivedFromStroe();
    }

    @OnClose
    public void onClose(){
        System.out.println("用户：" + userId + "下线");
        webSocketMap.remove(userId);
        msgToStore();
    }

    @OnMessage
    public void onMessage(String msg, Session HttpSession){
        JSONObject jsonObject = JSONObject.parseObject(msg);
        ChatMsg chatMsg = jsonObject.toJavaObject(ChatMsg.class);
        chatMsg.setSendTime(new Date());
        //写入Session会产生同步问题 ，不知道咋解决
//        chatMsg.setReceivedWebSocketSession(this.webSocketSession);
        sendTo(chatMsg);
    }

    @OnError
    public void onError(Throwable err, Session session){
        err.printStackTrace();
    }

    /**
     *
     */
    public String systemMessage(String text){
        ChatMsg chatMsg = new ChatMsg("0", userId, new Date(), text);
        return JSONObject.toJSONString(chatMsg);
    }
    /**
     * 收方在线发送消息，离线暂存消息
     * @param chatMsg
     */
    public void sendTo(ChatMsg chatMsg){
        String msg = JSONObject.toJSONString(chatMsg);
        String receivedId = chatMsg.getReceivedUserid();
        WebSockService that = webSocketMap.get(receivedId);

        //是否离线
        if (that != null){
            sendMsg(msg,that);
        }else{
            msgToTemp(chatMsg);
            //临时措施
            sendMsg(systemMessage("发送失败，用户离线"),this);
        }
    }

    /**
     * 暂存至内存中，待发送用户断开访问存入redis
     * K: receivedId V:chatMsgList
     * @param chatMsg
     */
    public void msgToTemp(ChatMsg chatMsg){
        String receivedId = chatMsg.getReceivedUserid();
        List<ChatMsg> chatMsgList = this.chatMsgs.get(receivedId);
        if (chatMsgList != null) {
            chatMsgList.add(chatMsg);
        }else{
            List<ChatMsg> list = new ArrayList<>();
            list.add(chatMsg);
            this.chatMsgs.put(receivedId, list);
        }
    }

    /**
     * 发送用户离线，消息从map中存入redis
     */
    public void msgToStore() {
        for (Map.Entry<String, List<ChatMsg>> entry : this.chatMsgs.entrySet()) {
            if (!redisTemplate.opsForHash().hasKey(RedisConst.REDIS_HASHMAP_KEY,entry.getKey())) {
                redisTemplate.opsForHash().put(RedisConst.REDIS_HASHMAP_KEY, entry.getKey(), entry.getValue());
            }else{
                //讲道理应该是Map 为啥是List？    List entry.getValue()
                List list = (List) redisTemplate.opsForHash().get(RedisConst.REDIS_HASHMAP_KEY,entry.getKey());
                list.addAll(entry.getValue());
                redisTemplate.opsForHash().put(RedisConst.REDIS_HASHMAP_KEY,entry.getKey(),list);
            }
        }
//        sessionToStore();
        System.out.println("当前缓存\t"+redisTemplate.opsForHash().entries(RedisConst.REDIS_HASHMAP_KEY));
    }

    /**
     * 发送方Session存入redis
     */
    public void sessionToStore(){
        redisTemplate.opsForHash().put(RedisConst.REDIS_SET_KEY, this.userId, this.webSocketSession);
    }

    /**
     * 接收用户上线，获取收到的chatMsgList和发送方Session
     */
    public void ReceivedFromStroe(){
        if (redisTemplate.opsForHash().hasKey(RedisConst.REDIS_HASHMAP_KEY, this.userId)) {
            List msgList = (List)redisTemplate.opsForHash().get(RedisConst.REDIS_HASHMAP_KEY, this.userId);
            System.out.println(msgList);
            List sessionList = (List)redisTemplate.opsForHash().get(RedisConst.REDIS_HASHMAP_KEY, this.userId);
            System.out.println(sessionList);
        }
    }

    public void sendMsg(String msg, WebSockService that){
        that.webSocketSession.getAsyncRemote().sendText(msg);
    }
}
