package com.httpqqrobot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "user_message")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 消息格式
     */
    String messageFormat;

    /**
     * 原始消息内容
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

    /**
     * 消息内容
     */
    String message;

    String userId;

    String groupId;

    String realId;

    @TableField(exist = false)
    Sender sender;

    /**
     * 发送人QQ号
     */
    String senderUserId;

    /**
     * 发送人QQ昵称
     */
    String senderNickname;

    /**
     * 发送人QQ群昵称
     */
    String senderCard;

    String subType;

    String operatorId;

    String postType;

    /**
     * 消息发送日期
     */
    String time;

    String messageSeq;

    String font;

    String noticeType;
}
