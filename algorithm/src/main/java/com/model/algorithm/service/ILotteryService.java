package com.model.algorithm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.model.algorithm.model.DO.Lottery;

import java.util.List;

public interface ILotteryService extends IService<Lottery> {

    void cleanData(String beginTime, String endTime);

    List<String> prediction();

}
