package org.unicrm.auth.configs;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.unicrm.auth.dto.kafka.KafkaUserDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String server;

    @Bean
    public Map<String, Object> producerConfig(){
        Map<String, Object> producer = new HashMap<>();
        producer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        producer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        producer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producer.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return producer;
    }

    @Bean
    public ProducerFactory<UUID, KafkaUserDto> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<UUID, KafkaUserDto> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}