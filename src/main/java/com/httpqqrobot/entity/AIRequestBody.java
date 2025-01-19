package com.httpqqrobot.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AIRequestBody implements Serializable {

    private static final long serialVersionUID = 1L;

    String model;

    List<Message> messages;

    boolean enable_search;


    @Data
    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;

        String role;

        String content;
    }


}