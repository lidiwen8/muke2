package com.muke.util;

/**
 * Created by ASUS on 2018/7/15.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtil {



    /**

     * 利用正则表达式判断str是否存在表情字符

     * @param str

     * @return

     */

    public static String resolveToByteFromEmoji(String str) {

        Pattern pattern = Pattern

                .compile("[^(\u2E80-\u9FFF\\w\\s`~!@#\\$%\\^&\\*\\(\\)_+-？（）——=\\[\\]{}\\|;。，、《》”：；“！……’:‘\"<,>\\.?/\\\\*)]");

        Matcher matcher = pattern.matcher(str);

        StringBuffer sb2 = new StringBuffer();

        while (matcher.find()) {

            matcher.appendReplacement(sb2, encode(matcher.group(0)));

        }

        matcher.appendTail(sb2);

        //将分隔符 替换成\ 得到的内容就是 \u1245

        return sb2.toString().replace("&ns;","\\");

    }



    /**

     * 表情字符进行处理

     * @param str

     * @return

     */

    public static String encode(String str) {

        if (null == str || str.equals(""))

            return "";

        StringBuffer sb = new StringBuffer();

        try {

            //用16bit数字编码表示一个字符，每8bit用byte表示。

            byte bytesUtf16[] = str.getBytes("UTF-16");

            for (int n = 0; n < bytesUtf16.length; n++) {

                // 截取后面8位，并用16进制表示。

                str = (Integer.toHexString(bytesUtf16[n] & 0XFF));

                // 将获得的16进制表示连成串

                sb.append((str.length() == 1) ? ("0" + str) : str);

            }

            // 去除第一个标记字符

            str = sb.toString().toUpperCase().substring(4);

            char[] chs = str.toCharArray();

            StringBuffer buff=new StringBuffer();

            // str = "";

            for (int i = 0; i < chs.length; i = i + 4) {

                //此处&ns;作为分割符号 本因是“\”但由于Matcher.appendReplacement()将会把 \给替换为空

                buff.append( "&ns;u" + chs[i] + chs[i+1] + chs[i+2] + chs[i+3]);

            }

            return buff.toString();

        } catch (Exception e) {

            e.printStackTrace();

            return str;

        }

    }





    public static void main(String[] args) {

        String str="萨瓦迪卡&ns;uD83D&ns;uDE33&ns;uD83D&ns;uDE33&ns;uD83D&ns;uDE33";

        System.out.println(str.concat("&ns;"));

        System.out.println(str.replace("&ns;", "\\"));

    }

}
