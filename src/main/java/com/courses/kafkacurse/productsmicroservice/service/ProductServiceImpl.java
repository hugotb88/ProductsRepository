package com.courses.kafkacurse.productsmicroservice.service;

import com.courses.kafkacurse.productsmicroservice.controller.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class ProductServiceImpl implements ProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String,ProductCreatedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel product) {
        String productId = UUID.randomUUID().toString();

        // TODO: Persist Product Details into a database table before publishing an Event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,product.getTitle(),product.getPrice(),product.getQuantity());
        CompletableFuture<SendResult<String,ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.error("Failed to send message: " + ex.getMessage());
            }else{
                LOGGER.info("Message sent successfully: " + result.getRecordMetadata());
            }
        });

        LOGGER.info("*** Returning Product ID ***");
        return productId;
    }
}
