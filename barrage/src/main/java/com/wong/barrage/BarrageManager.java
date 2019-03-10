package com.wong.barrage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class BarrageManager extends JFrame {
    
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHeight = screenSize.width;
    private List<Barrage> labelList = new ArrayList<Barrage>();
    
    public BarrageManager() {
        this.setSize(screenSize.width, screenHeight);
        // 隐藏窗口标题
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationByPlatform(true);
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // 隐藏任务栏窗口
        this.setType(JFrame.Type.UTILITY);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
    }

    /**
     * 添加弹幕
     * @param text
     */
    public void addBarrage(Barrage barrage) {
        labelList.add(barrage);
        this.add(barrage);
    }
    
    /**
     * 执行弹幕移动
     */
    public void move(boolean direct) {
        Iterator<Barrage> it = labelList.iterator();
        while (it.hasNext()) {
            Barrage barrage = it.next();
            if (direct) {
                barrage.setLocation(--barrage.moveWidth, barrage.getY());
                if (barrage.getX() + barrage.getWidth() < 0) {
                    this.remove(barrage);
                    it.remove();
                }
            } else {
                barrage.setLocation(++barrage.moveWidth, barrage.getY());
                if (barrage.getX() > screenWidth) {
                    this.remove(barrage);
                    it.remove();
                }
            }
        }
    }
    
    public boolean isFinish() {
        return labelList.size() == 0;
    }
}
