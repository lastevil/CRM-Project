package org.unicrm.chat.controller;

import org.unicrm.chat.config.WebSocketEventListener;
//import org.unicrm.chat.dto.ChatRoomDto;
import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.ChatRoom;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.model.*;
import org.unicrm.chat.service.ChatGroupService;
import org.unicrm.chat.service.ChatRoomService;
import org.unicrm.chat.service.GroupService;
import org.unicrm.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app-chat")
public class ChatRoomController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatGroupService chatGroupService;
    private final GroupService groupService;
    private final WebSocketEventListener webSocketEventListener;

    @MessageMapping("/chat")
    public void sendNotification(MessageHeaders messageHeaders, @Payload ChatMessage chatMessage,
                                 @Header(name = "simpSessionId") String sessionId) {
        if (chatMessage.getRecipientId() != null) {
            Long chatRoomId = chatRoomService.save(chatMessage);
            Optional<ChatRoom> chatRoom = chatRoomService.findById(chatRoomId);
            if (!chatRoom.isEmpty()) {
                List<ChatRoom> chatRoomList = new ArrayList<>();
                chatRoomList.add(chatRoom.get());
                chatMessage.setChatRoom(chatRoomList);
                messagingTemplate.convertAndSend(
                        "/queue/" + sessionId,
                        chatMessage
                );
                if (webSocketEventListener.getSessionId(chatMessage.getRecipientId()) != null) {
                    messagingTemplate.convertAndSend(
                            "/queue/" + webSocketEventListener.getSessionId(chatMessage.getRecipientId()),
                            chatMessage
                    );
                }
            }
        }
        if (chatMessage.getGroupId() != null) {
            List<User> users = userService.findByGroupsId(chatMessage);
            if (users.size() != 0) {
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
                for (User u : users) {
                    chatGroupService.save(chatMessage, u.getId(), date);
                    Optional<ChatGroup> chatGroup =
                            chatGroupService.findByChatdateAndRecipientId(date, u.getId());
                    if (!chatGroup.isEmpty()) {
                        List<ChatGroup> chatGroups = new ArrayList<>();
                        chatGroups.add(chatGroup.get());
                        chatMessage.setChatGroups(chatGroups);
                        chatMessage.setRecipientName(u.getNicName());
                        if (u.getId() == chatMessage.getSenderId()) {
                            messagingTemplate.convertAndSend(
                                    "/queue/" + sessionId,
                                    chatMessage
                            );
                        }else {
                            if (webSocketEventListener.getSessionId(u.getId()) != null) {
                                messagingTemplate.convertAndSend(
                                        "/queue/" + webSocketEventListener.getSessionId(u.getId()),
                                        chatMessage
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        System.out.println("register----------------");
        Optional<User> user = userService.findByLogin(chatMessage.getSender());
        if (user.isEmpty()) {
            chatMessage.setSenderId(null);
            chatMessage.setSender(null);
        } else {
            chatMessage.setSenderId(user.get().getId());
            chatMessage.setSender(user.get().getNicName());
        }
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());

        return chatMessage;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        System.out.println("sendMessage------------------------");
        return chatMessage;
    }

    @MessageMapping("/chatgroup")
    @SendToUser("/topic/list")
    public ChatMessage findAllChatGroup(@Payload ChatMessage chatMessage) {

        List<Group> groupList = groupService.findByUsersId(chatMessage);
        List<ChatListGroup> chatListGroups = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            ChatListGroup group = new ChatListGroup();
            group.setId(groupList.get(i).getId());
            group.setTitle(groupList.get(i).getTitle());
            chatListGroups.add(group);
        }
        chatMessage.setChatGroup(chatListGroups);

        return chatMessage;
    }

    @MessageMapping("/chatusers")
    @SendToUser("/topic/list")
    public ChatMessage findAll(@Payload ChatMessage chatMessage) {

        List<User> usersList = userService.findAllByNotSenderId(chatMessage.getSenderId());
        List<ChatListUser> chatListUsers = new ArrayList<>();
        for (int i = 0; i < usersList.size(); i++) {
            ChatListUser user = new ChatListUser();
            user.setId(usersList.get(i).getId());
            user.setNicName(usersList.get(i).getNicName());
            int count = chatRoomService.findBySenderIdAndStatus(
                    chatMessage.getSenderId(), usersList.get(i).getId());
            user.setCount(count);
            chatListUsers.add(user);
        }
        chatMessage.setChatUsers(chatListUsers);

        return chatMessage;
    }


    @MessageMapping("/history")
    @SendToUser("/topic/history")
    public ChatHistory findHistory(@Payload ChatHistory chatHistory) {
        if (chatHistory.getRecipientId() != null) {
            chatHistory.setChatRoom(chatRoomService.findBySenderIdAndRecipientId(chatHistory));
        }else {
            List<ChatGroup> chatGroups = chatGroupService.findByGroupIdAndRecipientId(chatHistory);
            List<ChatGroupHistory> groupHistories = new ArrayList<>();
            for (ChatGroup g :chatGroups) {
                ChatGroupHistory cgh = new ChatGroupHistory();
                cgh.setType(String.valueOf(chatHistory.getType()));
                cgh.setSenderId(g.getSenderId());
                cgh.setRecipientId(g.getRecipientId());
                cgh.setMessage(g.getMessage());
                cgh.setId(g.getId());
                cgh.setGroupId(g.getGroupId());
                cgh.setChatdate(g.getChatdate());
                cgh.setRecipientName(userService.findNickNameById(g.getRecipientId()));
                cgh.setSenderName(userService.findNickNameById(g.getSenderId()));
                groupHistories.add(cgh);
            }
            chatHistory.setChatGroup(groupHistories);
        }
        System.out.println("findHistory------------------------");
        return chatHistory;
    }

    @MessageMapping("/session")
    @SendToUser("/topic/session")
    public ChatMessage getSessionId(@Payload ChatMessage chatMessage) {
        chatMessage.setRecipientName(
                webSocketEventListener.getSessionId(chatMessage.getSenderId()));
        return chatMessage;
    }

    @MessageMapping("/activeusers")
    @SendToUser("/topic/list")
    public ChatMessage findAllIdUsers(@Payload ChatMessage chatMessage) {
        chatMessage.setAllIdUsers(webSocketEventListener.findAllIdUsers());
        return chatMessage;
    }
    @MessageMapping("/groupunread")
    @SendToUser("/topic/list")
    public ChatMessage findAllGroupUnread(@Payload ChatMessage chatMessage) {
        chatMessage.setAllIdUsers(webSocketEventListener.findAllIdUsers());
        return chatMessage;
    }
}
