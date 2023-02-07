package org.unicrm.chat.repository;

import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
    @Query(value = "select * from chatgroup",
            nativeQuery = true)
    List<ChatGroup> findAll();

    @Modifying
    @Query(value = "insert into chatgroup (chatdate,message,status,group_id,sender_id,recipient_id) " +
            "values(:chatdate, :message, :status, :group_id, :sender_id, :recipient_id)",
            nativeQuery = true)
    void insert(@Param("chatdate") String chatdate, @Param("message") String message,
                @Param("status") String status, @Param("group_id") Long group_id,
                @Param("sender_id") Long sender_id, @Param("recipient_id") Long recipient_id
                );

    @Query(value = "select max(id) from chatgroup " , nativeQuery = true)
    Long findMaxId();

    List<ChatGroup> findByGroupIdAndRecipientId(Long groupId, Long recipientId);

    Optional<ChatGroup> findByChatdateAndRecipientId(String chatdate, Long recipientId);
}