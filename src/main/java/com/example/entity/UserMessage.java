package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
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
    String nickName;

    /**
     * 日期
     */
    Date date;

    /**
     * 数量
     */
    Integer sum;
}
