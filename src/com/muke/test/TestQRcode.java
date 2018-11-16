package com.muke.test;

import com.muke.util.QRCodeUtil;
import java.util.Random;
import java.util.Scanner;
public class TestQRcode {



    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);
        System.out.println("请输入你要生成二维码的url链接地址：");
        String textt = scan.next();

            System.out.println("请输入你要生成二维码的是不是要logo,要回复1，不要回复0：");
            int flag=scan.nextInt();

            if(flag==0){
                Random random =new Random();
                String filename=String.valueOf(random.nextInt()) ;
                QRCodeUtil.encode(textt,"","D:\\Java web项目\\Spring boot项目\\QRcode\\",true,filename);
                System.out.println("二维码图片已经生成，文件名为："+filename+"-地址是：D:\\Java web项目\\Spring boot项目\\QRcode\\"+filename);
                System.out.println();
                System.out.println("请输入你要生成二维码的url链接地址：");
            }else{
                System.out.println("请输入你要生成二维码的logo图片所在地址：");
                String logoPath = scan.next();
                Random random =new Random();
                String filename=String.valueOf(random.nextInt()) ;
                QRCodeUtil.encode(textt,logoPath,"D:\\Java web项目\\Spring boot项目\\QRcode\\",true,filename);
                System.out.println("二维码图片已经生成，文件名为："+filename+"-地址是：D:\\Java web项目\\Spring boot项目\\QRcode\\"+filename);
            }




        //生成带logo 的二维码

//        String text = "http://my.csdn.net/ljheee";
//
//        QRCodeUtil.encode(text, "resources/static/images/favicon.png", "D:\\Java web项目\\Spring boot项目\\QRcode", true);



        //生成不带logo 的二维码

//        String textt = "http://www.baidu.com";
//
//        QRCodeUtil.encode(textt,"","D:\\Java web项目\\Spring boot项目\\QRcode",true,"");

        //指定二维码图片，解析返回数据

       System.out.println(QRCodeUtil.decode("D:\\Java web项目\\Spring boot项目\\QRcode\\573442233.jpg"));



    }

}

