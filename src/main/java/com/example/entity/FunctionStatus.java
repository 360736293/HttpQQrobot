package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionStatus {
    /**
     * 功能名
     */
    String name;

    /**
     * 状态
     */
    String status;
}
