package com.example.utils;

import com.example.constant.AppConstant;
import com.example.entity.FunctionStatus;
import com.example.service.IFunctionStatusService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class FunctionStatusLoad {
    @Resource
    private IFunctionStatusService functionStatusService;

    public void act() {
        List<FunctionStatus> allFunctionStatus = functionStatusService.getAllFunctionStatus();
        for (FunctionStatus functionStatus : allFunctionStatus) {
            switch (functionStatus.getName()) {
                case "GroupRecallStatus":
                    AppConstant.GroupRecallStatus = functionStatus.getStatus();
                    break;
                case "FlashImageStatus":
                    AppConstant.FlashImageStatus = functionStatus.getStatus();
                    break;
                case "PokeStatus":
                    AppConstant.PokeStatus = functionStatus.getStatus();
                    break;
                case "TodaySpeakRank":
                    AppConstant.TodaySpeakRank = functionStatus.getStatus();
                    break;
                default:
            }
        }
    }
}
