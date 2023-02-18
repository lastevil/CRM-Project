package org.unicrm.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.chat.model.ChatGroups;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupListDto {
    private String type;
    private List<ChatGroups> groups;
}
