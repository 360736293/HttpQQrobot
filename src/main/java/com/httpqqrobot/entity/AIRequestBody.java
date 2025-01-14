package com.httpqqrobot.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AIRequestBody implements Serializable {

    private static final long serialVersionUID = 1L;

    String model;

    Message input;

    ResultFormat parameters;

    @Data
    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;

        List<MessageContent> messages;

        @Data
        public class MessageContent implements Serializable {

            private static final long serialVersionUID = 1L;

            String role;

            String content;
        }
    }

    @Data
    public class ResultFormat implements Serializable {

        private static final long serialVersionUID = 1L;

        String result_format;
    }
}