/*
 * Copyleft
 */
package com.wong.barrage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 
 * @author 黄小天
 * @date 2019-03-09 13:48
 * @version 1.0
 */
public class MainLauncher {
    
    ExecutorService barragePool = Executors.newCachedThreadPool(); 
    
    public static void main(String[] args) {
        JLabel label = new JLabel("hahahahah");
        label.setFont(ConfigUtil.getFont());
        label.setForeground(ConfigUtil.getColor());
        JOptionPane.showMessageDialog(label, null);
        // new MainLauncher().launch();
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
            if (ConfigUtil.isParallel()) {
                shutParallel(barrageList);
            } else {
                shut(barrageList);
            }
            Thread.sleep(ConfigUtil.getBatchSchedule());
        }
    }
    
    private void shut(List<Barrage> barrageList) throws Exception {
        long current = System.currentTimeMillis();
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
        }
    }
    
    private void shutParallel(List<Barrage> barrageList) throws Exception {
        int barrier = 0;
        List<Future<Boolean>> taskList = new ArrayList<>();
        while (true) {
            barragePool.submit(barrageList.get(barrier));
            Thread.sleep(ConfigUtil.getTimeInterval() );
            barrier++;
            if (barrier >= barrageList.size()) {
                break;
            }
        }
        for (Future<Boolean> future : taskList) {
            future.get();
        }
    }
}
