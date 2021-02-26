package com.xiaosong.myframework.business.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

public class EmojiUtils {
    public static void main(String[] args) {
        String  testStr = "李明松！31msli";
        Boolean has = hasEmoji(testStr);
        System.out.println(has);
        System.out.println(checkUsername(testStr));
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
        Pattern pattern = compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
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

    /**
     * 检查用户名是否为常规名
     * 只能包括 数字 字母 _ - & 汉字 的组合，长度为 1-20 位
     * @param username
     * @return
     */
    public static boolean checkUsername(String username) {
        String regExp = "^[\\u4e00-\\u9fa5\\w-&]{0,19}$";
        return username.matches(regExp);
    }
}
