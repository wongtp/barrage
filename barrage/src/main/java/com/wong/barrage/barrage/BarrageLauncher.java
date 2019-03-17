/*
 * Copyleft
 */
package com.wong.barrage.barrage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.wong.barrage.config.Configuration;
import com.wong.barrage.config.Constant;
import com.wong.barrage.util.LogUtil;

/**
 * 弹幕管理与移动控制类
 * @author 黄小天
 * @date 2019-03-09 15:48
 * @version 1.0
 */
public class BarrageLauncher {
    
    private JFrame frame;
    private List<BarrageEntity> barragelList = new CopyOnWriteArrayList<BarrageEntity>();
    // 负责定时分批加载弹幕
    private ScheduledExecutorService batchSchedule = Executors.newSingleThreadScheduledExecutor();
    
    public BarrageLauncher(JFrame frame) {
        this.frame = frame;
    }
    
    public void launch() {
        batchSchedule.scheduleAtFixedRate(new BatchLoader(), 0, Configuration.getBatchSchedule(), TimeUnit.SECONDS);
        try {
            while (true) {
                this.move();
                if (!Configuration.isRadomSpeed()) {
                    Thread.sleep(Configuration.getSpeed());
                } else {
                    Thread.sleep(20);
                }
            }
        } catch (Exception e) {
            LogUtil.append(Constant.LOG_PATH, "while launch:" + e.getMessage());
        }
    }

    /**
     * 添加弹幕
     * @param text
     */
    public void addBarrage(BarrageEntity barrage) {
        barragelList.add(barrage);
        frame.add(barrage);
    }
    
    /**
     * 执行弹幕移动
     */
    public void move() {
        for (BarrageEntity barrage : barragelList) {
            barrage.move();
            if (barrage.isFinish()) {
                frame.remove(barrage);
                barragelList.remove(barrage);
            }
        }
    }
    
    /**
     * 负责分批加载弹幕
     */
    private class BatchLoader implements Runnable {
        // 弹幕批次数据索引
        int pageIndex = Configuration.getPageIndex();
        BatchLoader() {
            if (Configuration.isRefershPageIndex()) {
                pageIndex = 0;
            } else {
                pageIndex = Configuration.getPageIndex();
            }
        }
        @Override
        public void run() {
            try {
                List<BarrageEntity> barrageList = BarrageLoadder.loadFromFile(pageIndex, Configuration.getBatchNumber());
                if (barrageList.size() > 0) {
                    pageIndex += Configuration.getBatchNumber();
                    for (BarrageEntity barrage : barrageList) {
                        addBarrage(barrage);
                        Thread.sleep(Configuration.getTimeInterval());
                    }
                    Configuration.writePageIndex(pageIndex);
                } else if (Configuration.isRepeat()) {
                    pageIndex = 0;
                } else {
                    Configuration.writePageIndex(0);
                    System.exit(0);
                }
            } catch (Exception e) {
                LogUtil.append(Constant.LOG_PATH, "while run BatchLoader:" + e.getMessage());
            }
        }
    }
}
