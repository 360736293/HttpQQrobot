package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.FunctionStatus;

import java.util.List;

public interface IFunctionStatusService extends IService<FunctionStatus> {

    List<FunctionStatus> getAllFunctionStatus();

    Integer changeFunctionStatus(String name, String status);
}
