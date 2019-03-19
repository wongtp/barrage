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
                                System.out.println(line);
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
    
    public static void main(String[] args) {
        new DictConverter().findAndConvert();
    }
}
