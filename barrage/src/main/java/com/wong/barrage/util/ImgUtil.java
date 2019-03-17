/*
 * Copyleft
 */
package com.wong.barrage.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;

import sun.font.FontDesignMetrics;

/**
 * 文字转图片工具类
 * @author 黄小天
 * @date 2019-03-17 11:08
 * @version 1.0
 */
public class ImgUtil {
    
    /**
     * 根据文字内容创建 ImageIcon
     * @param text 文字内容
     * @param font 字体样式
     * @param bgColor 图片背景色
     * @param fontColor 文字颜色
     * @return
     */
    public static ImageIcon createImage(String text, FontDesignMetrics metrics, Color bgColor, Color fontColor) {
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        // TYPE_4BYTE_ABGR：透明
        BufferedImage image = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        // 设置背景色
        graphics.setColor(bgColor);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setFont(metrics.getFont());
        graphics.setColor(fontColor);
        // 写字到画布上
        graphics.drawString(text, 0, metrics.getAscent());
        graphics.dispose();
        // 输出透明 png 图片
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageOutputStream ios = new MemoryCacheImageOutputStream(bos);
        try {
            ImageIO.write(image, "png", ios);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon(bos.toByteArray());
    }
}
