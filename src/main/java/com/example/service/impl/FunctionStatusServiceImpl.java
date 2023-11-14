package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.FunctionStatus;
import com.example.mapper.FunctionStatusMapper;
import com.example.service.IFunctionStatusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FunctionStatusServiceImpl extends ServiceImpl<FunctionStatusMapper, FunctionStatus> implements IFunctionStatusService {

}
