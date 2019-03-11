/*
 * Copyleft
 */
package com.wong.barrage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

/**
 * 弹幕加载工具类
 * @author 黄小天
 * @date 2019-03-09 10:31
 * @version 1.0
 */
public class BarrageLoadder {
    
    /**
     * 按照给定的值获取定量的弹幕数量
     * @param index
     * @param size
     * @return
     */
    public static List<Barrage> get(int pageIndex, int pageSize) {
        List<Barrage> barrageList = new ArrayList<>();
        Path path = Paths.get("barrage.txt");
        if (Files.notExists(path)) {
            JOptionPane.showMessageDialog(null, "barrage.txt 弹幕文件不见了", "Σ(*ﾟдﾟﾉ)ﾉ", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        try (Stream<String> stream = Files.lines(path)) {
            stream.skip(pageIndex)
                .limit(pageSize)
                .forEach(line -> {
                    if (!StringUtil.isEmpty(line)) {
                        barrageList.add(new Barrage(line));
                    }
                });
        } catch (Exception e) {
            LogUtil.append("log.log", "BarrageLoadder thorw exception:" + e.getMessage());
        }
        return barrageList;
    }
}
