package com.httpqqrobot.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RobotPrivateMessageRequestBody implements Serializable {

    private static final long serialVersionUID = 1L;

    String user_id;

    List<Message> message;

    @Data
    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;

        String type;

        SubMessage data;

        @Data
        public class SubMessage implements Serializable {

            private static final long serialVersionUID = 1L;

            String text;
        }
    }
}
