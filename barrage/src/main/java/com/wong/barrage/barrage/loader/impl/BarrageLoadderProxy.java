/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.wong.barrage.barrage.BarrageEntity;
import com.wong.barrage.barrage.loader.BarrageLoadder;
import com.wong.barrage.config.Constant;

/**
 * 弹幕加载工具类
 * @author 黄小天
 * @date 2019-03-09 10:31
 * @version 1.0
 */
public class BarrageLoadderProxy implements BarrageLoadder {
    
    private AbstractBarrageLoader lineBarrageLoadder = new TextBarrageLoadder();
    private AbstractBarrageLoader jsonBarrageLoadder = new JsonBarrageLoadder();
    private AbstractBarrageLoader xmlBarrageLoadder = new XmlBarrageLoadder();
    
    /**
     * 按照给定的值获取定量的弹幕数量
     * @param index
     * @param size
     * @return
     */
    public List<BarrageEntity> load(int pageIndex, int pageSize) {
        List<Path> pathList = getDictList();
        List<BarrageEntity> barrageList = new ArrayList<>();
        try {
            AbstractBarrageLoader loader = null;
            for (Path path : pathList) {
                String pathStr = path.toString().toLowerCase();
                if (pathStr.endsWith(".txt")) {
                    loader = lineBarrageLoadder;
                } else if (pathStr.endsWith(".json")) {
                    loader = jsonBarrageLoadder;
                } else if (pathStr.endsWith(".xml")) {
                    loader = xmlBarrageLoadder;
                }
                loader.parse(path);
                // 索引比文件里面弹幕数量还要多的情况就跳到下一个弹幕文件
                if (pageIndex >= loader.getSize()) {
                    pageIndex -= loader.getSize();
                    continue;
                }
                loader.load(pageIndex, pageSize, barrageList);
                // 加载回来的数据比预期的要少，判断一下加载
                if (barrageList.size() >= pageSize) {
                    break;
                }
                pageSize -= barrageList.size();
                // 读下一个文件
                pageIndex = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barrageList;
    }
    
    private List<Path> getDictList() {
        Path pathDir = Paths.get(Constant.DICT_PATH);
        File fileDir = pathDir.toFile();
        if (!fileDir.exists() || !fileDir.isDirectory() || fileDir.listFiles().length == 0) {
            JOptionPane.showMessageDialog(null, Constant.DICT_PATH + " 文件夹竟然是空的 Σ(*ﾟдﾟ)ﾉ");
            System.exit(0);
        }
        List<Path> list = new ArrayList<>();
        try (DirectoryStream<Path> dictStream = Files.newDirectoryStream(pathDir)) {
            dictStream.forEach(path -> list.add(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
