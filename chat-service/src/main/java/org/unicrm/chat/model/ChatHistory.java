package org.unicrm.chat.model;

import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.ChatRoom;

import java.util.List;

public class ChatHistory {
    private Long id;
    private ChatMessage.MessageType type;
    private String content;
    private String sender;
    private Long senderId;
    private Long recipientId;
    private String recipientName;
    private Long groupId;
    private List<ChatRoom> chatRoom;
    private List<ChatGroupHistory> chatGroup;
    private String chatDate;

    public ChatHistory() {
    }

    public ChatMessage.MessageType getType() {
        return type;
    }

    public void setType(ChatMessage.MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<ChatRoom> getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(List<ChatRoom> chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getChatDate() {
        return chatDate;
    }

    public void setChatDate(String chatDate) {
        this.chatDate = chatDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ChatGroupHistory> getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(List<ChatGroupHistory> chatGroup) {
        this.chatGroup = chatGroup;
    }
}