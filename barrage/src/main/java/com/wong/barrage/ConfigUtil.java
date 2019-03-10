/*
 * Copyleft
 */
package com.wong.barrage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * 负责配置文件的加载及部分配置项的转换
 * @author 黄小天
 * @date 2019-03-08 19:15
 * @version 1.0
 */
public class ConfigUtil {
    
    /** 字体颜色 */
    private static Color color;
    
    /** 字体 */
    private static Font font;
    
    /** 从屏幕一侧滚动到另外一侧的时间，单位秒 */
    private static int elapse;
    
    /** 弹幕滚动方向，true：向左，false：向右 */
    private static boolean leftDirect;
    
    /** 弹幕距离屏幕顶端位置 */
    private static Integer locationTop;
    
    /** 每批发射的弹幕的数量 */
    private static int batchNumber;
    
    /** 每批发射的弹幕的时间间隔 */
    private static int batchSchedule;
    
    /** 每条发射的弹幕的时间间隔 */
    private static int timeInterval;
    
    /** 是否重复，所有弹幕发射完后，是否重头开始发射 */
    private static boolean repeat;
    
    /** 不解释 */
    private static Properties props;
    
    /** 配置项中颜色或弹幕距离屏幕顶部位置为随机时有用到 */
    private static Random random = new Random();  
    
    /** 屏幕尺寸 */
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    static {
        props = new Properties();
        try(InputStream inputStream = new FileInputStream("config.properties")) {
            if(inputStream != null) {
                props.load(inputStream);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        init();
    }
    
    /**
     * 初始化配置项参数
     */
    private static void init() {
        elapse = getInt("elapse", 15);
        batchNumber = getInt("batchNumber", 10);
        batchSchedule = getInt("batchSchedule", 300) * 1000;
        font = new Font(getString("fontName", "宋体"), getInt("fontStyle", 1), getInt("fontSize", 30));
        repeat =  getInt("repeat", 2) == 2 ? true : false;
        timeInterval = getInt("timeInterval", 2);
        
        // 初始化弹幕发射方向
        String direct = getString("direct");
        if ("left".equals(direct)) {
            leftDirect = false;
        } else {
            leftDirect = true;
        }
        
        // 初始化字体位置
        String position = getString("position");
        if ("bottom".equals(position)) {
            locationTop = (int) screenSize.getHeight();
        } else if ("top".equals(position)) {
            locationTop = 0;
        } else if (StringUtil.isNumber(position)) {
            locationTop = Integer.parseInt(position);
        }
        
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

    public static int getElapse() {
        return elapse;
    }

    public static boolean isLeftDirect() {
        return leftDirect;
    }

    public static Integer getLocationTop() {
        if (locationTop == null) {
            // 生成随机位置
            return random.nextInt((int)screenSize.getHeight() - 100);
        }
        return locationTop;
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
