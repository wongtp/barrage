/*
 * Copyleft
 */
package com.wong.barrage.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;

/**
 * 判断字符编码的简单工具类，只能做简单的编码判断
 */
public class CharsetUtil {
    
    private static int BYTE_SIZE = 8;
    
    public static Charset resolveCharset(String path) {
        try (InputStream is = Files.newInputStream(Paths.get(path))) {
            byte[] head = new byte[3];    
            is.read(head);
            System.out.println("head[0]:" + head[0] + 
                    ", head[1]:" + head[1] + 
                    ", head[2]:" + head[2]);
            if (head[0] == -1 && head[1] == -2) {
                return Charset.forName("UTF-16");
            }
            if (head[0] == -2 && head[1] == -1) {
                return Charset.forName("Unicode");
            }
            // 带 BOM UTF-8
            if((head[0] == -17 && head[1] == -69 && head[2] == -65)) {
                return Charset.forName("UTF-8");
            }
            if (isUTF8(is)) {
                return Charset.forName("UTF-8");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Charset.forName("gb2312"); 
    }
    
    /**
     * 是否是无 BOM 的 UTF8 格式，不判断常规场景，只区分无 BOM UTF8 和 GBK
     *
     * @param is
     * @return
     */
    private static boolean isUTF8(InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(0);
        bis.reset();
        //读取第一个字节
        int code = bis.read();
        while (code != -1) {
            BitSet bitSet = convert2BitSet(code);
            //判断是否为单字节, 多字节时，再读取N个字节
            if (bitSet.get(0)) {
                // 未检测通过,直接返回
                if (!checkMultiByte(bis, bitSet)) {
                    return false;
                }
            }
            code = bis.read();
        }
        return true;
    }
 
    /**
     * 检测多字节，判断是否为utf8，已经读取了一个字节
     *
     * @param is
     * @param bitSet
     * @return
     */
    private static boolean checkMultiByte(BufferedInputStream bis, BitSet bitSet) throws Exception {
        int count = getCountOfSequential(bitSet);
        // 已经读取了一个字节，不能再读取
        byte[] bytes = new byte[count - 1];
        bis.read(bytes);
        for (byte b : bytes) {
            if (!checkUtf8Byte(b)) {
                return false;
            }
        }
        return true;
    }
 
    /**
     * 检测单字节，判断是否为utf8
     *
     * @param b
     * @return
     */
    private static boolean checkUtf8Byte(byte b) throws Exception {
        BitSet bitSet = convert2BitSet(b);
        return bitSet.get(0) && !bitSet.get(1);
    }
 
    /**
     * 检测bitSet中从开始有多少个连续的1
     *
     * @param bitSet
     * @return
     */
    private static int getCountOfSequential(BitSet bitSet) {
        int count = 0;
        for (int i = 0; i < BYTE_SIZE; i++) {
            if (bitSet.get(i)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
 
 
    /**
     * 将整形转为BitSet
     *
     * @param code
     * @return
     */
    private static BitSet convert2BitSet(int code) {
        BitSet bitSet = new BitSet(BYTE_SIZE);
        for (int i = 0; i < BYTE_SIZE; i++) {
            int tmp3 = code >> (BYTE_SIZE - i - 1);
            int tmp2 = 0x1 & tmp3;
            if (tmp2 == 1) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }
}
