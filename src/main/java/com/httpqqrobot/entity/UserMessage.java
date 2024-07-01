package com.httpqqrobot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * QQ群
     */
    String qqGroup;

    /**
     * QQ号
     */
    String qqNumber;

    /**
     * 昵称
     */
    String qqName;

    /**
     * 内容
     */
    String content;

    /**
     * 日期
     */
    Date date;

    /**
     * 数量
     */
    @TableField(exist = false)
    Integer sum;
}
