package com.model.algorithm.service.impl;

import com.model.algorithm.model.DO.Lottery;
import com.model.algorithm.service.ILotteryService;
import com.model.algorithm.service.IPredictionService;
import org.apache.spark.util.CollectionsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PredictionServiceImpl implements IPredictionService {

    Logger logger = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    private ILotteryService lotteryService;

    List<String> numbersOfBerfore = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35");
    List<String> numbersOfBehind = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");

    @Override
    public List<String> dropFiveGroupHistoryNumbers() {
        List<Lottery> fiveLotteryList = lotteryService.lambdaQuery().orderByDesc(Lottery::getDrawingDate).list().stream().limit(5).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fiveLotteryList)) {
            return Collections.emptyList();
        }
        fiveLotteryList.stream().forEach(it -> {
            numbersOfBerfore.remove(it.getOne());
            numbersOfBerfore.remove(it.getTwo());
            numbersOfBerfore.remove(it.getThree());
            numbersOfBerfore.remove(it.getFour());
            numbersOfBerfore.remove(it.getFive());
            numbersOfBerfore.remove(it.getSix());
            numbersOfBerfore.remove(it.getSeven());
        });
        return numbersOfBerfore;
    }

    @Override
    public List<String> maxBeforeTimesNumbers() {
        //统计每个数字出现的次数
        List<Lottery> lotteryList = lotteryService.list();
        //统计次数
        Map<String, Integer> timesMap = initMap();
        for (Lottery lottery : lotteryList) {
            timesMap.put(lottery.getOne(), timesMap.get(lottery.getOne()) + 1);
            timesMap.put(lottery.getTwo(), timesMap.get(lottery.getTwo()) + 1);
            timesMap.put(lottery.getThree(), timesMap.get(lottery.getThree()) + 1);
            timesMap.put(lottery.getFour(), timesMap.get(lottery.getFour()) + 1);
            timesMap.put(lottery.getFive(), timesMap.get(lottery.getFive()) + 1);
        }
        Map<String, Integer> sortMap = sortByValue(timesMap);
        List<String> result = sortMap.entrySet().stream()
                .map(it -> it.getKey())
                .limit(20)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<String> twoWancode(List<String> maxNumbers) {
        //从频率最高号码和上期号码中组合选出两个号码，作为旺码
        return Collections.emptyList();
    }

    public Map<String, Integer> initMap() {
        //统计次数
        Map<String, Integer> times = new HashMap<>();
        for (int i = 1; i <= 35; i++) {
            times.put(String.valueOf(i), 0);
        }
        return times;
    }

    public Map<String, Integer> sortByValue(Map<String, Integer> map) {
        Map<String, Integer> sortedMap = map.entrySet()
                .stream()
                .sorted((Comparator<? super Map.Entry<String, Integer>>) Map.Entry.comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        () -> new LinkedHashMap<>()
                ));
        return sortedMap;
    }

}
