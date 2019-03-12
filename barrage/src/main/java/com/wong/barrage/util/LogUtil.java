package com.wong.barrage.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 日志工具类
 * @author 黄小天
 * @date 2019-03-07 22:02
 * @version 1.0
 */
public class LogUtil {
    
    /**
     * 追加一行内容到文件中
     * @param fileName
     * @param value 内容
     */
    public static void append(String fileName, String value) {
        write(fileName, "\n" + value, StandardOpenOption.APPEND);
    }
    
    public static void write(String fileName, String value) {
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        write(fileName, value, StandardOpenOption.WRITE);
    }
    
    /**
     * 获取文件中的所有内容
     * @param fileName
     */
    public static int getPageIndex() {
        Path path = Paths.get("pageIndex");
        try {
            if (Files.exists(path)) {
                byte[] arr = Files.readAllBytes(path);
                if (arr != null && arr.length > 0) {
                    return Integer.parseInt(new String(arr));
                } else {
                    writePageIndex(0);
                }
            } else {
                writePageIndex(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static void writePageIndex(int pageIndex) {
        write("pageIndex", pageIndex + "");
    }
    
    private static void write(String fileName, String value, OpenOption option) {
        Path path = Paths.get(fileName);
        try {
            if (Files.notExists(path)) {
                path = Files.createFile(path);
            }
            Files.write(path, value.getBytes("UTF-8"), option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
