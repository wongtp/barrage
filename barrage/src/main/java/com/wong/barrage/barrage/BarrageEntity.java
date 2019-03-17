/*
 * Copyleft
 */
package com.wong.barrage.barrage;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.wong.barrage.config.Configuration;
import com.wong.barrage.config.Constant;

import sun.font.FontDesignMetrics;

/**
 * 弹幕实体类，实际上是一个 JLabel
 * @author 黄小天
 * @date 2019-03-07 22:11
 * @version 1.0
 */
@SuppressWarnings("serial")
class BarrageEntity extends JLabel {
    
    private int screenWidth = (int)Configuration.getScreenSize().getWidth();
    /** 弹幕需要移动的距离 **/
    private int moveWidth;
    /** 弹幕是否完成了从屏幕一边到另外一边 **/
    private boolean finish = false;
    /** 弹幕移动方向，true：向右，false：向左 **/
    private boolean rightDirect;
    /** 默认速度，只有在配置文件中开启了随机速度这个值又有效 **/
    private int speed = 1;
    private boolean stop = false;
    private FontDesignMetrics metrics = FontDesignMetrics.getMetrics(Configuration.getFont());
    
    public void init(String text) {
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        BufferedImage image = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        //设置背影为透明
        graphics.setColor(Configuration.getTransparentColor());
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setFont(Configuration.getFont());
        graphics.setColor(Color.WHITE);
        graphics.drawString(text, 0, metrics.getAscent());//图片上写文字
        graphics.dispose();
    }
    
    /**
     * @param paddingTop：距离屏幕顶端位置
     * @param rightDirect 移动的方向，ture 向右，false 向左
     */
    public BarrageEntity(String text) {
        super();
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        BufferedImage image = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //设置背影为透明
        // g.setColor(Configuration.getTransparentColor());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.2f));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setFont(Configuration.getFont());
        g.setColor(Configuration.getColor());
        // 图片上写文字
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.drawString(text, 0, metrics.getAscent());
        g.dispose();
        setIcon(new ImageIcon(image));
        try {
            ImageIO.write(image, "png", new File("e:/a.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBackground(new Color(0, 0, 0, 0));
        this.rightDirect = Configuration.isRightDirect();
        if (rightDirect) {
            moveWidth = -textWidth;
        } else {
            moveWidth = screenWidth;
        }
        this.setBounds(moveWidth, Configuration.getLocationTop(), textWidth, textHeight);
        if (Configuration.isRadomSpeed()) {
            speed = Configuration.getRandom().nextInt(textWidth/(3 * textHeight));
            // 如果产生的随机数是 0 的话，弹幕会静止不动
            if (speed <= 0) {
                speed = 1;
            }
        }
        
        // 添加鼠标事件
        setMouseListener();
    }
    
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
    
    public void setMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stop = !stop;
                System.exit(0);
            }
            
        });
        
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                stop = true;
                // 设置拖拽后，窗口的位置
                moveWidth = e.getXOnScreen();
                setLocation(e.getXOnScreen(), e.getYOnScreen());
            }
        });
    }
}
