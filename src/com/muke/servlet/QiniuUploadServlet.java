package com.muke.servlet;

import com.muke.pojo.User;
import com.muke.service.IUserService;
import com.muke.service.impl.UserServiceImpl;
import com.muke.util.FileUtil;
import com.muke.util.QiniuCloudUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@WebServlet("/user/QiniuUploadServlet")
public class QiniuUploadServlet extends HttpServlet {
    // ******的内容需要查看七牛云账号的相关信息
    private static final String accessKey = "M3HmbBGSuAOhmwbURCjxXmme-R42r-OCWLSJiBf1";    //访问秘钥
    private static final String secretKey = "G1LCpQQwjYgeu2IGZLhO5oNYzxqBf_GCr__S1kw-";    //授权秘钥
    private static final String bucket = "lidiwen";       //存储空间名称
    private static final String domain = "http://love.lidiwen.club/";       //外链域名
    private IUserService userService = new UserServiceImpl();
    private String name = null;//原始图片名称
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");//解决编码问题
        String url = null;
        HttpSession session = request.getSession();
        User user1 = (User) session.getAttribute("user");//获取发布者
        String username = user1.getUsername();
        String user_img = user1.getUser_img();
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            response.getWriter().print("{\"res\": -1, \"info\":\"请选择一张图片上传！\"}");
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 中文处理
        upload.setHeaderEncoding("utf-8");
        try {
            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (item.isFormField()) {
                        name = item.getString();
                        name = new String(name.getBytes("ISO-8859-1"), "utf-8");
                    }
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        if (fileName.equals(null) || fileName.equals("")) {
                            response.getWriter().print("{\"res\": -1, \"info\":\"请选择一张图片上传！\"}");
                            return;
                        }
                        String filetype = "." + fileName.substring(fileName.lastIndexOf(".") + 1);
                        if (!(filetype.equals(".jpg") || filetype.equals(".png") || filetype.equals(".gif")||filetype.equals(".jpeg")||filetype.equals(".bmp"))) {
                            response.getWriter().print("{\"res\": -1, \"info\":\"你上传的图片格式不正确,请重新上传\"}");
                            return;
                        }
                        upload.setFileSizeMax(1024 * 1024);// 设置图片上传的最大不能超过1M
                        float size = item.getSize() / 1024;
                        if(size>512){
                            factory.setSizeThreshold(1024 * 512);//超过512k设置临时文件缓存
                        }
                        if (size > 1024) {
                            response.getWriter().print("{\"res\": -1, \"info\":\"上传的图片大小最大不能超过1M！\"}");
                            return;
                        }
                        // 存储的文件名根据获取的id来唯一确定, 这里测试使用 "test"
                        // id可以绑定到session或request变量等等，自己根据需要来扩展
                        String fileSaveName = FileUtil.getRandomFileName() + filetype; // id.后缀
                        byte[] data = item.get();
                        try {
                            //使用base64方式上传到七牛云
                            url = QiniuCloudUtil.upload2(data, fileSaveName);
                            if(url==null){
                                response.getWriter().print("{\"res\": -1, \"info\":\"上传失败\"}");
                                return;
                            }
                            int ok = userService.saveUserimg(username, url);
                            if (ok == 1) {
                                User user = userService.username(username);
                                HttpSession session1 = request.getSession();
                                session1.setAttribute("user", user);
                                response.getWriter().print("{\"res\": 1, \"info\":\"成功上传\",\"src\": \"" + url + "\"}");
                                //删除之前上传的七牛云图片
                                if (!(user_img.equals("image/nan.png") || user_img.equals("image/nu.png"))) {
                                    QiniuCloudUtil.delete(user_img);
                                }
                                return;
                            } else {
                                response.getWriter().print("{\"res\": -1, \"info\":\"上传失败\"}");
                                return;
                            }
                        } catch (Exception e) {
                            response.getWriter().print("{\"res\": -1, \"info\":\"上传失败\"}");
                            e.printStackTrace();
                            return;
                        }
                    }
                    // if (!item.isFormField())
                } // for
            } // try
        } catch (FileUploadException e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"上传的图片大小最大不能超过1M！\"}");
            return;
        } catch (Exception e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"图片上传错误，请检查网络是否连接正常！\"}");
            return;
        }
        request.setAttribute("user.user_img", url);
        request.getRequestDispatcher("user/center.jsp").forward(request, response);
        // 重定向到显示图片页面
    }

}
