/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.nio.file.Files;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.wong.barrage.barrage.BarrageEntity;
import com.wong.barrage.util.CharsetUtil;
import com.wong.barrage.util.StringUtil;

/**
 * 按文本行来读取
 * @author 黄小天
 * @date 2019-03-19 20:22
 * @version 1.0
 */
class TextBarrageLoadder extends AbstractBarrageLoader {
    
    /** 1:普通逐行文本弹幕， 2:金山词霸文本弹幕 默认1**/
    private int type = 1;
    
    @Override
    public void load(int pageIndex, int pageSize, Collection<BarrageEntity> barrageList) {
        switch (type) {
            case 1:
                parseNormalText(pageIndex, pageSize, barrageList);
                break;
            case 2:
                parsePowerWord(pageIndex, pageSize, barrageList);
                break;
            default:
                parseNormalText(pageIndex, pageSize, barrageList);
                break;
        }
    }
    
    /**
     * 解析金山词霸数据
     * @param pageIndex
     * @param pageSize
     * @param barrageList
     */
    private void parsePowerWord(int pageIndex, int pageSize, Collection<BarrageEntity> barrageList) {
        try (Stream<String> stream = Files.lines(getPath(), CharsetUtil.resolveCharset(getPath().toString()))) {
            stream.skip(pageIndex)
                .filter(line -> !StringUtil.isEmpty(line))
                .limit(pageSize)
                .forEach(new Consumer<String>() {
                    StringBuilder sb = new StringBuilder();
                    @Override
                    public void accept(String line) {
                        if (line.startsWith("+")) {
                            sb.append(line.substring(1));
                            sb.append(" ");
                        }
                        if (line.startsWith("#")) {
                            sb.append(line.substring(1));
                            sb.append(" ");
                        }
                        if ("$1".equals(line)) {
                            barrageList.add(new BarrageEntity(sb.toString()));
                            sb.setLength(0);
                        }
                    }
                });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 逐行解析普通文本弹幕
     */
    private void parseNormalText(int pageIndex, int pageSize, Collection<BarrageEntity> barrageList) {
        try (Stream<String> stream = Files.lines(getPath(), CharsetUtil.resolveCharset(getPath().toString()))) {
            stream.skip(pageIndex)
                .filter(line -> !StringUtil.isEmpty(line))
                .limit(pageSize)
                .forEach(line -> barrageList.add(new BarrageEntity(line)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    boolean parse() {
        try (Stream<String> stream = Files.lines(getPath(), CharsetUtil.resolveCharset(getPath().toString()))) {
            MyConsumer consumer = new MyConsumer();
            stream.filter(line -> !StringUtil.isEmpty(line)).forEach(consumer);
            // 如果出 20 行的数据现过两次 $1，初步判定为金山词霸的词典，
            // 这里有个问题就是如果单词本只有一个单词的话就 gg 了，会解析不出来，这个先不管了
            if (consumer.countDelimiter >= 2) {
                type = 2;
            }
            // 设置弹幕数量
            switch (type) {
                case 1:
                    setSize(consumer.lineCount);
                    break;
                case 2:
                    setSize(consumer.countDelimiter);
                    break;
                default:
                    setSize(consumer.lineCount);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    
    private class MyConsumer implements Consumer<String> {
        int lineCount, countDelimiter;
        @Override
        public void accept(String line) {
            // 金山词霸用 $1 占一整行来拆分词典里的词
            if ("$1".equals(line)) {
                ++countDelimiter;
            }
            ++lineCount;
        }
    }
}
