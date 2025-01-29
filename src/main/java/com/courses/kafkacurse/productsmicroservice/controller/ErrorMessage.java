package com.courses.kafkacurse.productsmicroservice.controller;

import java.util.Date;

public class ErrorMessage {

    //Attributes
    private Date timestamp;
    private String message;
    private String detailsl;

    //Constructor
    public ErrorMessage(Date timestamp, String message, String detailsl) {
        this.timestamp = timestamp;
        this.message = message;
        this.detailsl = detailsl;
    }

    //Getters and Setters
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailsl() {
        return detailsl;
    }

    public void setDetailsl(String detailsl) {
        this.detailsl = detailsl;
    }
}
