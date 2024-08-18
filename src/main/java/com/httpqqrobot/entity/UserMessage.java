package com.httpqqrobot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("用户信息")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * QQ群
     */
    @ApiModelProperty(value = "qq群号")
    String qqGroup;

    /**
     * QQ号
     */
    @ApiModelProperty(value = "qq号")
    String qqNumber;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "qq昵称")
    String qqName;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    String content;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    Date date;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    @TableField(exist = false)
    Integer sum;
}
