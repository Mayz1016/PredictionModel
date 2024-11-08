package com.model.algorithm.controller;

import com.model.algorithm.service.ILotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/prediction/model")
public class PredictionModelController {

    @Autowired
    private ILotteryService lotteryService;

    @GetMapping("/cleanData")
    public void cleanData(@RequestParam(value = "beginTime", required = false) String beginTime,
                          @RequestParam(value = "endTime", required = false) String endTime) {
        lotteryService.cleanData(beginTime, endTime);
    }

}