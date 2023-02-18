package org.unicrm.chat.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @JsonSerialize(using = UUIDSerializer.class)
    @Column(name = "id")
    private UUID uuid;

    @Column(name = "username")
    private String userName;

    @Column(name = "nickname")
    private String nickName;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
}