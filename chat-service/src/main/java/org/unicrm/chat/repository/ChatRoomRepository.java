package org.unicrm.chat.repository;

import org.unicrm.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    @Query(value = "select * from chatroom c where c.sender_id = :sender_id " +
            "and c.recipient_id = :recipient_id " +
            "or c.sender_id = :recipient_id " +
            "and c.recipient_id = :sender_id  ORDER BY c.chatdate",
            nativeQuery = true)
    List<ChatRoom> findBySenderIdAndRecipientId(@Param("sender_id") UUID senderId,
                                                @Param("recipient_id") UUID recipientId);

    Optional<ChatRoom> findBySenderIdAndRecipientIdAndChatdate(UUID senderId,
                                                               UUID recipientId, String chatdate);

    @Modifying
    @Query(value = "update chatroom set status = :status " +
            "where recipient_id = :recipient_id and sender_id = :sender_id",
            nativeQuery = true)
    void update(@Param("sender_id") UUID sender_id, @Param("recipient_id") UUID recipient_id,
                @Param("status") String status);

    @Query(value = "select * from chatroom c where c.sender_id = :sender_id " +
            "and c.recipient_id = :recipient_id " +
            "and c.status = :status ORDER BY c.chatdate",
            nativeQuery = true)
    List<ChatRoom> findBySenderIdAndRecipientIdAndStatus(@Param("sender_id") UUID senderId,
                                                         @Param("recipient_id") UUID recipientId,
                                                         @Param("status") String status);

    Optional<ChatRoom> findById(UUID id);
}
