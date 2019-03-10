/*
 * Copyleft
 */
package com.wong.barrage;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

/**
 * 弹幕实体类，实际上是一个 JFrame
 * @author 黄小天
 * @date 2019-03-07 22:11
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Barrage extends JLabel {
    
    private int screenWidth = (int)ConfigUtil.getScreenSize().getWidth();
    public int moveWidth;
    
    /**
     * @param paddingTop：距离屏幕顶端位置
     * @param direct 移动的方向，ture 向左，false 向右
     */
    public Barrage(String text, Color color, Font font, int paddingTop, boolean direct) {
        // 计算文本大约宽度
        int textWidth = text.length() * font.getSize() + 10;
        if (direct) {
            moveWidth = screenWidth;
        } else {
            moveWidth = -textWidth;
        }
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setBounds(moveWidth, paddingTop, textWidth, font.getSize());
    }
}
