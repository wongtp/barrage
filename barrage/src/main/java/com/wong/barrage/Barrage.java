/*
 * Copyleft
 */
package com.wong.barrage;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * 弹幕实体类，实际上是一个 JLabel
 * @author 黄小天
 * @date 2019-03-07 22:11
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Barrage extends JLabel {
    
    private int screenWidth = (int)ConfigLoader.getScreenSize().getWidth();
    private int moveWidth;
    private boolean finish = false;
    private boolean rightDirect;
    private int speed = 1;
    
    /**
     * @param paddingTop：距离屏幕顶端位置
     * @param rightDirect 移动的方向，ture 向右，false 向左
     */
    public Barrage(String text) {
        super(text);
        Font font = ConfigLoader.getFont();
        // 计算文本大约宽度
        int textWidth = text.length() * font.getSize() + 10;
        this.setFont(font);
        this.setForeground(ConfigLoader.getColor());
        this.rightDirect = ConfigLoader.isRightDirect();
        if (rightDirect) {
            moveWidth = -textWidth;
        } else {
            moveWidth = screenWidth;
        }
        this.setBounds(moveWidth, ConfigLoader.getLocationTop(), textWidth, font.getSize());
        if (ConfigLoader.isRadomSpeed()) {
            speed = ConfigLoader.getRandom().nextInt(textWidth/(3 * font.getSize()));
            // 如果产生的随机数是 0 的话，弹幕会静止不动
            speed = speed == 0 ? 1 : speed; 
        }
    }
    
    public void move() {
        if (rightDirect) {
            this.setLocation(moveWidth += speed, this.getY());
            if (this.getX() > screenWidth) {
                finish = true;
            }
        } else {
            this.setLocation(moveWidth -= speed, this.getY());
            if (this.getX() + this.getWidth() < 0) {
                finish = true;
            }
        }
    }
    
    /**
     * 判断当前弹幕是否完成从屏幕一边到另外一边的移动
     * @return
     */
    public boolean isFinish() {
        return finish;
    }
}
