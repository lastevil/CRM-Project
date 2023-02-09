package org.unicrm.analytic.api;

public enum Status {
    BACKLOG("Запланировано"),
    IN_PROGRESS("В работе"),
    DONE("Выполнено"),
    ACCEPTED("Принято"),
    DELETED("Удалено"),
    OVERDUE("Просрочено"),
    CLOSED("Закрыто");
    private final String value;

    private Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
