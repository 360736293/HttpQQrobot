package com.httpqqrobot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author mfliu
 * @since 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SteamDiscountNotify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String userId;

    private String gameId;

    private String gameName;

    private String url;

    private String imageUrl;


}
