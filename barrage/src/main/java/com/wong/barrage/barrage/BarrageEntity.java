/*
 * Copyleft
 */
package com.wong.barrage.barrage;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.wong.barrage.config.Config;
import com.wong.barrage.util.ImgUtil;

import sun.font.FontDesignMetrics;

/**
 * 弹幕实体类，实际上是一个 JLabel
 * @author 黄小天
 * @date 2019-03-07 22:11
 * @version 1.0
 */
@SuppressWarnings({"serial", "restriction"})
public class BarrageEntity extends JLabel {
    
    private int screenWidth = (int)Config.getScreenSize().getWidth();
    /** 弹幕需要移动的距离 **/
    private int moveWidth;
    /** 弹幕是否完成了从屏幕一边到另外一边 **/
    private boolean finish = false;
    /** 弹幕移动方向，true：向右，false：向左 **/
    private boolean rightDirect;
    /** 默认速度，只有在配置文件中开启了随机速度这个值又有效 **/
    private int speed = 1;
    /** 当前弹幕是否处于停止状态 **/
    private boolean stop = false;
    /** 鼠标点击弹幕时在弹幕上的相对 x 坐标 **/
    private int mouseX;
    /** 鼠标点击弹幕时在弹幕上的相对 y 坐标 **/
    private int mouseY;
    
    private BarrageEntity() {}
    /**
     * @param paddingTop：距离屏幕顶端位置
     * @param rightDirect 移动的方向，ture 向右，false 向左
     */
    public BarrageEntity(String text) {
        this();
        Font font = Config.getFont();
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        // 创建图片带代替直接用文字的方式，这样有更好的使用体验
        ImageIcon icon = ImgUtil.createImage(text, metrics, Config.getTransparentColor(), Config.getColor());
        setIcon(icon);
        
        // 配置弹幕位置和需要移动的距离
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        this.rightDirect = Config.isRightDirect();
        if (rightDirect) {
            moveWidth = -textWidth;
        } else {
            moveWidth = screenWidth;
        }
        this.setBounds(moveWidth, Config.getLocationTop(), textWidth, textHeight);
        if (Config.isRadomSpeed()) {
            speed = Config.getRandom().nextInt(textWidth/(3 * textHeight));
            // 如果产生的随机数是 0 的话，弹幕会静止不动
            if (speed <= 0) {
                speed = 1;
            }
        }
        // 添加鼠标事件
        setMouseListener();
    }
    
    /**
     * 让弹幕动起来
     */
    public void move() {
        if (stop) {
            return;
        }
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
    
    /**
     * 设置鼠标监听事件，主要是控制弹幕拖动和是否停止滚动
     */
    private void setMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Config.getStopType() == 1 || Config.getMotivateType() == 1) {
                    stop = !stop;
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (Config.getStopType() == 2) {
                    stop = true;
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (Config.getMotivateType() == 2) {
                    stop = false;
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                if (Config.getStopType() == 2) {
                    stop = true;
                }
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                stop = true;
                // 设置拖拽后，窗口的位置
                moveWidth = e.getXOnScreen() - mouseX;
                setLocation(moveWidth, e.getYOnScreen() - mouseY);
            }
        });
    }
}
