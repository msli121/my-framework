package com.xiaosong.myframework.business.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtils {
    public static void main(String[] args) {
        String  testStr = "🐺32313231msli";
        Boolean has = hasEmoji(testStr);
        System.out.println(has);
        System.out.println(replaceEmoji(testStr, "^_^"));
    }

    /**
     * 判断是否存在特殊字符串
     * @param strContent
     * @return
     */
    public static boolean hasEmoji(String strContent){
        if(StringUtils.isEmpty(strContent)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(strContent);
        return matcher.find();
    }

    /**
     * 替换字符串中的 emoji 字符
     * @param targetStr
     * @param replaceStr
     * @return
     */
    public static String replaceEmoji(String targetStr, String replaceStr){
        if (hasEmoji(targetStr)) {
            targetStr = targetStr.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", replaceStr);
        }
        return targetStr;
    }
}
