/*
 * Copyleft
 */
package com.wong.barrage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

/**
 * 弹幕管理与移动控制类
 * @author 黄小天
 * @date 2019-03-09 15:48
 * @version 1.0
 */
public class BarrageManager {
    
    private JFrame frame = new JFrame();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private List<Barrage> labelList = new CopyOnWriteArrayList<Barrage>();
    
    public BarrageManager() {
        frame.setSize(screenSize.width, screenSize.height);
        // 隐藏窗口标题
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // 隐藏任务栏窗口
        frame.setType(JFrame.Type.UTILITY);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }

    /**
     * 添加弹幕
     * @param text
     */
    public void addBarrage(Barrage barrage) {
        labelList.add(barrage);
        frame.add(barrage);
    }
    
    /**
     * 执行弹幕移动
     */
    public void move() {
        for (Barrage barrage : labelList) {
            barrage.move();
            if (barrage.isFinish()) {
                frame.remove(barrage);
                labelList.remove(barrage);
            }
        }
    }
    
    /**
     * 判断 labelList 中的弹幕是否所有都发送完毕
     * @return 
     */
    public boolean isFinish() {
        return labelList.size() == 0;
    }
}
