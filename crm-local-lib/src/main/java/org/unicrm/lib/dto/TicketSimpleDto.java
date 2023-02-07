package org.unicrm.lib.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketSimpleDto {

    private String title;

    private String status;

    private String username;

    private Long departmentId;

}
