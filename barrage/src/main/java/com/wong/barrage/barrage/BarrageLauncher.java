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

import com.wong.barrage.barrage.loader.BarrageLoadder;
import com.wong.barrage.barrage.loader.impl.BarrageLoadderProxy;
import com.wong.barrage.config.Config;
import com.wong.barrage.config.Constant;
import com.wong.barrage.util.LogUtil;

/**
 * 弹幕管理与移动控制类
 * @author 黄小天
 * @date 2019-03-09 15:48
 * @version 1.0
 */
public class BarrageLauncher {
    
    // 用来放置弹幕的窗体
    private JFrame frame;
    // 用于存放需要发射的弹幕
    private List<BarrageEntity> barragelList = new CopyOnWriteArrayList<BarrageEntity>();
    // 负责定时分批加载弹幕
    private ScheduledExecutorService batchSchedule = Executors.newSingleThreadScheduledExecutor();
    private BarrageLoadder barrageLoadder = new BarrageLoadderProxy();
    
    private BarrageLauncher() {}
    
    public BarrageLauncher(JFrame frame) {
        this();
        this.frame = frame;
    }
    
    /**
     * 开始你的表演吧！
     */
    public void launch() {
        batchSchedule.scheduleAtFixedRate(new BatchLoader(), 0, Config.getBatchSchedule(), TimeUnit.SECONDS);
        try {
            while (true) {
                this.move();
                if (!Config.isRadomSpeed()) {
                    Thread.sleep(Config.getSpeed());
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
        int pageIndex = Config.getPageIndex();
        BatchLoader() {
            if (Config.isRefershPageIndex()) {
                pageIndex = 0;
            } else {
                pageIndex = Config.getPageIndex();
            }
        }
        @Override
        public void run() {
            try {
                List<BarrageEntity> barrageList = barrageLoadder.load(pageIndex, Config.getBatchNumber());
                if (barrageList.size() > 0) {
                    pageIndex += Config.getBatchNumber();
                    for (BarrageEntity barrage : barrageList) {
                        addBarrage(barrage);
                        Thread.sleep(Config.getTimeInterval());
                    }
                    Config.writePageIndex(pageIndex);
                } else if (Config.isRepeat()) {
                    pageIndex = 0;
                    Config.writePageIndex(pageIndex);
                } else {
                    Config.writePageIndex(0);
                    System.exit(0);
                }
            } catch (Exception e) {
                LogUtil.append(Constant.LOG_PATH, "while run BatchLoader:" + e.getMessage());
            }
        }
    }
}
