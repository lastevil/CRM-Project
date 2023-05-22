package org.unicrm.ticket.exception.validators;

import org.springframework.stereotype.Component;
import org.unicrm.ticket.dto.TicketPage;

@Component
public class TicketPageValidator {

    public void validate(TicketPage index) {
        if (index.getPage() < 1) {
            index.setPage(1);
        }
        if (index.getSize() < 1) {
            index.setSize(10);
        }
    }
}
