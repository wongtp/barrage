/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.util.List;

import com.wong.barrage.barrage.BarrageEntity;

/**
 * 按 Json 格式来读取
 * @author 黄小天
 * @date 2019-03-19 20:22
 * @version 1.0
 */
class JsonBarrageLoadder extends AbstractBarrageLoader {
    
    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void load(int pageIndex, int pageSize, List<BarrageEntity> barrageList) {
        
    }
}
