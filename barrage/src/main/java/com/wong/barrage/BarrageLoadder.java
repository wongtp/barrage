/*
 * Copyleft
 */
package com.wong.barrage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        try (Stream<String> stream = Files.lines(Paths.get("barrage.txt"))) {
            stream.skip(pageIndex)
                .limit(pageSize)
                .forEach(line -> {
                    if (!StringUtil.isEmpty(line)) {
                        barrageList.add(new Barrage(line, ConfigUtil.getColor(), ConfigUtil.getFont(), 
                                ConfigUtil.getLocationTop(), ConfigUtil.isLeftDirect()));
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return barrageList;
    }
}
