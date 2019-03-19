/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader;

import java.util.List;

import com.wong.barrage.barrage.BarrageEntity;

/**
 * 
 * @author 黄小天
 * @date 2019-03-19 20:17
 * @version 1.0
 */
public interface BarrageLoadder {
    
    List<BarrageEntity> load(int pageIndex, int pageSize);
    
}
