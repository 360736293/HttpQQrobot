package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.FunctionStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FunctionStatusMapper extends BaseMapper<FunctionStatus> {

    List<FunctionStatus> getAllFunctionStatus();

    Integer changeFunctionStatus(@Param("name") String name, @Param("status") String status);
}
