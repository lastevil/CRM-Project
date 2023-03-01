package org.unicrm.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unicrm.chat.entity.ChatGroup;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, UUID> {

    @Modifying
    @Query(value = "update chatgroup set status = :status " +
            "where group_id = :group_id and recipient_id = :sender_id",
            nativeQuery = true)
    void update(@Param("group_id") Long group_id, @Param("sender_id") UUID sender_id,
                @Param("status") String status);

    @Query(value = "select * from chatgroup c where c.group_id = :group_id " +
            "and c.recipient_id = :recipient_id ORDER BY c.chatdate",
            nativeQuery = true)
    List<ChatGroup> findByGroupIdAndRecipientId(@Param("group_id") Long group_id,
                                                @Param("recipient_id") UUID recipient_id);

    @Query(value = "select * from chatgroup c where c.group_id = :group_id " +
            "and c.recipient_id = :recipient_id " +
            "and c.status = :status ORDER BY c.chatdate",
            nativeQuery = true)
    List<ChatGroup> findByGroupIdAndRecipientIdAndStatus(@Param("group_id") Long group_id,
                                                         @Param("recipient_id") UUID recipient_id,
                                                         @Param("status") String status);

    Optional<ChatGroup> findByChatdateAndRecipientId(String chatdate, UUID recipientId);
}