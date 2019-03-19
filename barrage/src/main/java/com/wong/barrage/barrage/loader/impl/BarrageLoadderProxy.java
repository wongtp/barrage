/*
 * Copyleft
 */
package com.wong.barrage.barrage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import com.wong.barrage.config.Constant;
import com.wong.barrage.util.LogUtil;
import com.wong.barrage.util.StringUtil;

/**
 * 弹幕加载工具类
 * @author 黄小天
 * @date 2019-03-09 10:31
 * @version 1.0
 */
class BarrageLoadder {
    
    /**
     * 按照给定的值获取定量的弹幕数量
     * @param index
     * @param size
     * @return
     */
    public static List<BarrageEntity> loadFromFile(int pageIndex, int pageSize) {
        List<BarrageEntity> barrageList = new ArrayList<>();
        Path path = Paths.get(Constant.BARRAGE_PATH);
        if (Files.notExists(path)) {
            JOptionPane.showMessageDialog(null, Constant.BARRAGE_PATH + " 弹幕文件不见了", "Σ(*ﾟдﾟﾉ)ﾉ", 
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        try (Stream<String> stream = Files.lines(path)) {
            stream.skip(pageIndex)
                // 遇到空行则跳过
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String line) {
                        return !StringUtil.isEmpty(line);
                    }
                })
                .limit(pageSize)
                .forEach(line -> {
                    barrageList.add(new BarrageEntity(line));
                });
        } catch (Exception e) {
            LogUtil.append(Constant.LOG_PATH, "BarrageLoadder thorw exception:" + e.getMessage());
        }
        return barrageList;
    }
}
