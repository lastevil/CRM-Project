package org.unicrm.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "chatroom")
@Data
@Builder
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "chatdate")
    private String chatdate;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "recipient_id")
    private UUID recipientId;

    @Column(name = "sender_id")
    private UUID senderId;

    public ChatRoom() {}

}