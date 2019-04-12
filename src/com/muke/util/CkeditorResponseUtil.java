package com.muke.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
public class CkeditorResponseUtil extends HashMap<String, Object> {

    Map<String, Object> msgMap = new HashMap<>();
    public String error(int code, String msg) {
        CkeditorResponseUtil result = new CkeditorResponseUtil();
        msgMap.put("message", msg);
        result.put("uploaded", code);
        result.put("error", msgMap);
        return JSONUtils.beanToJson(result);
    }

    public String success(int code, String fileName, String url, String msg) {
        CkeditorResponseUtil result = new CkeditorResponseUtil();
        if (!StringUtils.isEmpty(msg)) {
            msgMap.put("message", msg);
            result.put("error", msgMap);
        }
        result.put("uploaded", code);
        result.put("fileName", fileName);
        result.put("url", url);
        return JSONUtils.beanToJson(result);
    }
}
