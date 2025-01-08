package com.httpqqrobot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.httpqqrobot.entity.Sender;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 消息格式
     */
    String messageFormat;

    /**
     * 原始消息
     */
    String rawMessage;

    /**
     * 机器人QQ号
     */
    String selfId;

    /**
     * 消息ID
     */
    String messageId;

    /**
     * 消息类型
     */
    String messageType;

    String targetId;

    String message;

    String userId;

    String realId;

    Sender sender;

    String subType;

    String postType;

    /**
     * 消息发送日期
     */
    String time;

    String messageSeq;

    String font;
}
