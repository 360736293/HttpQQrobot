package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    List<UserMessage> getTodayUserSpeakRank(@Param("qqGroup") String qqGroup, @Param("date") String date);

    List<String> getTodayUserSpeakContent(@Param("qqGroup") String qqGroup, @Param("date") String date);

}
