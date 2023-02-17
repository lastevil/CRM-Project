package org.unicrm.ticket.entity;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Список временных интервалов")
public enum TicketTimeInterval {
    DAY("День"),
    WEEK("Неделя"),
    MONTH("Месяц"),
    THREE_MONTH("Квартал"),
    HALF_YEAR("Полугодие"),
    YEAR("Год");
    private final String value;

    private TicketTimeInterval(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}