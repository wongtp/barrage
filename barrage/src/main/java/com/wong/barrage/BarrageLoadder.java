/*
 * Copyleft
 */
package com.wong.barrage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    private static int index = 0;
    public static List<Barrage> get(int pageIndex, int pageSize) {
        index = 0;
        List<Barrage> barrageList = new ArrayList<>();
        try {
            Files.lines(Paths.get("barrage.txt")).forEach(line -> {
                if (!StringUtil.isEmpty(line) && index >= pageIndex && index < pageIndex + pageSize) {
                    line = line.replaceAll("</br>", "\n");
                    barrageList.add(new Barrage(line, ConfigUtil.getColor(), ConfigUtil.getFont(), 
                            ConfigUtil.getLocationTop()));
                    index++;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return barrageList;
    }
}
