/*
 * Copyleft
 */
package com.wong.barrage;

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
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        }));
        MainWindow window = MainWindow.init();
        BarrageLauncher launcher = BarrageLauncher.launchOnFrame(window.getFrame());
        window.addShutPopupMenu(launcher.getPopMenu());
    }
}
