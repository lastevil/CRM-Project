package org.unicrm.analytic.configurations;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.unicrm.analytic.dto.kafka.KafkaTicketDto;
import org.unicrm.analytic.dto.kafka.KafkaUserDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    String server;
    @Value("${spring.kafka.consumer.group-id}")
    String groupId;

    @Bean
    public Map<String, Object> consumerUserConfig() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        consumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        consumer.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,false);
        consumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumer.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumer.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "org.unicrm.analytic.dto.kafka.KafkaUserDto");
        consumer.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumer.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return consumer;
    }

    @Bean
    public Map<String, Object> consumerTicketConfig() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        consumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        consumer.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,false);
        consumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumer.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumer.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "org.unicrm.analytic.dto.kafka.KafkaTicketDto");
        consumer.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumer.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return consumer;
    }

    @Bean
    public ConsumerFactory<UUID, KafkaUserDto> userConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerUserConfig());
    }

    @Bean
    public ConsumerFactory<UUID, KafkaTicketDto> ticketsConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerTicketConfig());
    }

    @Bean
    public KafkaListenerContainerFactory userKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<UUID, KafkaUserDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory ticketKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<UUID, KafkaTicketDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ticketsConsumerFactory());
        return factory;
    }

}

