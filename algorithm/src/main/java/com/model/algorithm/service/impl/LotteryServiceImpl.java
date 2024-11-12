package com.model.algorithm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.model.algorithm.mapper.LotteryMapper;
import com.model.algorithm.model.DO.Lottery;
import com.model.algorithm.service.ILotteryService;
import com.model.algorithm.service.IPredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LotteryServiceImpl extends ServiceImpl<LotteryMapper, Lottery> implements ILotteryService {

    Logger logger = LoggerFactory.getLogger(LotteryServiceImpl.class);

    @Autowired
    private IPredictionService predictionService;


    @Override
    public void cleanData(String beginTime, String endTime) {
        List<Lottery> lotteryList = list();
        lotteryList = checkData(lotteryList);
        if (CollectionUtils.isEmpty(lotteryList)) {
            logger.info("没有查到需要检查的数据！！！");
        }
        //日期格式转换
        transferData(lotteryList);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        QueryWrapper<Lottery> queryWrapper = new QueryWrapper<>();
//        try {
//            if (Strings.isNotEmpty(beginTime) && Strings.isNotBlank(beginTime)) {
//                Date beginDate = sdf.parse(beginTime);
//                queryWrapper.ge("drawing_date", beginDate);
//            }
//            if (Strings.isNotEmpty(endTime) && Strings.isNotBlank(endTime)) {
//                Date endDate = sdf.parse(endTime);
//                queryWrapper.le("drawing_date", endDate);
//            }
//            lotteryList = list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("日期转换错误！！！");
//        }
    }

    @Override
    public List<String> prediction() {
        //前区号码
        List<String> preDropNumbers = predictionService.dropFiveGroupHistoryNumbers();
        List<String> maxTimesPreNumbers = predictionService.maxBeforeTimesNumbers();
        //取交集
        List<String> preNumbers = preDropNumbers.stream().filter(maxTimesPreNumbers::contains).collect(Collectors.toList());

        //TODO
        return Collections.emptyList();
    }

    public void model(){

    }

    //日期格式转换
    private void transferData(List<Lottery> list) {
        int count = 0;
        for (Lottery lottery : list) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                String drawingDateStr = lottery.getDrawingDateStr();
                String datestr = formatDate(drawingDateStr);
                lottery.setDrawingDate(sdf.parse(datestr));
                updateById(lottery);
                count++;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("更新日期数据{}条", count);
    }

    public String formatDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (isDate(dateStr)) {
            Date date = sdf1.parse(dateStr);
            return sdf.format(date);
        } else {
            return dateStr;
        }
    }

    public List<Lottery> checkData(List<Lottery> lotteryList) {
        List<Lottery> list = new ArrayList<>();
        lotteryList.forEach(it -> {
            if (isChinese(it.getDrawingDateStr())
                    || isChinese(it.getIssue())
                    || isChinese(it.getOne())
                    || isChinese(it.getTwo())
                    || isChinese(it.getThree())
                    || isChinese(it.getFour())
                    || isChinese(it.getFive())
                    || isChinese(it.getSix())
                    || isChinese(it.getSeven())) {
                list.add(it);
            }
            if (!isDate(it.getDrawingDateStr())) {
                list.add(it);
            }
        });
        List<Integer> removeIds = list.stream().map(Lottery::getId).collect(Collectors.toList());
        removeByIds(removeIds);
        lotteryList.removeAll(list);
        logger.info("删除数据{}条", list.size());
        return lotteryList;
    }

    /**
     * 是否中文
     *
     * @param input
     * @return
     */
    public static boolean isChinese(String input) {
        return input.matches("[\u4E00-\u9FA5]+");
    }

    /**
     * 是否日期格式
     *
     * @param input
     * @return
     */
    public static boolean isDate(String input) {
        return input.matches("\\d{4}-\\d{1,2}-\\d{1,2}");
    }


}
