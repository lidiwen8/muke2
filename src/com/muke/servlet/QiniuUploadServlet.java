package com.muke.servlet;

import com.muke.util.QiniuCloudUtil;
import com.qiniu.util.Auth;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/QiniuUploadServlet")
public class QiniuUploadServlet extends HttpServlet {
    // ******的内容需要查看七牛云账号的相关信息
    private static final String accessKey = "M3HmbBGSuAOhmwbURCjxXmme-R42r-OCWLSJiBf1";    //访问秘钥
    private static final String secretKey = "G1LCpQQwjYgeu2IGZLhO5oNYzxqBf_GCr__S1kw-";    //授权秘钥
    private static final String bucket = "lidiwen";       //存储空间名称
    private static final String domain = "http://love.lidiwen.club/";       //外链域名

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String action = request.getParameter("action");
        try {
            //使用反射定义方法
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class,
                    HttpServletResponse.class);
            //调用方法
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 七牛云上传生成凭证
     *
     * @throws Exception
     */
    public void QiniuUpToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<String, Object>();
        String suffix = request.getParameter("suffix");
        try {
            //验证七牛云身份是否通过
            Auth auth = Auth.create(accessKey, secretKey);
            //生成凭证
            String upToken = auth.uploadToken(bucket);
            result.put("token", upToken);
            //存入外链默认域名，用于拼接完整的资源外链路径
            result.put("domain", domain);

            // 是否可以上传的图片格式
            boolean flag = false;
            String[] imgTypes = new String[]{"jpg", "jpeg", "bmp", "gif", "png"};
            for (String fileSuffix : imgTypes) {
                if (suffix.substring(suffix.lastIndexOf(".") + 1).equalsIgnoreCase(fileSuffix)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new Exception("图片：" + suffix + " 上传格式不对！");
            }

            //生成实际路径名
            String randomFileName = UUID.randomUUID().toString() + suffix;
            result.put("imgUrl", randomFileName);
            result.put("success", 1);
        } catch (Exception e) {
            result.put("message", "获取凭证失败，" + e.getMessage());
            result.put("success", 0);
        } finally {
            JSONObject json = JSONObject.fromObject(result);
            response.getWriter().print(json);
            return;
//            return result;
        }
    }

    public void uploadImg(HttpServletRequest request, HttpServletResponse response,MultipartFile image) throws ServletException, IOException {
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            response.getWriter().print("{\"res\": -1, \"info\":\"请选择一张图片上传！\"}");
            return;
        }
        if (image.isEmpty()) {
            response.getWriter().print("{\"res\": -1, \"info\":\"请选择一张图片上传！\"}");
            return;
        }

        try {
            byte[] bytes = image.getBytes();
            String imageName = UUID.randomUUID().toString();
            try {
                //使用base64方式上传到七牛云
                String url = QiniuCloudUtil.put64image(bytes, imageName);
                response.getWriter().print("{\"res\": 1, \"info\":\"图片上传成功！\"，\"url\":"+url+"}");
                return;
//                result.setCode(200);
//                result.setMsg("文件上传成功");
//                result.setInfo(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"图片上传出现异常！\"}");
            return;
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
