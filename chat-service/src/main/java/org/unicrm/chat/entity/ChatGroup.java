package org.unicrm.chat.entity;

import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "chatgroup")
//@Data
//@NoArgsConstructor
public class ChatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chatdate")
    private String chatdate;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id")
    private Long recipientId;

    public ChatGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getChatdate() {
        return chatdate;
    }

    public void setChatdate(String chatdate) {
        this.chatdate = chatdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}
