package com.courses.kafkacurse.productsmicroservice.controller;

import java.math.BigDecimal;

public class CreateProductRestModel {

    //Attributes
    private String title;
    private BigDecimal price;
    private Integer quantity;

    //Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
