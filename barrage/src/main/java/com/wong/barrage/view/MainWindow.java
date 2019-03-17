/*
 * Copyleft
 */
package com.wong.barrage.view;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.wong.barrage.config.Config;
import com.wong.barrage.config.Constant;

/**
 * 
 * @author 黄小天
 * @date 2019-03-16 23:14
 * @version 1.0
 */
public class MainWindow {
    
    private JFrame frame = new JFrame();
    
    public JFrame init() {
        frame.setSize(Config.getScreenSize().width, Config.getScreenSize().height);
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
        return frame;
    }
    
    private void setTray() {
        // 判断当前系统是否支持系统托盘
        if(SystemTray.isSupported()) {
            // 定义系统托盘图标弹出菜单
            PopupMenu popupMenu = new PopupMenu();
            MenuItem exit = new MenuItem("exit");
            // 通过静态方法得到系统托盘
            SystemTray tray = SystemTray.getSystemTray();
            ImageIcon img = new ImageIcon(getClass().getResource(Constant.ICON_PATH));
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
