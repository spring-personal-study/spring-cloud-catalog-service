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
import java.util.LinkedHashMap;
import java.util.Map;

// 리스너를 통해 데이터를 가져오고 그 데이터로 DB에 있는 자료를 업데이트하는 목적으로 사용
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CatalogRepository repository;

    @KafkaListener(topics = {"example-catalog-topic"})
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

        /*
        {"schema":{
                "type":"struct",
                "fields":[{
                            "type":"string","optional":true,"field":"order_id"},
                            {"type":"string","optional":true,"field":"user_id"},
                            {"type":"string","optional":true,"field":"product_id"},
                            {"type":"int32","optional":true,"field":"qty"},
                            {"type":"int32","optional":true,"field":"unit_price"},
                            {"type":"int32","optional":true,"field":"total_price"
                          }],
                "optional":false,
                "name":"orders"
                },
          "payload":{"order_id":"99853df1-603b-4f49-bea3-c17676b60add",
                    "user_id":null,
                    "product_id":"CATALOG-001",
                    "qty":1,
                    "unit_price":150,
                    "total_price":150}
         }
         */

        //String productId = (String) map.get("productId");
        String productId = (String) ((LinkedHashMap<String, Object>) map.get("payload")).get("product_id");
        CatalogEntity entity = repository.findByProductId(productId);

        // 수량 변경
        if (entity != null) {
            entity.updateStock(entity.getStock() - (Integer) ((LinkedHashMap<String, Object>) map.get("payload")).get("qty"));
            repository.save(entity);
        }



    }

}
