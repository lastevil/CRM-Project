package org.unicrm.analytic.api;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Список возможных статусов")
public enum Status {
    BACKLOG("Запланировано"),
    IN_PROGRESS("В работе"),
    DONE("Выполнено"),
    ACCEPTED("Принято"),
    DELETED("Удалено");
    private final String value;

    private Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
