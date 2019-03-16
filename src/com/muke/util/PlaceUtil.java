package com.muke.util;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class PlaceUtil {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
//            JSONObject json = new JSONObject(jsonText);
            return JSONObject.fromObject(jsonText);
        } finally {
            is.close();
        }
    }

    public static String baiduGetCityCode(String ip) throws JSONException, IOException {

        ip="14.118.141.134";//开发环境写死，线上环境务必要删除
        //百度ak，自己去申请就可以
        String ak = "t89iFPNcq0sqOCuzlMwdRVTzGcSWgVoI";
        //这里调用百度的ip定位api服务 详见 http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
        JSONObject json = readJsonFromUrl("http://api.map.baidu.com/location/ip?ip=" + ip + "&ak=" + ak + "&coor=bd09ll");
        Object obj = ((JSONObject) json.get("content")).get("address");
        String s = obj.toString();
//        System.out.println(s);
//        JSONObject j = new JSONObject();
//        j=JSONObject.fromObject(s);
//        int city_code = (int) j.get("city_code");
//        String code = String.valueOf(city_code);
//        System.out.println(code);
        return s;
    }

//    public static void main(String[] args) throws IOException {
//    System.out.println(baiduGetCityCode("14.118.141.134"));
//    }
}
