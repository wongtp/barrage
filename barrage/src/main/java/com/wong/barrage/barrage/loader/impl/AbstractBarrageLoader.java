/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.nio.file.Path;
import java.util.List;

import com.wong.barrage.barrage.BarrageEntity;

/**
 * 
 * @author 黄小天
 * @date 2019-03-19 21:37
 * @version 1.0
 */
abstract class AbstractBarrageLoader {
    
    protected int size;
    
    private Path path;
    
    public void setSize(int size) {
        this.size = size;
    }
    
    abstract int getSize();
    
    public void setPath(Path path) {
        this.path = path;
    }
    
    public Path getPath() {
        if (path == null) {
            throw new NullPointerException("path can not be null!");
        }
        return path;
    }
    
    public abstract void load(int pageIndex, int pageSize, List<BarrageEntity> barrageList);

}
