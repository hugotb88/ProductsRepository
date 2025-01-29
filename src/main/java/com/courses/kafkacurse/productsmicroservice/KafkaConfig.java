package com.courses.kafkacurse.productsmicroservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic createTopic(){
        return TopicBuilder.name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of(
                        "min.insync.replicas","2"
                )) //Minimum number of replicas that should acknowledge about the operation to consider it successfull.
                .build();
    }
}
