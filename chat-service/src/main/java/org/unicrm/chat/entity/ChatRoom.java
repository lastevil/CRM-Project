package org.unicrm.chat.entity;

import org.unicrm.chat.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "chatroom")
//@Data
//@NoArgsConstructor
//@Builder
public class ChatRoom { //new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
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

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "sender_id")
    private Long senderId;

    public ChatRoom() {}

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

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public Long getRecipientId() {
        return recipientId;
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

    public String getChatdate() {
        return chatdate;
    }

    public void setChatdate(String chatdate) {
        this.chatdate = chatdate;
    }
}
