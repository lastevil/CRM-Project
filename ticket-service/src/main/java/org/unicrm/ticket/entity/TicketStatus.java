package org.unicrm.ticket.entity;

import lombok.Getter;

@Getter
public enum TicketStatus {
    BACKLOG("Запланировано"),
    IN_PROGRESS("В работе"),
    DONE("Выполнено"),
    ACCEPTED("Принято"),
    DELETED("Удалено"),
    CLOSED("Закрыто");

    private final String value;

    private TicketStatus(String value) {
        this.value = value;
    }
}
