package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.unicrm.auth.dto.kafka.KafkaUserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SenderHandler {

    private final List<KafkaUserDto> listUserDtoForSend = new ArrayList<>();
    private final KafkaTemplate<UUID, KafkaUserDto> kafkaTemplate;

    public List<KafkaUserDto> get() {
        return listUserDtoForSend;
    }

    public void sendToKafka() {
        while (listUserDtoForSend.iterator().hasNext()) {
            ListenableFuture<SendResult<UUID, KafkaUserDto>> future = kafkaTemplate.send("userTopic", UUID.randomUUID(), listUserDtoForSend.iterator().next());
            kafkaTemplate.flush();
            if (future.isDone()) listUserDtoForSend.remove(listUserDtoForSend.iterator().next());
        }
    }
}
