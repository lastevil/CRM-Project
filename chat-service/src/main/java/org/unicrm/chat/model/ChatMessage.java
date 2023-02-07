package org.unicrm.chat.model;

import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.ChatRoom;

import java.util.List;

public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private Long senderId;
    private Long recipientId;
    private String recipientName;
    private Long groupId;
    private List<ChatListGroup> chatGroup;
    private List<ChatListUser> chatUsers;
    private Long chatroomId;
    private Long chatgroupId;
    private List<ChatRoom> chatRoom;
    private List<ChatGroup> chatGroups;
    private String chatDate;
    private List<Long> allIdUsers;

    public ChatMessage() {

    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        LIST,
        HISTORY,
        SESSION,
        NOTIFICATION,
        ACTIVEUSERS,
        GROUPUNREAD,
        ERROR
    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public List<ChatListGroup> getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(List<ChatListGroup> chatGroup) {
        this.chatGroup = chatGroup;
    }

    public List<ChatListUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(List<ChatListUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }

    public Long getChatgroupId() {
        return chatgroupId;
    }

    public void setChatgroupId(Long chatgroupId) {
        this.chatgroupId = chatgroupId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
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

    public List<Long> getAllIdUsers() {
        return allIdUsers;
    }

    public void setAllIdUsers(List<Long> allIdUsers) {
        this.allIdUsers = allIdUsers;
    }

    public List<ChatGroup> getChatGroups() {
        return chatGroups;
    }

    public void setChatGroups(List<ChatGroup> chatGroups) {
        this.chatGroups = chatGroups;
    }
}