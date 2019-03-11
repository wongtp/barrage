/*
 * Copyleft
 */
package com.wong.barrage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 主启动类
 * @author 黄小天
 * @date 2019-03-09 13:48
 * @version 1.0
 */
public class MainLauncher {
    
    private BarrageManager manager = new BarrageManager();
    // 负责定时加载弹幕批次
    private ScheduledExecutorService batchSchedule = Executors.newSingleThreadScheduledExecutor();
    
    public static void main(String[] args) {
        new MainLauncher().launch();
    }
    
    public void launch() {
        batchSchedule.scheduleAtFixedRate(new BatchLoader(), 0, ConfigLoader.getBatchSchedule(), TimeUnit.SECONDS);
        try {
            while (true) {
                manager.move();
                if (!ConfigLoader.isRadomSpeed()) {
                    Thread.sleep(ConfigLoader.getElapse());
                } else {
                    Thread.sleep(20);
                }
            }
        } catch (Exception e) {
            LogUtil.append("log.log", "while launch:" + e.getMessage());
        }
    }
    
    private class BatchLoader implements Runnable {
        // 弹幕批次数据索引
        private int pageIndex = LogUtil.getPageIndex();
        BatchLoader() {
            if (ConfigLoader.isRefershPageIndex()) {
                pageIndex = 0;
            } else {
                pageIndex = LogUtil.getPageIndex();
            }
        }
        @Override
        public void run() {
            try {
                List<Barrage> barrageList = BarrageLoadder.get(pageIndex, ConfigLoader.getBatchNumber());
                if (barrageList.size() > 0) {
                    pageIndex += ConfigLoader.getBatchNumber();
                    for (Barrage barrage : barrageList) {
                        manager.addBarrage(barrage);
                        Thread.sleep(ConfigLoader.getTimeInterval());
                    }
                    LogUtil.writePageIndex(pageIndex);
                } else if (ConfigLoader.isRepeat()) {
                    pageIndex = 0;
                } else {
                    LogUtil.writePageIndex(0);
                    System.exit(0);
                }
            } catch (Exception e) {
                LogUtil.append("log.log", "while run BatchLoader:" + e.getMessage());
            }
        }
    }
}
