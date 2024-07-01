package com.httpqqrobot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.httpqqrobot.entity.FunctionStatus;
import com.httpqqrobot.mapper.FunctionStatusMapper;
import com.httpqqrobot.service.IFunctionStatusService;
import org.springframework.stereotype.Service;

@Service
public class FunctionStatusServiceImpl extends ServiceImpl<FunctionStatusMapper, FunctionStatus> implements IFunctionStatusService {

}
