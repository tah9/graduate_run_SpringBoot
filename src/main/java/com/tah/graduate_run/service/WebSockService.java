package com.tah.graduate_run.service;
/*
 * @author: HaoGre173
 *
 * */

import com.alibaba.fastjson.JSONObject;
import com.tah.graduate_run.constant.RedisConst;
import com.tah.graduate_run.entity.ChatMsg;
import com.tah.graduate_run.untils.SpringUtil;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
@ServerEndpoint(value = "/webSocket/{userId}/{receivedId}")
@Component
public class WebSockService {
    //单例不能autowrite
    private static RedisTemplate redisTemplate = SpringUtil.getBean(RedisTemplate.class);

    //存储当前Session
    private static Map<String, WebSockService> webSocketMap = new HashMap<>();
//    private static CopyOnWriteArraySet webSocketMap = new CopyOnWriteArraySet<Session>();

    //临时存储离线消息
    private Map<String, List<ChatMsg>> chatMsgs = new ConcurrentHashMap<>();
    private Session webSocketSession;
    private boolean isSendOfflineMsg = false;
    private String userId;
    private String receivedId;

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, @PathParam(value = "receivedId") String receivedId, Session webSocketSession){
        System.out.println(userId +"\t"+ receivedId );
        this.userId = userId;
        this.receivedId = receivedId;
        this.webSocketSession = webSocketSession;
        //是否收到离线消息
        if (hasOfflineMsg()) {
            ReceivedFromStroe();
        }
        this.webSocketMap.put(userId, this);
    }

    @OnClose
    public void onClose(){
        System.out.println("用户：" + userId + "下线");
        //发送了离线消息，保存Session至Map
        if (isSendOfflineMsg) {
            msgToStore();
        } else{
//            webSocketMap.remove(userId);
        }

    }

    @OnMessage
    public void onMessage(String msg, Session session){
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
     * 收方在线直接发送消息，离线暂存消息
     * @param chatMsg
     */
    public void sendTo(ChatMsg chatMsg){
//        String msg = JSONObject.toJSONString(chatMsg);
        String receivedId = chatMsg.getReceivedUserid();

        WebSockService that = this;
        //是否离线
        if (that != null){
            sendMsg(chatMsg,that);
        }else{
            this.isSendOfflineMsg = true;
            msgToTemp(chatMsg);
            //临时措施
//            sendMsg(systemMessage("发送失败，用户离线"),this);
        }
    }

    /**
     * 暂存至内存中，待发送用户离线存入redis
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
     * 将用户的发送离线消息从map中存入redis
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
    }

    public void delMsgFromStore(){
        List<Map> list = (List) redisTemplate.opsForHash().get(RedisConst.REDIS_HASHMAP_KEY, this.userId);
        Map<String, List<ChatMsg>> chatMsgMap = new HashMap();
        List<ChatMsg> chatMsgList = new ArrayList<>();
        for (Map msgMap: list){
            ChatMsg chatMsg = JSONObject.parseObject(JSONObject.toJSONString(msgMap), ChatMsg.class);
            if (!this.receivedId.equals(chatMsg.getSendUserid())){
                chatMsgList.add(chatMsg);
            }
        }
        chatMsgMap.put(this.userId, chatMsgList);
        if (chatMsgMap.size() != 0){
            redisTemplate.opsForHash().put(RedisConst.REDIS_HASHMAP_KEY,this.userId,chatMsgMap);
        }
    }

    public boolean hasOfflineMsg(){
        return redisTemplate.opsForHash().hasKey(RedisConst.REDIS_HASHMAP_KEY, this.userId);
    }

    /**
     * 接收用户上线，获取收到的chatMsgList
     */
    public void ReceivedFromStroe(){
        //redisTemplate.opsForHash().get()为什么返回的List里是Map而不是bean？
        //k是bean的属性 V是bean的值
        List msgsList = redisTemplate.opsForHash().multiGet(RedisConst.REDIS_HASHMAP_KEY, Collections.singleton(this.userId));
        for (Map msgMap : (List<Map>)msgsList.get(0)) {
            String sendUserid = (String)msgMap.get("sendUserid");
            //当前窗口的收方与离线消息中的发方是一个人时执行
            if (this.receivedId.equals(sendUserid)){
                ChatMsg msg = JSONObject.parseObject(JSONObject.toJSONString(msgMap), ChatMsg.class);
                sendMsg(msg,this);
            }
        }
        delMsgFromStore();
    }

    /**
     * 系统消息，sendId：0
     * @param text
     * @return
     */
    public ChatMsg systemMessage(String text){
        return new ChatMsg("0", userId, new Date(), text);
    }

    @SneakyThrows
    public void sendMsg(ChatMsg chatMsg, WebSockService that){
        Session session = that.webSocketSession;
        String msg = JSONObject.toJSONString(chatMsg);
        synchronized (session){
                session.getBasicRemote().sendText(msg);
        }
    }
}
