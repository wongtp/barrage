/*
 * Copyleft
 */
package com.wong.barrage.barrage;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.wong.barrage.barrage.loader.BarrageLoadder;
import com.wong.barrage.barrage.loader.impl.BarrageLoadderProxy;
import com.wong.barrage.config.Config;
import com.wong.barrage.config.Constant;
import com.wong.barrage.util.LogUtil;
import com.wong.barrage.view.PopupMenuCallBack;

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
    private ScheduledExecutorService scheduledThreadPool;
    private ExecutorService cacheThreadPool;
    private BarrageLoadder barrageLoadder = new BarrageLoadderProxy();
    private BatchLoader batchLoader = new BatchLoader();
    // 弹幕批次数据索引
    int pageIndex = Config.getPageIndex();
    private BarrageLauncher() {}
    
    /**
     * 开始你的表演吧！
     */
    public static BarrageLauncher launchOnFrame(JFrame frame) {
        return new BarrageLauncher().init(frame);
    }
    
    /**
     * 添加弹幕
     * @param text
     */
    public void addBarrage(BarrageEntity barrage) {
        barragelList.add(barrage);
        frame.add(barrage);
    }
    
    public ShutNextBatchPopMenu getPopMenu() {
        return new ShutNextBatchPopMenu();
    }
    
    private BarrageLauncher init(JFrame frame) {
        this.frame = frame;
        if (Config.isRefershPageIndex()) {
            this.pageIndex = 0;
        } else {
            this.pageIndex = Config.getPageIndex();
        }
        // 使用线程工厂来创建线程
        ThreadFactory factory = new ThreadFactory() {
            final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
            @Override
            public Thread newThread(final Runnable r) {
                Thread thread = defaultFactory.newThread(r);
                thread.setName("barrage worker-" + thread.getName());
                thread.setDaemon(true);
                return thread;
            }
        };
        cacheThreadPool = new ThreadPoolExecutor(2, 10, 0L, TimeUnit.MILLISECONDS, 
                new LinkedBlockingQueue<Runnable>(), factory);
        scheduledThreadPool = new ScheduledThreadPoolExecutor(1, factory);
        scheduledThreadPool.scheduleAtFixedRate(batchLoader, 0, Config.getBatchSchedule(), TimeUnit.SECONDS);
        cacheThreadPool.submit(new Launcher());
        return this;
    }
    
    /**
     * 负责分批加载弹幕
     */
    private class BatchLoader implements Runnable {
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
    
    /**
     * 负责不断移动 barragelList 中的弹幕
     */
    private class Launcher implements Runnable {
        @Override
        public void run() {
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
         * 执行弹幕移动
         */
        private void move() {
            for (BarrageEntity barrage : barragelList) {
                barrage.move();
                if (barrage.isFinish()) {
                    frame.remove(barrage);
                    barragelList.remove(barrage);
                }
            }
        }
    }
    
    /**
     * 用于托盘菜单点击发射下一批弹幕
     */
    class ShutNextBatchPopMenu implements PopupMenuCallBack {
        @Override
        public void actionPerformed(ActionEvent e) {
            cacheThreadPool.submit(batchLoader);
        }
        
        @Override
        public String getMenuName() {
            return "shut";
        }
    }
}
