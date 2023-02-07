package org.unicrm.chat.config;

import org.unicrm.chat.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.*;

@Component
public class WebSocketEventListener implements ApplicationListener<SessionSubscribeEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private Map<Long,String> mapSessionId = new HashMap<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("Received a new web socket connection");
    }
    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getSessionAttributes().get("username") != null){
            putSessionId((Long) headerAccessor.getSessionAttributes().get("username"),headerAccessor.getSessionId());
        }

    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = String.valueOf(headerAccessor.getSessionAttributes().get("username"));
        removeSessionId((Long) headerAccessor.getSessionAttributes().get("username"));

        if(username != null) {
            logger.info("User Disconnected : " + username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
    }
    public void putSessionId(Long id, String sessionId){
        mapSessionId.put(id, sessionId);
    }
    public void removeSessionId(Long id){
        if (mapSessionId.containsKey(id)) {
            mapSessionId.remove(id);
        }
    }

    public String getSessionId(Long id) {

        return mapSessionId.get(id);
    }

    public List<Long> findAllIdUsers(){
        List<Long> list = new ArrayList<>();
        for (Map.Entry<Long, String> set : mapSessionId.entrySet()){
            list.add(set.getKey());
        }
        return list;
    }
}