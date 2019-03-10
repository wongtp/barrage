/*
 * Copyleft
 */
package com.wong.barrage;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author 黄小天
 * @date 2019-03-10 18:02
 * @version 1.0
 */
public class Tester {
    
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        JFrame frame = new JFrame();
        frame.setSize(screenSize);
        frame.setResizable(false);
        // 设置窗口为无标题
        frame.setUndecorated(true); 
        frame.setBackground(new Color(0, 0, 0, 0));
        // frame.setType(JFrame.Type.UTILITY);
        frame.setAlwaysOnTop(true);
        
        JLabel label = new JLabel();
        label.setText("asdfadfdf");
        label.setFont(ConfigUtil.getFont());
        label.setForeground(ConfigUtil.getColor());
        label.setSize(200, 30);
        label.setLocation(0, 100);
        label.setVisible(true);
        frame.add(label);
        
        JLabel label2 = new JLabel();
        label2.setText("哈哈哈哈啊");
        label2.setFont(ConfigUtil.getFont());
        label2.setForeground(ConfigUtil.getColor());
        label2.setSize(200, 30);
        label2.setLocation(0, 800);
        label2.setVisible(true);
        
        frame.add(label2);
        
        List<JLabel> labelList = new ArrayList<>();
        labelList.add(label);
        labelList.add(label2);
        int i = 0;
        while (++i < 2000) {
            for (JLabel jLabel : labelList) {
                jLabel.setLocation(i, jLabel.getLocation().y);
            }
            frame.setVisible(true);
        }
        
    }

}
