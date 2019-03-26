/*
 * Copyleft
 */
package com.wong.barrage.view;

import java.awt.event.ActionEvent;

/**
 * 
 * @author 黄小天
 * @date 2019-03-26 21:24
 * @version 1.0
 */
public interface PopupMenuCallBack {
    
    default String getMenuName() {
        return this.getClass().getSimpleName();
    }
    
    void actionPerformed(ActionEvent e);
}
