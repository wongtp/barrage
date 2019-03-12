package com.wong.barrage.util;

import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author 黄小天
 * @date 2019-03-07 22:02
 * @version 1.0
 */
public class StringUtil {
    
    // 用于判断字符串是不是数字
    private static Pattern PATTERN_NUMBER = Pattern.compile("^[\\d]+$");
	
    /**
	 * 判断字符串是不是空
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		if(str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * 判断字符串是不是数字
     * @param content 字符串内容
     */
    public static boolean isNumber(String content) {
        if (isEmpty(content)) {
            return false;
        }
        return PATTERN_NUMBER.matcher(content).matches();
    }
}
