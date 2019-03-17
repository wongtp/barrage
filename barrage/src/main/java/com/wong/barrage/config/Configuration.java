/*
 * Copyleft
 */
package com.wong.barrage.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;

import com.wong.barrage.util.LogUtil;
import com.wong.barrage.util.StringUtil;

/**
 * 负责配置文件的加载及部分配置项的转换
 * @author 黄小天
 * @date 2019-03-08 19:15
 * @version 1.0
 */
public class Configuration {
    
    /** 字体颜色 */
    private static Color color;
    
    /** 字体颜色 */
    private static Color transparentColor = new Color(0, 0, 0, 0);
    
    /** 字体 */
    private static Font font;
    
    /** 从屏幕一侧滚动到另外一侧的时间，单位秒 */
    private static int speed;
    
    /** 弹幕滚动方向，true：向右，false：向左 */
    private static Boolean rightDirect;
    
    /** 每批发射的弹幕的数量 */
    private static int batchNumber;
    
    /** 每批发射的弹幕的时间间隔 */
    private static int batchSchedule;
    
    /** 每条发射的弹幕的时间间隔 */
    private static int timeInterval;
    
    /** 是否重复，所有弹幕发射完后，是否重头开始发射 */
    private static boolean repeat;
    
    /** 是否允许并发执行 */
    private static boolean radomSpeed;
    /** 每次重启后，弹幕都重头开始读取 */
    private static boolean refershPageIndex;
    
    /** 弹幕位置 */
    private static String position;
    
    /** 不解释 */
    private static Properties props;
    
    /** 配置项中颜色或弹幕距离屏幕顶部位置为随机时有用到 */
    private static Random random = new Random();  
    
    /** 屏幕尺寸 */
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    static {
        Path path = Paths.get(Constant.BARRAGE_PATH);
        if (Files.notExists(path)) {
            JOptionPane.showMessageDialog(null, Constant.BARRAGE_PATH + " 配置文件不见了", "Σ(*ﾟдﾟﾉ)ﾉ", 
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        props = new Properties();
        try(InputStream inputStream = Files.newInputStream(path)) {
            if(inputStream != null) {
                props.load(inputStream);
            }
        } catch (IOException e) {
            LogUtil.append(Constant.LOG_PATH, "while init config:" + e.getMessage());
        }
        init();
    }
    
    /**
     * 初始化配置项参数
     */
    private static void init() {
        speed = getInt("speed", 15);
        batchNumber = getInt("batchNumber", 10);
        batchSchedule = getInt("batchSchedule", 300);
        font = new Font(getString("fontName", "宋体"), getInt("fontStyle", 1), getInt("fontSize", 30));
        repeat =  getInt("repeat", 2) == 2 ? true : false;
        radomSpeed =  getInt("radomSpeed", 2) == 2 ? true : false;
        refershPageIndex =  getInt("refershPageIndex", 2) == 2 ? true : false;
        timeInterval = getInt("timeInterval", 2);
        
        // 初始化弹幕发射方向
        String direct = getString("direct");
        if ("left".equals(direct)) {
            rightDirect = false;
        } else if ("right".equals(direct)) {
            rightDirect = true;
        }
        
        // 初始化字体位置
        position = getString("position");
        
        // 初始化字体颜色
        String colorStr = getString("fontColor");
        String[] colorStrArr = colorStr.split(".");
        if (colorStrArr.length == 3) {
            if (StringUtil.isNumber(colorStrArr[0]) && StringUtil.isNumber(colorStrArr[1])
                    && StringUtil.isNumber(colorStrArr[2])) {
                color = new Color(Integer.parseInt(colorStrArr[0]), 
                        Integer.parseInt(colorStrArr[1]), Integer.parseInt(colorStrArr[2]));
            }
        } else if (colorStr.startsWith("#")) {
            try {
                color = new Color(Integer.parseInt(colorStr.substring(1), 16));
            } catch (Exception e) {
                color = null;
            }
        } else if ("white".equals(colorStr)) {
            color = Color.white;
        } else if ("lightGray".equals(colorStr)) {
            color = Color.lightGray;
        } else if ("gray".equals(colorStr)) {
            color = Color.gray;
        } else if ("darkGray".equals(colorStr)) {
            color = Color.darkGray;
        } else if ("black".equals(colorStr)) {
            color = Color.black;
        } else if ("red".equals(colorStr)) {
            color = Color.red;
        } else if ("pink".equals(colorStr)) {
            color = Color.pink;
        } else if ("orange".equals(colorStr)) {
            color = Color.orange;
        } else if ("yellow".equals(colorStr)) {
            color = Color.yellow;
        } else if ("green".equals(colorStr)) {
            color = Color.green;
        } else if ("magenta".equals(colorStr)) {
            color = Color.magenta;
        } else if ("cyan".equals(colorStr)) {
            color = Color.cyan;
        } else if ("blue".equals(colorStr)) {
            color = Color.blue;
        }
    }
    
    public static Color getColor() {
        if (color == null) {
            // 生成随机颜色，rgb 颜色 -16777216 ~ 0
            return new Color(-random.nextInt(16777216));
        }
        return color;
    }

    public static Font getFont() {
        return font;
    }

    public static int getSpeed() {
        return speed;
    }

    public static boolean isRightDirect() {
        if (rightDirect == null) {
            return random.nextInt(6) > 3;
        }
        return rightDirect;
    }

    // 用于防止前后两个弹幕重合，=_=#!
    private static boolean changeFlag = false;
    public static Integer getLocationTop() {
        int y;
        if ("bottom".equals(position)) {
            // 用随机数来生成位置，预防弹幕重合
            y = (int)screenSize.getHeight() - font.getSize()*2;
        } else if ("top".equals(position)) {
            y =  font.getSize()*2;
        } else if ("center".equals(position)) {
            y =  (int)screenSize.getHeight()/2;
        } else if (StringUtil.isNumber(position)) {
            y =  Integer.parseInt(position);
        } else {
            // 生成随机位置
            y =  (int)screenSize.getHeight() - random.nextInt((int)screenSize.getHeight() - font.getSize());
        }
        if (changeFlag) {
            changeFlag = false;
            return y - font.getSize();
        } else {
            changeFlag = true;
            return y;
        }
    }
    
    /**
     * 获取上次读取的行数，使得不用每次重启都重新开始
     * @param fileName
     */
    public static int getPageIndex() {
        Path path = Paths.get(Constant.PAGEINDEX_PATH);
        try {
            if (Files.exists(path)) {
                byte[] byteArr = Files.readAllBytes(path);
                if (byteArr != null && byteArr.length > 0) {
                    return Integer.parseInt(new String(byteArr));
                } else {
                    writePageIndex(0);
                }
            } else {
                writePageIndex(0);
            }
        } catch (Exception e) {
            LogUtil.append(Constant.LOG_PATH, "error while read pageIndex file:" + e.getMessage());
        }
        return 0;
    }
    
    public static void writePageIndex(int pageIndex) {
        LogUtil.write(Constant.PAGEINDEX_PATH, String.valueOf(pageIndex));
    }

    public static int getBatchNumber() {
        return batchNumber;
    }

    public static int getBatchSchedule() {
        return batchSchedule;
    }

    public static boolean isRepeat() {
        return repeat;
    }
    
    public static Dimension getScreenSize() {
        return screenSize;
    }

    public static int getTimeInterval() {
        return timeInterval;
    }
    
    public static boolean isRadomSpeed() {
        return radomSpeed;
    }
    
    public static Random getRandom() {
        return random;
    }
    
    public static boolean isRefershPageIndex() {
        return refershPageIndex;
    }
    
    public static Color getTransparentColor() {
        return transparentColor;
    }

    /**
     * 获取一个大于 0 的配置值，如果配置的值小于 0，则使用给定的默认值
     * @param key
     * @param defaultValue
     * @return
     */
    private static int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (StringUtil.isEmpty(value) || !StringUtil.isNumber(value)) {
            return defaultValue;
        }
        int valueInt = Integer.parseInt(value);
        return valueInt > 0 ? valueInt : defaultValue;
    }
    
    private static String getString(String key) {
        return getString(key, "");
    }
    
    private static String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue).trim();
    }
}
