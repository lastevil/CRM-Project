package org.unicrm.ticket.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Tag(name = "Список статусов")
public enum TicketStatus {
    BACKLOG("Запланировано"),
    IN_PROGRESS("В работе"),
    DONE("Выполнено"),
    ACCEPTED("Принято"),
    DELETED("Удалено"),
    CLOSED("Закрыто"),
    OVERDUE("Просрочено"),
    THREE_DAYS_LEFT("Осталось 3 дня"),
    TWO_DAYS_LEFT("Осталось 2 дня"),
    TODAY_LEFT("Истечет сегодня");

    private final String value;

    private TicketStatus(String value) {
        this.value = value;
    }

    private static Map<String, TicketStatus> FORMAT_MAP = Stream
            .of(TicketStatus.values())
            .collect(Collectors.toMap(s -> s.value, Function.identity()));

    @JsonCreator
    public static TicketStatus fromString(String string) {
        return Optional
                .ofNullable(FORMAT_MAP.get(string))
                .orElseThrow(() -> new IllegalArgumentException(string));
    }
}
