package org.unicrm.chat.repository;

import org.unicrm.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "select * from chatroom c where c.sender_id = :sender_id " +
            "and c.recipient_id = :recipient_id " +
            "or c.sender_id = :recipient_id " +
            "and c.recipient_id = :sender_id ",
            nativeQuery = true)
    List<ChatRoom> findBySenderIdAndRecipientId(@Param("sender_id") Long senderId, @Param("recipient_id") Long recipientId);

    @Modifying
    @Query(value = "insert into chatroom (chatdate,message,status,recipient_id,sender_id) " +
            "values(:chatdate, :message, :status, :recipient_id, :sender_id)",
            nativeQuery = true)
    void insert(@Param("sender_id") Long sender_id, @Param("recipient_id") Long recipient_id,
                @Param("chatdate") String chatdate, @Param("status") String status,
                @Param("message") String message
                );

    @Query(value = "select c from chatroom c where c.sender_id = :sender_id " +
            "and c.recipient_id = :recipient_id " +
            "and c.chatdate = :chatdate",
            nativeQuery = true)
    Optional<ChatRoom> findBySenderIdAndRecipientIdAndChatdate(@Param("sender_id") Long senderId,
                       @Param("recipient_id") Long recipientId, @Param("chatdate") String chatdate);

    @Query(value = "select max(id) from chatroom " , nativeQuery = true)
    Long findMaxId();

    @Query(value = "select * from chatroom c where c.sender_id = :sender_id " +
            "and c.recipient_id = :recipient_id " +
            "and c.status = :status ", nativeQuery = true)
    List<ChatRoom> findBySenderIdAndRecipientIdAndStatus(@Param("sender_id") Long senderId,
                                                         @Param("recipient_id") Long recipientId,
                                                         @Param("status") String status);
}
