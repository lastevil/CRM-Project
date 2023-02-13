package org.unicrm.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "groups")
@Data
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "users_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;

    public Group() {
    }
}