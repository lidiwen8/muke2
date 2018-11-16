package com.muke.util;

import java.security.MessageDigest;
import java.util.Scanner;

/**
 * Created by lidiwen on 2018/7/17.
 */
public class Md5Encrypt {
    public  static  String  Encrypt(String s)throws Exception{
        MessageDigest md5=MessageDigest.getInstance("MD5");
        sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
        return base64Encoder.encode(md5.digest(s.getBytes("utf-8")));

    }
    public static String MD5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    public static void main(String[] args) {

//      String str=".gif',";
//      for(int i=0;i<100;i++){
//          str="'"+i+str;
//
//      }
//        System.out.println(str);
        String str;
        Scanner sb = new Scanner(System.in);
        while (true) {
            str = sb.nextLine();
            try {
                System.out.println("第一种加密："+Md5Encrypt.Encrypt(str)+"第二种加密："+MD5(str));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
