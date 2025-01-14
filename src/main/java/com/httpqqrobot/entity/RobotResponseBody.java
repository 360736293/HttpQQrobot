package com.httpqqrobot.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RobotResponseBody implements Serializable {

    private static final long serialVersionUID = 1L;

    String group_id;

    List<Message> message;

    @Data
    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;

        String type;

        SubMessage data;

        @Data
        public class SubMessage implements Serializable {

            private static final long serialVersionUID = 1L;

            String id;

            String text;
        }
    }
}
