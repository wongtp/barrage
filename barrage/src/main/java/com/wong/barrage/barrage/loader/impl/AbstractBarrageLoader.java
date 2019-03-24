/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.nio.file.Path;
import java.util.Collection;

import com.wong.barrage.barrage.BarrageEntity;

/**
 * 这个抽象类一直不知道怎么写，有点懵逼，抽象能力还不够，先这样吧，或许以后会有更好的方法
 * @author 黄小天
 * @date 2019-03-19 21:37
 * @version 1.0
 */
abstract class AbstractBarrageLoader {
    
    /** 对应 {@link #path} 中弹幕的总数 **/
    private int size;
    
    /** 要读取的弹幕文件 path **/
    private Path path;
    
    /**
     * 该方法会在 {@link #setPath(Path)} 方法里面调用<br>
     * 在子类中 {@link #parse()} 操作应至少完成 path 文件读取后 size 属性的设置，<br>
     * 否则就要重写 {@link #getSize()} 来获取要读取的文件中弹幕的数量
     */
    abstract boolean parse();
    
    /**
     * @param pageIndex 要读取弹幕在 {@link #path} 文件中的索引
     * @param pageSize 要读取的弹幕数量
     * @param barrageList 用来存放弹幕的 List 容器
     */
    public abstract void load(int pageIndex, int pageSize, Collection<BarrageEntity> barrageContainer);
    
    /**
     * 如果在这行这个方法之前有设置 path 的话，则不用再传了;
     * @return
     */
    public int getSize() {
        return size;
    }
    
    void setSize(int size) {
        this.size = size;
    }
    
    public void parse(Path path) {
        this.path = path;
        parse();
    }
    
    public Path getPath() {
        return path;
    }
}
