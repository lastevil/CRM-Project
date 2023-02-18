package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListNewUsersGroup {
    private String type;
    private Long id;
    private String title;
    private UUID users;
}
