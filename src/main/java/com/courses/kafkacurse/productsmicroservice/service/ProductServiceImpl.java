package com.courses.kafkacurse.productsmicroservice.service;

import com.courses.kafkacurse.productsmicroservice.controller.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class ProductServiceImpl implements ProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String,ProductCreatedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel product) throws Exception {
        String productId = UUID.randomUUID().toString();

        // TODO: Persist Product Details into a database table before publishing an Event

        //Asynchronous way
//        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,product.getTitle(),product.getPrice(),product.getQuantity());
//        CompletableFuture<SendResult<String,ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
//
//        future.whenComplete((result, ex) -> {
//            if (ex != null) {
//                LOGGER.error("Failed to send message: " + ex.getMessage());
//            }else{
//                LOGGER.info("Message sent successfully: " + result.getRecordMetadata());
//            }
//        });


        //Synchronous way 1
//        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,product.getTitle(),product.getPrice(),product.getQuantity());
//        CompletableFuture<SendResult<String,ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
//
//        future.whenComplete((result, ex) -> {
//            if (ex != null) {
//                LOGGER.error("Failed to send message: " + ex.getMessage());
//            }else{
//                LOGGER.info("Message sent successfully: " + result.getRecordMetadata());
//            }
//        });
//
//        //Make the call synchronous, but it can be confusing if we have CompletableFuture above
//        future.join();

        //Synchronous way 2
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,product.getTitle(),product.getPrice(),product.getQuantity());
        SendResult<String,ProductCreatedEvent> result =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();

        LOGGER.info("Partition: " + result.getRecordMetadata().partition());
        LOGGER.info("Topic: " + result.getRecordMetadata().topic());
        LOGGER.info("Offset: " + result.getRecordMetadata().offset());

        LOGGER.info("*** Returning Product ID ***");
        return productId;
    }
}
