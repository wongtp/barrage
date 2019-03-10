/*
 * Copyleft
 */
package com.wong.barrage;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 弹幕实体类，实际上是一个 JFrame
 * @author 黄小天
 * @date 2019-03-07 22:11
 * @version 1.0
 */
public class Barrage implements Runnable {
    
    private JFrame jFrame = new JFrame();
    private int locationTop;
    private int textWidth;
    private int paddingLeft;
    private int paddingLeftBak;
    
    public void add() {
        
    }
    
    public Barrage(String text, Color fontColor, Font font, int locationTop) {
        this.textWidth = text.length() * font.getSize() + 10;
        this.locationTop = locationTop;
        this.paddingLeft = (int) ConfigUtil.getScreenSize().getWidth();
        this.paddingLeftBak = this.paddingLeft;
        
        jFrame.setSize(this.textWidth, font.getSize() * 5 + 5);
        jFrame.setResizable(false);
        // 先隐藏起来
        jFrame.setLocation(-3000, 0);
        // 设置窗口为无标题
        jFrame.setUndecorated(true); 
        
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fontColor);
        
        jFrame.setContentPane(label);
        jFrame.setBackground(new Color(0, 0, 0, 0));
        jFrame.setType(JFrame.Type.UTILITY);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);
        
        JOptionPane.showMessageDialog(label, null);
    }
    
    /**
     * 如果弹幕完成了从屏幕一边移动到另外一边，则返回 true
     * @return
     */
    public boolean move() {
        int x = --this.paddingLeft;
        // 如果是向右移动，则 X 坐标是不断增加的
        if (!ConfigUtil.isLeftDirect()) {
            x = this.paddingLeftBak - x;
        }
        jFrame.setLocation(x, this.locationTop);
        if (this.paddingLeft + textWidth == 0) {
            this.paddingLeft = this.paddingLeftBak;
            return true;
        }
        return false;
    }
    
    @Override
    public void run() {
        int sleepTime = ConfigUtil.getElapse() * 500 / (int)ConfigUtil.getScreenSize().getWidth();
        try {
            while (!move()) {
                Thread.sleep(sleepTime);
                System.out.println("当前线程=== " + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
