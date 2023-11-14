package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {


    Integer addUserMessage(@Param("qqGroup") String qqGroup, @Param("qqNumber") String qqNumber,@Param("nickName") String nickName, @Param("content") String content, @Param("timeStamp") Date timeStamp);

    List<UserMessage> getTodayUserSpeakRank(@Param("qqGroup") String qqGroup, @Param("date") String date);

    List<String> getTodayUserSpeakContent(@Param("qqGroup") String qqGroup, @Param("date") String date);

}
