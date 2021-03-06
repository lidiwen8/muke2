package com.muke.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * 七牛云上传文件工具类
 */
public class QiniuCloudUtil {

    // 设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "M3HmbBGSuAOhmwbURCjxXmme-R42r-OCWLSJiBf1";
    private static final String SECRET_KEY = "G1LCpQQwjYgeu2IGZLhO5oNYzxqBf_GCr__S1kw-";

    // 要上传的空间
    private static final String bucketname = "lidiwen";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    //上传的图片路径
    private static final String DOMAIN = "http://love.lidiwen.club/";


    //自定义的图片样式
    private static final String style = "imageMogr2/blur/1x0/quality/100|watermark/2/text/54ix5LmL5a62LeaZk-aWhw==/font/5a6L5L2T/fontsize/240/fill/I0Y5RkJGRQ==/dissolve/100/gravity/SouthEast/dx/10/dy/10";

    public static String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }

    /**
     * 上传文件到七牛
     * @param data 文件字节数组    fileName 要上传到七牛的文件名
     * @return flag  true上传成功    false上传失败
     */
    public static String upload2(byte[] data, String fileName){
        String url=null;
        //要上传的空间(bucket)的存储区域为华南时
        Zone z = Zone.zone2();
        //密钥配置
        Configuration c = new Configuration(z);
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        UploadManager uploadManager = new UploadManager(c);
        try {
            uploadManager.put(data, fileName, auth.uploadToken(bucketname));
            url=DOMAIN +fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

//    // 普通上传
//    public static String upload(String filePath, String fileName) throws IOException {
//        // 创建上传对象
//        UploadManager uploadManager = new UploadManager();
//        try {
//            // 调用put方法上传
//            String token = auth.uploadToken(bucketname);
//            if(StringUtils.isEmpty(token)) {
//                System.out.println("未获取到token，请重试！");
//                return null;
//            }
//            Response res = uploadManager.put(filePath, fileName, token);
//            // 打印返回的信息
//            System.out.println(res.bodyString());
//            if (res.isOK()) {
//                Ret ret = res.jsonToObject(Ret.class);
//                //如果不需要对图片进行样式处理，则使用以下方式即可
//                //return DOMAIN + ret.key;
//                return DOMAIN + ret.key + "?" + style;
//            }
//        } catch (QiniuException e) {
//            Response r = e.response;
//            // 请求失败时打印的异常的信息
//            System.out.println(r.toString());
//            try {
//                // 响应的文本信息
//                System.out.println(r.bodyString());
//            } catch (QiniuException e1) {
//                // ignore
//            }
//        }
//        return null;
//    }

    //base64方式上传
    public static String put64image(byte[] base64, String key) throws Exception{
        String file64 = Base64.encodeToString(base64, 0);
        Integer len = base64.length;

        //华北空间使用 upload-z1.qiniu.com，华南空间使用 upload-z2.qiniu.com，北美空间使用 upload-na0.qiniu.com
        String url = "http://upload-z2.qiniu.com/putb64/" + len + "/key/"+ UrlSafeBase64.encodeToString(key);

        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        //System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        response.close();
//        System.out.println(response);
        //如果不需要添加图片样式，使用以下方式
        //return DOMAIN + key;
        return DOMAIN + key + "?" + style;
    }

    //base64方式上传
    public static String put64image2(byte[] base64, String key) throws Exception{
        String file64 = Base64.encodeToString(base64, 0);
        Integer len = base64.length;
        //华北空间使用 upload-z1.qiniu.com，华南空间使用 upload-z2.qiniu.com，北美空间使用 upload-na0.qiniu.com
        String url = "http://upload-z2.qiniu.com/putb64/" + len + "/key/"+ UrlSafeBase64.encodeToString(key);

        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        //System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
//        System.out.println(response);
        response.close();
        //如果不需要添加图片样式，使用以下方式
        return DOMAIN + key;
    }

//     普通删除(暂未使用以下方法，未测试)
    public static void delete(String key) throws IOException {
        // 实例化一个BucketManager对象
        Zone z = Zone.zone2();
        //密钥配置
        Configuration config = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, config);
        // 此处的25是去掉：http://love.lidiwen.club/,剩下的key就是图片在七牛云的名称
        key = key.substring(25);
//        System.out.println(key);
        try {
            // 调用delete方法移动文件
            bucketManager.delete(bucketname, key);
        } catch (QiniuException e) {
            // 捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
            r.close();
        }
    }

    class Ret {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }
}