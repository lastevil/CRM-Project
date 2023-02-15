package org.unicrm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDepartmentDto {

    private Long departmentId;

    private String title;
}
