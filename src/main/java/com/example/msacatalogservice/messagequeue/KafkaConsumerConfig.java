package com.example.msacatalogservice.messagequeue;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;

/*
    producer 는 order-service 이고
    consumer 는 catalog-service 로 설정
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    // 컨슈머 설정
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        HashMap<String, Object> properties = new HashMap<>();
        // 카프카 컨테이너 호스트 (kafka container host)
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // 카프카 서버의 주소. config-service 에서 값을 가져와 설정할 수도 있다.
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId"); // 여러개의 consumer 가 값을 전달받는 상황을 고려해 몇몇 consumer 를 특정 그룹으로 묶는 설정
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // 키 역직렬화 설정
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);  // 값 역직렬화 설정
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    // 리스너 등록
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return kafkaListenerContainerFactory;
    }
}
