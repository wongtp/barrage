/*
 * Copyleft
 */
package com.wong.barrage.barrage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.wong.barrage.config.Constant;
import com.wong.barrage.util.CharsetUtil;
import com.wong.barrage.util.LogUtil;
import com.wong.barrage.util.StringUtil;

/**
 * 
 * @author 黄小天
 * @date 2019-03-17 23:05
 * @version 1.0
 */
public class DictConverter {
    
    private void findAndConvert() {
        Path dictDir = Paths.get(Constant.DICT_PATH);
        if (Files.exists(dictDir)) {
            try (DirectoryStream<Path> dictStream = Files.newDirectoryStream(dictDir)) {
                Set<String> barrageSet = loadBarrage();
                StringBuilder lineBuilder = new StringBuilder();
                dictStream.forEach(new Consumer<Path>() {
                    @Override
                    public void accept(Path path) {
                        try (Stream<String> stream = Files.lines(path, CharsetUtil.resolveCharset(path.toString()))) {
                            stream.filter(new Predicate<String>() {
                                @Override
                                public boolean test(String line) {
                                    return !StringUtil.isEmpty(line);
                                }
                            })
                            .forEach(line -> {
                                if (parseLine(lineBuilder, line)) {
                                    barrageSet.add(lineBuilder.toString());
                                    lineBuilder.setLength(0);
                                }
                            });
                        } catch (UncheckedIOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            LogUtil.append(Constant.LOG_PATH, "DictConverter thorw exception:" + e.getMessage());
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private boolean parseLine(StringBuilder lineBuilder, String line) {
        int commaIndex = line.indexOf(",");
        return true;
    }
    
    private Set<String> loadBarrage() {
        Path path = Paths.get(Constant.BARRAGE_PATH);
        Set<String> barrageSet = new TreeSet<>();
        try (Stream<String> stream = Files.lines(path)) {
            stream.filter(new Predicate<String>() {
                    @Override
                    public boolean test(String line) {
                        return !StringUtil.isEmpty(line);
                    }
                }).forEach(line -> {
                    barrageSet.add(line);
                });
        } catch (Exception e) {
            LogUtil.append(Constant.LOG_PATH, "DictConverter thorw exception:" + e.getMessage());
        }
        return barrageSet;
    }
    
    public static void main(String[] args) {
        new DictConverter().findAndConvert();
    }
}
