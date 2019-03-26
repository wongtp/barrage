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
 * 弹幕承载主窗口
 * @author 黄小天
 * @date 2019-03-16 23:14
 * @version 1.0
 */
public class MainWindow {
    
    private JFrame frame;
    /** 定义系统托盘图标弹出菜单 **/
    private final PopupMenu popupMenu = new PopupMenu();
    
    public static MainWindow init() {
        MainWindow window = new MainWindow();
        window.initWindow();
        return window;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    /**
     * @param launcher
     */
    public void addShutPopupMenu(PopupMenuCallBack callBack) {
        if(SystemTray.isSupported()) {
            final MenuItem shut = new MenuItem(callBack.getMenuName());
            shut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    callBack.actionPerformed(e);
                }
            });
            popupMenu.addSeparator();
            popupMenu.add(shut);
        }
    }
    
    private MainWindow() {}
    
    private JFrame initWindow() {
        frame = new JFrame();
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
            // 通过静态方法得到系统托盘
            final SystemTray tray = SystemTray.getSystemTray();
            ImageIcon img = new ImageIcon(getClass().getResource(Constant.ICON_PATH));
            // 创建 TrayIcon对象得到托盘图标
            TrayIcon trayIcon = new TrayIcon(img.getImage(), "弹幕", popupMenu);
            // 按下退出键
            final MenuItem exit = new MenuItem("exit");
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
