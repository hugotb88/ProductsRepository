package com.courses.kafkacurse.productsmicroservice.service;

import com.courses.kafkacurse.productsmicroservice.controller.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel product);

}
