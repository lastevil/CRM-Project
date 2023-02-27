package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.unicrm.auth.dto.kafka.KafkaUserDto;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SenderHandler {

    private final KafkaTemplate<UUID, KafkaUserDto> kafkaTemplate;

    public void sendToKafka(List<KafkaUserDto> listUserDtoForSend) {
        Iterator<KafkaUserDto> iter = listUserDtoForSend.iterator();
        while (iter.hasNext()) {
            ListenableFuture<SendResult<UUID, KafkaUserDto>> future = kafkaTemplate.send("userTopic", UUID.randomUUID(), iter.next());
            kafkaTemplate.flush();
            if (future.isDone()) iter.remove();
        }
    }
}
