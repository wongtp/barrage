/*
 * Copyleft
 */
package com.wong.barrage.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.wong.barrage.config.Configuration;

import sun.font.FontDesignMetrics;

/**
 * 
 * @author 黄小天
 * @date 2019-03-17 11:08
 * @version 1.0
 */
public class ImgUtil {
    
    public static void main(String[] args) throws Exception {
        createImage("请A1003到3号窗口", new Font("宋体", Font.BOLD, 30), new File("e:/a.png"));
    }

    /**
     * 根据str,font的样式以及输出文件目录
     * @param str   字符串
     * @param font  字体
     * @param outFile   输出文件位置
     * @param width 宽度
     * @param height    高度
     * @return 
     * @throws Exception
     */
    public static ImageIcon createImage(String str, Font font, File outFile) throws Exception {
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = metrics.stringWidth(str);
        int height = metrics.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        //设置背影为透明
        graphics.setColor(Configuration.getTransparentColor());
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setFont(font);
        graphics.setColor(Configuration.getColor());
        graphics.drawString(str, 0, metrics.getAscent());//图片上写文字
        graphics.dispose();
        // 输出png图片
        ImageIO.write(image, "png", outFile);
        return new ImageIcon(image);
    }
}
