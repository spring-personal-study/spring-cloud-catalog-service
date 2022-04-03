package com.example.msacatalogservice.messagequeue;

import com.example.msacatalogservice.model.CatalogEntity;
import com.example.msacatalogservice.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// 리스너를 통해 데이터를 가져오고 그 데이터로 DB에 있는 자료를 업데이트하는 목적으로 사용
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CatalogRepository repository;

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage) {
      log.info("Kafka Message: ->" + kafkaMessage);

      Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 카프카로부터 받은 데이터를 json 타입으로 변환해서 사용해보려고 함
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String productId = (String) map.get("productId");
        CatalogEntity entity = repository.findByProductId(productId);

        // 수량 변경
        if (entity != null) {
            entity.updateStock(entity.getStock() - (Integer) map.get("qty"));
            // repository.save(entity);
        }



    }

}
