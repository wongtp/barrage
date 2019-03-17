/*
 * Copyleft
 */
package com.wong.barrage.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 
 * @author 黄小天
 * @date 2019-03-17 23:45
 * @version 1.0
 */
public class CharsetUtils {
    
    public static Charset resolveCode(String path) {  
        try (InputStream inputStream = new FileInputStream(path)) {
            byte[] head = new byte[3];    
            inputStream.read(head);      
            if (head[0] == -1 && head[1] == -2 ) {
                return Charset.forName("UTF-16");
            }
            if (head[0] == -2 && head[1] == -1 ) {
                return Charset.forName("Unicode");    
            }
            if(head[0] == -17 && head[1] == -69 && head[2] == -65) {
                return Charset.forName("UTF-8");    
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Charset.forName("gb2312"); 
    }
}
