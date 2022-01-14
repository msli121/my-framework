package com.paradigm.ocr.business.utils;

import java.util.Random;

/**
 * @Description
 * @Author msli
 * @Date 2021/02/22
 */

public class SysRandomUtil {
    private static final String[] GENERATE_SOURCE = new String[]{"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z"};
    private static final int GENERATE_SOURCE_LEN = GENERATE_SOURCE.length;

    /**
     * 随机生成数字和字符组合，区分大小写
     *
     * @param strLength 随机字符串的长度
     * @return
     */
    public static String generateWithCase(int strLength){
        StringBuilder randomBuffer = new StringBuilder(strLength);
        Random random = new Random();
        for(int i=0; i < strLength; i++) {
            int index = random.nextInt(GENERATE_SOURCE_LEN);
            randomBuffer.append(GENERATE_SOURCE[index]);
        }
        return randomBuffer.toString();
    }

    /**
     * 随机生成指定长度的字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i=0; i< length; i++) {
            int index = random.nextInt(GENERATE_SOURCE_LEN);
            sb.append(GENERATE_SOURCE[index]);
        }
        return sb.toString();
    }
}
