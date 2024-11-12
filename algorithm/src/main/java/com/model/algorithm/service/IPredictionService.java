package com.model.algorithm.service;

import java.util.List;

/**
 * 前区：前五个号码（01-35）
 * 后区：后两个号码（01-12）
 * 规则：
 * 1、剔除历史号码：剔除历史5期号码前区，剩余30个号码作为备选号码
 * 2、圈定重点号码：确定哪些范围内的码中奖几率比较高，选择两个号码跟踪上期号码采用临码加1减1（如上期号码是15，它的临码则是14和16，则选取13和17作为本期号码）
 * 3、跟踪上期号码：选取1-2个旺码；
 * 4、大码选择：28-35中必选一个；
 */
public interface IPredictionService {

    /**
     * 剔除五期前区号码
     *
     * @return
     */
    List<String> dropFiveGroupHistoryNumbers();

    /**
     * 出现次数最多的20个数字
     *
     * @return
     */
    List<String> maxBeforeTimesNumbers();

    List<String> twoWancode(List<String> maxNumbers);

}