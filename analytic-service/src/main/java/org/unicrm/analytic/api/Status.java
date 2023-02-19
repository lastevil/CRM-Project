package org.unicrm.analytic.api;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Список возможных статусов")
public enum Status {
    BACKLOG("Запланировано"),
    IN_PROGRESS("В работе"),
    DONE("Выполнено"),
    ACCEPTED("Принято"),
    DELETED("Удалено"),
    OVERDUE("Просрочено"),
    THREE_DAYS_LEFT("Осталось 3 дня"),
    TWO_DAYS_LEFT("Осталось 2 дня"),
    TODAY_LEFT("Истечет сегодня");
    private final String value;

    private Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
