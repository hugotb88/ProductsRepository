package com.courses.kafkacurse.productsmicroservice.service;

import com.courses.kafkacurse.productsmicroservice.controller.CreateProductRestModel;
import com.courses.kafkacurse.productsmicroservice.controller.ProductController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    //@Autowired
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String,ProductCreatedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel product) {
        String productId = UUID.randomUUID().toString();

        // TODO: Persist Product Details into a database table before publishing an Event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,product.getTitle(),product.getPrice(),product.getQuantity());
        kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
        return productId;
    }
}
