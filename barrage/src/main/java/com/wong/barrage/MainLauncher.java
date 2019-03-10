/*
 * Copyleft
 */
package com.wong.barrage;

import java.util.List;

/**
 * 
 * @author 黄小天
 * @date 2019-03-09 13:48
 * @version 1.0
 */
public class MainLauncher {
    
    BarrageManager manager = new BarrageManager();
    
    public static void main(String[] args) {
        new MainLauncher().launch();
    }
    
    public void launch() {
        try {
            boolean repeat = ConfigUtil.isRepeat();
            if (repeat) {
                // 这个死循环负责重复执行弹幕
                while (true) {
                    shutAllBarrage();
                }
            } else {
                shutAllBarrage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 把文件中的所有弹幕都遍历一遍
     * @throws InterruptedException 
     */
    private void shutAllBarrage() throws Exception {
        int pageIndex = 0;
        // 这个死循环负责遍历弹幕文件的所有弹幕
        while (true) {
            List<Barrage> barrageList = BarrageLoadder.get(pageIndex, ConfigUtil.getBatchNumber());
            if (barrageList.size() == 0) {
                return;
            }
            pageIndex += ConfigUtil.getBatchNumber();
            shut(barrageList);
            Thread.sleep(ConfigUtil.getBatchSchedule());
        }
    }
    
    private void shut(List<Barrage> barrageList) throws Exception {
        for (Barrage barrage : barrageList) {
            manager.addBarrage(barrage);
        }
        while (!manager.isFinish()) {
            manager.move(ConfigUtil.isLeftDirect());
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*long current = System.currentTimeMillis();
        int barrier = 0;
        int finishCount = 0;
        boolean changeTimeFlag = false;
        int sleepTime = ConfigUtil.getElapse() * 500 / (int)ConfigUtil.getScreenSize().getWidth();
        // 这个死循环负责把 barrageList 这一批从屏幕一边送到另外一边
        while (true) {
            if (System.currentTimeMillis() - current > ConfigUtil.getTimeInterval() 
                    && barrageList.size() > barrier) {
                changeTimeFlag = true;
                barrier++;
            }
            for (int i = finishCount; i < barrier; i++) {
                if (barrageList.get(i).move()) {
                    finishCount++;
                }
                if (finishCount == barrageList.size()) {
                    return;
                }
            }
            Thread.sleep(sleepTime);
            // 这样的写的做法是，减少上面 for 循环以及睡眠带来是时间误差
            if (changeTimeFlag) {
                current = System.currentTimeMillis();
                changeTimeFlag = false;
            }
        }*/
    }
}
