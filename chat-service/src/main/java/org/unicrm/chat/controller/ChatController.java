package org.unicrm.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicrm.chat.config.WebSocketEventListener;
import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.ChatRoom;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.model.*;
import org.unicrm.chat.service.ChatGroupService;
import org.unicrm.chat.service.ChatRoomService;
import org.unicrm.chat.service.GroupService;
import org.unicrm.chat.service.UserService;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Чат", description = "Контроллер обработки запросов по чату")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatGroupService chatGroupService;
    private final GroupService groupService;
    private final WebSocketEventListener webSocketEventListener;

    @Operation(summary = "метод получения сообщения, сохранения его в базе и передача отправителю и получателям")
    @MessageMapping("/chat")
    public void sendNotification(MessageHeaders messageHeaders, @Payload ChatMessage chatMessage,
                                 @Header(name = "simpSessionId") String sessionId) {
        if (chatMessage.getRecipientId() != null) {
            UUID chatRoomId = chatRoomService.save(chatMessage);
            Optional<ChatRoom> chatRoom = chatRoomService.findById(chatRoomId);
            if (!chatRoom.isEmpty()) {
                ChatMessage chat = ChatMessage.builder()
                        .chatDate(chatRoom.get().getChatdate())
                        .groupId(null)
                        .message(chatRoom.get().getMessage())
                        .recipientId(chatRoom.get().getRecipientId())
                        .recipientName(chatMessage.getRecipientName())
                        .senderId(chatRoom.get().getSenderId())
                        .senderName(chatMessage.getSenderName())
                        .type(chatMessage.getType())
                        .build();
                messagingTemplate.convertAndSend(
                        "/queue/" + sessionId,
                        chat
                );
                if (webSocketEventListener.getSessionId(chatMessage.getRecipientId()) != null) {
                    messagingTemplate.convertAndSend(
                            "/queue/" + webSocketEventListener.getSessionId(chatMessage.getRecipientId()),
                            chat
                    );
                }
            }
        }
        if (chatMessage.getGroupId() != null) {
            List<User> users = userService.findByGroupsId(chatMessage);
            if (users.size() != 0) {
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
                for (User u : users) {
                    chatGroupService.save(chatMessage, u.getUuid(), date);
                    Optional<ChatGroup> chatGroup =
                            chatGroupService.findByChatdateAndRecipientId(date, u.getUuid());
                    if (!chatGroup.isEmpty()) {
                        ChatMessage chat = ChatMessage.builder()
                                .chatDate(chatGroup.get().getChatdate())
                                .groupId(chatGroup.get().getGroupId())
                                .message(chatGroup.get().getMessage())
                                .recipientId(chatGroup.get().getRecipientId())
                                .recipientName(u.getNickName())
                                .senderId(chatGroup.get().getSenderId())
                                .senderName(chatMessage.getSenderName())
                                .type(chatMessage.getType())
                                .build();
                        if (u.getUuid() == chatMessage.getSenderId()) {
                            messagingTemplate.convertAndSend(
                                    "/queue/" + sessionId,
                                    chat
                            );
                        }else {
                            if (webSocketEventListener.getSessionId(u.getUuid()) != null) {
                                messagingTemplate.convertAndSend(
                                        "/queue/" + webSocketEventListener.getSessionId(u.getUuid()),
                                        chat
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    @Operation(summary = "метод регистрации в чате пользователя")
    @MessageMapping("/register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Optional<User> user = userService.findByUsername(chatMessage.getSenderName());
        if (user.isEmpty()) {
            chatMessage.setSenderId(null);
            chatMessage.setSenderName(null);
        } else {
            chatMessage.setSenderId(user.get().getUuid());
            chatMessage.setSenderName(user.get().getNickName());

            headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        }
        return chatMessage;
    }

//    @MessageMapping("/chat.send")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }

    @Operation(summary = "метод получения списка групп, в которых состоит пользователь, с количеством непрочитанных сообщений")
    @MessageMapping("/chatgroup")
    @SendToUser("/topic/list")
    public RequestListGroup findAllChatGroup(@Payload RequestListGroup group) {
        List<Group> groups = groupService.findByUsersId(group.getSenderId());
        List<ChatGroups> groupsList = new ArrayList<>();
        for (Group g : groups) {
            int count = chatGroupService.findByGroupIdAndRecipientIdAndStatus(
                    g.getId(), group.getSenderId());
            ChatGroups chatGroups = ChatGroups.builder()
                    .count(count)
                    .id(g.getId())
                    .title(g.getTitle())
                    .build();
            groupsList.add(chatGroups);
        }
        return RequestListGroup.builder()
                .senderId(group.getSenderId())
                .groups(groupsList)
                .build();
    }

    @Operation(summary = "метод получения списка всех пользователей(исключая текущего) с количеством непрочитанных сообщений")
    @MessageMapping("/chatusers")
    @SendToUser("/topic/list")
    public RequestListUsers findAll(@Payload RequestListUsers users) {
        List<User> usersList = userService.findAllByNotSenderId(users.getSenderId());
        List<ChatUsers> chatUsers = new ArrayList<>();
        for (int i = 0; i < usersList.size(); i++) {
            int count = chatRoomService.findBySenderIdAndRecipientIdAndStatus(
                    users.getSenderId(), usersList.get(i).getUuid());
            ChatUsers user = ChatUsers.builder()
                    .id(usersList.get(i).getUuid())
                    .nickName(usersList.get(i).getNickName())
                    .count(count)
                    .build();
            chatUsers.add(user);
        }
        return RequestListUsers.builder()
                .senderId(users.getSenderId())
                .users(chatUsers)
                .build();
    }


    @Operation(summary = "метод получения истории по группам и пользователям")
    @MessageMapping("/history")
    @SendToUser("/topic/history")
    public UserHistory findHistory(@Payload UserHistory userHistory) {
        if (userHistory.getRecipientId() != null) {
            List<ChatRoom> chatRooms = chatRoomService.findBySenderIdAndRecipientId(userHistory);
            userHistory.setChatRoom(chatRooms);
        }else {
            List<ChatGroup> chatGroups = chatGroupService.findByGroupIdAndRecipientId(userHistory);
            List<GroupHistory> groupHistories = new ArrayList<>();
            for (ChatGroup g :chatGroups) {
                GroupHistory cgh = GroupHistory.builder()
                        .senderId(g.getSenderId())
                        .recipientId(g.getRecipientId())
                        .message(g.getMessage())
                        .groupId(g.getGroupId())
                        .chatdate(g.getChatdate())
                        .recipientName(userService.findNickNameById(g.getRecipientId()))
                        .senderName(userService.findNickNameById(g.getSenderId()))
                        .build();
                groupHistories.add(cgh);
            }
            userHistory.setChatGroup(groupHistories);
        }
        return userHistory;
    }

    @Operation(summary = "метод получения сессии пользователя")
    @MessageMapping("/session")
    @SendToUser("/topic/session")
    public RequestSession getSessionId(@Payload RequestSession session) {
        return RequestSession.builder()
                .senderId(session.getSenderId())
                .session(webSocketEventListener.getSessionId(session.getSenderId()))
                .build();
    }

    @Operation(summary = "метод получения активных пользователей")
    @MessageMapping("/activeusers")
    @SendToUser("/topic/activeusers")
    public List<UUID> findAllIdUsers() {  //@Payload ChatMessage chatMessage
        return webSocketEventListener.findAllIdUsers();
    }
}