package com.bert.jpa;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yangbo
 * @date 2018/12/27
 */
@RestController
public class DbController {

    @Resource
    private ChannelStockDao channelStockDao;
    @Resource
    private ChannelPriceDao channelPriceDao;

    @Resource
    private JpaRepository<ChannelPriceEntity, Long> channelPriceModelDao;

    @GetMapping("/db1")
    public String db1() throws Exception {
        ChannelPriceEntity lvyue = ChannelPriceEntity.builder().accessPlatform(12)
                .channelCode("hhh")
                .createTime(new Date()).deleted(2).build();
        TimeUnit.SECONDS.sleep(1L);
        channelPriceDao.save(lvyue);
        return "";
    }

    @GetMapping("/db")
    public String db() {
        List<LocalDate> dateList = localDateList(LocalDate.of(2018, 11, 1),
                LocalDate.of(2019, 11, 2));
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("db-pool-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(5, 50, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        try {
            final CountDownLatch cd = new CountDownLatch(1000);
            for (int i = 1; i <= 1000; i++) {
                int finalI1 = i;
                executorService.execute(() -> {
                    try {
                        List<ChannelStockEntity> list = new ArrayList<>();
                        for (int j = 1; j <= 6; j++) {
                            int finalI = finalI1;
                            int finalJ = j;
                            dateList.forEach(localDate -> {
                                ChannelStockEntity lvyue = ChannelStockEntity.builder().accessPlatform(finalJ)
                                        .adjustNum(0).channelCode(finalJ + "")
                                        .closedFlag(1).createTime(new Date()).deleted(2).freeSales(1).keepRoomNum(1)
                                        .lastSyncResult("").lastSyncTime(new Date()).operator("").operatorName("")
                                        .physicsStockNum(2).pmsHotelCode(finalI + "").pmsRatePlanCode(UUID.randomUUID().toString())
                                        .pmsRoomTypeCode(UUID.randomUUID().toString()).pmsSysCode("lvyue").reviewFlag(1)
                                        .sellDate(localDate.toString()).stockNum(1).syncFlag(1).syncStatus(1)
                                        .uniqueId(UUID.randomUUID().toString()).updateTime(new Date()).build();
                                list.add(lvyue);
                            });
                        }
                        channelStockDao.saveAll(list);
                    } finally {
                        cd.countDown();
                    }
                });
            }
            cd.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return "";
    }

    /**
     * 生成连续的日期(顺序从前到后)
     */
    public static List<LocalDate> localDateList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();
        list.add(startDate);
        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusDays(1);
            list.add(startDate);
        }
        return list;
    }

    /**
     * 根据一段时间区间，按月份拆分成多个时间段
     */
    public static List<SegmentDate> getSegmentDate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        List<SegmentDate> list = Lists.newArrayList();
        while (start.isBefore(end) || start.isEqual(end)) {
            SegmentDate temp = SegmentDate.builder().startDate(start.toString())
                    .endDate(start.with(TemporalAdjusters.lastDayOfMonth()).toString()).build();
            if (start.with(TemporalAdjusters.firstDayOfMonth()).isEqual(end.with(TemporalAdjusters.firstDayOfMonth()))) {
                temp.setEndDate(end.toString());
            }
            start = start.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            list.add(temp);
        }
        return list;
    }

    @Data
    @Builder
    private static class SegmentDate {
        private String startDate;
        private String endDate;
    }

}
