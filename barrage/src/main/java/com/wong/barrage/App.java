/*
 * Copyleft
 */
package com.wong.barrage;

import javax.swing.JFrame;

import com.wong.barrage.barrage.BarrageLauncher;
import com.wong.barrage.view.MainWindow;

/**
 * 主启动类
 * @author 黄小天
 * @date 2019-03-09 13:48
 * @version 1.0
 */
public class App {
    
    public static void main(String[] args) {
        JFrame frame = new MainWindow().init();
        new BarrageLauncher(frame).launch();
    }
}
