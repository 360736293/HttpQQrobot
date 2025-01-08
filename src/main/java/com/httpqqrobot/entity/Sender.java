package com.httpqqrobot.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Sender implements Serializable {

    private static final long serialVersionUID = 1L;

    String userId;

    String nickname;

    String card;
}
