/*
 * Copyleft
 */
package com.wong.barrage;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
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
        
        setTray();
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
    
    private void setTray() {
        // 判断当前系统是否支持系统托盘
        if(SystemTray.isSupported()) {
            // 定义系统托盘图标弹出菜单
            PopupMenu popupMenu = new PopupMenu();
            MenuItem exit = new MenuItem("exit");
            // 通过静态方法得到系统托盘
            SystemTray tray = SystemTray.getSystemTray();
            ImageIcon img = new ImageIcon(getClass().getResource("/com/wong/barrage/res/icon.png"));
            // 创建 TrayIcon对象得到托盘图标
            TrayIcon trayIcon = new TrayIcon(img.getImage(), "弹幕", popupMenu);
            // 按下退出键
            exit.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });
            popupMenu.add(exit);
            // 设置托盘图标自动设置尺寸
            trayIcon.setImageAutoSize(true);
            try {
                // 将托盘图标设置到系统托盘中
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
    
}
