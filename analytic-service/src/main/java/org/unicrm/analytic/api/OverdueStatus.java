package org.unicrm.analytic.api;

public enum OverdueStatus {
    OVERDUE("Просрочено"),
    THREE_DAYS_LEFT("Осталось 3 дня"),
    TWO_DAYS_LEFT("Осталось 2 дня"),
    TODAY_LEFT("Истечет сегодня");
    private final String value;

    private OverdueStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
