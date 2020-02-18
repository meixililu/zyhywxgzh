package com.xbkj.zyhy.wx.utils;

import java.util.Random;

public class StringUtils {

    public static boolean isEnglish(String charaString) {
        char[] arr = charaString.toCharArray();
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] >= 65 && arr[i] <= 90) || (arr[i] >= 97 && arr[i] <= 125) || (arr[i] == 39)
                    || (arr[i] == 8217)) {
                count++;
            }
        }
        return (double)count / arr.length  > 0.5;
    }

    //length表示生成字符串的长度
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
