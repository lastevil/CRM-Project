package org.unicrm.analytic.api;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Список возможных временных интервалов")
public enum TimeInterval {
    DAY("День"),
    WEEK("Неделя"),
    MONTH("Месяц"),
    THREE_MONTH("Квартал"),
    HALF_YEAR("Полугодие"),
    YEAR("Год");
    private final String value;

    private TimeInterval(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}