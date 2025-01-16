package com.httpqqrobot.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RobotGroupIntegrativeReplyRequestBody implements Serializable {

    private static final long serialVersionUID = 1L;

    String group_id;

    List<Message> messages;

    List<New> news;

    /**
     * 移动端提示
     */
    String prompt;

    /**
     * 外显总结
     */
    String summary;

    /**
     * 标题
     */
    String source;

    @Data
    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;

        String type;

        OuterData data;

        @Data
        public class OuterData implements Serializable {

            private static final long serialVersionUID = 1L;

            String user_id;

            String nickname;

            List<Content> content;

            @Data
            public class Content implements Serializable {

                private static final long serialVersionUID = 1L;

                String type;

                InnerData data;

                @Data
                public class InnerData implements Serializable {

                    private static final long serialVersionUID = 1L;

                    String text;

                    String file;
                }
            }
        }
    }

    @Data
    public class New implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 外显内容
         */
        String text;
    }
}
