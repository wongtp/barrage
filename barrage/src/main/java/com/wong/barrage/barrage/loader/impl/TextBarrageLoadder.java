/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.wong.barrage.barrage.BarrageEntity;
import com.wong.barrage.util.StringUtil;

/**
 * 按文本行来读取
 * @author 黄小天
 * @date 2019-03-19 20:22
 * @version 1.0
 */
class TextBarrageLoadder extends AbstractBarrageLoader {
    
    @Override
    public void load(int pageIndex, int pageSize, List<BarrageEntity> barrageList) {
        try (Stream<String> stream = Files.lines(getPath())) {
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSize() {
        Stream<String> stream = null;
        try {
            stream = Files.lines(getPath());
            return (int)stream.count();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
        return 0;
    }
}
