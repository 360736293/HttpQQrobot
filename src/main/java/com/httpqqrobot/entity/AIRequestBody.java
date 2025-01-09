package com.httpqqrobot.entity;

import lombok.Data;

import java.util.List;

@Data
public class AIRequestBody {

    String model;

    Message input;

    ResultFormat parameters;

    @Data
    public class Message {

        List<MessageContent> messages;

        @Data
        public class MessageContent {

            String role;

            String content;
        }
    }

    @Data
    public class ResultFormat {

        String result_format;
    }
}